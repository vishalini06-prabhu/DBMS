package inventoryMan;

import java.awt.*;
import java.awt.event.*;

public class InventoryMenu extends Frame implements ActionListener {
    TextField nameField, qtyField;
    TextArea displayArea;

    MenuItem addItem, updateItem, removeItem, viewItems, clearDisplay, exitApp;

    String[] itemNames = new String[20];
    int[] itemQuantities = new int[20];
    int itemCount = 0;

    public InventoryMenu() {
        setTitle("Inventory Management");
        setSize(400, 350);  // Reduced width and height
        setLayout(null);

        // --- Menu Bar ---
        MenuBar menuBar = new MenuBar();

        Menu itemMenu = new Menu("Item");
        addItem = new MenuItem("Add Item");
        updateItem = new MenuItem("Update Quantity");
        removeItem = new MenuItem("Remove Item");
        itemMenu.add(addItem);
        itemMenu.add(updateItem);
        itemMenu.add(removeItem);
        menuBar.add(itemMenu);

        Menu viewMenu = new Menu("View");
        viewItems = new MenuItem("View Inventory");
        clearDisplay = new MenuItem("Clear Display");
        viewMenu.add(viewItems);
        viewMenu.add(clearDisplay);
        menuBar.add(viewMenu);

        Menu exitMenu = new Menu("Exit");
        exitApp = new MenuItem("Exit Application");
        exitMenu.add(exitApp);
        menuBar.add(exitMenu);

        setMenuBar(menuBar);

        // Input fields
        Label nameLabel = new Label("Item Name:");
        nameLabel.setBounds(40, 60, 80, 25);
        add(nameLabel);

        nameField = new TextField();
        nameField.setBounds(130, 60, 200, 25);
        add(nameField);

        Label qtyLabel = new Label("Quantity:");
        qtyLabel.setBounds(40, 100, 80, 25);
        add(qtyLabel);

        qtyField = new TextField();
        qtyField.setBounds(130, 100, 200, 25);
        add(qtyField);

        // Display Area
        displayArea = new TextArea();
        displayArea.setBounds(40, 150, 290, 140);
        add(displayArea);

        // Menu item actions
        addItem.addActionListener(this);
        updateItem.addActionListener(this);
        removeItem.addActionListener(this);
        viewItems.addActionListener(this);
        clearDisplay.addActionListener(this);
        exitApp.addActionListener(this);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                dispose();
            }
        });

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == addItem) {
            String name = nameField.getText().trim();
            String qtyText = qtyField.getText().trim();

            if (name.isEmpty() || qtyText.isEmpty()) {
                displayArea.append("Please enter both name and quantity.\n");
                return;
            }

            int qty = Integer.parseInt(qtyText);
            itemNames[itemCount] = name;
            itemQuantities[itemCount] = qty;
            itemCount++;
            displayArea.append("Added: " + name + " (" + qty + ")\n");

        } else if (src == updateItem) {
            String name = nameField.getText().trim();
            String qtyText = qtyField.getText().trim();

            if (name.isEmpty() || qtyText.isEmpty()) {
                displayArea.append("Please enter name and new quantity.\n");
                return;
            }

            int newQty = Integer.parseInt(qtyText);
            boolean found = false;
            for (int i = 0; i < itemCount; i++) {
                if (itemNames[i].equalsIgnoreCase(name)) {
                    itemQuantities[i] = newQty;
                    found = true;
                    displayArea.append("Updated " + name + " to quantity " + newQty + "\n");
                    break;
                }
            }
            if (!found) displayArea.append("Item not found.\n");

        } else if (src == removeItem) {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                displayArea.append("Enter the item name to remove.\n");
                return;
            }

            boolean found = false;
            for (int i = 0; i < itemCount; i++) {
                if (itemNames[i].equalsIgnoreCase(name)) {
                    for (int j = i; j < itemCount - 1; j++) {
                        itemNames[j] = itemNames[j + 1];
                        itemQuantities[j] = itemQuantities[j + 1];
                    }
                    itemCount--;
                    found = true;
                    displayArea.append("Removed: " + name + "\n");
                    break;
                }
            }
            if (!found) displayArea.append("Item not found.\n");

        } else if (src == viewItems) {
            displayArea.append("---- Inventory ----\n");
            for (int i = 0; i < itemCount; i++) {
                displayArea.append((i + 1) + ". " + itemNames[i] + " - " + itemQuantities[i] + "\n");
            }
            displayArea.append("-------------------\n");

        } else if (src == clearDisplay) {
            displayArea.setText("");

        } else if (src == exitApp) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        new InventoryMenu();
    }
}
