import java.sql.*;
import java.util.Scanner;

public class StudentConsole {

    static Connection con;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        connectDB();

        while(true) {
            System.out.println("\n1.Insert");
            System.out.println("2.Update");
            System.out.println("3.Delete");
            System.out.println("4.Display All Students");
            System.out.println("5.Display Student + Score Card");
            System.out.println("6.Exit");

            int ch = sc.nextInt();
            sc.nextLine();

            switch(ch) {
                case 1: insert(sc); break;
                case 2: update(sc); break;
                case 3: delete(sc); break;
                case 4: displayAll(); break;
                case 5: displayOne(sc); break;
                case 6: System.exit(0);
            }
        }
    }

    // DB CONNECTION
    static void connectDB() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");

            con = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521/XEPDB1",
                "vishalinidb",
                "vishalini");

            con.setAutoCommit(false);

        } catch(Exception e) {
            System.out.println(e);
        }
    }

    // INSERT
    static void insert(Scanner sc) {
        try {
            System.out.print("Enter User ID: ");
            String id = sc.nextLine().trim().toUpperCase();

            if(id.isEmpty()) {
                System.out.println("ID cannot be empty!");
                return;
            }

            if(!id.matches("24STU\\d{3}")) {
                System.out.println("Invalid ID! Format: 24STU001");
                return;
            }

            // CHECK DUPLICATE
            PreparedStatement check = con.prepareStatement(
                "SELECT USER_ID FROM USERS WHERE USER_ID=?");
            check.setString(1, id);
            ResultSet rsCheck = check.executeQuery();
            if(rsCheck.next()) {
                System.out.println("User already exists!");
                return;
            }

            System.out.print("Enter Name: ");
            String name = sc.nextLine().trim();

            if(name.isEmpty()) {
                System.out.println("Name cannot be empty!");
                return;
            }

            System.out.print("Enter Email: ");
            String email = sc.nextLine().trim();

            if(!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                System.out.println("Invalid Email!");
                return;
            }

            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO USERS(USER_ID,FIRST_NAME,EMAIL) VALUES(?,?,?)");

            ps.setString(1, id);
            ps.setString(2, name);
            ps.setString(3, email);

            ps.executeUpdate();
            con.commit();

            System.out.println("Inserted!");

        } catch(Exception e) {
            System.out.println(e);
        }
    }

    // UPDATE
    static void update(Scanner sc) {
        try {
            System.out.print("Enter User ID: ");
            String id = sc.nextLine().trim().toUpperCase();

            if(id.isEmpty()) {
                System.out.println("ID cannot be empty!");
                return;
            }

            System.out.print("Enter New Name: ");
            String name = sc.nextLine().trim();

            if(name.isEmpty()) {
                System.out.println("Name cannot be empty!");
                return;
            }

            System.out.print("Enter New Email: ");
            String email = sc.nextLine().trim();

            if(!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                System.out.println("Invalid Email!");
                return;
            }

            PreparedStatement ps = con.prepareStatement(
                "UPDATE USERS SET FIRST_NAME=?, EMAIL=? WHERE USER_ID=?");

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, id);

            int rows = ps.executeUpdate();
            con.commit();

            System.out.println(rows > 0 ? "Updated!" : "User not found");

        } catch(Exception e) {
            System.out.println(e);
        }
    }

    // DELETE
    static void delete(Scanner sc) {
        try {
            System.out.print("Enter User ID: ");
            String id = sc.nextLine().trim().toUpperCase();

            if(id.isEmpty()) {
                System.out.println("ID cannot be empty!");
                return;
            }

            // CHECK EXISTENCE
            PreparedStatement check = con.prepareStatement(
                "SELECT USER_ID FROM USERS WHERE USER_ID=?");
            check.setString(1, id);
            ResultSet rs = check.executeQuery();

            if(!rs.next()) {
                System.out.println("User does not exist!");
                return;
            }
         // 🔥 DELETE TEST ATTEMPTS FIRST (IMPORTANT)
            String sql0 = "DELETE FROM TEST_ATTEMPTS WHERE S_USER_ID = ?";
            PreparedStatement ps0 = con.prepareStatement(sql0);
            ps0.setString(1, id);
            ps0.executeUpdate();
            ps0.close();
            con.commit();

            String sql1 = "DELETE FROM attendance WHERE S_USER_ID = ?";
            PreparedStatement ps1 = con.prepareStatement(sql1);
            ps1.setString(1, id);
            ps1.executeUpdate();

            sql1 = "DELETE FROM USER_PHONE WHERE USER_ID = ?";
            ps1 = con.prepareStatement(sql1);
            ps1.setString(1, id);
            ps1.executeUpdate();

            sql1 = "DELETE FROM NOTIFICATIONS WHERE S_USER_ID = ?";
            ps1 = con.prepareStatement(sql1);
            ps1.setString(1, id);
            ps1.executeUpdate();

            sql1 = "DELETE FROM ENROLLMENTS WHERE S_USER_ID = ?";
            ps1 = con.prepareStatement(sql1);
            ps1.setString(1, id);
            ps1.executeUpdate();

            sql1 = "DELETE FROM STUDENTS WHERE S_USER_ID = ?";
            ps1 = con.prepareStatement(sql1);
            ps1.setString(1,id);
            int rows = ps1.executeUpdate();

            sql1 = "DELETE FROM USERS WHERE USER_ID = ?";
            ps1 = con.prepareStatement(sql1);
            ps1.setString(1, id);
            ps1.executeUpdate();

            con.commit();

            
            System.out.println(rows > 0 ? "Deleted" : "Deleted");

        } catch(Exception e) {
            System.out.println(e);
        }
    }

    // DISPLAY ALL
    static void displayAll() {
        try {
            PreparedStatement ps = con.prepareStatement(
                "SELECT USER_ID, FIRST_NAME, EMAIL FROM USERS WHERE USER_ID LIKE '24STU%'");

            ResultSet rs = ps.executeQuery();

            System.out.println("\n--- ALL STUDENTS ---");
            while(rs.next()) {
                System.out.println(
                    rs.getString(1) + " | " +
                    rs.getString(2) + " | " +
                    rs.getString(3));
            }

        } catch(Exception e) {
            System.out.println(e);
        }
    }

    // SCORE CARD
    static void displayOne(Scanner sc) {
        try {
            System.out.print("Enter Student ID: ");
            String id = sc.nextLine().trim().toUpperCase();

            if(!id.matches("24STU\\d{3}")) {
                System.out.println("Invalid Student ID!");
                return;
            }

            PreparedStatement ps1 = con.prepareStatement(
                "SELECT USER_ID, FIRST_NAME, EMAIL FROM USERS WHERE USER_ID=?");
            ps1.setString(1, id);

            ResultSet rs1 = ps1.executeQuery();

            if(!rs1.next()) {
                System.out.println("Student not found!");
                return;
            }

            System.out.println("\n--- STUDENT DETAILS ---");
            System.out.println(
                rs1.getString(1) + " | " +
                rs1.getString(2) + " | " +
                rs1.getString(3));

            PreparedStatement ps2 = con.prepareStatement(
                "SELECT S.SUBJECT_NAME, T.SCORE " +
                "FROM TEST_ATTEMPTS T " +
                "JOIN BATCHES B ON T.BATCH_ID = B.BATCH_ID " +
                "JOIN SUBJECTS S ON B.SUBJECT_ID = S.SUBJECT_ID " +
                "WHERE T.S_USER_ID=?");

            ps2.setString(1, id);

            ResultSet rs2 = ps2.executeQuery();

            int total = 0, count = 0;

            System.out.println("\n--- SUBJECT WISE MARKS ---");
            while(rs2.next()) {
                String subject = rs2.getString(1);
                int score = rs2.getInt(2);

                System.out.println(subject + " : " + score);

                total += score;
                count++;
            }

            if(count == 0) {
                System.out.println("No test records!");
                return;
            }

            double avg = total / (double) count;

            String grade;
            if(avg >= 90) grade = "A";
            else if(avg >= 75) grade = "B";
            else if(avg >= 50) grade = "C";
            else grade = "Fail";

            System.out.println("\n--- SCORE CARD ---");
            System.out.println("Total = " + total);
            System.out.println("Average = " + avg);
            System.out.println("Grade = " + grade);

        } catch(Exception e) {
            System.out.println(e);
        }
    }
}
