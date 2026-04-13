package collection;

import java.util.*;

public class ContactList {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Map<String, String> contacts = new HashMap<>();
        int choice;

        do {
            System.out.println("\n===== CONTACT LIST MENU =====");
            System.out.println("1. Add Contact");
            System.out.println("2. View All Contacts");
            System.out.println("3. Search Contact");
            System.out.println("4. Update Contact");
            System.out.println("5. Delete Contact");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter contact name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter phone number: ");
                    String phone = sc.nextLine();
                    if (contacts.containsKey(name)) {
                        System.out.println("Contact already exists!");
                    } else {
                        contacts.put(name, phone);
                        System.out.println("Contact added successfully.");
                    }
                    break;

                case 2:
                    if (contacts.isEmpty()) {
                        System.out.println("No contacts found.");
                    } else {
                        System.out.println("\n--- Contact List ---");
                        for (Map.Entry<String, String> entry : contacts.entrySet()) {
                            System.out.println("Name: " + entry.getKey() + " | Phone: " + entry.getValue());
                        }
                    }
                    break;

                case 3:
                    System.out.print("Enter name to search: ");
                    String searchName = sc.nextLine();
                    if (contacts.containsKey(searchName)) {
                        System.out.println("Phone Number: " + contacts.get(searchName));
                    } else {
                        System.out.println("Contact not found.");
                    }
                    break;

                case 4:
                    System.out.print("Enter name to update: ");
                    String updateName = sc.nextLine();
                    if (contacts.containsKey(updateName)) {
                        System.out.print("Enter new phone number: ");
                        String newPhone = sc.nextLine();
                        contacts.put(updateName, newPhone);
                        System.out.println("Contact updated successfully.");
                    } else {
                        System.out.println("Contact not found.");
                    }
                    break;

                case 5:
                    System.out.print("Enter name to delete: ");
                    String deleteName = sc.nextLine();
                    if (contacts.remove(deleteName) != null) {
                        System.out.println("Contact deleted successfully.");
                    } else {
                        System.out.println("Contact not found.");
                    }
                    break;

                case 6:
                    System.out.println("Exiting Contact List...");
                    break;

                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        } while (choice != 6);

        sc.close();
    }
}
