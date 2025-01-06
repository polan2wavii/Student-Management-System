import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("")
public class StudentManagementApp implements ActionListener {
    private JButton addButton, deleteButton, viewButton, updateButton;
    private JTextField idField, nameField, ageField, gradeField;
    private static final String URL = "jdbc:postgresql://localhost:5432/test";
    private static final String USER = "polanr";
    private static final String PASSWORD = "Mylove$76";
    private Connection conn;

    public static void main(String[] args) {
        new StudentManagementApp().start();
    }

    public void start() {
        try {
            // Establish the database connection once
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to the database!");
            
            // Start the App
            initializeGUI();

        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }
    }

    private void initializeGUI() {
        JFrame frame = new JFrame("Student Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 400);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Label and text field panel
        JLabel idLabel = new JLabel("Student ID:");
        idField = new JTextField();
        idField.setPreferredSize(new Dimension(150, 20)); // Set size of the text field

        JLabel nameLabel = new JLabel("Student Name:");
        nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(150, 20));

        JLabel ageLabel = new JLabel("Student Age:");
        ageField = new JTextField();
        ageField.setPreferredSize(new Dimension(150, 20));

        JLabel gradeLabel = new JLabel("Student Grade:");
        gradeField = new JTextField();
        gradeField.setPreferredSize(new Dimension(150, 20));

        // Add each label and text field to its own sub-panel
        panel.add(createFieldPanel(idLabel, idField));
        panel.add(createFieldPanel(nameLabel, nameField));
        panel.add(createFieldPanel(ageLabel, ageField));
        panel.add(createFieldPanel(gradeLabel, gradeField));

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        addButton = new JButton("Add Student");
        viewButton = new JButton("View Students");
        deleteButton = new JButton("Delete Student");
        updateButton = new JButton("Update Student");

        addButton.addActionListener(this);
        viewButton.addActionListener(this);
        deleteButton.addActionListener(this);
        updateButton.addActionListener(this);

        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);

        // Add panels to the frame
        frame.add(panel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    public StudentManagementApp() {

    }

    // Helper method to create a panel for each label and text field
    private static JPanel createFieldPanel(JLabel label, JTextField textField) {
        JPanel fieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fieldPanel.setBackground(new Color(240, 240, 250)); // Light background
        label.setFont(new Font("Arial", Font.BOLD, 14));    // Custom font
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        fieldPanel.add(label);
        fieldPanel.add(textField);
        return fieldPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            addStudent();
        } else if (e.getSource() == viewButton) {
            viewStudent();
        } else if (e.getSource() == updateButton) {
            updateStudent();
        } else if (e.getSource() == deleteButton) {
            deleteStudent();
        }
    }

    private void addStudent() {
        String sql = "INSERT INTO students (id, name, age, grade) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idField.getText().trim());
            stmt.setString(2, nameField.getText().trim());
            stmt.setInt(3, Integer.parseInt(ageField.getText().trim()));
            stmt.setString(4, gradeField.getText().trim());

            if (stmt.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(null, "Student added successfully!");
                clearFields();
            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(null, "Invalid input for age. Please enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error adding student: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteStudent() {
        String sql = "DELETE FROM Students WHERE id = ?";
        try {
            String id = JOptionPane.showInputDialog("Enter the Student ID to delete:");
            if (id == null || id.trim().isEmpty()) return;

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, id.trim());
                if (stmt.executeUpdate() > 0) {
                    JOptionPane.showMessageDialog(null, "Student deleted successfully!");
                } else {
                    JOptionPane.showMessageDialog(null, "Student not found!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error deleting student: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewStudent() {
        String sql = "SELECT * FROM Students";
        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            StringBuilder students = new StringBuilder("ID\tName\tAge\tGrade\n");
            while (rs.next()) {
                students.append(rs.getString("id")).append("\t")
                        .append(rs.getString("name")).append("\t")
                        .append(rs.getInt("age")).append("\t")
                        .append(rs.getString("grade")).append("\n");
            }
            JOptionPane.showMessageDialog(null, students.toString(), "Students", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error retrieving students: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateStudent() {
        String sql = "UPDATE students SET name = ?, age = ?, grade = ? WHERE id = ?";
        try {
            String id = JOptionPane.showInputDialog("Enter the Student ID to update:");
            if (id == null || id.trim().isEmpty()) return;

            String name = JOptionPane.showInputDialog("Enter new name:");
            String ageStr = JOptionPane.showInputDialog("Enter new age:");
            String grade = JOptionPane.showInputDialog("Enter new grade:");

            if (name == null || ageStr == null || grade == null) return;

            int age = Integer.parseInt(ageStr.trim());

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, name.trim());
                stmt.setInt(2, age);
                stmt.setString(3, grade.trim());
                stmt.setString(4, id.trim());
                if (stmt.executeUpdate() > 0) {
                    JOptionPane.showMessageDialog(null, "Student updated successfully!");
                } else {
                    JOptionPane.showMessageDialog(null, "Student not found!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(null, "Invalid input for age. Please enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error updating student: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        ageField.setText("");
        gradeField.setText("");
    }
}