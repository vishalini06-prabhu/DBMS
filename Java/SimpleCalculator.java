import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SimpleCal implements ActionListener {
    JFrame frame;
    Button add, sub, div, mul, pow, tan, root, exp, clear;
    TextField a, b, res;
    Label num1, num2, title, result;

    SimpleCal() {
        frame = new JFrame("Simple Calculator");
        frame.setSize(370, 350);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        title = new Label("Simple Calculator");
        title.setBounds(120, 20, 120, 25);
        title.setFont(new Font("Arial", Font.BOLD, 14));
        frame.add(title);

        num1 = new Label("Number1");
        num1.setBounds(20, 50, 60, 25);
        num1.setFont(new Font("Arial", Font.BOLD, 14));
        a = new TextField();
        a.setBounds(120, 50, 195, 25);
        frame.add(num1);
        frame.add(a);

        num2 = new Label("Number2");
        num2.setBounds(20, 80, 62, 25);
        num2.setFont(new Font("Arial", Font.BOLD, 14));
        b = new TextField();
        b.setBounds(120, 80, 195, 25);
        frame.add(num2);
        frame.add(b);

        result = new Label("Result");
        result.setFont(new Font("Arial", Font.BOLD, 14));
        result.setBounds(20, 110, 50, 30);
        res = new TextField();
        res.setBounds(120, 110, 195, 25);
        frame.add(result);
        frame.add(res);

        add = new Button("ADD");
        add.setFont(new Font("Arial", Font.BOLD, 14));
        add.addActionListener(this);
        add.setBounds(20, 150, 50, 30);
        frame.add(add);

        sub = new Button("SUB");
        sub.setFont(new Font("Arial", Font.BOLD, 14));
        sub.addActionListener(this);
        sub.setBounds(100, 150, 50, 30);
        frame.add(sub);

        div = new Button("DIV");
        div.setFont(new Font("Arial", Font.BOLD, 14));
        div.addActionListener(this);
        div.setBounds(180, 150, 50, 30);
        frame.add(div);

        mul = new Button("MUL");
        mul.setFont(new Font("Arial", Font.BOLD, 14));
        mul.addActionListener(this);
        mul.setBounds(260, 150, 50, 30);
        frame.add(mul);

        tan = new Button("TAN");
        tan.setFont(new Font("Arial", Font.BOLD, 14));
        tan.addActionListener(this);
        tan.setBounds(20, 200, 50, 30);
        frame.add(tan);

        exp = new Button("e");
        exp.setFont(new Font("Arial", Font.BOLD, 19));
        exp.addActionListener(this);
        exp.setBounds(100, 200, 50, 30);
        frame.add(exp);

        pow = new Button("X^Y");
        pow.setFont(new Font("Arial", Font.BOLD, 14));
        pow.addActionListener(this);
        pow.setBounds(180, 200, 50, 30);
        frame.add(pow);

        root = new Button("SQRT");
        root.setFont(new Font("Arial", Font.BOLD, 14));
        root.addActionListener(this);
        root.setBounds(260, 200, 50, 30);
        frame.add(root);

        clear = new Button("CLEAR");
        clear.setFont(new Font("Arial", Font.BOLD, 14));
        clear.addActionListener(this);
        clear.setBounds(130, 250, 70, 30);
        frame.add(clear);

        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        double val1 = 0.0, val2 = 0.0;
        try {
        	if(!a.getText().equals("")) {
        		val1 = Double.parseDouble(a.getText());
        	}
        	if(!b.getText().equals("")) {
        		val2 = Double.parseDouble(b.getText());
        	}  
        } catch (Exception ex) {
            res.setText("Invalid Input");
            return;
        }

        if (e.getSource() == add) {
            double v3 = val1 + val2;
            res.setText(String.valueOf(v3));
        } else if (e.getSource() == sub) {
            double v3 = val1 - val2;
            res.setText(String.valueOf(v3));
        } else if (e.getSource() == mul) {
            double v3 = val1 * val2;
            res.setText(String.valueOf(v3));
        } else if (e.getSource() == div) {
            if (val2 != 0) {
                double v3 = val1 / val2;
                res.setText(String.valueOf(v3));
            } else {
                res.setText("Cannot divide by 0");
            }
        } else if (e.getSource() == tan) {
            double tan1 = Math.tan(Math.toRadians(val1));
            res.setText("Tan1: " + tan1 );
        } else if (e.getSource() == pow) {
            double v3 = Math.pow(val1, val2);
            res.setText(String.valueOf(v3));
        } else if (e.getSource() == root) {
            double v3 = Math.sqrt(val1);
            res.setText(String.valueOf(v3));
        } else if (e.getSource() == exp) {
            double v3 = Math.exp(val1);
            res.setText(String.valueOf(v3));
        } else if (e.getSource() == clear) {
            a.setText("");
            b.setText("");
            res.setText("");
        }
    }

    public static void main(String[] args) {
        new SimpleCal();
    }
}
