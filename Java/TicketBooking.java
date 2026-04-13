package ticketBooking;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class TicketBookingSystem extends JFrame {
    private JTextField nameField, seatsField, dateField;
    private JComboBox<String> showBox, timeBox;
    private JLabel ticketPriceLabel, totalPriceLabel;
    private JButton bookButton, clearButton;

    static final String URL = "jdbc:oracle:thin:@localhost:1521/XEPDB1";
    static final String USER = "vishalinidb";
    static final String PASSWORD = "vishalini";

    public TicketBookingSystem() {
        setTitle("Ticket Booking System");
        setSize(500, 450);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(new Color(240, 250, 255));

        JLabel title = new JLabel("TICKET BOOKING PORTAL", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBounds(60, 20, 370, 30);
        title.setForeground(new Color(0, 102, 204));
        add(title);

        addLabel("Customer Name:", 70, 80);
        addLabel("Select Show:", 70, 120);
        addLabel("Show Time:", 70, 160);
        addLabel("Date (YYYY-MM-DD):", 70, 200);
        addLabel("Seats:", 70, 240);
        addLabel("Ticket Price:", 70, 280);
        addLabel("Total Price:", 70, 320);

        nameField = new JTextField();
        nameField.setBounds(250, 80, 160, 25);
        add(nameField);

        String[] shows = {"Avengers: Endgame", "The Lion King", "Frozen 2", "Hi Nana"};
        showBox = new JComboBox<>(shows);
        showBox.setBounds(250, 120, 160, 25);
        add(showBox);

        timeBox = new JComboBox<>(new String[]{"10:00 AM", "1:00 PM", "4:00 PM", "7:00 PM"});
        timeBox.setBounds(250, 160, 160, 25);
        add(timeBox);

        dateField = new JTextField();
        dateField.setBounds(250, 200, 160, 25);
        add(dateField);

        seatsField = new JTextField();
        seatsField.setBounds(250, 240, 160, 25);
        add(seatsField);

        ticketPriceLabel = new JLabel("-");
        ticketPriceLabel.setBounds(250, 280, 160, 25);
        add(ticketPriceLabel);

        totalPriceLabel = new JLabel("-");
        totalPriceLabel.setBounds(250, 320, 160, 25);
        add(totalPriceLabel);

        bookButton = new JButton("Book Ticket");
        bookButton.setBounds(130, 370, 120, 30);
        bookButton.setBackground(new Color(0, 102, 204));
        bookButton.setForeground(Color.WHITE);
        add(bookButton);

        clearButton = new JButton("Clear");
        clearButton.setBounds(280, 370, 100, 30);
        clearButton.setBackground(Color.GRAY);
        clearButton.setForeground(Color.WHITE);
        add(clearButton);

        // Event handlers
        showBox.addActionListener(e -> computeTotal());
        seatsField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                computeTotal();
            }
        });

        bookButton.addActionListener(e -> bookTicket());
        clearButton.addActionListener(e -> clearFields());
    }

    private void addLabel(String text, int x, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setBounds(x, y, 160, 25);
        lbl.setFont(new Font("Arial", Font.PLAIN, 14));
        add(lbl);
    }

    // Assign ticket prices using simple if-else logic
    private double getTicketPrice(String show) {
        if (show.equals("Avengers: Endgame"))
            return 250.0;
        else if (show.equals("The Lion King"))
            return 200.0;
        else if (show.equals("Frozen 2"))
            return 180.0;
        else if (show.equals("Hi Nana"))
            return 220.0;
        else
            return 0.0;
    }

    private void computeTotal() {
        try {
            String show = (String) showBox.getSelectedItem();
            int seats = Integer.parseInt(seatsField.getText());
            double pricePerTicket = getTicketPrice(show);
            double total = pricePerTicket * seats;

            ticketPriceLabel.setText(String.format("%.2f", pricePerTicket));
            totalPriceLabel.setText(String.format("%.2f", total));
        } catch (Exception e) {
            ticketPriceLabel.setText("-");
            totalPriceLabel.setText("-");
        }
    }

    private void bookTicket() {
        String name = nameField.getText();
        String show = (String) showBox.getSelectedItem();
        String time = (String) timeBox.getSelectedItem();
        String date = dateField.getText();
        int seats;

        try {
            seats = Integer.parseInt(seatsField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Enter valid number of seats");
            return;
        }

        double ticketPrice = getTicketPrice(show);
        double total = ticketPrice * seats;

        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "INSERT INTO TICKET_BOOKING (NAME, SHOW_NAME, SHOW_TIME, SEATS, DATE_OF_SHOW, TICKET_PRICE, TOTAL_PRICE) " +
                         "VALUES (?, ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, show);
            ps.setString(3, time);
            ps.setInt(4, seats);
            ps.setString(5, date);
            ps.setDouble(6, ticketPrice);
            ps.setDouble(7, total);
            ps.executeUpdate();

            generateReceipt(name, show, time, date, seats, ticketPrice, total);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void clearFields() {
        nameField.setText("");
        seatsField.setText("");
        dateField.setText("");
        ticketPriceLabel.setText("-");
        totalPriceLabel.setText("-");
        showBox.setSelectedIndex(0);
        timeBox.setSelectedIndex(0);
    }

    private void generateReceipt(String name, String show, String time, String date, int seats, double price, double total) {
        JFrame receipt = new JFrame("Ticket Receipt");
        receipt.setSize(400, 350);
        receipt.setLocationRelativeTo(this);
        receipt.setLayout(new GridLayout(8, 1));
        receipt.getContentPane().setBackground(new Color(255, 255, 240));

        receipt.add(new JLabel("***** Ticket Receipt *****", SwingConstants.CENTER));
        receipt.add(new JLabel("Customer: " + name, SwingConstants.CENTER));
        receipt.add(new JLabel("Show: " + show + " (" + time + ")", SwingConstants.CENTER));
        receipt.add(new JLabel("Date: " + date, SwingConstants.CENTER));
        receipt.add(new JLabel("Seats: " + seats, SwingConstants.CENTER));
        receipt.add(new JLabel(String.format("Price per Ticket: %.2f", price), SwingConstants.CENTER));
        receipt.add(new JLabel(String.format("Total Price: %.2f", total), SwingConstants.CENTER));
        receipt.add(new JLabel("Enjoy your show!", SwingConstants.CENTER));

        receipt.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (Exception e) {
            System.out.println("Oracle Driver not found");
        }
        SwingUtilities.invokeLater(() -> new TicketBookingSystem().setVisible(true));
    }
}
