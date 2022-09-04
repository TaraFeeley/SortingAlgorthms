//Tara Feeley CIS2168
import java.io.*;
import java.util.*;
public class writeCSV extends SortingAlgorithms {

    private static String filePathway = "./sortingAlgData.csv";
    private static FileWriter fileWriter;
    private static String[] sorts = {"Insertion Sort", "QuickSort", "Merge Sort", "TimSort"};

    public static void main(String[] args) throws IOException {
        fileWriter = new FileWriter(filePathway, true);
        System.out.println("File pathway: " + filePathway);
        try {
            Map<String, ArrayList<String>> dataMap = new HashMap<>(dataToCSV());
            for (String sort : sorts) {
                fileWriter.append(sort + "\n");
                fileWriter.append("n, Average Comparisons, Average Exchanges, Average Runtime\n");

                String d = "";
                for (String run : dataMap.get(sort)) {
                    d += run + "\n";
                }
                fileWriter.append(d);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public static Map<String, ArrayList<String>> dataToCSV() throws IOException {
        Map<String, ArrayList<String>> sortData = new HashMap<>();
        for (String sort : sorts) {
            sortData.putIfAbsent(sort, new ArrayList<>());
        }
        for (int i = 5; i <= 12; i++) {
            int size = (int) Math.pow(2, i);
            int[] arr = generateArrayOfNums(size);
            for (String sort : sorts) {
                sortData.get(sort).add(averageData(arr, size, sort));
            }
        }
        return sortData;
    }


    public static String averageData(int[] arr, int n, String sort) throws IOException {
        double avgComparisons = 0.0;
        double avgExchanges = 0.0;
        double avgRunTime = 0.0;
        long start = 0;
        long end = 0;
        for (int j = 0; j < 20; j++) {
            Data d = new Data();
            d.setArray(Arrays.copyOf(arr, arr.length));
            if (sort.equals("Insertion Sort")) {
                start = System.nanoTime();
                insertionSort(d, 0, d.array.length - 1);
                end = System.nanoTime();
                d.setRunTime(end, start);
            } else if (sort.equals("QuickSort")) {
                start = System.nanoTime();
                quickSort(0, d.array.length - 1, d);
                end = System.nanoTime();
                d.setRunTime(end, start);
            } else if (sort.equals("Merge Sort")) {
                start = System.nanoTime();
                mergeSort(d, 0, d.array.length - 1);
                end = System.nanoTime();
                d.setRunTime(end, start);
            } else if (sort.equals("TimSort")) {
                start = System.nanoTime();
                timSort(d, n);
                end = System.nanoTime();
                d.setRunTime(end, start);
            }
            avgComparisons += d.comparisons;
            avgExchanges += d.exchanges;
            avgRunTime += d.runTime;
        }
        avgComparisons /= 20;
        avgExchanges /= 20;
        avgRunTime /= 20;
        String commaSepData = n + "," + avgComparisons + "," + avgExchanges + "," + avgRunTime;
        return commaSepData;
    }
}

