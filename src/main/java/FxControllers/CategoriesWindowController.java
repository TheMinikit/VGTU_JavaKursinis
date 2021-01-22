package FxControllers;

import ModelControllers.CategoryController;
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

public class CategoriesWindowController {

    private CategoryController catcontroller;
    private Database db = Database.Database();
    private ArrayList<Category> categoriesList;

    public ComboBox selectCategoryComboBox;
    public ComboBox importCategoriesComboBox;
    public ComboBox categoryAccessUserComboBox;
    public ComboBox categorySubCategoryComboBox;
    public TextField exportCategoriesFileText;
    public TextField categoryNameText;
    public TextField categorySpendingText;
    public TextField categoryEarningText;
    public Label categoryIdText;
    public Label categoryOwnerText;
    public ComboBox filterCategoriesPropertyComboBox;
    public ComboBox filterCategoriesOperationComboBox;
    public TextField filterCategoriesValueText;
    public Button filterCategoriesButton;

    private String selectedCategoryAccessUserPersonalCode;
    private String selectedCategorySubCategoryId;
    private ArrayList<String> properties = new ArrayList<String>();
    private ArrayList<Character> operations = new ArrayList<Character>();

    @FXML
    protected void initialize() throws SQLException, ClassNotFoundException {
        //categoriesList = db.getCategoriesList();
        categoriesList = db.getAllCategories();
        for (Category cat : categoriesList) {
            selectCategoryComboBox.getItems().addAll(cat.getId());
        }
        File directoryPath = new File("src");
        for (File file : directoryPath.listFiles()) {
            importCategoriesComboBox.getItems().addAll(file.getName());
        }
        properties.add("id");
        properties.add("earning");
        properties.add("spending");
        operations.add('>');
        operations.add('<');
        operations.add('=');
        for (String str : properties) {
            filterCategoriesPropertyComboBox.getItems().add(str);
        }
        for (char ch : operations) {
            filterCategoriesOperationComboBox.getItems().add(ch);
        }
    }

    public void loadCategoryData(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        categoryAccessUserComboBox.getItems().clear();
        categorySubCategoryComboBox.getItems().clear();
        int selectedCategoryId = -1;
        try {
            selectedCategoryId = Integer.parseInt(selectCategoryComboBox.getValue().toString());
        } catch (Exception e) {

        }
        String selectedCategoryString = (db.getAllWhere(8, "categories", "id", '=', String.valueOf(selectedCategoryId))).get(0);
        categoryNameText.setText(selectedCategoryString.split(";")[0]);
        categoryIdText.setText(selectedCategoryString.split(";")[1]);
        String parent = selectedCategoryString.split(";")[2];
        String children = selectedCategoryString.split(";")[3];
        categoryEarningText.setText(selectedCategoryString.split(";")[4]);
        categorySpendingText.setText(selectedCategoryString.split(";")[5]);
        categoryOwnerText.setText(selectedCategoryString.split(";")[6]);
        String userswithaccess = selectedCategoryString.split(";")[7];
        //System.out.println(selectedCategoryString.split(";")[0] + " " + selectedCategoryString.split(";")[1] + " " + parent + " " + children + " " + selectedCategoryString.split(";")[4] + " " + selectedCategoryString.split(";")[5] + " " + selectedCategoryString.split(";")[6] + " " + selectedCategoryString.split(";")[7]);
        for (String str : userswithaccess.split(",")) {
            categoryAccessUserComboBox.getItems().add(str);
        }
        for (String str : children.split(",")) {
            categorySubCategoryComboBox.getItems().add(str);
        }
    }

