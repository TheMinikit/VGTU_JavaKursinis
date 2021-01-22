package FxControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Database;

import java.io.IOException;
import java.sql.SQLException;

public class LoginWindowController {

    public TextField loginNameField;
    public TextField loginSurnameField;
    public PasswordField loginPasswordField;

    Database db = Database.Database();

    public void Login(ActionEvent actionEvent) throws IOException, SQLException, ClassNotFoundException {
        String name = loginNameField.getText();
        String surname = loginSurnameField.getText();
        String password = loginPasswordField.getText();
        db.getAllUsers();
        if (db.checkForUser(name, surname, password)) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainWindow.fxml"));
            Parent root = loader.load();
            MainWindowController mainWindowController = loader.getController();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
            Stage thisStage = (Stage)loginNameField.getScene().getWindow();
            thisStage.close();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Invalid Credentials");
            alert.setHeaderText(null);
            alert.setContentText("Neteisingi duomenys.");
            alert.showAndWait();
        }
    }

}
