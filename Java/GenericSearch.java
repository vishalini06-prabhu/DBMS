package generic;

class GenericSearch {

    // Generic method to search an element in an array
    public static <T> int search(T[] arr, T key) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(key)) {
                return i; // return index if found
            }
        }
        return -1; // return -1 if not found
    }

    public static void main(String[] args) {
        // Integer array
        Integer[] numbers = {10, 20, 30, 40, 50};
        int index1 = search(numbers, 30);
        System.out.println("Index of 30: " + index1);

        // String array
        String[] names = {"Amit", "Ravi", "Neha", "Priya"};
        int index2 = search(names, "Neha");
        System.out.println("Index of Neha: " + index2);
    }
}
