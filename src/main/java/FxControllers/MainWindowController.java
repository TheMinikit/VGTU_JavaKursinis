package FxControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MainWindowController {

    public Button KategorijuNustatymaiButton;
    public Button VartotojuNustatymaiButton;
    public Button KompanijuNustatymaiButton;

    public void KategorijuNustatymai(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CategoriesWindow.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
        Stage thisStage = (Stage)KategorijuNustatymaiButton.getScene().getWindow();
        thisStage.close();
    }

    public void VartotojuNustatymai(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/UsersWindow.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
        Stage thisStage = (Stage)VartotojuNustatymaiButton.getScene().getWindow();
        thisStage.close();
    }

    public void KompanijuNustatymai(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CompaniesWindow.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
        Stage thisStage = (Stage)KompanijuNustatymaiButton.getScene().getWindow();
        thisStage.close();
    }

    public void HelpAbout(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About the application");
        alert.setHeaderText(null);
        alert.setContentText("Programavimo technologiju Laboratorinis darbas.\n\nVardas: Dmitrij\nPavarde: Kovner\nGrupe: PRIf18/2");
        alert.showAndWait();
    }

    public void SystemLogout(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginWindow.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
        Stage thisStage = (Stage)KategorijuNustatymaiButton.getScene().getWindow();
        thisStage.close();
    }

    public void SystemExit(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void helpAbout(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About the application");
        alert.setHeaderText(null);
        alert.setContentText("Programavimo technologiju Laboratorinis darbas.\n\nVardas: Dmitrij\nPavarde: Kovner\nGrupe: PRIf18/2");
        alert.showAndWait();
    }
}
