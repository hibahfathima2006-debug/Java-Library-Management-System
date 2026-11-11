package src;

import javax.swing.*;
import java.awt.*;

public class LoginPage extends JFrame {

    public LoginPage() {

        setTitle("Library Management System - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel title = new JLabel(
            "LIBRARY MANAGEMENT SYSTEM",
            SwingConstants.CENTER
        );

        title.setFont(new Font("Arial", Font.BOLD, 18));

        JLabel userLabel = new JLabel("Username:");
        JTextField username = new JTextField(15);

        JLabel passLabel = new JLabel("Password:");
        JPasswordField password = new JPasswordField(15);

        JButton loginButton = new JButton("Login");

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 15));

        panel.setBorder(
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        );

        panel.add(userLabel);
        panel.add(username);

        panel.add(passLabel);
        panel.add(password);

        panel.add(new JLabel(""));
        panel.add(loginButton);

        add(title, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);

        loginButton.addActionListener(e -> {

            String user = username.getText();

            String pass = new String(password.getPassword());

            if (user.equals("admin") &&
                pass.equals("admin123")) {

                JOptionPane.showMessageDialog(
                    this, "Login Successful!"
                );

                dispose();

                LibraryApp app = new LibraryApp();
                app.setVisible(true);

            } else {

                JOptionPane.showMessageDialog(
                    this, "Invalid Username or Password!"
                );
            }
        });
    }
}