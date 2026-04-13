import javax.swing.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class GUI_CRUD_Score {

    JFrame f;
    JTextField id, name, email;
    JTable table;
    JScrollPane scroll;

    JButton insert, update, delete, view, score;

    Connection con;

    GUI_CRUD_Score() {

        f = new JFrame("CRUD + Score Card");

        id = new JTextField(); id.setBounds(120,30,150,25);
        name = new JTextField(); name.setBounds(120,70,150,25);
        email = new JTextField(); email.setBounds(120,110,150,25);

        insert = new JButton("Insert"); insert.setBounds(20,160,80,30);
        update = new JButton("Update"); update.setBounds(110,160,80,30);
        delete = new JButton("Delete"); delete.setBounds(200,160,80,30);
        view   = new JButton("View Students"); view.setBounds(60,200,120,30);
        score  = new JButton("Score Card"); score.setBounds(200,200,120,30);

        table = new JTable();
        scroll = new JScrollPane(table);
        scroll.setBounds(20,250,340,200);

        f.add(new JLabel("User ID")).setBounds(30,30,80,25);
        f.add(id);

        f.add(new JLabel("Name")).setBounds(30,70,80,25);
        f.add(name);

        f.add(new JLabel("Email")).setBounds(30,110,80,25);
        f.add(email);

        f.add(insert); f.add(update); f.add(delete);
        f.add(view); f.add(score);
        f.add(scroll);

        f.setSize(400,520);
        f.setLayout(null);
        f.setVisible(true);

        connectDB();

        // INSERT
        insert.addActionListener(e -> {
            try {
                if(id.getText().isEmpty() || name.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(f,"Fill all fields!");
                    return;
                }

                PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO USERS(USER_ID,FIRST_NAME,EMAIL) VALUES(?,?,?)");

                ps.setString(1, id.getText());
                ps.setString(2, name.getText());
                ps.setString(3, email.getText());

                ps.executeUpdate();
                JOptionPane.showMessageDialog(f,"Inserted!");

            } catch(Exception ex){ System.out.println(ex); }
        });

        // UPDATE
        update.addActionListener(e -> {
            try {
                PreparedStatement ps = con.prepareStatement(
                    "UPDATE USERS SET FIRST_NAME=?, EMAIL=? WHERE USER_ID=?");

                ps.setString(1, name.getText());
                ps.setString(2, email.getText());
                ps.setString(3, id.getText());

                int rows = ps.executeUpdate();
                JOptionPane.showMessageDialog(f, rows>0?"Updated!":"User not found");

            } catch(Exception ex){ System.out.println(ex); }
        });

        //  DELETE
        delete.addActionListener(e -> {
            try {
                PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM USERS WHERE USER_ID=?");

                ps.setString(1, id.getText());

                int rows = ps.executeUpdate();
                JOptionPane.showMessageDialog(f, rows>0?"Deleted!":"User not found");

            } catch(Exception ex){ System.out.println(ex); }
        });

        //  VIEW STUDENTS 
        view.addActionListener(e -> {
            try {
                PreparedStatement ps = con.prepareStatement(
                    "SELECT USER_ID, FIRST_NAME, EMAIL FROM USERS WHERE USER_ID LIKE '24STU%'");

                ResultSet rs = ps.executeQuery();

                DefaultTableModel model = new DefaultTableModel();
                model.addColumn("User ID");
                model.addColumn("Name");
                model.addColumn("Email");

                while(rs.next()) {
                    model.addRow(new Object[]{
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3)
                    });
                }

                table.setModel(model);

            } catch(Exception ex){
                System.out.println(ex);
            }
        });
        //  SCORE CARD 
        score.addActionListener(e -> {
            try {
                if(id.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(f,"Enter User ID!");
                    return;
                }

                PreparedStatement ps = con.prepareStatement(
                    "SELECT S.SUBJECT_NAME, T.SCORE " +
                    "FROM TEST_ATTEMPTS T " +
                    "JOIN BATCHES B ON T.BATCH_ID = B.BATCH_ID " +
                    "JOIN SUBJECTS S ON B.SUBJECT_ID = S.SUBJECT_ID " +
                    "WHERE T.S_USER_ID=?");

                ps.setString(1, id.getText());

                ResultSet rs = ps.executeQuery();

                DefaultTableModel model = new DefaultTableModel();
                model.addColumn("Subject");
                model.addColumn("Score");

                int total = 0, count = 0;

                while(rs.next()) {
                    String subject = rs.getString(1);
                    int sc = rs.getInt(2);

                    model.addRow(new Object[]{subject, sc});

                    total += sc;
                    count++;
                }

                if(count == 0) {
                    JOptionPane.showMessageDialog(f,"No records!");
                    return;
                }

                double avg = total / (double) count;

                String grade;
                if(avg>=90) grade="A";
                else if(avg>=75) grade="B";
                else if(avg>=50) grade="C";
                else grade="Fail";

                // ADD RESULT ROWS
                model.addRow(new Object[]{"Total", total});
                model.addRow(new Object[]{"Average", avg});
                model.addRow(new Object[]{"Grade", grade});

                table.setModel(model);

            } catch(Exception ex){
                System.out.println(ex);
            }
        });
    }

    void connectDB() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");

            con = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521/XEPDB1",
                "vishalinidb",
                "vishalini");

        } catch(Exception e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        new GUI_CRUD_Score();
    }
}
