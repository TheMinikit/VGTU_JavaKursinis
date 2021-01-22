package ModelControllers;

import model.Company;
import model.Database;
import model.User;

import java.util.ArrayList;
import java.util.Scanner;

public class CompanyController {

    private Company company;

    public CompanyController(Company company) {
        this.company = company;
    }

    public String getCompanyName() {
        return company.getName();
    }

    public void setCompanyName(String name) {
        company.setName(name);
    }

    public int getCompanyId() {
        return company.getId();
    }

    public ArrayList<User> getCompanyUsers() {
        return company.getUsers();
    }

    public void addCompanyUsers(User user) {
        company.addUsers(user);
    }

    public Company getCompany() {
        return company;
    }

    public void editCompany() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("0) Grizti atgal" +
                "\n1) Pavadinimas" +
                "\n2) Vartotoju sarasas");
        System.out.print("Ka norite redaguoti?: ");
        int editInput = scanner.nextInt();
        switch (editInput) {
            case 0:
                break;
            case 1:
                System.out.print("Iveskite nauja pavadinima: ");
                String editedName = scanner.next();
                setCompanyName(editedName);
                break;
            case 2:
                int i = 0;
                for (User usr : getCompanyUsers()) {
                    System.out.println(i + ") " + usr.getName() + " " + usr.getSurname());
                    i++;
                }
                System.out.print("Pasirinkite egzistuojanti tam, kad istrinti" +
                        "\nArba" +
                        "\nParasykite -1 jeigu norite prideti nauja vartotoja");
                i = scanner.nextInt();
                if (i == -1) {
                    Database db = Database.Database();
                    System.out.print("Iveskite vartotojo asmens koda: ");
                    String newPersonalCode = scanner.next();
                    User newUser = db.findUser(newPersonalCode);
                    addCompanyUsers(newUser);
                } else if (i == 0 || i > getCompanyUsers().size()) {
                    break;
                } else {
                    getCompanyUsers().remove(i - 1);
                }
                break;
            default:
                System.out.println("Neteisingas pasirinkimas");
        }
    }


}