    public void addAccessUser(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Prideti nauja vartotoja");
        dialog.setHeaderText("Irasykite vartotojo asmens koda");
        dialog.setContentText("Vartotojo asmens kodas: ");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            User newUser = db.findUser(result.get());
            Category selectedCategory = db.findCategory(Integer.parseInt(selectCategoryComboBox.getValue().toString()));
            String ids = db.getPropertyWhere("userswithaccess", "categories", "id", '=', String.valueOf(selectedCategory.getId()));
            ArrayList<String> personalcodessarray = new ArrayList<String>();
            for (String id : ids.split(",")) {
                personalcodessarray.add(id);
            }
            personalcodessarray.add(String.valueOf(newUser.getPersonalCode()));
            String userswithaccess = personalcodessarray.get(0);
            for (String id : personalcodessarray) {
                if (id != personalcodessarray.get(0)) {
                    userswithaccess += "," + id;
                }
            }
            db.updateCategory(selectedCategory, "userswithaccess", userswithaccess);
            getAllCategories(actionEvent);
        }
    }

    public void removeAccessUser(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Category selectedCategory = db.findCategory(Integer.parseInt(selectCategoryComboBox.getValue().toString()));
        String ids = db.getPropertyWhere("userswithaccess", "categories", "id", '=', String.valueOf(selectedCategory.getId()));
        ArrayList<String> idsarray = new ArrayList<String>();
        for (String id : ids.split(",")) {
            idsarray.add(id);
        }
        idsarray.remove(selectedCategoryAccessUserPersonalCode);
        String userswithaccess = idsarray.get(0);
        for (String id : idsarray) {
            if (id != idsarray.get(0)) {
                userswithaccess += "," + id;
            }
        }
        db.updateCategory(selectedCategory, "userswithaccess", userswithaccess);
        getAllCategories(actionEvent);
    }

    public void addSubCategory(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Prideti nauja subkategorija");
        dialog.setHeaderText("Irasykite subkategorijos id");
        dialog.setContentText("Subkategorijos id: ");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            Category newCategory = db.findCategory(Integer.parseInt(result.get()));
            Category selectedCategory = db.findCategory(Integer.parseInt(selectCategoryComboBox.getValue().toString()));
            String ids = db.getPropertyWhere("children", "categories", "id", '=', String.valueOf(selectedCategory.getId()));
            ArrayList<String> idsarray = new ArrayList<String>();
            for (String id : ids.split(",")) {
                idsarray.add(id);
            }
            idsarray.add(String.valueOf(newCategory.getId()));
            String childrenids = idsarray.get(0);
            for (String id : idsarray) {
                if (id != idsarray.get(0)) {
                    childrenids += "," + id;
                }
            }
            System.out.print(childrenids);
            db.updateCategory(selectedCategory, "children", childrenids);
            getAllCategories(actionEvent);
        }
    }

    public void removeSubCategory(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Category selectedCategory = db.findCategory(Integer.parseInt(selectCategoryComboBox.getValue().toString()));
        String ids = db.getPropertyWhere("children", "categories", "id", '=', String.valueOf(selectedCategory.getId()));
        ArrayList<String> idsarray = new ArrayList<String>();
        for (String id : ids.split(",")) {
            idsarray.add(id);
        }
        idsarray.remove(selectedCategorySubCategoryId);
        String childrenids = idsarray.get(0);
        for (String id : idsarray) {
            if(id != idsarray.get(0)) {
                childrenids += "," + id;
            }
        }
        db.updateCategory(selectedCategory, "children", childrenids);
        getAllCategories(actionEvent);
    }

    public void setCategoryName(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String newName = categoryNameText.getText();
        Category selectedCategory = db.findCategory(Integer.parseInt(selectCategoryComboBox.getValue().toString()));
        db.updateCategory(selectedCategory, "name", String.valueOf(newName));
    }

    public void setCategorySpending(ActionEvent actionEvent) {
        try {
            float newSpending = Float.parseFloat(categorySpendingText.getText());
            Category selectedCategory = db.findCategory(Integer.parseInt(selectCategoryComboBox.getValue().toString()));
            db.updateCategory(selectedCategory, "spending", String.valueOf(newSpending));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setCategoryEarning(ActionEvent actionEvent) {
        try {
            float newEarning = Float.parseFloat(categoryEarningText.getText());
            Category selectedCategory = db.findCategory(Integer.parseInt(selectCategoryComboBox.getValue().toString()));
            db.updateCategory(selectedCategory, "earning", String.valueOf(newEarning));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void importCategories(ActionEvent actionEvent) {
        String fileName = importCategoriesComboBox.getValue().toString();
        db.importCategoriesDatabase(fileName);
    }

    public void exportCategories(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String fileName = exportCategoriesFileText.getText();
        db.getAllCategories();
        db.exportCategoriesDatabase("src/" + fileName);
    }

    public void SystemLogout(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginWindow.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
        Stage thisStage = (Stage) selectCategoryComboBox.getScene().getWindow();
        thisStage.close();
    }

    public void SystemExit(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void EditCategoryDelete(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        //db.getCategoriesList().remove(catcontroller.getCategory());
        Category selectedCategory = db.findCategory(Integer.parseInt(selectCategoryComboBox.getValue().toString()));
        db.deleteCategory(selectedCategory);
        getAllCategories(actionEvent);
    }

    public void helpAbout(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About the application");
        alert.setHeaderText(null);
        alert.setContentText("Programavimo technologiju Laboratorinis darbas.\n\nVardas: Dmitrij\nPavarde: Kovner\nGrupe: PRIf18/2");
        alert.showAndWait();
    }

    public void SystemReturn(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainWindow.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
        Stage thisStage = (Stage) selectCategoryComboBox.getScene().getWindow();
        thisStage.close();
    }

    public void filterCategories(ActionEvent actionEvent) {
        try {
            selectCategoryComboBox.getItems().clear();
            String property = filterCategoriesPropertyComboBox.getValue().toString();
            char operation = filterCategoriesOperationComboBox.getValue().toString().charAt(0);
            float value = Float.parseFloat(filterCategoriesValueText.getText());
            ArrayList<String> resultsArray = db.getAllWhere(8, "categories", property, operation, String.valueOf(value));
            int id = 0;
            for (String str : resultsArray) {
                for (int i = 0; i < str.split(";").length; i++) {
                    if (i == 1) {
                        id = Integer.parseInt(str.split(";")[1]);
                    }
                }
                selectCategoryComboBox.getItems().add(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getAllCategories(ActionEvent actionEvent) {
        selectCategoryComboBox.getItems().clear();
        categorySubCategoryComboBox.getItems().clear();
        categoryAccessUserComboBox.getItems().clear();
        for (Category cat : categoriesList) {
            selectCategoryComboBox.getItems().addAll(cat.getId());
        }
    }

    public void selectCategoryAccessUser(ActionEvent actionEvent) {
        try {
            selectedCategoryAccessUserPersonalCode = categoryAccessUserComboBox.getValue().toString();
        } catch (Exception e) {

        }
    }

    public void selectCategorySubCategory(ActionEvent actionEvent) {
        try {
            selectedCategorySubCategoryId = categorySubCategoryComboBox.getValue().toString();
        } catch (Exception e) {

        }
    }

    public void EditCategoryCreate(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Sukurti nauja kategorija");
        dialog.setHeaderText("Nauja kategorija");
        dialog.setContentText("Ivestis: ");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String results = result.get();
            String[] array = results.split(" ");
            if (array.length == 8) {
                String name = array[0];
                int id = Integer.valueOf(array[1]);
                Category parent = db.findCategory(Integer.valueOf(array[2]));
                String children = array[3];
                ArrayList<Category> childrenList = new ArrayList<>();
                for(String str : children.split(",")) {
                    childrenList.add(db.findCategory(Integer.valueOf(str)));
                }
                float earning = Float.valueOf(array[4]);
                float spending = Float.valueOf(array[5]);
                User owner = db.findUser(array[6]);
                String userswithaccess = array[7];
                ArrayList<User> usersList = new ArrayList<User>();
                for(String str : userswithaccess.split(",")) {
                    usersList.add(db.findUser(str));
                }
                Category newCat = new Category(name,id,parent,childrenList,earning,spending,owner,usersList);
                db.addCategory(newCat);
                db.getAllCategories();
                getAllCategories(actionEvent);
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
