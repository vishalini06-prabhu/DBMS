package generic;

import java.util.Arrays;

class GenericSort {

    // Generic method to sort an array
    public static <T extends Comparable<T>> void sortArray(T[] arr) {
        Arrays.sort(arr); // built-in sort method
        System.out.println("Sorted Array: " + Arrays.toString(arr));
    }

    public static void main(String[] args) {
        // Integer array
        Integer[] numbers = {50, 10, 30, 20, 40};
        System.out.println("Original Integer Array: " + Arrays.toString(numbers));
        sortArray(numbers);

        // String array
        String[] names = {"Ravi", "Amit", "Neha", "Priya"};
        System.out.println("\nOriginal String Array: " + Arrays.toString(names));
        sortArray(names);
    }
}
