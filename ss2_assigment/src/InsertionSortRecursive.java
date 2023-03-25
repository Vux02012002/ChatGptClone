import java.util.Arrays;

public class InsertionSortRecursive {

    public static void sort(int[] arr) {
        if (arr == null || arr.length <= 1) {
            return;
        }
        sortRecursive(arr, arr.length - 1);
    }

    private static void sortRecursive(int[] arr, int n) {
        if (n == 0) {
            return;
        }
        sortRecursive(arr, n - 1);
        int key = arr[n], j = n - 1;
        while (j >= 0 && arr[j] > key) {
            arr[j + 1] = arr[j];
            j--;
        }
        arr[j + 1] = key;
    }

    public static void main(String[] args) {
        int[] arr = { 5, 1, 9, 3, 7 };

        // Print the unsorted array
        System.out.println("Unsorted array:");
        Arrays.stream(arr).forEach(e -> System.out.print(e + " "));
        System.out.println();

        // Sort the array using Insertion Sort recursively
        sort(arr);

        // Print the sorted array
        System.out.println("Sorted array:");
        Arrays.stream(arr).forEach(e -> System.out.print(e + " "));
        System.out.println();
    }
}
