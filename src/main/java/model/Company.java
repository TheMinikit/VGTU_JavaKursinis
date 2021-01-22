package model;

import java.util.ArrayList;

public class Company {

    public String name;
    private int companyId;
    private ArrayList<User> companyUsers = new ArrayList<User>();

    public Company(String name, int companyId, ArrayList<User> companyUsers) {
        this.name = name;
        this.companyId = companyId;
        this.companyUsers = companyUsers;
    }

    public Company(String name, int companyId) {
        this.name = name;
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return companyId;
    }

    public ArrayList<User> getUsers() {
        return companyUsers;
    }

    public void addUsers(User user) {
        companyUsers.add(user);
    }

    public void setName(String name) {
        this.name = name;
    }

}
