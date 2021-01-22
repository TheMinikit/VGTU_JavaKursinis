package model;

import java.util.Scanner;

public class Main {

    public static void main(String args[]) {
        Database db = Database.Database();
        Scanner scanner = new Scanner(System.in);
        int menuInput;
        String inputName;
        String inputSurname;
        String inputPassword;
        User loggedUser;
        while (1 == 1) {
            System.out.println("Iveskite prisijungimo duomenys:\n");
            System.out.print("Vardas: ");
            inputName = scanner.nextLine();
            System.out.print("Pavarde: ");
            inputSurname = scanner.nextLine();
            System.out.print("Slaptazodis: ");
            inputPassword = scanner.nextLine();
            if (db.checkForUser(inputName, inputSurname, inputPassword)) {
                loggedUser = db.findUser(inputName, inputSurname, inputPassword);
                break;
            } else {
                System.out.print("Wrong credentials. Please try again.\n\n");
            }
        }
        while (1 == 1) {
            System.out.println("Buhalterines apskaitos sistemos menu" +
                    "\n" + "Jusu pasirinkimas?" +
                    "\n" + "" +
                    "\n" + "1. Kategoriju nustatymai" +
                    "\n" + "2. Vartotoju nustatymai" +
                    "\n" + "3. Kompaniju nustatymai" +
                    "\n" + "");
            menuInput = scanner.nextInt();
            System.out.println("");
            switch (menuInput) {
                case 1:
                    db.manageCategories(loggedUser);
                    break;
                case 2:
                    db.manageUsers(loggedUser);
                    break;
                case 3:
                    db.manageCompanies(loggedUser);
                    break;
                default:
                    System.out.println("Neteisingas pasirinkimas. Bandykite dar karta.");
            }
        }
    }


}
