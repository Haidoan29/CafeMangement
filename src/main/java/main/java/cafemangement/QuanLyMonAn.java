package main.java.cafemangement;

import java.io.IOException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import main.java.cafemangement.product;
import javafx.scene.control.TableCell;
import main.java.cafemangement.mysqlconnect;
import java.sql.Connection;
import java.sql.SQLException;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import javafx.application.Platform;
import javafx.scene.control.ScrollPane;
import java.io.InputStream;

public class QuanLyMonAn {

    Connection conn = null;

    @FXML
    private Button AddNewButton;
    @FXML
    private TextField SearchBar;

    @FXML
    private Button SearchButton;

    @FXML
    private Label ChiTietMonAn_TenMon;

    @FXML
    private Label ChiTietMonAn_GiaBan;

    @FXML
    private Label ChiTietMonAn_TrangThaiBan;

    @FXML
    private ImageView ChiTietMonAn_ImageMon;

    @FXML
    private TableView LichSuGia_Table;

    @FXML
    private TableView<product> MonAns_Table;

    @FXML
    private TableColumn<product, String> clmMaMon;

    @FXML
    private TableColumn<product, String> clmTenMon;

    @FXML
    private TableColumn<product, Double> clmGiaBan;

    @FXML
    private TableColumn<product, String> clmTrangThaiBan;
    @FXML
    private TableColumn<product, Void> clmSeeDetails;
    @FXML
    private TableColumn<product, Void> clmActions;

    private ObservableList<product> productList;

    @FXML
    private ScrollPane ChiTietMonAn_Pane;

    private String imageDirectory;

    @FXML
    private void AddNewButton_Clicked(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//       stage.close();

        // Tải lại trang login.fxml và hiển thị nó
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/FXML/Them_ChinhSua_MonAn.fxml"));
            Stage loginStage = new Stage();
            loginStage.setTitle("Quản lý quán café - Cafe Mangement");
            loginStage.getIcons().add(new Image(getClass().getResourceAsStream("/main/resources/img/app-icon.png")));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            loginStage.setScene(scene);
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void Addcate_Clicked(MouseEvent event) {
        // Tải lại trang cate.fxml và hiển thị nó
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/FXML/Addcate.fxml"));
            Stage loginStage = new Stage();
            loginStage.setTitle("Quản lý quán café - Cafe Mangement");
            loginStage.getIcons().add(new Image(getClass().getResourceAsStream("/main/resources/img/app-icon.png")));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            loginStage.setScene(scene);
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        // Initialize your columns with appropriate types
        clmMaMon.setCellValueFactory(new PropertyValueFactory<>("maMon"));
        clmTenMon.setCellValueFactory(new PropertyValueFactory<>("tenMon"));
        clmGiaBan.setCellValueFactory(new PropertyValueFactory<>("giaBan"));
        clmTrangThaiBan.setCellValueFactory(new PropertyValueFactory<>("trangThaiBan"));

        // Initialize your ObservableList
        productList = FXCollections.observableArrayList();

        // Fetch data from the database
        fetchDataFromDatabase();

        // Set the TableView items
        MonAns_Table.setItems(productList);

        clmSeeDetails.setCellFactory(param -> new TableCell<>() {
            private final Button detailsButton = new Button("Detail");

            {
                detailsButton.setOnAction(event -> {
                    // Handle the button click event here
                    product selectedProduct = getTableView().getItems().get(getIndex());
                    showDetails(selectedProduct);
                    showDetailsInChiTietMonAn(selectedProduct);

                    // Change the visibility of the ScrollPane to true
                    ChiTietMonAn_Pane.setVisible(true);
                });

            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(detailsButton);
                }
            }
        });
    }

