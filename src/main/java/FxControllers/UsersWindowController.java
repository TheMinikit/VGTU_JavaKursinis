package FxControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import model.Category;
import model.Database;
import model.User;
import ModelControllers.UserController;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class UsersWindowController {

    private UserController usrcontroller;
    private Database db = Database.Database();

    public ComboBox selectUserComboBox;
    public ComboBox importUsersComboBox;
    public TextField userNameText;
    public TextField userSurnameText;
    public TextField userBirthYearText;
    public TextField userPasswordText;
    public TextField exportUsersFileText;
    public ComboBox filterUsersPropertyComboBox;
    public ComboBox filterUsersOperationComboBox;
    public TextField filterUsersValueText;

    private String importUsersFileName;
    private ArrayList<User> usersList;

    private ArrayList<String> properties = new ArrayList<String>();
    private ArrayList<Character> operations = new ArrayList<Character>();

    @FXML
    protected void initialize() throws SQLException, ClassNotFoundException {
        usersList = db.getAllUsers();
        for (User usr : usersList) {
            selectUserComboBox.getItems().add(usr.getPersonalCode());
        }
        File directoryPath = new File("src");
        for (File file : directoryPath.listFiles()) {
            if (file.getName().contains(".txt"))
                importUsersComboBox.getItems().add(file.getName());
        }
        properties.add("personalcode");
        properties.add("name");
        properties.add("surname");
        properties.add("birthyear");
        operations.add('>');
        operations.add('<');
        operations.add('=');
        for (String str : properties) {
            filterUsersPropertyComboBox.getItems().add(str);
        }
        for (char ch : operations) {
            filterUsersOperationComboBox.getItems().add(ch);
        }
    }

    public void setUserName(ActionEvent actionEvent) {
        String newUserName = userNameText.getText();
        usrcontroller.setUserName(newUserName);
        try {
            String newName = userNameText.getText();
            User selectedUser = db.findUser(selectUserComboBox.getValue().toString());
            db.updateUser(selectedUser, "name", newName);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void importUsers(ActionEvent actionEvent) {
        db.importUserDatabase(importUsersFileName);
    }

    public void setUserBirthYear(ActionEvent actionEvent) {
        int newUserBirthYear = Integer.parseInt(userBirthYearText.getText());
        usrcontroller.setUserBirthYear(newUserBirthYear);
        try {
            String newBirthYear = userBirthYearText.getText();
            User selectedUser = db.findUser(selectUserComboBox.getValue().toString());
            db.updateUser(selectedUser, "birthyear", newBirthYear);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setUserPassword(ActionEvent actionEvent) {
        String newUserPassword = userPasswordText.getText();
        usrcontroller.setUserPassword(newUserPassword);
        try {
            String newPassword = userPasswordText.getText();
            User selectedUser = db.findUser(selectUserComboBox.getValue().toString());
            db.updateUser(selectedUser, "password", newPassword);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void exportUsers(ActionEvent actionEvent) {
        String fileName = exportUsersFileText.getText();
        db.exportUserDatabase(fileName);
    }

    public void systemLogout(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginWindow.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
        Stage thisStage = (Stage) selectUserComboBox.getScene().getWindow();
        thisStage.close();
    }

    public void systemExit(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void editUserDelete(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        db.getUsersList().remove(usrcontroller.getUser());
        db.deleteUser(usrcontroller.getUser());
    }

    public void editUserCreate(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Sukurti nauja vartotoja");
        dialog.setHeaderText("Naujas Vartotojas");
        dialog.setContentText("Naujo vartotojo duomenys: ");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String results = result.get();
            String[] array = results.split(" ");
            if (array.length == 5) {
                String name = array[0];
                String surname = array[1];
                String pass = array[2];
                int birthyear = Integer.valueOf(array[3]);
                String personalcode = array[4];
                User newUser = new User(name, surname, birthyear, pass, personalcode);
                db.addUser(newUser);
                db.getAllUsers();
                getAllUsers(actionEvent);
            }
            else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Invalid Input");
                alert.setHeaderText(null);
                alert.setContentText("Invalid Input.");
                alert.showAndWait();
            }
        }
    }

    public void helpAbout(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About the application");
        alert.setHeaderText(null);
        alert.setContentText("Programavimo technologiju Laboratorinis darbas.\n\nVardas: Dmitrij\nPavarde: Kovner\nGrupe: PRIf18/2");
        alert.showAndWait();
    }

    public void loadImportUsersFile(ActionEvent actionEvent) {
        importUsersFileName = importUsersComboBox.getValue().toString();
    }

    public void loadUserData(ActionEvent actionEvent) {
        String selectedUserPersonalCode = String.valueOf(selectUserComboBox.getValue());
        User selectedUser = db.findUser(selectedUserPersonalCode);
        usrcontroller = new UserController(db.findUser(selectedUserPersonalCode));
        userNameText.setText(selectedUser.getName());
        userSurnameText.setText(selectedUser.getSurname());
        userPasswordText.setText(selectedUser.getPassword());
        userBirthYearText.setText(String.valueOf(selectedUser.getBirthYear()));
    }

    public void SystemReturn(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainWindow.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
        Stage thisStage = (Stage) selectUserComboBox.getScene().getWindow();
        thisStage.close();
    }

    public void setUserSurname(ActionEvent actionEvent) {
        try {
            String newSurname = userSurnameText.getText();
            User selectedUser = db.findUser(selectUserComboBox.getValue().toString());
            db.updateUser(selectedUser, "surname", newSurname);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void filterUsers(ActionEvent actionEvent) {
        try {
            selectUserComboBox.getItems().clear();
            String property = String.valueOf(filterUsersPropertyComboBox.getValue());
            char operation = String.valueOf(filterUsersOperationComboBox.getValue()).charAt(0);
            String value = filterUsersValueText.getText();
            int valueInt = 0;
            try {
                valueInt = Integer.parseInt(value);
            } catch (Exception e) {
                value = '"' + value + '"';
            }
            ArrayList<String> resultsArray = db.getAllWhere(5, "users", property, operation, value);
            String name = "";
            String surname = "";
            String password = "";
            int birthyear = 0;
            String personalcode = "";
            for (String str : resultsArray) {
                for (int i = 0; i < str.split(";").length; i++) {
                    if (i == 0) name = str.split(";")[0];
                    else if (i == 1) surname = str.split(";")[1];
                    else if (i == 2) password = str.split(";")[2];
                    else if (i == 3) birthyear = Integer.parseInt(str.split(";")[3]);
                    else if (i == 4) {
                        personalcode = str.split(";")[4];
                        System.out.println(personalcode);
                    }
                }
                selectUserComboBox.getItems().add(personalcode);
            }
        } catch (Exception e) {
            System.out.println("Filter Users error.");
            e.printStackTrace();
        }
    }

    public void getAllUsers(ActionEvent actionEvent) {
        selectUserComboBox.getItems().clear();
        for (User usr : usersList) {
            selectUserComboBox.getItems().add(usr.getPersonalCode());
        }
    }

}
