/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.cafemangement;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import main.java.cafemangement.mysqlconnect;
import java.time.LocalDate;
import javafx.scene.control.DatePicker;


public class ThongKeController {

    @FXML
    private TableView<Order> hoadonTableView;

    @FXML
    private TableColumn<Order, String> maHoaDonCol;

    @FXML
    private TableColumn<Order, String> tenKHCol;

    @FXML
    private TableColumn<Order, Double> thanhTienCol;

    @FXML
    private TableColumn<Order, Double> tienTraCol;

    @FXML
    private TableColumn<Order, Double> tienThoiCol;

    @FXML
    private TableColumn<Order, String> ngayGiaoDichCol;

    @FXML
    private TableColumn<Order, String> nhanVienTaoCol;
    @FXML
    private DatePicker fromDayDatePicker;
    @FXML
    private DatePicker toDayDatePicker;

    private ObservableList<Order> orderList;

    @FXML
    private void initialize() {
        // Initialize the columns to bind with the Order class properties
        maHoaDonCol.setCellValueFactory(new PropertyValueFactory<>("maHoaDon"));
        tenKHCol.setCellValueFactory(new PropertyValueFactory<>("tenKH"));
        thanhTienCol.setCellValueFactory(new PropertyValueFactory<>("thanhTien"));
        tienTraCol.setCellValueFactory(new PropertyValueFactory<>("tienTra"));
        tienThoiCol.setCellValueFactory(new PropertyValueFactory<>("tienThoi"));
        ngayGiaoDichCol.setCellValueFactory(new PropertyValueFactory<>("ngayGiaoDich"));
        nhanVienTaoCol.setCellValueFactory(new PropertyValueFactory<>("nhanVienTao"));
// Initialize the orderList
        orderList = FXCollections.observableArrayList();

        // Fetch data from the database and populate the orderList
        fetchOrderData();

        // Bind the TableView to the orderList
        hoadonTableView.setItems(orderList);
    }

    private void fetchOrderData() {
        try {
            Connection conn = mysqlconnect.ConnectDb();
            String sql = "SELECT orders.id AS maHoaDon, orders.name_customer AS tenKH, orders.total AS thanhTien, orders.money_customer AS tienTra, orders.money_redundant AS tienThoi, orders.created_at AS ngayGiaoDich, users.name AS nhanVienTao FROM orders INNER JOIN users ON orders.user_id = users.id;";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Order order = new Order(
                        resultSet.getString("maHoaDon"),
                        resultSet.getString("tenKH"),
                        resultSet.getDouble("thanhTien"),
                        resultSet.getDouble("tienTra"),
                        resultSet.getDouble("tienThoi"),
                        resultSet.getString("ngayGiaoDich"),
                        resultSet.getString("nhanVienTao")
                );
                orderList.add(order);
            }

//            resultSet.close();
//            preparedStatement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
private void handleReloadButtonClick(ActionEvent event) {
    LocalDate fromDate = fromDayDatePicker.getValue();
    LocalDate toDate = toDayDatePicker.getValue();

    if (fromDate != null && toDate != null) {
        try {
            Connection conn = mysqlconnect.ConnectDb();
            String sql = "SELECT orders.id AS maHoaDon, orders.name_customer AS tenKH, orders.total AS thanhTien, orders.money_customer AS tienTra, orders.money_redundant AS tienThoi, orders.created_at AS ngayGiaoDich, users.name AS nhanVienTao FROM orders INNER JOIN users ON orders.user_id = users.id WHERE orders.created_at BETWEEN ? AND ?;";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            // Set the fromDate and toDate parameters
            preparedStatement.setDate(1, java.sql.Date.valueOf(fromDate));
            preparedStatement.setDate(2, java.sql.Date.valueOf(toDate));

            ResultSet resultSet = preparedStatement.executeQuery();

            // Clear the existing orderList
            orderList.clear();

            while (resultSet.next()) {
                Order order = new Order(
                        resultSet.getString("maHoaDon"),
                        resultSet.getString("tenKH"),
                        resultSet.getDouble("thanhTien"),
                        resultSet.getDouble("tienTra"),
                        resultSet.getDouble("tienThoi"),
                        resultSet.getString("ngayGiaoDich"),
                        resultSet.getString("nhanVienTao")
                );
                orderList.add(order);
            }

            resultSet.close();
            preparedStatement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    } else {
        // Handle the case where fromDate or toDate is not selected
        // You can show an alert or display a message to the user
    }
}


}