    private void fetchDataFromDatabase() {
        try {
            conn = mysqlconnect.ConnectDb();
            String sql = "SELECT id, name, price , thumbnail FROM product";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                String thumbnail = resultSet.getString("thumbnail");
                productList.add(new product(id, name, price, thumbnail));
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    @FXML
    private void showDetails(product product) {
        // Implement the logic to show details for the given product
        // You can access the properties of the product and display them as needed
        String maMon = product.getMaMon();
        String tenMon = product.getTenMon();
        Double giaBan = product.getGiaBan();

        // Display the details in your UI, e.g., in a dialog or another view
        // You can use JavaFX controls like labels or text fields to display the information
    }

    // Thêm sự kiện cho nút "Tìm kiếm"
    @FXML
    private void search(ActionEvent event) {
        String productName = SearchBar.getText(); // Lấy tên sản phẩm từ TextField

        // Thực hiện truy vấn dựa trên tên sản phẩm
        String sql = "SELECT id, name, price , thumbnail FROM product WHERE name LIKE ?";

        try (Connection conn = mysqlconnect.ConnectDb(); PreparedStatement pst = conn.prepareStatement(sql)) {

            productList.clear(); // Xóa danh sách sản phẩm hiện tại
            pst.setString(1, "%" + productName + "%");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                String thumbnail = rs.getString("thumbnail");
                productList.add(new product(id, name, price, thumbnail));
            }
            MonAns_Table.setItems(productList);

            // Xử lý kết quả trả về từ cơ sở dữ liệu, ví dụ: hiển thị trong TableView hoặc ListView
            // Nếu bạn sử dụng TableView, bạn cần tạo các cột và thêm dữ liệu vào đó
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void Update_Clicked(ActionEvent event) {
        try {
            // Kiểm tra xem đã chọn hàng sản phẩm nào trong MonAns_Table chưa
            product selectedProduct = MonAns_Table.getSelectionModel().getSelectedItem();

            if (selectedProduct != null) {
                // Tạo đối tượng product mới và thiết lập thông tin dựa trên sản phẩm đã chọn
                product updatedProduct = new product(
                        selectedProduct.getMaMon(),
                        selectedProduct.getTenMon(),
                        selectedProduct.getGiaBan(),
                        selectedProduct.getimageUrl(),
                        selectedProduct.getMoTa()
                        
                        
                        
                );

                // Mở trang update.fxml và truyền thông tin sản phẩm
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/FXML/update.fxml"));
                Parent root = loader.load();
                UpdateController updateController = loader.getController();
                updateController.initData(updatedProduct);

                Stage updateStage = new Stage();
                updateStage.setTitle("Cập nhật sản phẩm");
                updateStage.getIcons().add(new Image(getClass().getResourceAsStream("/main/resources/img/app-icon.png")));
                Scene scene = new Scene(root);

                updateStage.setScene(scene);
                updateStage.show();
            } else {
                // Thông báo cho người dùng rằng họ chưa chọn sản phẩm nào
                System.out.println("Vui lòng chọn sản phẩm cần cập nhật.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showDetailsInChiTietMonAn(product product) {

        try {

            // Establish a database connection (replace with your connection code)
            conn = mysqlconnect.ConnectDb();

            // Prepare a SQL query to retrieve product details by product ID
            String sql = "SELECT name, price, description, thumbnail FROM product WHERE name = ?";
//        System.out.println("SQL Query: " + sql);
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, product.getTenMon()); // Set the product ID parameter

            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();
//            System.out.println("hello1");
            if (resultSet.next()) {
//                System.out.println("hello!");
                // Retrieve data from the result set
                String tenMon = resultSet.getString("name");
                double giaBan = resultSet.getDouble("price");
                String moTa = resultSet.getString("description");
                String imageUrl = resultSet.getString("thumbnail");
//                System.out.println(imageUrl);
                // Load and set the image
                Image image;
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    String simplifiedDirectory = "/main/resources/img/";
                    String fullImagePath = simplifiedDirectory + imageUrl;
                    InputStream imageStream = getClass().getResourceAsStream(fullImagePath);
                    if (imageStream != null) {
                        image = new Image(imageStream);
                    } else {
                        // If the file doesn't exist, set a default image
                        image = new Image(getClass().getResourceAsStream("/main/resources/img/app-icon.png"));
                    }

                } else {
                    // If no image URL is available, set a default image
                    image = new Image(getClass().getResourceAsStream("/main/resources/img/signup2.jpg"));
                }

                // Update JavaFX components on the JavaFX Application Thread
                Platform.runLater(() -> {
//                    System.out.println("Code inside Platform.runLater is running!");
                    ChiTietMonAn_TenMon.setText(tenMon);
                    ChiTietMonAn_GiaBan.setText(String.valueOf(giaBan));
                    ChiTietMonAn_TrangThaiBan.setText(moTa);
                    ChiTietMonAn_ImageMon.setImage(image);

                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void DeleteButton(ActionEvent event) {
        product selectedProduct = MonAns_Table.getSelectionModel().getSelectedItem();

        if (selectedProduct != null) {
            String productId = selectedProduct.getMaMon(); // Assuming you have a method getId() in your product class

            if (productId != null && !productId.isEmpty()) {
                try {
                    Connection conn = mysqlconnect.ConnectDb();
                    String sql = "DELETE FROM product WHERE id = ?";
                    PreparedStatement preparedStatement = conn.prepareStatement(sql);
                    preparedStatement.setString(1, productId);

                    int deletedRows = preparedStatement.executeUpdate();

                    if (deletedRows > 0) {
                        // User deleted successfully from the database
                        productList.remove(selectedProduct); // Remove from the ObservableList
                        MonAns_Table.refresh(); // Refresh the TableView
                    } else {
                        // Handle deletion failure
                        // You can show an error message or log the error
                        System.out.println("Deletion failed. No rows deleted.");
                    }

                    preparedStatement.close();
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    // Handle the SQL exception, e.g., show an error message
                    System.err.println("SQL Exception: " + e.getMessage());
                }
            } else {
                // Handle the case where productId is empty or null
                System.out.println("Invalid productId: " + productId);
            }
        } else {
            // Handle the case where no item is selected in the table
            System.out.println("No product selected for deletion.");
        }
    }

// Define a method to populate the TableView with price history data
}
