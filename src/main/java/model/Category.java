package model;

import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;

public class Category {

    ArrayList<Category> emptyCategoryArrayList = new ArrayList<Category>();
    ArrayList<User> emptyUsersArrayList = new ArrayList<User>();

    public String name;
    public int id;
    public Category parent;
    public ArrayList<Category> children;
    private float earning = 0;
    private float spending = 0;
    public User owner;
    public ArrayList<User> usersWithAccess;


    public Category(String name, int id, Category parent, User owner) {
        this.name = name;
        this.id = id;
        this.parent = parent;
        this.owner = owner;
    }

    public Category(String name, int id, float earning, float spending) {
        this.name = name;
        this.id = id;
        //this.parent = db.findCategory(-1);
        this.parent = null;
        //emptyCategoryArrayList.add(db.findCategory(-1));
        //this.children = emptyCategoryArrayList;
        this.children = null;
        this.earning = earning;
        this.spending = spending;
        //this.owner = db.findUser("-1");
        this.owner = null;
        //emptyUsersArrayList.add(db.findUser("-1"));
        //this.usersWithAccess = emptyUsersArrayList;
        this.usersWithAccess = null;
    }

    public Category(EntityManagerFactory entityManagerFactory) {

    }

    public Category(String name, int id, Category parent, ArrayList<Category> children, float earning, float spending, User owner, ArrayList<User> userswithaccess) {
        this.name = name;
        this.id = id;
        this.parent = parent;
        this.children = children;
        this.earning = earning;
        this.spending = spending;
        this.owner = owner;
        this.usersWithAccess = userswithaccess;
    }

    public ArrayList<Category> getChildren() {
        return children;
    }

    public void createChild(String name, int id) {
        Category newCat = new Category(name, id, this, this.owner);
        children.add(newCat);
    }

    public void addChild(Category cat) {
        children.add(cat);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public Category getParent() {
        return parent;
    }

    public float getEarning() {
        return earning;
    }

    public void setEarning(float earning) {
        this.earning = earning;
    }

    public float getSpending() {
        return spending;
    }

    public void setSpending(float spending) {
        this.spending = spending;
    }

    public User getOwner() {
        return owner;
    }

    public ArrayList<User> getAccessList() {
        return usersWithAccess;
    }

    public void giveAccess(User user) {
    }

}
