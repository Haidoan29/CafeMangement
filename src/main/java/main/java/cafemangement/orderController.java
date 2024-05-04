package main.java.cafemangement;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.layout.TilePane;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import main.java.cafemangement.mysqlconnect;
import java.sql.SQLException;
import java.io.InputStream;
import javafx.application.Platform;
import javafx.scene.layout.VBox;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import javafx.scene.control.SelectionMode;
import main.java.cafemangement.logincontroller;
import javafx.collections.ListChangeListener;
import javax.swing.JOptionPane;

public class orderController implements Initializable {

    @FXML
    private ScrollPane menuContainerScrollPane;

    @FXML
    private TilePane menuTilePane;

    @FXML
    private Label tenNhanvienLabel;

    @FXML
    private TextField searchTextField;

    @FXML
    private Label todayLabel;
    @FXML
    private TextField tempPriceTextField;
    @FXML
    private TextField totalPriceTextField;
    @FXML
    private TextField receivedAmountTextField;
    @FXML
    private TextField changeAmountTextField;
    @FXML
    private TextField nameKH;

    @FXML
    public TableView<product> billTableView;
    @FXML
    private TableColumn<product, String> maMonCol;
    @FXML
    private TableColumn<product, String> tenMonCol;
    @FXML
    private TableColumn<product, Integer> soLuongCol;
    @FXML
    private TableColumn<product, Double> giaBanCol;
    @FXML
    private TextField product_idTextField;
    @FXML
    private TextField user_idTextField;

    public orderController(TableView<product> billTableView) {
        this.billTableView = billTableView;
    }

    // Method to set the user's name
    public void setTenNhanVien(String name) {
        tenNhanvienLabel.setText("Staff: " + name);

    }

    public void setProductID(String maMon) {
        product_idTextField.setText(maMon);
    }

    private String loggedInUserId;

    public void setLoggedInUserId(String id) {
        this.loggedInUserId = id;
    }

    public TableView<product> getBillTableView() {
        return billTableView;
    }

    public orderController() {
    }
    // Khai báo biến allProducts là một biến thành viên của lớp orderController
    private ObservableList<product> allProducts;
    private ObservableList<product> billTableData = FXCollections.observableArrayList();
    private ObservableList<product> productList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Lấy ngày hiện tại
        LocalDate currentDate = LocalDate.now();

        // Định dạng ngày thành chuỗi (dd/MM/yyyy)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = currentDate.format(formatter);

        // Hiển thị ngày trong Label
        todayLabel.setText("Ngày " + formattedDate);
        billTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Call the method to fetch and display all products
        displayAllProducts1();
        // Attach a click event handler to the ImageView
        setupTableView();
//        allProducts = billTableView.getItems();
        productList.addAll(billTableData);

    }

    // Define a custom TableCell for the quantity column
    private void setupTableView() {
        // Create columns for TableView
        TableColumn<product, String> maMonCol = new TableColumn<>("maMon");
        maMonCol.setCellValueFactory(new PropertyValueFactory<>("maMon"));
//
        TableColumn<product, String> tenMonCol = new TableColumn<>("Product name");
        tenMonCol.setCellValueFactory(new PropertyValueFactory<>("tenMon"));

        TableColumn<product, Integer> soLuongCol = new TableColumn<>("quantity");
        soLuongCol.setCellValueFactory(new PropertyValueFactory<>("soLuong"));

        TableColumn<product, Double> giaBanCol = new TableColumn<>("price");
        giaBanCol.setCellValueFactory(new PropertyValueFactory<>("giaBan"));

        // Add columns to the TableView
        billTableView.getColumns().addAll(maMonCol, tenMonCol, soLuongCol, giaBanCol);

        // Debugging: Print the productList size
        System.out.println("productList size: " + productList.size());
        billTableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        productList.addListener(new ListChangeListener<product>() {
            @Override
            public void onChanged(Change<? extends product> c) {
                while (c.next()) {
                    if (c.wasAdded() || c.wasRemoved()) {
                        calculateTotalAmountBasedOnQuantity();
                    }
                }
            }
        });

        // Set the items of the TableView
        billTableView.setItems(productList);

    }

    // Define a Map to store product quantities
    private Map<String, Integer> productQuantities = new HashMap<>();
