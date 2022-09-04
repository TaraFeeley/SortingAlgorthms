//Tara Feeley CIS2168

import java.io.IOException;
import java.util.*;

public class SortingAlgorithms {
    static int RUN = 32;

    protected static class Data {
        int exchanges = 0;
        int comparisons = 0;
        int[] array;
        long runTime;

        public void setArray(int[] array) {
            this.array = array;
        }

        public void setRunTime(long end, long start) {
            long elapsed = end - start;
            this.runTime = elapsed;
        }

        public int compareWith(int i, int j) {
            this.comparisons++;
            if (i > j) {
                return 1;
            } else if (i < j) {
                return -1;
            } else {
                return 0;
            }
        }

        public void swap(int i, int j) {
            int tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
            this.exchanges++;
        }

        public void set(int i, int value) {
            array[i] = value;
            this.exchanges++;
        }

        public String toString() {
            return "\n" + this.comparisons + ",  " + this.exchanges + ", " + this.runTime;
        }
    }


    //used geeksforgeeks
    public static void insertionSort(Data data, int left, int right) {

        for (int i = left + 1; i <= right; i++) {
            int key = data.array[i];
            int j = i - 1;
            while (j >= 0 && data.compareWith(data.array[j], key) > 0) { //compare arr[j] and key
                data.swap(j, j + 1);
                j = j - 1;
            }
            data.set(j + 1, key);
        }

    }

    // used: https://www.softwaretestinghelp.com/quicksort-in-java/
    public static int partition(Data data, int left, int right) {
        int pi = data.array[right];
        int i = (left - 1); // smaller element index
        for (int j = left; j < right; j++) {
            // check if current element is less than or equal to pi
//            if (data.compareWith(data.array[j], pi) <= 0) {
            if (data.compareWith(data.array[j], pi) < 0) {
                i++;
                // swap intArray[i] and intArray[j]
                data.swap(i, j);
            }
        }
        // swap intArray[i+1] and intArray[high] (or pi)
        data.swap(i + 1, right);
        return i + 1;
    }


    //routine to sort the array partitions recursively
    public static void quickSort(int left, int right, Data data) {
//algorithm from   https://www.softwaretestinghelp.com/quicksort-in-java/
        if (left < right) { //comparison++
            //partition the array around pi=>partitioning index and return pi
            int pi = partition(data, left, right);
            // sort each partition recursively
//            quickSort(left, pi - 1, data);
//            quickSort(pi + 1, right, data);
            quickSort(pi+1, right, data);
            quickSort(left, pi-1, data);
        }
    }

    public static void merge(Data data, int l, int m, int r) {
        //also from:         https://www.geeksforgeeks.org/merge-sort/
        // Find sizes of two subarrays to be merged
        int n1 = m - l + 1;
        int n2 = r - m;
        /* Create temp arrays */
        int L[] = new int[n1];
        int R[] = new int[n2];

        /*Copy data to temp arrays*/
        for (int i = 0; i < n1; i++)
            L[i] = data.array[l + i];
        for (int j = 0; j < n2; j++)
            R[j] = data.array[m + 1 + j];

        /* Merge the temp arrays */
        // Initial indexes of first and second subarrays
        int i = 0, j = 0;
        // Initial index of merged subarray array
        int k = l;
        while (i < n1 && j < n2) {
//            if (L[i] <= R[j]) {
            if (data.compareWith(L[i], R[j]) <= 0) {
//                arr[k] = L[i];
                data.set(k, L[i]);
                i++;
            } else {
//                arr[k] = R[j];
                data.set(k, R[j]);
                j++;
            }
            k++;
        }

        /* Copy remaining elements of L[] if any */
        while (i < n1) {
//            arr[k] = L[i];
            data.set(k, L[i]);
            i++;
            k++;
        }

        /* Copy remaining elements of R[] if any */
        while (j < n2) {
//            arr[k] = R[j];
            data.set(k, R[j]);
            j++;
            k++;
        }
    }

    public static void mergeSort(Data data, int l, int r) {
//algorithm from:         https://www.geeksforgeeks.org/merge-sort/
        if (l < r) {
            // Find the middle point
            int m = l + (r - l) / 2;

            // Sort first and second halves
            mergeSort(data, l, m);
            mergeSort(data, m + 1, r);

            // Merge the sorted halves
            merge(data, l, m, r);
        }
    }


    public static void bubbleSort(Data data) {

        int n = data.array.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (data.compareWith(data.array[j], data.array[j + 1]) > 0) {
                    // swap arr[j+1] and arr[j]
                    data.swap(j, j + 1);
                }
            }
        }
    }


    public static int[] generateArrayOfNums(int size) {
        Random random = new Random(1000);
        int[] arr = new int[size];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = random.nextInt();
        }
        return arr;
    }


    //used https://tutorialspoint.dev/algorithm/sorting-algorithms/timsort , which seems to reference geeksforgeeks
// iterative Timsort function to sort the array[0...n-1] (similar to merge sort)
    public static void timSort(Data d, int n) {

        // Sort individual subarrays of size RUN
        for (int i = 0; i < n; i += RUN) {
            insertionSort(d, i, Math.min((i + 31),(n - 1)));
        }
        // start merging from size RUN (or 32). It will merge
        // to form size 64, then 128, 256 and so on ....
        for (int size = RUN; size < n; size = 2 * size) {
            // pick starting point of left sub array. We
            // are going to merge arr[left..left+size-1]
            // and arr[left+size, left+2*size-1]
            // After every merge, we increase left by 2*size
            for (int left = 0; left < n; left += 2 * size) {

                // find ending point of left sub array
                // mid+1 is starting point of right sub array
                int mid = left + size - 1;
                int right = Math.min((left + 2 * size - 1), (n - 1));

                // merge sub array arr[left.....mid] &
                // arr[mid+1....right]
                merge(d, left, mid, right);
            }
        }
    }
}


//    public static void main(String[] args) throws IOException {
//        for (int i = 3; i <= 3; i++) {
//            List<Data> results = new ArrayList<>();
//            List<Data> results2 = new ArrayList<>();
//            List<Data> results3 = new ArrayList<>();
//            List<Data> results4 = new ArrayList<>();
//            for (int j = 0; j < 1; j++) {
//
//                int size = (int) Math.pow(2, i);
//                int[] arr = generateArrayOfNums(size);
//                Data dI = new Data();
//                dI.setArray(Arrays.copyOf(arr,size));
//                insertionSort(dI);
//                results.add(dI);
//                results.toString();
////                Data dB = new Data();
////                dB.setArray(Arrays.copyOf(arr,size));
////                bubbleSort(dB);
////                results2.add(dB);
////                results2.toString();
//                Data dQ = new Data();
//                dQ.setArray(Arrays.copyOf(arr,size));
//                System.out.println(Arrays.toString(dQ.array));
//                quickSort(0,dQ.array.length-1,dQ);
//                System.out.println(Arrays.toString(dQ.array));
//                results3.add(dQ);
//                results3.toString();
//                Data dM = new Data();
//                dM.setArray(Arrays.copyOf(arr,size));
//                System.out.println(Arrays.toString(dM.array));
//                mergeSort(dM, 0, dM.array.length-1 );
//                System.out.println(Arrays.toString(dM.array));
//                results4.add(dM);
//                results4.toString();
//            }
//            System.out.println(i+"\n"+results.toString()+"\n");
////            System.out.println(i+"\n"+results2.toString()+"\n");
//            System.out.println(i+"\n"+results3.toString()+"\n");
//            System.out.println(i+"\n"+results4.toString()+"\n");
