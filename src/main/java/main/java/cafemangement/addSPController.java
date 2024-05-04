package main.java.cafemangement;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.java.cafemangement.mysqlconnect;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class addSPController {

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;

    @FXML
    private ComboBox<String> comboBox;

    @FXML
    private Button browseButton;

    @FXML
    private ImageView MonAnImage_Display;

    @FXML
    private TextField imageLink_Field; // Add this line

    @FXML
    private Button GoBackButton;
    @FXML
    private Button Add_Edit_MonAnButton;

    @FXML
    private TextField TenMonAn_Field;

    @FXML
    private TextField MoTaSP_Field;

    @FXML
    private TextField GiaBan_Field;

    @FXML
    public void initialize() {
        ObservableList<String> items = FXCollections.observableArrayList();
        GoBackButton.setOnAction(this::handleGoBack);
        try {
            // Establish a database connection
            conn = mysqlconnect.ConnectDb();

            // Define your SQL query
            String sql = "SELECT * FROM category";

            // Create a PreparedStatement
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            // Execute the query and get the result set
            ResultSet resultSet = preparedStatement.executeQuery();

            // Add data from the result set to the ObservableList
            while (resultSet.next()) {
                String categoryName = resultSet.getString("cate_name");
                items.add(categoryName);
            }

            // Set the items to the ComboBox
            comboBox.setItems(items);

            // Close the resources
//            resultSet.close();
//            preparedStatement.close();
//            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Define a method to handle the button click event
    @FXML
    private void browseButtonClicked(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.jpeg")
        );

        // Show open file dialog
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            // Load the selected image into the ImageView
            Image image = new Image(selectedFile.toURI().toString());
            MonAnImage_Display.setImage(image);

            // Set only the file name in the TextField
            String fileName = selectedFile.getName();
            imageLink_Field.setText(fileName);
        }
    }

    @FXML
    private void AddSP(ActionEvent event) {
        try {
            conn = mysqlconnect.ConnectDb();
            String sql = "INSERT INTO product (name, description, cate_id, thumbnail, price) VALUES (?, ?, ?, ?, ?)";

            pst = conn.prepareStatement(sql);
            pst.setString(1, TenMonAn_Field.getText());
            pst.setString(2, MoTaSP_Field.getText());

            // Check if comboBox value is not null and is a valid integer
            String selectedCategoryName = comboBox.getValue();

            // Check if the selectedCategoryName is not null
            if (selectedCategoryName != null) {
                // Retrieve the category ID based on the selected category name
                int categoryId = getCategoryIdByName(selectedCategoryName);

                if (categoryId != -1) {
                    pst.setInt(3, categoryId);
                } else {
                    showAlert("Invalid category name", AlertType.ERROR);
                    return; // Stop execution if category name is invalid
                }
            } else {
                showAlert("Category is not selected", AlertType.ERROR);
                return; // Stop execution if no category is selected
            }

            pst.setString(4, imageLink_Field.getText());

            // Check if GiaBan_Field value is a valid double
            if (GiaBan_Field.getText().matches("\\d+(\\.\\d+)?")) {
                pst.setDouble(5, Double.parseDouble(GiaBan_Field.getText()));
            } else {
                showAlert("Invalid price", AlertType.ERROR);
                return; // Stop execution if price is invalid
            }

            int rowsAffected = pst.executeUpdate();
            Stage currentStage = (Stage) Add_Edit_MonAnButton.getScene().getWindow();
                currentStage.hide();
            if (rowsAffected > 0) {
                showAlert("Product saved successfully", AlertType.INFORMATION);
            } else {
                showAlert("Failed to save product", AlertType.ERROR);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            showAlert("Error: " + e.getMessage(), AlertType.ERROR);
        }
    }

    private void showAlert(String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private int getCategoryIdByName(String categoryName) {
        int categoryId = -1; // Default to -1 if category name is not found

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet resultSet = null;

        try {
            conn = mysqlconnect.ConnectDb();
            String sql = "SELECT id FROM category WHERE cate_name = ?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, categoryName);

            resultSet = pst.executeQuery();
            if (resultSet.next()) {
                categoryId = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately in your application
        } finally {
            // Close the resources in a finally block to ensure they are closed even if an exception occurs
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace(); // Handle the exception appropriately
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace(); // Handle the exception appropriately
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace(); // Handle the exception appropriately
                }
            }
        }

        return categoryId;
    }

    @FXML
    private void handleGoBack(ActionEvent event) {
        // Get the stage (window) that contains the GoBackButton
        Stage stage = (Stage) GoBackButton.getScene().getWindow();

        // Close the stage
        stage.close();
    }

}
