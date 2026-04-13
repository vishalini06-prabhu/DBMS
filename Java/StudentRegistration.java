package registrationForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class StudentRegistrationOracle implements ActionListener {

    Label name, mobile, dob, gender, address, title, subject;
    Button submit, reset;
    TextField fname, lname, mob;
    JRadioButton male, female, other;
    Choice day, month, year;
    TextArea addr;
    ButtonGroup group;
    Checkbox accept, java, python, c, dsa;

    // Database connection info
    static final String URL = "jdbc:oracle:thin:@localhost:1521/XEPDB1";
    static final String USER = "vishalinidb";  
    static final String PASSWORD = "vishalini"; 

    StudentRegistrationOracle() {

        JFrame frame = new JFrame("Student Registration Form");
        frame.setSize(380, 490);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // title
        title = new Label("REGISTRATION FORM");
        title.setBounds(120, 10, 200, 30);
        frame.add(title);

        // Name
        name = new Label("Name");
        name.setBounds(30, 50, 50, 30);
        fname = new TextField();
        fname.setBounds(130, 50, 100, 25);
        lname = new TextField();
        lname.setBounds(240, 50, 100, 25);
        frame.add(name);
        frame.add(fname);
        frame.add(lname);

        // mobile no
        mobile = new Label("Mobile Number");
        mobile.setBounds(30, 80, 100, 25);
        mob = new TextField();
        mob.setBounds(130, 80, 210, 25);
        frame.add(mobile);
        frame.add(mob);

        // gender (radio button)
        gender = new Label("Gender");
        gender.setBounds(30, 110, 50, 25);
        male = new JRadioButton("Male");
        male.setBounds(130, 110, 55, 25);
        female = new JRadioButton("Female");
        female.setBounds(190, 110, 75, 25);
        other = new JRadioButton("Other");
        other.setBounds(270, 110, 60, 25);
        group = new ButtonGroup();
        group.add(female);
        group.add(male);
        group.add(other);
        frame.add(gender);
        frame.add(female);
        frame.add(male);
        frame.add(other);

        // DOB
        dob = new Label("DOB");
        dob.setBounds(30, 140, 30, 25);
        frame.add(dob);

        // Day
        day = new Choice();
        day.add("Day");
        for (int i = 1; i <= 31; i++) {
            day.add(String.valueOf(i));
        }
        day.setBounds(130, 140, 55, 25);
        frame.add(day);

        // Month
        month = new Choice();
        month.add("Month");
        String[] mont = {"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};
        for (String m : mont) {
            month.add(m);
        }
        month.setBounds(193, 140, 79, 25);
        frame.add(month);

        // Year
        year = new Choice();
        year.add("Year");
        for (int i = 1990; i <= 2010; i++) {
            year.add(String.valueOf(i));
        }
        year.setBounds(280, 140, 60, 25);
        frame.add(year);

        // Subject / Course
        subject = new Label("Course");
        subject.setBounds(30, 170, 60, 25);
        python = new Checkbox("Python");
        python.setBounds(130, 170, 60, 25);
        c = new Checkbox("C");
        c.setBounds(200, 170, 40, 25);
        java = new Checkbox("Java");
        java.setBounds(240, 170, 50, 25);
        dsa = new Checkbox("DSA");
        dsa.setBounds(300, 170, 50, 25);
        frame.add(subject);
        frame.add(python);
        frame.add(c);
        frame.add(java);
        frame.add(dsa);

        // Address
        address = new Label("Address");
        address.setBounds(30, 200, 60, 25);
        addr = new TextArea();
        addr.setBounds(130, 210, 210, 100);
        frame.add(addr);
        frame.add(address);

        // Terms and Conditions
        accept = new Checkbox("Accept terms and conditions.");
        accept.setBounds(30, 310, 200, 25);
        frame.add(accept);

        // Buttons
        submit = new Button("Submit");
        submit.setBounds(100, 340, 70, 30);
        submit.addActionListener(this);
        frame.add(submit);

        reset = new Button("Reset");
        reset.setBounds(200, 340, 70, 30);
        reset.addActionListener(this);
        frame.add(reset);

        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submit) {
            String first = fname.getText().trim();
            String last = lname.getText().trim();
            String mobileNum = mob.getText().trim();
            String genderText = male.isSelected() ? "Male" : (female.isSelected() ? "Female" : "Other");
            String d = day.getSelectedItem();
            String m = month.getSelectedItem();
            String y = year.getSelectedItem();
            String addressText = addr.getText().trim();
            boolean isSelected = accept.getState();
            boolean pySel = python.getState();
            boolean cSel = c.getState();
            boolean javaSel = java.getState();
            boolean dsaSel = dsa.getState();
            boolean courseSel = pySel || cSel || javaSel || dsaSel;

            if (first.equals("") || last.equals("") || mobileNum.equals("") ||
                    d.equals("Day") || m.equals("Month") || y.equals("Year") ||
                    addressText.equals("") || !isSelected || !courseSel) {
                JOptionPane.showMessageDialog(null, "Please fill all fields and accept the terms.");
            } else {
                String dobText = d + "-" + m + "-" + y;
                String course = "";
                if (pySel) course += "Python ";
                if (cSel) course += "C ";
                if (javaSel) course += "Java ";
                if (dsaSel) course += "DSA ";

                try {
                    // Load Oracle Driver
                    Class.forName("oracle.jdbc.driver.OracleDriver");

                    // Connect to Oracle
                    Connection con = DriverManager.getConnection(URL, USER, PASSWORD);

                    // Insert Query
                    String sql = "INSERT INTO studentsData (firstname, lastname, mobile, gender, dob, address, course) VALUES (?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.setString(1, first);
                    ps.setString(2, last);
                    ps.setString(3, mobileNum);
                    ps.setString(4, genderText);
                    ps.setString(5, dobText);
                    ps.setString(6, addressText);
                    ps.setString(7, course.trim());
                    ps.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Form Submitted Successfully!");

                    con.close();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Database Error: " + ex.getMessage());
                }
            }
        } else if (e.getSource() == reset) {
            fname.setText("");
            lname.setText("");
            mob.setText("");
            group.clearSelection();
            day.select("Day");
            month.select("Month");
            year.select("Year");
            addr.setText("");
            accept.setState(false);
            python.setState(false);
            c.setState(false);
            java.setState(false);
            dsa.setState(false);
        }
    }

    public static void main(String[] args) {
        new StudentRegistrationOracle();
    }
}
