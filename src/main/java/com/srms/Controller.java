package com.srms;

import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;

/**
 * Controller - Handles all CRUD operations.
 * year_level is stored and retrieved as INT matching the database schema.
 */
public class Controller {

    // ── FXML Fields ───────────────────────────────────────────────────────
    @FXML private TextField            txtName;
    @FXML private TextField            txtCourse;
    @FXML private ChoiceBox<YearLevel> cbYear;
    @FXML private Label                lblStatus;

    @FXML private TableView<Student>             table;
    @FXML private TableColumn<Student, Integer>  colId;
    @FXML private TableColumn<Student, String>   colName;
    @FXML private TableColumn<Student, String>   colCourse;
    @FXML private TableColumn<Student, Integer>  colYear;  // INT column

    // ── State ─────────────────────────────────────────────────────────────
    private ObservableList<Student> list = FXCollections.observableArrayList();
    private Connection conn;
    private int selectedId = -1;

    // ── Initialization ────────────────────────────────────────────────────

    @FXML
    public void initialize() {
        conn = DBConnection.connect();
        if (conn == null) {
            setStatus("ERROR: Could not connect to database. Check DBConnection.java.", true);
            return;
        }

        // Load enum values into ChoiceBox
        cbYear.getItems().setAll(YearLevel.values());

        // Bind columns — colYear uses IntegerProperty
        colId.setCellValueFactory(    data -> data.getValue().idProperty().asObject());
        colName.setCellValueFactory(  data -> data.getValue().nameProperty());
        colCourse.setCellValueFactory(data -> data.getValue().courseProperty());
        colYear.setCellValueFactory(  data -> data.getValue().yearLevelProperty().asObject());

        loadData();

        // Row click → populate form fields
        table.setOnMouseClicked(e -> {
            Student s = table.getSelectionModel().getSelectedItem();
            if (s != null) {
                selectedId = s.getId();
                txtName.setText(s.getName());
                txtCourse.setText(s.getCourse());

                // Convert int back to enum for the ChoiceBox
                cbYear.setValue(YearLevel.fromInt(s.getYearLevel()));

                setStatus("Selected: " + s.getName(), false);
            }
        });
    }

    // ── CRUD Operations ───────────────────────────────────────────────────

    /** INSERT - Add a new student record. */
    @FXML
    private void addStudent() {
        if (!validateInputs()) return;

        try {
            String query = "INSERT INTO students(name, course, year_level) VALUES (?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, txtName.getText().trim());
            pst.setString(2, txtCourse.getText().trim());
            pst.setInt(3, cbYear.getValue().getValue());  // store INT
            pst.executeUpdate();

            setStatus("Student added successfully.", false);
            loadData();
            clearFields();

        } catch (Exception e) {
            setStatus("Add failed: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    /** UPDATE - Update the selected student record. */
    @FXML
    private void updateStudent() {
        if (selectedId == -1) {
            setStatus("Please select a student from the table first.", true);
            return;
        }
        if (!validateInputs()) return;

        try {
            String query = "UPDATE students SET name=?, course=?, year_level=? WHERE id=?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, txtName.getText().trim());
            pst.setString(2, txtCourse.getText().trim());
            pst.setInt(3, cbYear.getValue().getValue());  // store INT
            pst.setInt(4, selectedId);
            pst.executeUpdate();

            setStatus("Student updated successfully.", false);
            loadData();
            clearFields();

        } catch (Exception e) {
            setStatus("Update failed: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    /** DELETE - Delete the selected student record. */
    @FXML
    private void deleteStudent() {
        if (selectedId == -1) {
            setStatus("Please select a student from the table first.", true);
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Student");
        confirm.setHeaderText(null);
        confirm.setContentText("Are you sure you want to delete this record?");
        if (confirm.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) return;

        try {
            String query = "DELETE FROM students WHERE id=?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, selectedId);
            pst.executeUpdate();

            setStatus("Student deleted successfully.", false);
            loadData();
            clearFields();

        } catch (Exception e) {
            setStatus("Delete failed: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    /** CLEAR - Reset all input fields. */
    @FXML
    private void clearFields() {
        txtName.clear();
        txtCourse.clear();
        cbYear.setValue(null);
        selectedId = -1;
        table.getSelectionModel().clearSelection();
        setStatus("Fields cleared.", false);
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    /** Load all records from database into the TableView. */
    private void loadData() {
        list.clear();
        try {
            String query = "SELECT * FROM students ORDER BY id";
            ResultSet rs = conn.createStatement().executeQuery(query);
            while (rs.next()) {
                list.add(new Student(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("course"),
                    rs.getInt("year_level")   // read as INT
                ));
            }
            table.setItems(list);
        } catch (Exception e) {
            setStatus("Load failed: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    /** Validate that all fields are filled before any DB operation. */
    private boolean validateInputs() {
        if (txtName.getText().trim().isEmpty()) {
            setStatus("Name cannot be empty.", true);
            return false;
        }
        if (txtCourse.getText().trim().isEmpty()) {
            setStatus("Course cannot be empty.", true);
            return false;
        }
        if (cbYear.getValue() == null) {
            setStatus("Please select a year level.", true);
            return false;
        }
        return true;
    }

    private void setStatus(String message, boolean isError) {
        lblStatus.setText(message);
        lblStatus.setStyle(isError
            ? "-fx-text-fill: red;"
            : "-fx-text-fill: green;"
        );
    }
}
