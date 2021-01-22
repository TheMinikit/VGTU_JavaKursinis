package FxControllers;

import ModelControllers.CompanyController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class CompaniesWindowController {

    private Database db = Database.Database();
    private CompanyController cmpcontroller;
    private ArrayList<Company> companiesList;
    private String selectedCompanyUserPersonalCode;
    private String selectedCompaniesImportFile;

    public ComboBox selectCompanyComboBox;
    public ComboBox importCompaniesComboBox;
    public TextField exportCompaniesFileText;
    public ComboBox filterCompaniesOperationComboBox;
    public TextField filterCompaniesValueText;
    public TextField companyNameText;
    public ComboBox companyUsersComboBox;

    private ArrayList<Character> operations = new ArrayList<Character>();

    @FXML
    protected void initialize() throws SQLException, ClassNotFoundException {
        companiesList = db.getAllCompanies();
        for (Company cmp : companiesList) {
            selectCompanyComboBox.getItems().add(cmp.getId());
        }
        File directoryPath = new File("\\");
        for (File file : directoryPath.listFiles()) {
            importCompaniesComboBox.getItems().add(file.getName());
        }
        operations.add('>');
        operations.add('<');
        operations.add('=');
        for (char ch : operations) {
            filterCompaniesOperationComboBox.getItems().add(ch);
        }
    }

    public void addCompanyUser(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Prideti nauja vartotoja");
        dialog.setHeaderText("Irasykite vartotojo asmens koda");
        dialog.setContentText("Vartotojo asmens kodas: ");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            User newUser = db.findUser(result.get());
            cmpcontroller.addCompanyUsers(newUser);
            Company selectedCompany = db.findCompany(Integer.parseInt(selectCompanyComboBox.getValue().toString()));
            String ids = db.getPropertyWhere("companyusers", "companies", "companyid", '=', String.valueOf(selectedCompany.getId()));
            ArrayList<String> personalcodessarray = new ArrayList<String>();
            for (String id : ids.split(",")) {
                personalcodessarray.add(id);
            }
            personalcodessarray.add(String.valueOf(newUser.getPersonalCode()));
            String userswithaccess = personalcodessarray.get(0);
            for (String id : personalcodessarray) {
                if (personalcodessarray.get(0) != id) {
                    userswithaccess += "," + id;
                }
            }
            System.out.println("New userswithaccess: " + userswithaccess);
            db.updateCompany(selectedCompany, "companyusers", userswithaccess);
        }
    }

    public void removeCompanyUser(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        System.out.println("PersonalCode: " + selectedCompanyUserPersonalCode);
        User selectedUser = db.findUser(selectedCompanyUserPersonalCode);
        cmpcontroller.getCompanyUsers().remove(selectedUser);
        Company selectedCompany = db.findCompany(Integer.parseInt(selectCompanyComboBox.getValue().toString()));
        String ids = db.getPropertyWhere("companyusers", "companies", "companyid", '=', String.valueOf(selectedCompany.getId()));
        System.out.println(ids);
        ArrayList<String> idsarray = new ArrayList<String>();
        for (String id : ids.split(",")) {
            idsarray.add(id);
        }
        idsarray.remove(selectedCompanyUserPersonalCode);
        String userswithaccess = idsarray.get(0);
        for (String id : idsarray) {
            if(id != idsarray.get(0)) {
                userswithaccess += "," + id;
            }
        }
        db.updateCompany(selectedCompany, "companyusers", userswithaccess);
        loadCompanyData(actionEvent);
    }

    public void systemLogout(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginWindow.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
        Stage thisStage = (Stage) selectCompanyComboBox.getScene().getWindow();
        thisStage.close();
    }

    public void systemExit(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void editCompanyDelete(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        db.getCompaniesList().remove(cmpcontroller.getCompany());
        Company selectedCompany = db.findCompany(Integer.parseInt(selectCompanyComboBox.getValue().toString()));
        db.deleteCompany(selectedCompany);
    }

    public void helpAbout(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About the application");
        alert.setHeaderText(null);
        alert.setContentText("Programavimo technologiju Laboratorinis darbas.\n\nVardas: Dmitrij\nPavarde: Kovner\nGrupe: PRIf18/2");
        alert.showAndWait();
    }

    public void exportCompanies(ActionEvent actionEvent) {
        String fileName = exportCompaniesFileText.getText();
        db.exportCompaniesDatabase(fileName);
    }

    public void importCompanies(ActionEvent actionEvent) {
        String fileName = importCompaniesComboBox.getValue().toString();
        db.importCompaniesDatabase(fileName);
    }

    public void loadCompanyData(ActionEvent actionEvent) {
        try {
            int selectedCompanyId = Integer.parseInt(selectCompanyComboBox.getValue().toString());
            Company selectedCompany = db.findCompany(selectedCompanyId);
            cmpcontroller = new CompanyController(selectedCompany);
            companyUsersComboBox.getItems().clear();
            for (User usr : cmpcontroller.getCompanyUsers()) {
                companyUsersComboBox.getItems().add(usr.getPersonalCode());
            }
            companyNameText.setText(cmpcontroller.getCompanyName());
        } catch (Exception e) {

        }
    }

    public void loadImportFile(ActionEvent actionEvent) {
        selectedCompaniesImportFile = importCompaniesComboBox.getValue().toString();
        db.importCompaniesDatabase(selectedCompaniesImportFile);
    }

    public void SystemReturn(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainWindow.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
        Stage thisStage = (Stage) selectCompanyComboBox.getScene().getWindow();
        thisStage.close();
    }

    public void filterCompanies(ActionEvent actionEvent) {
        try {
            selectCompanyComboBox.getItems().clear();
            char operation = String.valueOf(filterCompaniesOperationComboBox.getValue()).charAt(0);
            String value = filterCompaniesValueText.getText();
            int valueInt = 0;
            try {
                valueInt = Integer.parseInt(value);
            } catch (Exception e) {
                value = '"' + value + '"';
            }
            ArrayList<String> resultsArray = db.getAllWhere(3, "companies", "companyid", operation, value);
            int id = 0;
            for (String str : resultsArray) {
                System.out.println(str);
                for (int i = 0; i < str.split(";").length; i++) {
                    if (i == 1) {
                        id = Integer.parseInt(str.split(";")[1]);
                    }
                }
                selectCompanyComboBox.getItems().add(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCompanyName(ActionEvent actionEvent) {
        try {
            String newName = companyNameText.getText();
            Company selectedCompany = db.findCompany(Integer.parseInt(selectCompanyComboBox.getValue().toString()));
            db.updateCompany(selectedCompany, "name", String.valueOf(newName));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void selectCompanyUser(ActionEvent actionEvent) {
        try {
            selectedCompanyUserPersonalCode = companyUsersComboBox.getValue().toString();
        } catch (Exception e) {

        }
    }

    public void getAllCompanies(ActionEvent actionEvent) {
        selectCompanyComboBox.getItems().clear();
        for (Company cmp : companiesList) {
            selectCompanyComboBox.getItems().add(cmp.getId());
        }
    }

    public void editCompanyCreate(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Sukurti nauja kompanija");
        dialog.setHeaderText("Nauja kompanija");
        dialog.setContentText("Ivestis: ");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String results = result.get();
            String[] array = results.split(" ");
            if (array.length == 3) {
                String name = array[0];
                String id = array[1];
                String users = array[2];
                ArrayList<User> userslist = new ArrayList<User>();
                for(String str : users.split(",")) {
                    userslist.add(db.findUser(str));
                }
                Company newCompany = new Company(name, Integer.valueOf(id), userslist);
                db.addCompany(newCompany);
                db.getAllCompanies();
                getAllCompanies(actionEvent);
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
}

