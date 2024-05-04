/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.cafemangement;

/**
 *
 * @author Admin
 */
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.fxml.FXML;
import java.time.LocalDate;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.cafemangement.mysqlconnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javax.swing.JOptionPane;

public class QuanLyNhanVien {

    @FXML
    private TableView<Users> nhanvienTableView;
    @FXML
    private TableColumn<Users, String> maNvCol;
    @FXML
    private TableColumn<Users, String> tenNvCol;
    @FXML
    private TableColumn<Users, String> dienThoaiCol;
    @FXML
    private TableColumn<Users, String> diaChiCol;
    @FXML
    private TableColumn<Users, LocalDate> ngaySinhCol;
    @FXML
    private TableColumn<Users, String> chucVuCol;
    @FXML
    private TableColumn<Users, String> EmailCol;

    @FXML
    private TextField searchEmpTextField;

    @FXML
    private Button searchEmpButton;

    @FXML
    private ComboBox newChucVuComboBox;

    @FXML
    private TextField newEmailNvTextField;

    @FXML
    private TextField newDientThoaiTextField;

    @FXML
    private TextField newDiaChiTextField;

    @FXML
    private DatePicker newNgaySinhDatePicker;

    @FXML
    private TextField newTenDangNhapTextField;

    @FXML
    private PasswordField newMatKhauPwField;

    @FXML
    private PasswordField newMatKhau2PwField;

    @FXML
    private TextField newTenNvTextField1;

    @FXML
    private Label newAccountInfoStatusLabel;

    private ObservableList<Users> userList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialize your TableView columns
        maNvCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        tenNvCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        dienThoaiCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        diaChiCol.setCellValueFactory(new PropertyValueFactory<>("Address"));
        ngaySinhCol.setCellValueFactory(new PropertyValueFactory<>("Date_birth"));
        chucVuCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        EmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Populate the ObservableList with data from the database
        fetchDataFromDatabase();

        // Set the TableView items to the ObservableList
        nhanvienTableView.setItems(userList);
        // Refresh the TableView
        nhanvienTableView.refresh();

