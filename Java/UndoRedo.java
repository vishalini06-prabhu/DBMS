package collection;
import java.util.*;

public class UndoRedoDemo {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Stack<String> undoStack = new Stack<>();
        Stack<String> redoStack = new Stack<>();

        int choice;
        do {
            System.out.println("\n===== MENU =====");
            System.out.println("1. Do Action");
            System.out.println("2. Undo Last Action");
            System.out.println("3. Redo Last Undone Action");
            System.out.println("4. Display All Actions");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter action to perform: ");
                    String action = sc.nextLine();
                    undoStack.push(action);
                    redoStack.clear(); // once new action is done, redo history resets
                    System.out.println("Action performed: " + action);
                    break;

                case 2:
                    if (!undoStack.isEmpty()) {
                        String undone = undoStack.pop();
                        redoStack.push(undone);
                        System.out.println(" Undone action: " + undone);
                    } else {
                        System.out.println(" Nothing to undo!");
                    }
                    break;

                case 3:
                    if (!redoStack.isEmpty()) {
                        String redone = redoStack.pop();
                        undoStack.push(redone);
                        System.out.println("Redone action: " + redone);
                    } else {
                        System.out.println(" Nothing to redo!");
                    }
                    break;

                case 4:
                    System.out.println("\nActions performed so far: " + undoStack);
                    break;

                case 5:
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println(" Invalid choice! Try again.");
            }
        } while (choice != 5);

        sc.close();
    }
}
