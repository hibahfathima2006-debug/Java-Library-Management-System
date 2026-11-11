package src;


import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LibraryApp extends JFrame {

    public LibraryApp() {
        setTitle("Library Management System");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel title = new JLabel(
            "LIBRARY MANAGEMENT SYSTEM",
            SwingConstants.CENTER
        );
        title.setFont(new Font("Arial", Font.BOLD, 24));

        JButton books = new JButton("Manage Books");
        JButton members = new JButton("Manage Members");
        JButton issue = new JButton("Issue Book");
        JButton returns = new JButton("Return Book");
        JButton records = new JButton("View Records");

        JPanel buttons = new JPanel(new GridLayout(5, 1, 10, 10));
        buttons.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));

        buttons.add(books);
        buttons.add(members);
        buttons.add(issue);
        buttons.add(returns);
        buttons.add(records);

        add(title, BorderLayout.NORTH);
        add(buttons, BorderLayout.CENTER);

        books.addActionListener(e -> manageBooks());

        members.addActionListener(e -> manageMembers());

        issue.addActionListener(e -> issueBook());
        
        returns.addActionListener(e -> returnBook());

        records.addActionListener(e -> viewRecords());
    }

    private void manageBooks() {
        String[] options = {"Add Book", "View Books", "Cancel"};

        int choice = JOptionPane.showOptionDialog(
            this,
            "Choose an action",
            "Manage Books",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );

        if (choice == 0) {
            addBook();
        } else if (choice == 1) {
            viewBooks();
        }
    }

    private void addBook() {
        try {
            String title = JOptionPane.showInputDialog(this, "Enter book title:");
            if (title == null || title.trim().isEmpty()) return;

            String author = JOptionPane.showInputDialog(this, "Enter author:");
            if (author == null || author.trim().isEmpty()) return;

            String category = JOptionPane.showInputDialog(this, "Enter category:");
            if (category == null || category.trim().isEmpty()) return;

            String qty = JOptionPane.showInputDialog(this, "Enter quantity:");
            if (qty == null) return;

            int quantity = Integer.parseInt(qty);
            if (quantity < 0) {
                JOptionPane.showMessageDialog(this, "Quantity cannot be negative.");
                return;
            }

            String sql = "INSERT INTO books (title, author, category, quantity) VALUES (?, ?, ?, ?)";

            try (Connection con = DBConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, title.trim());
                ps.setString(2, author.trim());
                ps.setString(3, category.trim());
                ps.setInt(4, quantity);
                ps.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "Book added successfully!");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid whole number for quantity.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Could not add book: " + ex.getMessage());
        }
    }

    private void viewBooks() {
        String sql = "SELECT * FROM books";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            String[] columns = {"Book ID", "Title", "Author", "Category", "Quantity"};
            javax.swing.table.DefaultTableModel model =
                new javax.swing.table.DefaultTableModel(columns, 0);

            while (rs.next()) {
                model.addRow(new Object[] {
                    rs.getInt("book_id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("category"),
                    rs.getInt("quantity")
                });
            }

            JTable table = new JTable(model);
            JScrollPane scroll = new JScrollPane(table);
            scroll.setPreferredSize(new Dimension(600, 300));

            JOptionPane.showMessageDialog(
                this, scroll, "Books in Library", JOptionPane.PLAIN_MESSAGE
            );

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Could not view books: " + ex.getMessage());
        }
    }

private void manageMembers() {
    String[] options = {"Add Member", "View Members", "Cancel"};

    int choice = JOptionPane.showOptionDialog(
        this,
        "Choose an action",
        "Manage Members",
        JOptionPane.DEFAULT_OPTION,
        JOptionPane.QUESTION_MESSAGE,
        null,
        options,
        options[0]
    );

    if (choice == 0) {
        addMember();
    } else if (choice == 1) {
        viewMembers();
    }
}

private void addMember() {
    try {
        String name = JOptionPane.showInputDialog(this, "Enter member name:");
        if (name == null || name.trim().isEmpty()) return;

        String department = JOptionPane.showInputDialog(this, "Enter department:");
        if (department == null || department.trim().isEmpty()) return;

        String phone = JOptionPane.showInputDialog(this, "Enter phone number:");
        if (phone == null || phone.trim().isEmpty()) return;

        String sql = "INSERT INTO members (name, department, phone) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name.trim());
            ps.setString(2, department.trim());
            ps.setString(3, phone.trim());

            ps.executeUpdate();
        }

        JOptionPane.showMessageDialog(this, "Member added successfully!");

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
    }
}