        newChucVuComboBox.getItems().addAll("Admin", "Staff");

    }

    private void fetchDataFromDatabase() {
        try {
            Connection conn = mysqlconnect.ConnectDb();
            String sql = "SELECT id, name , phone, Address, Date_birth, type , email FROM `users` WHERE type = \"Staff\";";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Users user = new Users(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("phone"),
                        resultSet.getString("Address"),
                        resultSet.getObject("Date_birth", LocalDate.class), // Use getObject to retrieve LocalDate
                        resultSet.getString("type"),
                        resultSet.getString("email")
                );
                userList.add(user);
            }

//            resultSet.close();
//            preparedStatement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteNv(ActionEvent event) {
        Users selectedUser = nhanvienTableView.getSelectionModel().getSelectedItem();

        if (selectedUser != null) {
            String userId = selectedUser.getId(); // Assuming you have a method getId() in your Users class

            try {
                Connection conn = mysqlconnect.ConnectDb();
                String sql = "DELETE FROM users WHERE id = ?";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, userId);

                int deletedRows = preparedStatement.executeUpdate();

                if (deletedRows > 0) {
                    // User deleted successfully from the database
                    userList.remove(selectedUser); // Remove from the ObservableList
                    nhanvienTableView.refresh(); // Refresh the TableView
                } else {
                    // Handle deletion failure
                    // You can show an error message or log the error
                }

                preparedStatement.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle the SQL exception, e.g., show an error message
            }
        }
    }

    @FXML
    private void search(ActionEvent event) {
        String searchQuery = searchEmpTextField.getText().trim();
        if (!searchQuery.isEmpty()) {
            searchEmployees(searchQuery);
        } else {
            // Handle empty search query (e.g., show all employees)
            fetchDataFromDatabase();
        }
    }

    private void searchEmployees(String query) {
        String searchQuery = searchEmpTextField.getText().trim();
        if (!searchQuery.isEmpty()) {
            try {
                Connection conn = mysqlconnect.ConnectDb();
                String sql = "SELECT id, username, phone, Address, Date_birth, type, email, name FROM users "
                        + "WHERE type = 'Staff' AND (name LIKE ? OR phone LIKE ? OR Address LIKE ?)";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);

                // Set parameters based on the search query
                String searchPattern = "%" + searchQuery + "%";
                preparedStatement.setString(1, searchPattern);
                preparedStatement.setString(2, searchPattern);
                preparedStatement.setString(3, searchPattern);

                ResultSet resultSet = preparedStatement.executeQuery();
                userList.clear(); // Clear the existing list

                while (resultSet.next()) {
                    Users user = new Users(
                            resultSet.getString("id"),
                            resultSet.getString("name"),
                            resultSet.getString("phone"),
                            resultSet.getString("Address"),
                            resultSet.getObject("Date_birth", LocalDate.class), // Use getObject to retrieve LocalDate
                            resultSet.getString("type"),
                            resultSet.getString("email")
                    );
                    userList.add(user);
                }

                resultSet.close();
                preparedStatement.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            // Handle empty search query (e.g., show all employees)
            fetchDataFromDatabase();
        }
    }

    @FXML
    private void AddNv(ActionEvent event) {
        String tenNv = newTenNvTextField1.getText();
        String emailNv = newEmailNvTextField.getText();
        String dienThoai = newDientThoaiTextField.getText();
        String diaChi = newDiaChiTextField.getText();
        LocalDate ngaySinh = newNgaySinhDatePicker.getValue();
        String chucVu = (String) newChucVuComboBox.getValue();
        String tenDangNhap = newTenDangNhapTextField.getText();
        String matKhau = newMatKhauPwField.getText();
        String matKhau2 = newMatKhau2PwField.getText();

        // Kiểm tra các trường dữ liệu không được để trống
        if (tenNv.isEmpty() || dienThoai.isEmpty() || diaChi.isEmpty() || ngaySinh == null || chucVu == null || tenDangNhap.isEmpty() || matKhau.isEmpty() || matKhau2.isEmpty()) {
            newAccountInfoStatusLabel.setText("Mời điền đủ thông tin");
            newAccountInfoStatusLabel.setTextFill(javafx.scene.paint.Color.DARKORANGE);
            newAccountInfoStatusLabel.setVisible(true);
            return;
        }

        // Kiểm tra xác nhận mật khẩu
        if (!matKhau.equals(matKhau2)) {
            newAccountInfoStatusLabel.setText("Xác nhận mật khẩu không khớp");
            newAccountInfoStatusLabel.setTextFill(javafx.scene.paint.Color.RED);
            newAccountInfoStatusLabel.setVisible(true);
            return;
        }

        // Kiểm tra email hợp lệ
        if (!isValidEmail(emailNv)) {
            newAccountInfoStatusLabel.setText("Địa chỉ email không hợp lệ");
            newAccountInfoStatusLabel.setTextFill(javafx.scene.paint.Color.RED);
            newAccountInfoStatusLabel.setVisible(true);
            return;
        }

        // Kiểm tra mật khẩu mạnh hơn (đối với ví dụ này, mật khẩu phải có ít nhất 8 ký tự)
        if (matKhau.length() < 8) {
            newAccountInfoStatusLabel.setText("Mật khẩu phải có ít nhất 8 ký tự");
            newAccountInfoStatusLabel.setTextFill(javafx.scene.paint.Color.RED);
            newAccountInfoStatusLabel.setVisible(true);
            return;
        }

        // Mã hóa mật khẩu
        String hashedPassword;
        try {
            hashedPassword = hashPassword(matKhau);
        } catch (NoSuchAlgorithmException e) {
            newAccountInfoStatusLabel.setText("Lỗi mã hóa mật khẩu.");
            newAccountInfoStatusLabel.setTextFill(javafx.scene.paint.Color.RED);
            newAccountInfoStatusLabel.setVisible(true);
            e.printStackTrace();
            return;
        }

        try (Connection conn = mysqlconnect.ConnectDb(); PreparedStatement pst = conn.prepareStatement("INSERT INTO users (name, phone, Address, Date_birth, type, username, password, email) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?)")) {

            pst.setString(1, tenNv);
            pst.setString(2, dienThoai);
            pst.setString(3, diaChi);
            pst.setObject(4, ngaySinh);
            pst.setString(5, chucVu);
            pst.setString(6, tenDangNhap);
            pst.setString(7, hashedPassword);
            pst.setString(8, emailNv);

            int rowsInserted = pst.executeUpdate();
            if (rowsInserted > 0) {
                newAccountInfoStatusLabel.setText("Đã lưu");
                newAccountInfoStatusLabel.setTextFill(javafx.scene.paint.Color.GREEN);
                newAccountInfoStatusLabel.setVisible(true);
            } else {
                newAccountInfoStatusLabel.setText("Không thể lưu nhân viên");
                newAccountInfoStatusLabel.setTextFill(javafx.scene.paint.Color.RED);
                newAccountInfoStatusLabel.setVisible(true);
            }
        } catch (SQLException e) {
            // Xử lý lỗi SQL một cách an toàn hơn, không hiển thị thông điệp lỗi SQL trực tiếp
            newAccountInfoStatusLabel.setText("Lỗi cơ sở dữ liệu. Vui lòng thử lại sau.");
            newAccountInfoStatusLabel.setTextFill(javafx.scene.paint.Color.RED);
            newAccountInfoStatusLabel.setVisible(true);
            e.printStackTrace();
        }
    }

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] hashBytes = md.digest(password.getBytes());

        StringBuilder hashedPassword = new StringBuilder();
        for (byte b : hashBytes) {
            hashedPassword.append(String.format("%02x", b));
        }
        return hashedPassword.toString();
    }

    private boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(regex);
    }
    @FXML
    private TextField tenDangNhapTextField;

    @FXML
    private PasswordField matKhauMoiTextField;

    @FXML
    private PasswordField matKhauMoi2TextField;

    @FXML
    private void UpdatePass(ActionEvent event) {
        String tenDangNhap = tenDangNhapTextField.getText();
        String matKhauMoi = matKhauMoiTextField.getText();
        String matKhauMoi2 = matKhauMoi2TextField.getText();

        // Kiểm tra xác nhận mật khẩu và mật khẩu mới có đủ mạnh không
        if (!matKhauMoi.equals(matKhauMoi2) || matKhauMoi.length() < 8) {

            System.out.println("Mật khẩu không hợp lệ hoặc xác nhận mật khẩu không khớp.");
            return;
        }

        // Mã hóa mật khẩu mới
        String hashedPassword;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] hashBytes = md.digest(matKhauMoi.getBytes());

            StringBuilder hashedPasswordBuilder = new StringBuilder();
            for (byte b : hashBytes) {
                hashedPasswordBuilder.append(String.format("%02x", b));
            }
            hashedPassword = hashedPasswordBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Lỗi mã hóa mật khẩu.");
            e.printStackTrace();
            return;
        }

        // Thực hiện cập nhật mật khẩu vào cơ sở dữ liệu
        if (updateMatKhau(tenDangNhap, hashedPassword)) {

            JOptionPane.showMessageDialog(null, "Password updated successfully");
            tenDangNhapTextField.clear();
            matKhauMoiTextField.clear();
            matKhauMoi2TextField.clear();
        } else {
            JOptionPane.showMessageDialog(null, "Password update failed");
        }
    }

    private boolean updateMatKhau(String tenDangNhap, String hashedPassword) {
        try (Connection conn = mysqlconnect.ConnectDb(); PreparedStatement pst = conn.prepareStatement("UPDATE users SET password = ? WHERE username = ?")) {
            pst.setString(1, hashedPassword);
            pst.setString(2, tenDangNhap);
            int rowsUpdated = pst.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @FXML
    private Button refreshButton; // Reference to the "Làm mới màn hình" button in your controller

    @FXML
    private void handleRefreshButtonAction(ActionEvent event) {
        try {
            // Load the FXML file and create a new scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/FXML/AdminScene.fxml"));
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

}
