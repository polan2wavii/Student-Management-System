import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class StudentManagementApp implements ActionListener {

    private JButton addButton, deleteButton, viewButton, updateButton;
    private JTextField idField, nameField, ageField, gradeField;
    private ArrayList<Student> students = new ArrayList<>();

    public static void main(String[] args) {
        new StudentManagementApp();
    }

    public StudentManagementApp() {
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

    // Helper method to create a panel for each label and text field
    private static JPanel createFieldPanel(JLabel label, JTextField textField) {
        JPanel fieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Align to the left
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
        try {
            String name = nameField.getText().trim();
            int age = Integer.parseInt(ageField.getText().trim());
            String grade = gradeField.getText().trim();
            String id = idField.getText().trim();

            if (id == null || name.isEmpty() || grade.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Student student = new Student(name, age, grade, id);
            students.add(student);

            idField.setText("");
            nameField.setText("");
            ageField.setText("");
            gradeField.setText("");

            JOptionPane.showMessageDialog(null, "Student Successfully Added!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid Age! Please enter a valid age", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteStudent() {
        String idToDelete = JOptionPane.showInputDialog(null, "Enter the Student ID to delete:", 
                                                    "Delete Student", JOptionPane.QUESTION_MESSAGE);

        if (idToDelete == null || idToDelete.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "No ID entered. Deletion cancelled.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean found = false;

        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId().equals(idToDelete.trim())) {
                students.remove(i);
                found = true;
                break;
            }
        }

        if (found) {
            JOptionPane.showMessageDialog(null, "Student with ID " + idToDelete + " deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Student with ID " + idToDelete + " not found!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewStudent() {
        if (students.isEmpty()) {
           JOptionPane.showMessageDialog(null, "No students found!", "Info", JOptionPane.INFORMATION_MESSAGE);
            return; 
        }
        

        StringBuilder studentList = new StringBuilder("Student List:\n");
        for (Student student : students) {
            studentList.append(student).append("\n");
        }

        JOptionPane.showMessageDialog(null, studentList.toString(), "Students", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateStudent() {

    }
}