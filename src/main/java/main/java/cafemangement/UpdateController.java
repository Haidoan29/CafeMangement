/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.cafemangement;

import java.io.File;
import main.java.cafemangement.product;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import main.java.cafemangement.mysqlconnect;
import java.sql.Connection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;

public class UpdateController {

    @FXML
    private TextField TenMonAn_Field; // Thêm các @FXML fields tương ứng với các thành phần trên giao diện

    @FXML
    private TextField GiaBan_Field;

    @FXML
    private TextField MoTaSP_Field;
    @FXML
    private TextField imageLink_Field;

    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private Button GoBackButton;
    @FXML
    private Button Add_Edit_MonAnButton;
    @FXML
    private ImageView MonAnImage_Display;
    private PreparedStatement preparedStatement;
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    private product selectedProduct;

    public void setSelectedProduct(product product) {
        this.selectedProduct = product;
    }

    public void initData(product selectedProduct) {
        this.selectedProduct = selectedProduct;
//        ObservableList<String> items = FXCollections.observableArrayList();
        // Truyền dữ liệu sản phẩm vào các trường trên giao diện
        TenMonAn_Field.setText(selectedProduct.getTenMon()); // Sử dụng các phương thức getter của sản phẩm để lấy dữ liệu
        GiaBan_Field.setText(String.valueOf(selectedProduct.getGiaBan())); // Chuyển giá thành chuỗi

        try {
            // Create a PreparedStatement for your database query
            Connection conn = mysqlconnect.ConnectDb();

            String sql = "SELECT description FROM product WHERE id = ?"; // Modify this SQL query

            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, selectedProduct.getMaMon()); // Set the parameter

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String moTa = resultSet.getString("description");
                if (moTa != null) {
                    MoTaSP_Field.setText(moTa);
                } else {
                    MoTaSP_Field.setText("No description available");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database errors...
        }

        try {
            // Establish a database connection
            Connection conn = mysqlconnect.ConnectDb();

            // Define your SQL query
            String sql = "SELECT * FROM Category";

            // Create a PreparedStatement
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            // Execute the query and get the result set
            ResultSet resultSet = preparedStatement.executeQuery();

            // Create an ObservableList to store the items
            ObservableList<String> items = FXCollections.observableArrayList();

            // Add data from the result set to the ObservableList
            while (resultSet.next()) {
                String categoryName = resultSet.getString("cate_name");
                items.add(categoryName);
                comboBox.setItems(items);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            // Establish a database connection
            Connection conn = mysqlconnect.ConnectDb();

            // Define your SQL query to fetch the category name based on the cate_id of the selected product
            String sql = "SELECT category.cate_name as cate_name FROM product INNER JOIN category ON product.cate_id=category.id WHERE product.id = ?";

            // Create a PreparedStatement
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, selectedProduct.getMaMon()); // Set the product ID parameter

            // Execute the query and get the result set
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String categoryName = resultSet.getString("cate_name");
                // Set the value of the ComboBox to the fetched category ID as a String
                comboBox.setValue(String.valueOf(categoryName));
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        imageLink_Field.setText(selectedProduct.getimageUrl());
        String imgUrl = selectedProduct.getimageUrl();
        if (imgUrl != null && !imgUrl.isEmpty()) {
            try {
                String simplifiedDirectory = "/main/resources/img/";
                String fullImagePath = simplifiedDirectory + imgUrl;
                Image image = new Image(fullImagePath);
                MonAnImage_Display.setImage(image);
            } catch (IllegalArgumentException e) {
                // Handle the case where the URL is invalid or the resource is not found.
                // You can provide a default image or show an error message.
                MonAnImage_Display.setImage(null); // Set to a default image or leave it empty
                System.err.println("Invalid image URL: " + imgUrl);
            }
        } else {
            MonAnImage_Display.setImage(null);
        }

    }

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
    private void update(ActionEvent event) {
        try (Connection conn = mysqlconnect.ConnectDb()) { // Use try-with-resources for automatic resource management
            String sql = "UPDATE product SET name = ?, description = ?, cate_id = ?, thumbnail = ?, price = ? WHERE id = ?";

            // Execute the query and get the result set
            try (PreparedStatement pst = conn.prepareStatement(sql)) { // Use try-with-resources for PreparedStatement

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
                System.out.println(selectedProduct);
                // Set the product ID for the WHERE clause
                if (selectedProduct != null) {
                    pst.setString(6, selectedProduct.getMaMon());
                } else {
                    showAlert("No product selected", AlertType.ERROR);

                    return; // Stop execution if no product is selected
                }

                int rowsAffected = pst.executeUpdate();

                if (rowsAffected > 0) {
                    showAlert("Product updated successfully", AlertType.INFORMATION);
                    Stage currentStage = (Stage) Add_Edit_MonAnButton.getScene().getWindow();
                    currentStage.close();

                } else {
                    showAlert("Failed to update product", AlertType.ERROR);
                }
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

        try {
            conn = mysqlconnect.ConnectDb();
            String sql = "SELECT id FROM category WHERE cate_name = ?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, categoryName);

            rs = pst.executeQuery();
            if (rs.next()) {
                categoryId = rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately in your application
        } finally {
            // Close the resources in a finally block to ensure they are closed even if an exception occurs
            if (rs != null) {
                try {
                    rs.close();
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
