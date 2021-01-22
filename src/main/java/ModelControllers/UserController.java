package ModelControllers;


import model.User;

import java.util.Scanner;

public class UserController {

    private User user;

    public UserController(User user) {
        this.user = user;
    }

    public String getUserName() {
        return user.getName();
    }

    public void setUserName(String name) {
        user.setName(name);
    }

    public String getUserSurname() {
        return user.getSurname();
    }

    public void setUserSurname(String surname) {
        user.setSurname(surname);
    }

    public String getUserPassword() {
        return user.getPassword();
    }

    public void setUserPassword(String password) {
        user.setPassword(password);
    }

    public int getUserBirthYear() {
        return user.getBirthYear();
    }

    public void setUserBirthYear(int birthyear) {
        user.setBirthYear(birthyear);
    }

    public String getUserPersonalCode() {
        return user.getPersonalCode();
    }

    public void setUserData() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("0) Grizti atgal" +
                "\n1) " + user.getName() +
                "\n2) " + user.getSurname() +
                "\n3) " + user.getBirthYear() +
                "\n4) " + user.getPassword());
        System.out.print("Ka norite redaguoti?: ");
        int editInput = scanner.nextInt();
        switch (editInput) {
            case 0:
                break;
            case 1:
                System.out.print("Iveskite nauja varda: ");
                String editedName = scanner.next();
                setUserName(editedName);
                break;
            case 2:
                System.out.print("Iveskite nauja pavarde: ");
                String editedSurname = scanner.next();
                setUserSurname(editedSurname);
                break;
            case 3:
                System.out.print("Iveskite naujus gimimo metus: ");
                int editedBirthYear = scanner.nextInt();
                setUserBirthYear(editedBirthYear);
                break;
            case 4:
                System.out.print("Iveskite nauja slaptazodi: ");
                String editedPassword = scanner.next();
                setUserPassword(editedPassword);
                break;
            default:
                System.out.println("Neteisingas pasirinkimas");
        }
    }

    public User getUser() {
        return user;
    }
}
