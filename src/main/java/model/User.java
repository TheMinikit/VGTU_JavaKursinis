package model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {

    public String name;
    private String surname;
    private String password;
    protected int birthYear;
    protected @Id String personalCode;

    public User(String name, String surname, int birthYear, String password, String personalCode) {
        this.name = name;
        this.surname = surname;
        this.birthYear = birthYear;
        this.password = password;
        this.personalCode = personalCode;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public String getPassword() {
        return password;
    }

    public String getPersonalCode() {
        return personalCode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPersonalCode(String personalCode) {
        this.personalCode = personalCode;
    }

    public User() {

    }
}
