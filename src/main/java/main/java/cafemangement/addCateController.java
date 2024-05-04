package main.java.cafemangement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.java.cafemangement.mysqlconnect;

public class addCateController {

    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private TextField txt_catename;

    @FXML
    public void Add_cate(ActionEvent event) {
        // Check if txt_catename is empty
        String categoryName = txt_catename.getText().trim(); // Trim leading and trailing spaces

        if (categoryName.isEmpty()) {
            // Show an error message if the category name is empty
            JOptionPane.showMessageDialog(null, "Category name cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            return; // Exit the method
        }

        Connection conn = mysqlconnect.ConnectDb();

        String sql = "INSERT INTO category (cate_name) VALUES (?)";
        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, categoryName);

            pst.execute();
            JOptionPane.showMessageDialog(null, "Saved");
             Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.hide();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
       
    }

}