// A method to fetch and display all products

    private void displayAllProducts1() {
        try {
            Connection conn = mysqlconnect.ConnectDb();
            String sql = "SELECT id, name, price, thumbnail FROM product";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String maMon = resultSet.getString("id");
                String tenMon = resultSet.getString("name");
                String imageUrl = resultSet.getString("thumbnail");
                double giaBan = resultSet.getDouble("price");

                // Load and set the image
                Image image = loadImage(imageUrl);

                Platform.runLater(() -> {
                    // Create or update UI components for each product
                    ImageView imageView = new ImageView(image);
                    Label nameLabel = new Label(tenMon);
                    imageView.setFitWidth(200.0);
                    imageView.setFitHeight(200.0);

                    VBox productBox = new VBox(imageView, nameLabel);
                    productBox.setPrefHeight(200.0);
                    productBox.setPrefWidth(200.0);
                    // Create a new product instance
                    product newproduct = new product(maMon, tenMon, giaBan, 0);

                    productBox.setOnMouseClicked(event -> {
                        // Handle the product click event
                        handleProductClick(maMon, tenMon, giaBan);

                    });

                    menuContainerScrollPane.setVvalue(0.0);
                    menuContainerScrollPane.setHvalue(0.0);

                    menuTilePane.getChildren().add(productBox);
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleProductClick(String maMon, String tenMon, double giaBan) {
        if (productQuantities.containsKey(maMon)) {
            int currentQuantity = productQuantities.get(maMon);
            productQuantities.put(maMon, currentQuantity + 1);
        } else {
            productQuantities.put(maMon, 1);
        }

        int index = findProductIndexInTableView(maMon);
        product newProduct = new product(maMon, tenMon, giaBan, 0);

        Integer quantity = productQuantities.get(maMon);
        if (quantity != null) {
            newProduct.setSoLuong(quantity);
        }

        if (index >= 0) {
            billTableView.getItems().set(index, newProduct); // Update the existing product
        } else {
            billTableView.getItems().add(newProduct);
        }

        // Refresh the TableView
        billTableView.refresh();
        System.out.println("Clicked on product: " + maMon + ", name: " + tenMon + ", gia: " + giaBan + ", Quantity: " + newProduct.getSoLuong());

        // Add the selected product to the selectedProducts list
        billTableData.add(newProduct);
    }
// This method finds the index of a product in the TableView

    private int findProductIndexInTableView(String maMon) {
        ObservableList<product> items = billTableView.getItems();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getMaMon().equals(maMon)) {
                return i;
            }
        }
        return -1;
    }
// A method to load an image from the specified URL

    private Image loadImage(String imageUrl) {
        try {
            String simplifiedDirectory = "/main/resources/img/";
            String fullImagePath = simplifiedDirectory + imageUrl;
            InputStream imageStream = getClass().getResourceAsStream(fullImagePath);
            if (imageStream != null) {
                return new Image(imageStream);
            } else {
                // If the file doesn't exist, return a default image
                return new Image(getClass().getResourceAsStream("/main/resources/img/app-icon.png"));
            }
        } catch (Exception e) {
            System.out.println("Error loading image: " + e.getMessage());
            return new Image(getClass().getResourceAsStream("/main/resources/img/app-icon.png"));
        }
    }

    @FXML
    private void logout(ActionEvent event) {
        // Hiển thị hộp thoại xác nhận
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận đăng xuất");
        alert.setHeaderText("Bạn có muốn đăng xuất không?");
        alert.setContentText("Nhấn OK để đăng xuất hoặc Cancel để tiếp tục.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Thực hiện đăng xuất nếu người dùng chọn OK

            // Đóng cửa sổ hiện tại
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();

            // Tải lại trang login.fxml và hiển thị nó
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/FXML/login.fxml"));
                stage.setTitle("Quản lý quán café - Cafe Mangement");
                stage.getIcons().add(new Image(getClass().getResourceAsStream("/main/resources/img/app-icon.png")));
                Parent root = loader.load();
                Scene scene = new Scene(root);

                Stage loginStage = new Stage();
                loginStage.setScene(scene);
                loginStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

        }
    }

// Thêm sự kiện cho nút "Tìm kiếm"
    @FXML
    private void searchMon(ActionEvent event) {
        String productName = searchTextField.getText(); // Lấy tên sản phẩm từ TextField
        // Create a list to store previously selected products
        List<product> previouslySelectedProducts = new ArrayList<>(billTableView.getItems());

        // Clear the productList
        productList.clear();
        // Thực hiện truy vấn dựa trên tên sản phẩm
        String sql = "SELECT * FROM product WHERE name LIKE ?";

        try (Connection conn = mysqlconnect.ConnectDb(); PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, "%" + productName + "%");
            ResultSet rs = pst.executeQuery();

            productList.clear(); // Xóa danh sách sản phẩm hiện tại
            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                String imageUrl = rs.getString("thumbnail");
                productList.add(new product(id, name, price, imageUrl));
            }
            displaySearchResults();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Add previously selected products back to the productList
        productList.addAll(previouslySelectedProducts);
    }

// A method to display search results
    private void displaySearchResults() {
        // Clear existing products from the TilePane
        menuTilePane.getChildren().clear();

        // Iterate through the search results and add them to the TilePane
        for (product product : productList) {
            String id = product.getMaMon();
            String tenMon = product.getTenMon();
            double price = product.getGiaBan();
            String imageUrl = product.getimageUrl();

            // Load and set the image (must be done on the JavaFX Application Thread)
            Image image = loadImage(imageUrl);

            // Create or update UI components for each product
            Platform.runLater(() -> {
                ImageView imageView = new ImageView(image);
                Label nameLabel = new Label(tenMon);
                imageView.setFitWidth(200.0); // Set the desired width
                imageView.setFitHeight(200.0);

                // Create a VBox to hold the image and label
                VBox productBox = new VBox(imageView, nameLabel);
                productBox.setPrefHeight(200.0);
                productBox.setPrefWidth(200.0);

                // Create a new product instance
                product newproduct = new product(id, tenMon, price, 0);

                productBox.setOnMouseClicked(event -> {
                    // Handle the product click event
                    handleProductClick(id, tenMon, price);
                });

                menuContainerScrollPane.setVvalue(0.0);
                menuContainerScrollPane.setHvalue(0.0);

                // Add the VBox to the TilePane
                menuTilePane.getChildren().add(productBox);

            });
        }
        billTableView.getItems().clear();
    }

    @FXML
    private Button refreshButton; // Reference to the "Làm mới màn hình" button in your controller

    @FXML
    private void refreshButton(ActionEvent event) {
        try {
            // Load the FXML file and create a new scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/FXML/order.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Get the current stage and set the new scene
            Stage currentStage = (Stage) refreshButton.getScene().getWindow();
            currentStage.setScene(scene);

            

        } catch (IOException e) {
            // Handle any exceptions that occur during reloading
            e.printStackTrace();
        }
    }
//    private TableView<product> billTableView;

    @FXML
    private void createBill(ActionEvent event) {
//        calculateTotalAmountBasedOnQuantity();
        insertData(loggedInUserId);
    }

    public void insertData(String loggedInUserId) {
        try {
            if (billTableView != null) { // Check if billTableView is not null
                ObservableList<product> allProducts = billTableView.getItems(); // Get all items in the TableView

                if (!allProducts.isEmpty() && loggedInUserId != null) { // Check if loggedInUserId is not null and there are products
                    Connection conn = mysqlconnect.ConnectDb();
                    String insertQuery = "INSERT INTO orders (product_id, user_id, name_customer, money_customer, money_redundant, total) VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement preparedStatement = conn.prepareStatement(insertQuery);
                    String userId = loggedInUserId;
                    System.err.println("loggedInUserId: " + userId);

                    // Iterate through all products in the TableView
                    for (product selectedProduct : allProducts) {
                        String productId = selectedProduct.getMaMon();

                        String customerName = nameKH.getText();
                        double moneyCustomer = Double.parseDouble(receivedAmountTextField.getText());
                        double moneyRedundant = Double.parseDouble(changeAmountTextField.getText());
                        double totalAmount = Double.parseDouble(totalPriceTextField.getText());

                        // Set values in the PreparedStatement
                        preparedStatement.setString(1, productId);
                        preparedStatement.setString(2, userId);
                        preparedStatement.setString(3, customerName);
                        preparedStatement.setDouble(4, moneyCustomer);
                        preparedStatement.setDouble(5, moneyRedundant);
                        preparedStatement.setDouble(6, totalAmount);

                        // Add batch for execution
                        preparedStatement.addBatch();
                    }

                    // Execute the batch INSERT statement
                    int[] rowsAffected = preparedStatement.executeBatch();

                    // Close the resources
                    preparedStatement.close();
                    conn.close();

                    for (int rows : rowsAffected) {
                        if (rows > 0) {
                            // Successful insertion
                            // You can display a success message or perform any other actions
                            System.out.println("Data inserted successfully.");
                            nameKH.clear();
                            receivedAmountTextField.clear();
                            changeAmountTextField.clear();
                            totalPriceTextField.clear();
                            productQuantities.clear();
                            billTableView.getItems().clear();
                            billTableView.refresh();

                        } else {
                            // Failed insertion
                            System.out.println("Failed to insert data.");
                            JOptionPane.showMessageDialog(null, "Failed to insert data");

                        }
                    }
                    JOptionPane.showMessageDialog(null, "Data inserted successfully");
                } else {
                    // Handle the case where no product is in the TableView or loggedInUserId is null
                    System.err.println("Please make sure there are products in the TableView and loggedInUserId is not null.");
                }
            } else {
                System.err.println("billTableView is null. Make sure it is initialized.");
            }
        } catch (SQLException e) {
            // Handle database errors
            e.printStackTrace();
            System.err.println("Database error: " + e.getMessage());
            // You can display an error message to the user here
        } catch (NumberFormatException e) {
            // Handle parsing errors (e.g., if the user enters non-numeric values)
            System.err.println("Invalid numeric input: " + e.getMessage());
            // You can display an error message to the user here
        }
    }

    public void calculateTotalAmountBasedOnQuantity() {
        double totalAmount = 0.0;

        // Duyệt qua từng sản phẩm trong billTableView
        for (product item : billTableView.getItems()) {
            double price = item.getGiaBan(); // Giả sử getPrice() trả về giá của sản phẩm
            int quantity = item.getSoLuong(); // Giả sử getQuantity() trả về số lượng của sản phẩm
            double productTotal = (quantity == 1) ? price : (price * quantity); // Tính tổng giá trị cho sản phẩm
            totalAmount += productTotal;
        }

        // Hiển thị tổng giá trị
        System.out.println("Tổng giá trị: " + totalAmount);
        // Cập nhật giá trị tổng vào totalPriceTextField hoặc nơi bạn muốn hiển thị nó trên giao diện người dùng
        totalPriceTextField.setText(String.valueOf(totalAmount));
        tempPriceTextField.setText(String.valueOf(totalAmount));
    }

}
