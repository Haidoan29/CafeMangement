package main.java.cafemangement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.scene.control.Label;

public class AdminScene implements Initializable {
	

    @FXML
    private Pane containerPane; // Đảm bảo bạn đã đặt fx:id cho Pane
    @FXML
    private Label tenChuQuanLabel;
     public void setTenChuQuan(String name) {
        tenChuQuanLabel.setText("Hello: " + name);

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void goToQLMon(ActionEvent event  ) {
    	
        try {
            // Load tệp quanlymonan.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/FXML/QuanLyMonAn.fxml"));
            
            Parent root = loader.load();

            // Set nội dung mới cho containerPane
            containerPane.getChildren().clear();
            containerPane.getChildren().add(root);
            
        } catch (Exception ex) {
            ex.printStackTrace(); // Print the stack trace of the exception
        }
    }

    @FXML
    private void goToQLThongKe(ActionEvent event) {
    	try {
            // Load tệp quanlymonan.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/FXML/ThongKe.fxml"));
            
            Parent root = loader.load();

            // Set nội dung mới cho containerPane
            containerPane.getChildren().clear();
            containerPane.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void goToQLNhanvien(ActionEvent event) {
    	try {
            // Load tệp quanlymonan.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/FXML/QuanLyNhanVien.fxml"));
            
            Parent root = loader.load();

            // Set nội dung mới cho containerPane
            containerPane.getChildren().clear();
            containerPane.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void logout(ActionEvent event) {
        // Hiển thị hộp thoại xác nhận
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận đăng xuất");
//        Image icon = new Image(getClass().getResourceAsStream("/main/resources/img/app-icon.png"));
//        alert.setGraphic(new ImageView(icon));
        
//        alert.showAndWait();
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
        } else {
            // Người dùng chọn Cancel, không đăng xuất
        }
    }
}
