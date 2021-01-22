package ModelControllers;

import model.Category;
import model.Database;
import model.User;

import java.util.ArrayList;
import java.util.Scanner;

public class CategoryController {

    private Category category;

    public CategoryController(Category category) {
        this.category = category;
    }

    public ArrayList<Category> getCategoryChildren() {
        return category.getChildren();
    }

    public void addCategoryChild(Category cat) {
        category.addChild(cat);
    }

    public void createChildCategory(String name, int id) {
        category.createChild(name, id);
    }

    public String getCategoryName() {
        return category.getName();
    }

    public void setCategoryName(String name) {
        category.setName(name);
    }

    public int getCategoryId() {
        return category.getId();
    }

    public Category getCategoryParent() {
        return category.getParent();
    }

    public float getCategoryEarning() {
        return category.getEarning();
    }

    public void setCategoryEarning(float earning) {
        category.setEarning(earning);
    }

    public float getCategorySpending() {
        return category.getSpending();
    }

    public void setCategorySpending(float spending) {
        category.setSpending(spending);
    }

    public User getCategoryOwner() {
        return category.getOwner();
    }

    public ArrayList<User> getCategoryAccessList() {
        return category.getAccessList();
    }

    public void giveCategoryAccess(User user) {
        category.giveAccess(user);
    }

    public Category getCategory() {
        return category;
    }

    public void editCategory() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("0) Grizti atgal" +
                "\n1) Pavadinimas" +
                "\n2) Islaidos" +
                "\n3) Pajamos" +
                "\n4) Subkategorijos" +
                "\n5) Vartotoju leidimai");
        System.out.print("Ka norite redaguoti?: ");
        int editInput = scanner.nextInt();
        switch (editInput) {
            case 0:
                break;
            case 1:
                System.out.print("Iveskite nauja pavadinima: ");
                String editedName = scanner.next();
                setCategoryName(editedName);
                break;
            case 2:
                if (getCategoryChildren().size() > 0) {
                    System.out.println("Negalima redaguoti islaidas jeigu yra subkategorijos");
                } else {
                    System.out.print("Iveskite naujas islaidas: ");
                    int editedSpending = scanner.nextInt();
                    setCategorySpending(editedSpending);
                }
                break;
            case 3:
                if (getCategoryChildren().size() > 0) {
                    System.out.println("Negalima redaguoti pajamas jeigu yra subkategorijos");
                } else {
                    System.out.print("Iveskite naujas pajamas: ");
                    int editedEarning = scanner.nextInt();
                    setCategorySpending(editedEarning);
                }
                break;
            case 4:
                int i4 = 1;
                System.out.println("-1) Grizti Atgal");
                System.out.println("0) Prideti subkategorija");
                for (Category cat : getCategoryChildren()) {
                    System.out.println(i4 + ") " + cat.getName());
                    i4++;
                }
                System.out.print("\nJusu Pasirinkimas: ");
                i4 = scanner.nextInt();
                if (i4 < -1 || i4 > getCategoryChildren().size()) break;
                else if (i4 == -1) break;
                else if (i4 == 0) {
                    System.out.print("Iveskite subkategorijos pavadinima: ");
                    String newCategoryChild = scanner.next();
                    System.out.print("Iveskite subkategorijos id: ");
                    int newCategoryId = scanner.nextInt();
                    createChildCategory(newCategoryChild, newCategoryId);
                } else {
                    CategoryController catcontroller = new CategoryController(getCategoryChildren().get(i4 - 1));
                    catcontroller.editCategory();
                }
                break;
            case 5:
                int i5 = 1;
                for (User usr : getCategoryAccessList()) {
                    System.out.println(i5 + ") " + usr.getName() + " " + usr.getSurname());
                    i5++;
                }
                System.out.println("-1) Grizti atgal" +
                        "\n0) Prideti nauja vartotoja prie kategorijos");
                i5 = scanner.nextInt();
                if (i5 < -1 || i5 > getCategoryAccessList().size()) break;
                else if (i5 == -1) break;
                else if (i5 == 0) {
                    Database db = Database.Database();
                    System.out.println("Iveskite vartotojo asmenini koda: ");
                    String newAccess = scanner.next();
                    giveCategoryAccess(db.findUser(newAccess));
                    break;
                } else {
                    getCategoryAccessList().remove(i5 - 1);
                }
                break;
            default:
                System.out.println("Neteisingas pasirinkimas");
        }
    }

}
