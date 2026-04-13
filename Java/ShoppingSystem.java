import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ShoppingSystem {

    Connection con;
    int loggedCustomerId = -1;

    JTable cartTable, productTable, orderTable, checkoutTable;

    public ShoppingSystem() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection(
            		"jdbc:oracle:thin:@localhost:1521/XEPDB1",
    	            "vishalinidb",
    	            "vishalini"
            );
            con.setAutoCommit(true);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // ---------------- START ----------------
    void startPage() {
        JFrame f = new JFrame("Online Shopping");
        f.setSize(400,300);
        f.setLayout(new GridLayout(3,1));

        JButton login = new JButton("Customer Login");
        JButton reg = new JButton("Register");
        JButton admin = new JButton("Admin Login");

        f.add(login); f.add(reg); f.add(admin);

        login.addActionListener(e -> customerLogin());
        reg.addActionListener(e -> register());
        admin.addActionListener(e -> adminLogin());

        f.setVisible(true);
    }

    // ---------------- REGISTER ----------------
    void register() {
        JFrame f = new JFrame("Register");
        f.setSize(300,250);
        f.setLayout(new GridLayout(5,2));

        JTextField name = new JTextField();
        JTextField email = new JTextField();
        JTextField phone = new JTextField();
        JTextField pass = new JTextField();

        JButton btn = new JButton("Register");

        f.add(new JLabel("Name")); f.add(name);
        f.add(new JLabel("Email")); f.add(email);
        f.add(new JLabel("Phone")); f.add(phone);
        f.add(new JLabel("Password")); f.add(pass);
        f.add(btn);

        btn.addActionListener(e -> {
            try {

                String n = name.getText().trim();
                String em = email.getText().trim();
                String ph = phone.getText().trim();
                String pw = pass.getText().trim();

                if(n.isEmpty()){
                    JOptionPane.showMessageDialog(f,"Name cannot be empty!");
                    return;
                }

                if(em.isEmpty()){
                    JOptionPane.showMessageDialog(f,"Email cannot be empty!");
                    return;
                }

                if(!em.matches("^[A-Za-z0-9+_.-]+@(.+)$")){
                    JOptionPane.showMessageDialog(f,"Invalid Email!");
                    return;
                }

                if(ph.isEmpty()){
                    JOptionPane.showMessageDialog(f,"Phone cannot be empty!");
                    return;
                }

                if(!ph.matches("\\d{10}")){
                    JOptionPane.showMessageDialog(f,"Phone must be 10 digits!");
                    return;
                }

                if(pw.isEmpty()){
                    JOptionPane.showMessageDialog(f,"Password cannot be empty!");
                    return;
                }

                if(pw.length() < 4){
                    JOptionPane.showMessageDialog(f,"Password too short!");
                    return;
                }

                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO Customers VALUES (?,?,?,?,?)"
                );

                ps.setInt(1,(int)(Math.random()*1000));
                ps.setString(2,n);
                ps.setString(3,em);
                ps.setString(4,ph);
                ps.setString(5,pw);

                ps.executeUpdate();

                JOptionPane.showMessageDialog(f,"Registration Successful!");

            } catch(SQLIntegrityConstraintViolationException ex){
                JOptionPane.showMessageDialog(f,"Email already exists!");
            } catch(Exception ex){
                JOptionPane.showMessageDialog(f,"Error: "+ex.getMessage());
            }
        });
        
        f.setVisible(true);
    }

    // ---------------- LOGIN ----------------
    void customerLogin() {

        String email = JOptionPane.showInputDialog("Email");
        String pass = JOptionPane.showInputDialog("Password");

        // 🔥 EMPTY VALIDATION
        if(email == null || email.trim().isEmpty()){
            JOptionPane.showMessageDialog(null,"Email cannot be empty!");
            return;
        }

        if(pass == null || pass.trim().isEmpty()){
            JOptionPane.showMessageDialog(null,"Password cannot be empty!");
            return;
        }

        try {

            // 🔥 STEP 1: CHECK EMAIL ONLY
            PreparedStatement ps = con.prepareStatement(
                    "SELECT customer_id, password FROM Customers WHERE email=?"
            );

            ps.setString(1,email.trim());
            ResultSet rs = ps.executeQuery();

            //  EMAIL NOT FOUND
            if(!rs.next()){
                JOptionPane.showMessageDialog(null,"Email not registered!");
                return;
            }

            int id = rs.getInt("customer_id");
            String dbPass = rs.getString("password");

            //  PASSWORD WRONG
            if(!dbPass.equals(pass)){
                JOptionPane.showMessageDialog(null,"Incorrect Password!");
                return;
            }

            // ✅ SUCCESS
            loggedCustomerId = id;
            JOptionPane.showMessageDialog(null,"Login Successful!");
            customerDashboard();

        } catch(Exception e){
            JOptionPane.showMessageDialog(null,"Login Error: " + e.getMessage());
        }
    }

    void adminLogin() {
        String u = JOptionPane.showInputDialog("Admin Username");
        String p = JOptionPane.showInputDialog("Password");

        if(u.equals("admin")){
        	if(p.equals("admin123")){
        		adminDashboard();
        	}
        	else {
        		JOptionPane.showMessageDialog(null,"Pasword is wrong");
        		}
        	}
        else {
            JOptionPane.showMessageDialog(null,"User id is wrong");
        }
    }

    // ---------------- ADMIN ----------------
    void adminDashboard() {

        JFrame f = new JFrame("Admin");
        f.setSize(700,400);

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Products", productPanel());
        tabs.add("Orders", ordersPanel());

        JButton logout = new JButton("Logout");

        f.add(tabs,BorderLayout.CENTER);
        f.add(logout,BorderLayout.SOUTH);

        logout.addActionListener(e -> {
            f.dispose();
            startPage();
        });

        f.setVisible(true);

        new Timer(2000,e->{
            loadProducts(productTable);
            loadOrders(orderTable);
        }).start();
    }

    JPanel productPanel() {

        JPanel panel = new JPanel(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(5,2,10,10));

        JTextField name = new JTextField();
        JTextField cat = new JTextField();
        JTextField price = new JTextField();
        JTextField stock = new JTextField();

        JButton add = new JButton("Add Product");

        form.add(new JLabel("Product Name:"));
        form.add(name);
        form.add(new JLabel("Category:"));
        form.add(cat);
        form.add(new JLabel("Price:"));
        form.add(price);
        form.add(new JLabel("Stock Quantity:"));
        form.add(stock);
        form.add(new JLabel(""));
        form.add(add);

        productTable = new JTable();

        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(productTable), BorderLayout.CENTER);

        add.addActionListener(e -> {
            try {

                String productName = name.getText().trim();
                String category = cat.getText().trim();
                String priceStr = price.getText().trim();
                String stockStr = stock.getText().trim();

                // 🔥 VALIDATIONS
                if(productName.isEmpty()){
                    JOptionPane.showMessageDialog(null,"Product name required!");
                    return;
                }

                if(category.isEmpty()){
                    JOptionPane.showMessageDialog(null,"Category required!");
                    return;
                }

                if(priceStr.isEmpty()){
                    JOptionPane.showMessageDialog(null,"Price required!");
                    return;
                }

                if(stockStr.isEmpty()){
                    JOptionPane.showMessageDialog(null,"Stock required!");
                    return;
                }

                double priceVal;
                int stockVal;

                try {
                    priceVal = Double.parseDouble(priceStr);
                } catch(Exception ex){
                    JOptionPane.showMessageDialog(null,"Price must be a valid number!");
                    return;
                }

                try {
                    stockVal = Integer.parseInt(stockStr);
                } catch(Exception ex){
                    JOptionPane.showMessageDialog(null,"Stock must be an integer!");
                    return;
                }

                if(priceVal <= 0){
                    JOptionPane.showMessageDialog(null,"Price must be greater than 0!");
                    return;
                }

                if(stockVal < 0){
                    JOptionPane.showMessageDialog(null,"Stock cannot be negative!");
                    return;
                }

                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO Products VALUES (?,?,?,?,?)"
                );

                ps.setInt(1,(int)(Math.random()*1000));
                ps.setString(2,productName);
                ps.setString(3,category);
                ps.setDouble(4,priceVal);
                ps.setInt(5,stockVal);

                ps.executeUpdate();

                JOptionPane.showMessageDialog(null,"Product Added Successfully!");
                loadProducts(productTable);

            } catch(Exception ex){
                JOptionPane.showMessageDialog(null,"Error: " + ex.getMessage());
            }
        });

        loadProducts(productTable);
        return panel;
    }

    JPanel ordersPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        orderTable = new JTable();

        panel.add(new JScrollPane(orderTable), BorderLayout.CENTER);

        loadOrders(orderTable);

        return panel;
    }

    // ---------------- CUSTOMER ----------------
    void customerDashboard() {

        JFrame f = new JFrame("Customer");
        f.setSize(700,400);

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Products", customerProductsPanel());
        tabs.add("Cart", cartPanel());
        tabs.add("Checkout", checkoutPanel());

        JButton logout = new JButton("Logout");

        f.add(tabs,BorderLayout.CENTER);
        f.add(logout,BorderLayout.SOUTH);

        logout.addActionListener(e -> {
            f.dispose();
            startPage();
        });
        loadProducts(productTable);
        f.setVisible(true);

        new Timer(5000,e->{
            loadCart(cartTable);
            loadCart(checkoutTable);
            
        }).start();
    }

    JPanel customerProductsPanel() {

        JPanel p = new JPanel(new BorderLayout());
        productTable = new JTable();

        JButton add = new JButton("Add to Cart");

        p.add(new JScrollPane(productTable));
        p.add(add,BorderLayout.SOUTH);

        add.addActionListener(e->{
            try{
                int row = productTable.getSelectedRow();
                if(row==-1){
                    JOptionPane.showMessageDialog(null,"Select Product");
                    return;
                }

                int pid = (int)productTable.getValueAt(row,0);
                int qty;

                try {
                    qty = Integer.parseInt(JOptionPane.showInputDialog("Qty"));
                } catch(Exception ex){
                    JOptionPane.showMessageDialog(null,"Enter valid quantity!");
                    return;
                }

                if(qty <= 0){
                    JOptionPane.showMessageDialog(null,"Quantity must be > 0!");
                    return;
                }
                
                int cartId = getCartId();

                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO Cart_Items VALUES (?,?,?,?)"
                );

                ps.setInt(1,(int)(Math.random()*1000));
                ps.setInt(2,cartId);
                ps.setInt(3,pid);
                ps.setInt(4,qty);

                ps.executeUpdate();

            }catch(Exception ex){ System.out.println(ex); }
        });

        loadProducts(productTable);
        return p;
    }

    JPanel cartPanel() {

        JPanel p = new JPanel(new BorderLayout());
        cartTable = new JTable();

        JButton remove = new JButton("Remove");
        JButton update = new JButton("Update Qty");

        JPanel btn = new JPanel();
        btn.add(remove);
        btn.add(update);

        p.add(new JScrollPane(cartTable));
        p.add(btn,BorderLayout.SOUTH);

        remove.addActionListener(e->removeItem());
        update.addActionListener(e->updateQty());
        
        loadCart(cartTable);
        return p;
    }

    void removeItem(){
        try{
            int row = cartTable.getSelectedRow();

            if(row == -1){
                JOptionPane.showMessageDialog(null,"Select item first!");
                return;
            }

            int productId = (int) cartTable.getValueAt(row,0);
            int cartId = getCartId();

            PreparedStatement ps = con.prepareStatement(
                "DELETE FROM Cart_Items WHERE cart_id=? AND product_id=?"
            );

            ps.setInt(1,cartId);
            ps.setInt(2,productId);
            ps.executeUpdate();

            loadCart(cartTable);

        }catch(Exception e){ System.out.println(e); }
    }

    void updateQty(){
        try{
            int row = cartTable.getSelectedRow();

            if(row == -1){
                JOptionPane.showMessageDialog(null,"Select item first!");
                return;
            }

            int productId = (int) cartTable.getValueAt(row,0);
            int cartId = getCartId();

            int qty;

            try {
                qty = Integer.parseInt(JOptionPane.showInputDialog("New Qty"));
            } catch(Exception ex){
                JOptionPane.showMessageDialog(null,"Invalid number!");
                return;
            }

            if(qty <= 0){
                JOptionPane.showMessageDialog(null,"Quantity must be positive!");
                return;
            }
            
            PreparedStatement ps = con.prepareStatement(
                "UPDATE Cart_Items SET quantity=? WHERE cart_id=? AND product_id=?"
            );

            ps.setInt(1,qty);
            ps.setInt(2,cartId);
            ps.setInt(3,productId);
            ps.executeUpdate();

            loadCart(cartTable);

        }catch(Exception e){ System.out.println(e); }
    }

    JPanel checkoutPanel() {

        JPanel p = new JPanel(new BorderLayout());
        checkoutTable = new JTable();
        JButton checkout = new JButton("Checkout");

        p.add(new JScrollPane(checkoutTable));
        p.add(checkout,BorderLayout.SOUTH);

        loadCart(checkoutTable);

        checkout.addActionListener(e->{
            try{

                // ✅ STEP 1: CONFIRM ORDER
                int confirm = JOptionPane.showConfirmDialog(
                        null,
                        "Do you want to place the order?",
                        "Confirm Order",
                        JOptionPane.YES_NO_OPTION
                );

                if(confirm != JOptionPane.YES_OPTION){
                    return;
                }

                // ✅ STEP 2: GET ADDRESS
                String address = JOptionPane.showInputDialog("Enter Delivery Address");

                if(address == null || address.trim().isEmpty()){
                    JOptionPane.showMessageDialog(null,"Address cannot be empty!");
                    return;
                }

                int cartId = getCartId();
                int orderId = (int)(Math.random()*1000);
                double total = 0;

                // 👉 Get Customer Name
                String custName = "";
                PreparedStatement custPs = con.prepareStatement(
                    "SELECT name FROM Customers WHERE customer_id=?"
                );
                custPs.setInt(1, loggedCustomerId);
                ResultSet custRs = custPs.executeQuery();
                if(custRs.next()){
                    custName = custRs.getString(1);
                }

                // 👉 Fetch Cart Items
                PreparedStatement ps = con.prepareStatement(
                    "SELECT ci.product_id, p.product_name, ci.quantity, p.price " +
                    "FROM Cart_Items ci JOIN Products p ON ci.product_id=p.product_id WHERE ci.cart_id=?"
                );

                ps.setInt(1,cartId);
                ResultSet rs = ps.executeQuery();

                StringBuilder bill = new StringBuilder();

                bill.append("\n========== 🧾 BILL ==========\n");
                bill.append("Customer Name : ").append(custName).append("\n");
                bill.append("Address       : ").append(address).append("\n");
                bill.append("Payment Mode  : Cash on Delivery\n");
                bill.append("---------------------------------\n");
                bill.append(String.format("%-15s %-5s %-10s\n","Product","Qty","Amount"));
                bill.append("---------------------------------\n");

                // 👉 Calculate total + build bill
                while(rs.next()){
                    String pname = rs.getString("product_name");
                    int qty = rs.getInt("quantity");
                    double price = rs.getDouble("price");

                    double amt = qty * price;
                    total += amt;

                    bill.append(String.format("%-15s %-5d %-10.2f\n",pname,qty,amt));
                }

                bill.append("---------------------------------\n");
                bill.append("TOTAL AMOUNT : ").append(String.format("%.2f",total)).append("\n");
                bill.append("=================================\n");

                // 👉 Insert Order
                PreparedStatement order = con.prepareStatement(
                    "INSERT INTO Orders VALUES (?,?,SYSDATE,?,?,?)"
                );

                order.setInt(1,orderId);
                order.setInt(2,loggedCustomerId);
                order.setDouble(3,total);
                order.setString(4,"Placed");
                order.setString(5,address);
                order.executeUpdate();

                // 👉 Re-execute for order items
                rs = ps.executeQuery();

                while(rs.next()){
                    int pid = rs.getInt("product_id");
                    int qty = rs.getInt("quantity");
                    double price = rs.getDouble("price");

                    PreparedStatement oi = con.prepareStatement(
                        "INSERT INTO Order_Items VALUES (?,?,?,?,?)"
                    );

                    oi.setInt(1,(int)(Math.random()*1000));
                    oi.setInt(2,orderId);
                    oi.setInt(3,pid);
                    oi.setInt(4,qty);
                    oi.setDouble(5,price);
                    oi.executeUpdate();

                    // STOCK UPDATE
                    PreparedStatement stock = con.prepareStatement(
                        "UPDATE Products SET stock_quantity = stock_quantity - ? WHERE product_id=?"
                    );

                    stock.setInt(1,qty);
                    stock.setInt(2,pid);
                    stock.executeUpdate();
                }

                // 👉 CLEAR CART
                PreparedStatement clear = con.prepareStatement(
                    "DELETE FROM Cart_Items WHERE cart_id=?"
                );

                clear.setInt(1,cartId);
                clear.executeUpdate();

                loadCart(cartTable);
                loadCart(checkoutTable);
                loadProducts(productTable);

                // ✅ SHOW BILL (PROPER FORMAT)
                JTextArea area = new JTextArea(bill.toString());
                area.setFont(new Font("Monospaced", Font.PLAIN, 12));
                JOptionPane.showMessageDialog(null, new JScrollPane(area), "Bill", JOptionPane.INFORMATION_MESSAGE);
                
                JOptionPane.showMessageDialog(
                	    null,
                	    "Order Placed Successfully!\n\nThank you for shopping!\nVisit Again!",
                	    "Success",
                	    JOptionPane.INFORMATION_MESSAGE
                	);

            }catch(Exception ex){
                ex.printStackTrace();
            }
        });

        return p;
    }

    // ---------------- LOAD ----------------
    void loadProducts(JTable t){
        try{
            ResultSet rs=con.createStatement().executeQuery("SELECT * FROM Products");
            DefaultTableModel m=new DefaultTableModel(new String[]{"ID","Name","Category","Price","Stock"},0);
            while(rs.next()){
                m.addRow(new Object[]{rs.getInt(1),rs.getString(2),rs.getString(3),rs.getDouble(4),rs.getInt(5)});
            }
            t.setModel(m);
        }catch(Exception e){}
    }

    void loadCart(JTable t){
        try{
            PreparedStatement ps=con.prepareStatement(
                "SELECT p.product_id, p.product_name, ci.quantity " +
                "FROM Cart_Items ci JOIN Cart c ON ci.cart_id=c.cart_id " +
                "JOIN Products p ON ci.product_id=p.product_id " +
                "WHERE c.customer_id=?"
            );

            ps.setInt(1,loggedCustomerId);
            ResultSet rs=ps.executeQuery();

            DefaultTableModel m=new DefaultTableModel(
                new String[]{"ID","Product","Qty"},0
            );

            while(rs.next()){
                m.addRow(new Object[]{
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getInt(3)
                });
            }

            t.setModel(m);

        }catch(Exception e){}
    }

    void loadOrders(JTable t){
        try{
            ResultSet rs=con.createStatement().executeQuery("SELECT * FROM Orders");
            DefaultTableModel m=new DefaultTableModel(new String[]{"ID","Cust","Total","Status"},0);
            while(rs.next()){
                m.addRow(new Object[]{rs.getInt(1),rs.getInt(2),rs.getDouble(4),rs.getString(5)});
            }
            t.setModel(m);
        }catch(Exception e){}
    }

    int getCartId() throws Exception{
        PreparedStatement ps=con.prepareStatement("SELECT cart_id FROM Cart WHERE customer_id=?");
        ps.setInt(1,loggedCustomerId);
        ResultSet rs=ps.executeQuery();

        if(rs.next()) return rs.getInt(1);

        int id=(int)(Math.random()*1000);
        PreparedStatement ps2=con.prepareStatement("INSERT INTO Cart VALUES (?,?,SYSDATE)");
        ps2.setInt(1,id);
        ps2.setInt(2,loggedCustomerId);
        ps2.executeUpdate();
        return id;
    }

    public static void main(String[] args){
        new ShoppingSystem().startPage();
    }
}