private void viewMembers() {
    String sql = "SELECT * FROM members";

    try (Connection con = DBConnection.getConnection();
         Statement st = con.createStatement();
         ResultSet rs = st.executeQuery(sql)) {

        String[] columns = {"Member ID", "Name", "Department", "Phone"};

        javax.swing.table.DefaultTableModel model =
            new javax.swing.table.DefaultTableModel(columns, 0);

        while (rs.next()) {
            model.addRow(new Object[] {
                rs.getInt("member_id"),
                rs.getString("name"),
                rs.getString("department"),
                rs.getString("phone")
            });
        }

        JTable table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(550, 300));

        JOptionPane.showMessageDialog(
            this, scroll, "Registered Members", JOptionPane.PLAIN_MESSAGE
        );

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
    }
}

private void issueBook() {
    try {
        String bookIdText =
            JOptionPane.showInputDialog(this, "Enter Book ID:");

        if (bookIdText == null) return;

        String memberIdText =
            JOptionPane.showInputDialog(this, "Enter Member ID:");

        if (memberIdText == null) return;

        int bookId = Integer.parseInt(bookIdText.trim());
        int memberId = Integer.parseInt(memberIdText.trim());

        String sql = "INSERT INTO issue_records " +
                     "(book_id, member_id, issue_date, status) " +
                     "VALUES (?, ?, CURDATE(), 'Issued')";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, bookId);
            ps.setInt(2, memberId);
            ps.executeUpdate();
        }

        JOptionPane.showMessageDialog(
            this, "Book issued successfully!"
        );

    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(
            this, "Please enter valid numeric IDs."
        );
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(
            this, "Could not issue book: " + ex.getMessage()
        );
    }
}
private void returnBook() {
    try {
        String bookText = JOptionPane.showInputDialog(
            this, "Enter Book ID:"
        );

        if (bookText == null) return;

        String memberText = JOptionPane.showInputDialog(
            this, "Enter Member ID:"
        );

        if (memberText == null) return;

        int bookId = Integer.parseInt(bookText.trim());
        int memberId = Integer.parseInt(memberText.trim());

        String sql = "UPDATE issue_records " +
                     "SET return_date = CURDATE(), status = 'Returned' " +
                     "WHERE book_id = ? AND member_id = ? " +
                     "AND status = 'Issued' LIMIT 1";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, bookId);
            ps.setInt(2, memberId);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(
                    this, "Book returned successfully!"
                );
            } else {
                JOptionPane.showMessageDialog(
                    this, "No active issue found for these IDs."
                );
            }
        }

    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(
            this, "Please enter valid numeric IDs."
        );
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(
            this, "Could not return book: " + ex.getMessage()
        );
    }
}
private void viewRecords() {
    String sql =
        "SELECT i.issue_id, b.title, m.name, " +
        "i.issue_date, i.return_date, i.status " +
        "FROM issue_records i " +
        "JOIN books b ON i.book_id = b.book_id " +
        "JOIN members m ON i.member_id = m.member_id";

    try (Connection con = DBConnection.getConnection();
         Statement st = con.createStatement();
         ResultSet rs = st.executeQuery(sql)) {

        String[] columns = {
            "Issue ID", "Book Title", "Member Name",
            "Issue Date", "Return Date", "Status"
        };

        javax.swing.table.DefaultTableModel model =
            new javax.swing.table.DefaultTableModel(columns, 0);

        while (rs.next()) {
            model.addRow(new Object[] {
                rs.getInt("issue_id"),
                rs.getString("title"),
                rs.getString("name"),
                rs.getDate("issue_date"),
                rs.getDate("return_date"),
                rs.getString("status")
            });
        }

        JTable table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(750, 300));

        JOptionPane.showMessageDialog(
            this, scroll, "Library Issue Records",
            JOptionPane.PLAIN_MESSAGE
        );

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(
            this, "Could not view records: " + ex.getMessage()
        );
    }
}
    public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        LoginPage login = new LoginPage();
        login.setVisible(true);
    });
    }
}
