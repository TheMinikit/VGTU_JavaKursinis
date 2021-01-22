package model;

import ModelControllers.CategoryController;
import ModelControllers.CompanyController;
import ModelControllers.UserController;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Database {

    protected ArrayList<User> usersList = new ArrayList<User>();
    protected ArrayList<Company> companiesList = new ArrayList<Company>();
    protected ArrayList<Category> categoriesList = new ArrayList<Category>();

    private static Database db = null;

    public static Database Database() {
        try {
            if (db == null) db = new Database();
            return db;
        } catch (Exception e) {
            System.out.println("Caught an Exception while initializing Database: ");
            e.printStackTrace();
            return null;
        }
    }

    public boolean checkForUser(String name, String surname, String password) {
        for (User user : usersList) {
            if (user.getName().equals(name) && user.getSurname().equals(surname) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public User findUser(String name, String surname, String password) {
        for (User usr : usersList) {
            if (usr.getName().equals(name) && usr.getSurname().equals(surname) && usr.getPassword().equals(password)) {
                return usr;
            }
        }
        return new User("0", "0", 0, "0", "0");
    }

    public User findUser(String personalCode) {
        for (User usr : usersList) {
            if (usr.getPersonalCode().equals(personalCode)) {
                return usr;
            }
        }
        return new User("0", "0", 0, "0", "0");
    }

    public Company findCompany(int id) {
        for (Company cmp : companiesList) {
            if (cmp.getId() == id) {
                return cmp;
            }
        }
        return new Company("0", -1, null);
    }

    public Category findCategory(int id) {
        for (Category cat : categoriesList) {
            if (cat.getId() == id) {
                return cat;
            }
        }
        return new Category("0", -1, null, null);
    }

    public void updateUsersDatabase() {
        File usersFile = new File("Users.txt");
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(usersFile, false);
            for (User userData : usersList) {
                fileWriter.write(userData.getName() + " " +
                        userData.getSurname() + " " +
                        userData.getBirthYear() + " " +
                        userData.getPassword() + " " +
                        userData.getPersonalCode() + "\n");
            }
            fileWriter.close();
        } catch (Exception e) {
            System.out.println("Caught Exception: ");
            e.printStackTrace();
        }
    }

    public void updateCompaniesDatabase() {
        File usersFile = new File("Companies.txt");
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(usersFile, false);
            for (Company companyData : companiesList) {
                fileWriter.write(companyData.getName() + " " +
                        companyData.getId() + " " + companyData.getUsers().size() + " ");
                for (User usr : companyData.getUsers()) {
                    fileWriter.write(usr.getPersonalCode() + " ");
                }
                fileWriter.write("\n");
            }
            fileWriter.close();
        } catch (Exception e) {
            System.out.println("Caught Exception: ");
            e.printStackTrace();
        }
    }

    public void updateCategoriesDatabase() {
        File categoriesFile = new File("Categories.txt");
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(categoriesFile, false);
            for (Category categoryData : categoriesList) {
                fileWriter.write(categoryData.getName() + " " +
                        categoryData.getId() + " " + categoryData.getAccessList().size() + " ");
                fileWriter.write(categoryData.getOwner().getPersonalCode() + " ");
                for (User usr : categoryData.getAccessList()) {
                    fileWriter.write(usr.getPersonalCode() + " ");
                }
                fileWriter.write(categoryData.getChildren().size() + " " + categoryData.getParent().getId() + " ");
                for (Category subcat : categoryData.getChildren()) {
                    fileWriter.write(subcat.getId() + " ");
                }
                fileWriter.write("\n");
            }
            fileWriter.close();
        } catch (Exception e) {
            System.out.println("Caught Exception: ");
            e.printStackTrace();
        }
    }

    public void importUserDatabase(String fileName) {
        try {
            Scanner scanner = new Scanner(new File(fileName));
            while (scanner.hasNext()) {
                String name = scanner.next();
                String surname = scanner.next();
                int birthYear = scanner.nextInt();
                String password = scanner.next();
                String personalCode = scanner.next();
                User usr = new User(name, surname, birthYear, password, personalCode);
                usersList.add(usr);
            }
        } catch (Exception e) {
            System.out.println("Caught an Exception: ");
            e.printStackTrace();
        }
    }

    public void importCompaniesDatabase(String fileName) {
        try {
            Scanner scanner = new Scanner(new File(fileName));
            while (scanner.hasNext()) {
                String name = scanner.next();
                int id = scanner.nextInt();
                int size = scanner.nextInt();
                ArrayList<User> userCodes = new ArrayList<User>();
                for (int i = 0; i < size; i++) {
                    userCodes.add(findUser(String.valueOf(scanner.nextInt())));
                }
                Company cmp = new Company(name, id, userCodes);
                companiesList.add(cmp);
            }
        } catch (Exception e) {
            System.out.println("Caught an Exception: ");
            e.printStackTrace();
        }
    }

    public void importCategoriesDatabase(String fileName) {
        try {
            Scanner scanner = new Scanner(new File(fileName));
            while (scanner.hasNext()) {
                String name = scanner.next();
                int id = scanner.nextInt();
                int parent = scanner.nextInt();
                int subcatsSize = scanner.nextInt();
                ArrayList<Category> subcategories = new ArrayList<Category>();
                for (int i = 0; i < subcatsSize; i++) {
                    subcategories.add(findCategory(scanner.nextInt()));
                }
                int earning = scanner.nextInt();
                int spending = scanner.nextInt();
                String owner = scanner.next();
                int usersSize = scanner.nextInt();
                ArrayList<User> userCodes = new ArrayList<User>();
                for (int i = 0; i < usersSize; i++) {
                    userCodes.add(findUser(String.valueOf(scanner.nextInt())));
                }
                //Category cat = new Category(name, id, findCategory(parent), findUser(owner));
                Category cat = new Category(name, id, findCategory(parent), subcategories, earning, spending, findUser(owner), userCodes);
                categoriesList.add(cat);
            }
        } catch (Exception e) {
            System.out.println("Caught an Exception: ");
            e.printStackTrace();
        }
    }

    public void exportUserDatabase(String fileName) {
        File usersFile = new File("src/" + fileName);
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(usersFile, false);
            for (User userData : usersList) {
                fileWriter.write(userData.getName() + " " +
                        userData.getSurname() + " " +
                        userData.getBirthYear() + " " +
                        userData.getPassword() + " " +
                        userData.getPersonalCode() + "\n");
            }
            fileWriter.close();
        } catch (Exception e) {
            System.out.println("Caught Exception: ");
            e.printStackTrace();
        }
    }

    public void exportCompaniesDatabase(String fileName) {
        File usersFile = new File(fileName);
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(usersFile, false);
            for (Company companyData : companiesList) {
                fileWriter.write(companyData.getName() + " " +
                        companyData.getId() + " " + companyData.getUsers().size() + " ");
                for (User usr : companyData.getUsers()) {
                    fileWriter.write(usr.getPersonalCode() + " ");
                }
            }
            fileWriter.close();
        } catch (Exception e) {
            System.out.println("Caught Exception: ");
            e.printStackTrace();
        }
    }

    public void exportCategoriesDatabase(String fileName) {
        File categoriesFile = new File(fileName);
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(categoriesFile, false);
            for (Category categoryData : categoriesList) {
                try {
                    fileWriter.write(categoryData.getName() + " " +
                            categoryData.getId() + " " + categoryData.getParent().getId() + " " + categoryData.getChildren().size() + " ");
                    for (Category cat : categoryData.getChildren()) {
                        fileWriter.write(cat.getId() + " ");
                    }
                } catch (Exception e) {
                    fileWriter.write(categoryData.getName() + " " +
                            categoryData.getId() + "-1 1 -1 ");
                }
                fileWriter.write(categoryData.getEarning() + " " + categoryData.getSpending() + " ");
                try {
                    fileWriter.write(categoryData.getOwner().getPersonalCode() + " " + categoryData.getAccessList().size() + " ");
                    for (User usr : categoryData.getAccessList()) {
                        fileWriter.write(usr.getPersonalCode() + " ");
                    }
                } catch (Exception e) {
                    fileWriter.write("-1 1 -1");
                }
            }
            fileWriter.close();
        } catch (Exception e) {
            System.out.println("Caught Exception: ");
            e.printStackTrace();
        }
    }

    public void manageUsers(User loggedUser) {
        UserController loggedUserController = new UserController(loggedUser);
        Scanner scanner = new Scanner(System.in);
        int menuInput = 0;
        while (menuInput != 6) {
            System.out.println("Pasirinkite veiksma: " +
                    "\n1. Sukurti nauja vartotoja" +
                    "\n2. Redaguoti egzistuojanti vartotoja" +
                    "\n3. Istrinti vartotoja" +
                    "\n4. Eksportuoti vartotojus" +
                    "\n5. Importuoti vartotojus" +
                    "\n6. Grizti atgal");
            menuInput = scanner.nextInt();
            switch (menuInput) {
                case 1:
                    System.out.print("Iveskite varda: ");
                    String newName = scanner.next();
                    System.out.print("Iveskite pavarde: ");
                    String newSurname = scanner.next();
                    System.out.print("Iveskite gimimo metus: ");
                    int newBirthYear = scanner.nextInt();
                    System.out.print("Iveskite slaptazodi: ");
                    String newPassword = scanner.next();
                    System.out.print("Iveskite asmens koda: ");
                    String newPersonalCode = scanner.next();
                    User newUser = new User(newName, newSurname, newBirthYear, newPassword, newPersonalCode);
                    usersList.add(newUser);
                    updateUsersDatabase();
                    break;
                case 2:
                    int i = 0;
                    for (User usr : usersList) {
                        System.out.println(i + ") " + usr.getName() + " " + usr.getSurname());
                        i++;
                    }
                    System.out.print("Kuri vartotoja norite redaguoti?(0 - grizti atgal): ");
                    i = scanner.nextInt();
                    if (i == 0 || i > usersList.size()) {
                        break;
                    } else {
                        UserController selectedUserController = new UserController(usersList.get(i));
                        selectedUserController.setUserData();
                        updateUsersDatabase();
                    }
                    break;
                case 3:
                    int i2 = 0;
                    for (User usr : usersList) {
                        System.out.println(i2 + ") " + usr.getName() + " " + usr.getSurname());
                        i2++;
                    }
                    System.out.print("Kuri vartotoja norite istrinti?(0 - grizti atgal): ");
                    i2 = scanner.nextInt();
                    if (i2 == 0 || i2 > usersList.size()) {
                        break;
                    } else {
                        usersList.remove(i2);
                        updateUsersDatabase();
                    }
                    break;
                case 4:
                    System.out.print("Iveskie failo pavadinima: ");
                    String exportFileName = scanner.next();
                    exportUserDatabase(exportFileName);
                    break;
                case 5:
                    System.out.print("Iveskie failo pavadinima: ");
                    String importFileName = scanner.next();
                    importUserDatabase(importFileName);
                    break;
                case 6:
                    break;
                default:
                    System.out.println("Neteisingas pasirinkimas.");
            }
        }
    }

    public void manageCompanies(User loggedUser) {
        Scanner scanner = new Scanner(System.in);
        int menuInput = 0;
        while (menuInput != 6) {
            System.out.println("Pasirinkite veiksma: " +
                    "\n1. Sukurti nauja kompanija" +
                    "\n2. Redaguoti egzistuojancia kompanija" +
                    "\n3. Istrinti kompanija" +
                    "\n4. Eksportuoti kompanijas" +
                    "\n5. Importuoti kompanijas" +
                    "\n6. Grizti atgal");
            menuInput = scanner.nextInt();
            switch (menuInput) {
                case 1:
                    System.out.print("Iveskite pavadinima: ");
                    String newName = scanner.next();
                    System.out.print("Iveskite unikalu kompanijos koda: ");
                    int newId = scanner.nextInt();
                    ArrayList<User> userCreatorArray = new ArrayList<User>();
                    userCreatorArray.add(loggedUser);
                    Company newCompany = new Company(newName, newId, userCreatorArray);
                    companiesList.add(newCompany);
                    updateCompaniesDatabase();
                    break;
                case 2:
                    int i = 0;
                    for (Company cmp : companiesList) {
                        System.out.println(i + 1 + ") " + cmp.getName());
                    }
                    System.out.println("Kuria kompanija norite redaguoti?(0 - grizti atgal): ");
                    i = scanner.nextInt();
                    if (i == 0 || i > companiesList.size()) {
                        break;
                    } else {
                        CompanyController selectedCompanyController = new CompanyController(companiesList.get(i - 1));
                        selectedCompanyController.editCompany();
                        updateUsersDatabase();
                    }
                    break;
                case 3:
                    int i2 = 0;
                    for (Company cmp : companiesList) {
                        System.out.println(i2 + ") " + cmp.getName());
                        i2++;
                    }
                    System.out.print("Kuria kompanija?(0 - grizti atgal): ");
                    i2 = scanner.nextInt();
                    if (i2 == 0 || i2 > companiesList.size()) {
                        break;
                    } else {
                        companiesList.remove(i2 - 1);
                        updateCompaniesDatabase();
                    }
                    break;
                case 4:
                    System.out.print("Iveskie failo pavadinima: ");
                    String exportFileName = scanner.next();
                    exportCompaniesDatabase(exportFileName);
                    break;
                case 5:
                    System.out.print("Iveskie failo pavadinima: ");
                    String importFileName = scanner.next();
                    importCompaniesDatabase(importFileName);
                    break;
                case 6:
                    break;
                default:
                    System.out.println("Neteisingas pasirinkimas.");
                    break;
            }
        }
    }

    public void manageCategories(User loggedUser) {
        Scanner scanner = new Scanner(System.in);
        int menuInput = 0;
        while (menuInput != 6) {
            System.out.println("Pasirinkite veiksma: " +
                    "\n1. Sukurti nauja kategorija" +
                    "\n2. Redaguoti egzistuojancia kategorija" +
                    "\n3. Istrinti kategorija" +
                    "\n4. Eksportuoti kategorijas" +
                    "\n5. Importuoti kategorijas" +
                    "\n6. Grizti atgal");
            menuInput = scanner.nextInt();
            switch (menuInput) {
                case 1:
                    System.out.print("Iveskite pavadinima: ");
                    String newName = scanner.next();
                    System.out.print("Iveskite unikalu kompanijos koda: ");
                    int newId = scanner.nextInt();
                    Category newCategory = new Category(newName, newId, null, loggedUser);
                    categoriesList.add(newCategory);
                    updateCategoriesDatabase();
                    break;
                case 2:
                    int i2 = 1;
                    for (Company cmp : companiesList) {
                        System.out.println(i2 + ") " + cmp.getName());
                    }
                    System.out.println("Kuria kategorija norite redaguoti?(0 - grizti atgal): ");
                    i2 = scanner.nextInt();
                    if (i2 == 0 || i2 > categoriesList.size()) {
                        break;
                    } else {
                        CategoryController selectedCategoryController = new CategoryController(categoriesList.get(i2 - 1));
                        selectedCategoryController.editCategory();
                        updateCategoriesDatabase();
                    }
                    break;
                case 3:
                    int i3 = 0;
                    for (Category cat : categoriesList) {
                        System.out.println(i3 + ") " + cat.getName());
                        i3++;
                    }
                    System.out.print("Kuria kategorija?(0 - grizti atgal): ");
                    i3 = scanner.nextInt();
                    if (i3 == 0 || i3 > categoriesList.size()) {
                        break;
                    } else {
                        categoriesList.remove(i3 - 1);
                        updateCategoriesDatabase();
                    }
                    break;
                case 4:
                    System.out.print("Iveskie failo pavadinima: ");
                    String exportFileName = scanner.next();
                    exportCategoriesDatabase(exportFileName);
                    break;
                case 5:
                    System.out.print("Iveskie failo pavadinima: ");
                    String importFileName = scanner.next();
                    importCategoriesDatabase(importFileName);
                    break;
                case 6:
                    break;
                default:
                    System.out.println("Neteisingas pasirinkimas.");
                    break;
            }
        }
    }

    public ArrayList<User> getUsersList() {
        return usersList;
    }

    public ArrayList<Company> getCompaniesList() {
        return companiesList;
    }

    public ArrayList<Category> getCategoriesList() {
        return categoriesList;
    }

    public static Connection connectToDb() throws ClassNotFoundException {
        Connection con = null;
        Class.forName("com.mysql.cj.jdbc.Driver");
        String DB_URL = "jdbc:mysql://localhost/lab";
        String USER = "root";
        String PASS = "";
        try {
            con = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return con;
    }

    public static void disconnectFromDb(Connection connection, Statement statement) throws SQLException {
        connection.close();
        statement.close();
    }

    public String getPropertyWhere(String property, String table, String property2, char operation, String value) throws SQLException, ClassNotFoundException {
        Connection con = connectToDb();
        Statement stmt = con.createStatement();
        String sql = "SELECT " + property + " FROM " + table + " WHERE " + property2 + " " + operation + " " + value;
        ResultSet rs = stmt.executeQuery(sql);
        String resultString = "0";
        rs.next();
        try {
            resultString = rs.getString(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        disconnectFromDb(con, stmt);
        System.out.println("ResultsString: " + resultString);
        return resultString;
    }

    public ArrayList<String> getAllWhere(int size, String table, String property, char operation, String value) throws SQLException, ClassNotFoundException {
        Connection con = connectToDb();
        Statement stmt = con.createStatement();
        String sql = "Select * from " + table + " where " + property + " " + operation + " " + value;
        ResultSet rs = stmt.executeQuery(sql);
        String tempString = "";
        ArrayList<String> resultsString = new ArrayList<String>();
        while (rs.next()) {
            tempString = "";
            for (int i = 1; i <= size; i++) {
                tempString += rs.getString(i) + ";";
            }
            resultsString.add(tempString);
        }
        disconnectFromDb(con, stmt);
        return resultsString;
    }

    public ArrayList<User> getAllUsers() throws ClassNotFoundException, SQLException {
        //ArrayList<User> users = new ArrayList<User>();
        usersList.clear();
        Connection con = connectToDb();
        Statement stmt = con.createStatement();
        String sql = "Select * from users";
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            String name = rs.getString(1);
            String surname = rs.getString(2);
            String password = rs.getString(3);
            int birthyear = rs.getInt(4);
            String personalcode = rs.getString(5);
            //StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
            //for (int i = 0; i < stackTraceElements.length; i++) {
            //    System.out.println(stackTraceElements[i]);
            //}
            //System.out.println("getAllUsers");
            //System.out.println(name + " " + surname + " " + birthyear + " " + password + " " + personalcode);
            User usr = new User(name, surname, birthyear, password, personalcode);
            usersList.add(usr);
        }
        disconnectFromDb(con, stmt);
        return usersList;
    }

    public void addUser(User usr) throws ClassNotFoundException, SQLException {
        Connection con = connectToDb();
        String insertString = "insert into users(`name`, `surname`, `password`, `birthyear`, `personalcode`) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement insertUser = con.prepareStatement(insertString);
        insertUser.setString(1, usr.getName());
        insertUser.setString(2, usr.getSurname());
        insertUser.setString(3, usr.getPassword());
        insertUser.setInt(4, usr.getBirthYear());
        insertUser.setString(5, usr.getPersonalCode());
        insertUser.execute();
        disconnectFromDb(con, insertUser);
    }

    public void deleteUser(User usr) throws ClassNotFoundException, SQLException {
        Connection con = connectToDb();
        String insertString = "delete from users where personalcode = ?";
        PreparedStatement deleteUser = con.prepareStatement(insertString);
        deleteUser.setString(1, usr.getPersonalCode());
        deleteUser.execute();
        disconnectFromDb(con, deleteUser);
    }

    public void updateUser(User usr, String property, String value) throws ClassNotFoundException, SQLException {
        Connection con = connectToDb();
        if (property != "birthyear") value = '"' + value + '"';
        String insertString = "UPDATE users SET " + property + " = " + value + " WHERE personalcode = " + '"' + usr.getPersonalCode() + '"';
        PreparedStatement updateUser = con.prepareStatement(insertString);
        updateUser.execute();
        disconnectFromDb(con, updateUser);
    }

    public ArrayList<Company> getAllCompanies() throws ClassNotFoundException, SQLException {
        ArrayList<Company> companies = new ArrayList<Company>();
        Connection con = connectToDb();
        Statement stmt = con.createStatement();
        String sql = "Select * from companies";
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            String name = rs.getString(1);
            int id = rs.getInt(2);
            String users = rs.getString(3);
            ArrayList<User> userswithaccess = new ArrayList<User>();
            for (String str : users.split(",")) {
                User founduser = findUser(str);
                userswithaccess.add(founduser);
            }
            Company cmp = new Company(name, id, userswithaccess);
            companiesList.add(cmp);
        }
        disconnectFromDb(con, stmt);
        return companiesList;
    }

    public void addCompany(Company cmp) throws ClassNotFoundException, SQLException {
        Connection con = connectToDb();
        String insertString = "insert into companies(`name`, `id`, `userswithaccess`) VALUES (?, ?, ?)";
        PreparedStatement insertCompany = con.prepareStatement(insertString);
        insertCompany.setString(1, cmp.getName());
        insertCompany.setInt(2, cmp.getId());
        String userswithaccess = "";
        try {
            for (User usr : cmp.getUsers()) {
                userswithaccess += usr.getPersonalCode() + ",";
            }
            insertCompany.setString(3, userswithaccess);
        } catch (Exception e) {
            insertCompany.setString(3, "-1");
        }
        insertCompany.execute();
        disconnectFromDb(con, insertCompany);
    }

    public void deleteCompany(Company cmp) throws ClassNotFoundException, SQLException {
        Connection con = connectToDb();
        String insertString = "delete from companies where id = ?";
        PreparedStatement deleteCompany = con.prepareStatement(insertString);
        deleteCompany.setInt(1, cmp.getId());
        deleteCompany.execute();
        disconnectFromDb(con, deleteCompany);
    }

    public void updateCompany(Company cmp, String property, String value) throws ClassNotFoundException, SQLException {
        Connection con = connectToDb();
        //String insertString = "update companies set ? = ? where id = ?";
        String insertString = "update companies set " + property + " = '" + value + "' where companyid = " + cmp.getId();
        PreparedStatement updateCompany = con.prepareStatement(insertString);
        //updateCompany.setString(1, property);
        //updateCompany.setString(2, value);
        //updateCompany.setInt(3, cmp.getId());
        System.out.println(insertString);
        updateCompany.execute();
        disconnectFromDb(con, updateCompany);
    }

    public ArrayList<Category> getAllCategories() throws ClassNotFoundException, SQLException {
        ArrayList<Category> categories = new ArrayList<Category>();
        Connection con = connectToDb();
        Statement stmt = con.createStatement();
        String sql = "Select * from categories";
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            String name = rs.getString(1);
            int id = rs.getInt(2);
            int parent = rs.getInt(3);
            String children = rs.getString(4);
            ArrayList<Category> childrenids = new ArrayList<Category>();
            /*for (String str : children.split(",")) {
                childrenids.add(findCategory(Integer.parseInt(str)));
            }*/
            float earning = rs.getFloat(5);
            float spending = rs.getFloat(6);
            String owner = rs.getString(7);
            String users = rs.getString(8);
            ArrayList<User> userswithaccess = new ArrayList<User>();
            /*for (String str : users.split(",")) {
                userswithaccess.add(findUser(str));
            }*/
            Category cat = new Category(name, id, earning, spending);
            categoriesList.add(cat);
        }
        disconnectFromDb(con, stmt);
        return categoriesList;
    }

    public void addCategory(Category cat) throws ClassNotFoundException, SQLException {
        Connection con = connectToDb();
        String insertString = "insert into categories(`name`, `id`, `parent`, `children`, `earning`, `spending`, `owner`, `userswithaccess`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement insertCategory = con.prepareStatement(insertString);
        insertCategory.setString(1, cat.getName());
        insertCategory.setInt(2, cat.getId());
        try {
            insertCategory.setInt(3, cat.getParent().getId());
        }
        catch (Exception e) {
            insertCategory.setInt(3, -1);
        }
        ArrayList<Category> cats = cat.getChildren();
        String categorychildren = "";
        try {
            for (Category categ : cats) {
                categorychildren += categ.getId() + ",";
            }
            insertCategory.setString(4, categorychildren);
        }
        catch (Exception e) {
            insertCategory.setString(4, "-1");
        }
        insertCategory.setFloat(5, cat.getEarning());
        insertCategory.setFloat(6, cat.getSpending());
        try {
            insertCategory.setString(7, cat.getOwner().getPersonalCode());
        }
        catch (Exception e) {
            insertCategory.setString(7, "-1");
        }
        ArrayList<User> users = cat.getAccessList();
        String userswithaccess = "";
        try {
            for (User usr : users) {
                userswithaccess += usr.getPersonalCode() + ",";
            }
            insertCategory.setString(8, userswithaccess);
        }
        catch (Exception e) {
            insertCategory.setString(8, "-1");
        }
        insertCategory.execute();
        disconnectFromDb(con, insertCategory);
    }

    public void deleteCategory(Category cat) throws ClassNotFoundException, SQLException {
        Connection con = connectToDb();
        String insertString = "delete from categories where id = ?";
        PreparedStatement deleteCategory = con.prepareStatement(insertString);
        deleteCategory.setInt(1, cat.getId());
        deleteCategory.execute();
        disconnectFromDb(con, deleteCategory);
    }

    public void updateCategory(Category cat, String property, String value) throws ClassNotFoundException, SQLException {
        Connection con = connectToDb();
        String insertString = "update categories set " + property + " = '" + value + "' where id = " + cat.getId();
        PreparedStatement updateCompany = con.prepareStatement(insertString);
        System.out.println(insertString);
        updateCompany.execute();
        disconnectFromDb(con, updateCompany);
    }
}
