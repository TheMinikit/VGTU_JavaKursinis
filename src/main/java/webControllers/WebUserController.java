package webControllers;

import GsonSerializable.AllUsersGsonSerializer;
import GsonSerializable.UserGsonSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.Company;
import model.Database;
import model.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.List;

@RestController
public class WebUserController {

    Database db = Database.Database();
    GsonBuilder gson = new GsonBuilder();

    @GetMapping(path = "/user/list")
    public String getAllUsers() throws SQLException, ClassNotFoundException {
        List<User> allUsers = db.getAllUsers();
        gson.registerTypeAdapter(User.class, new UserGsonSerializer());
        Gson parser = gson.create();
        parser.toJson(allUsers.get(0));
        Type userList = new TypeToken<List<Company>>() {
        }.getType();
        gson.registerTypeAdapter(userList, new AllUsersGsonSerializer());
        parser = gson.create();
        return parser.toJson(allUsers);
    }

    @GetMapping(path = "user/info/{personalcode}")
    public String getUserInfo(@PathVariable(name = "personalcode") String personalcode) throws SQLException, ClassNotFoundException {
        db.getAllUsers();
        User usr = db.findUser(personalcode);
        gson.registerTypeAdapter(User.class, new UserGsonSerializer());
        Gson parser = gson.create();
        return parser.toJson(usr);
    }

    @PostMapping(path = "/user/create")
    public String newUser(@RequestBody User newUser) throws SQLException, ClassNotFoundException {
        db.addUser(newUser);
        db.getAllUsers();
        gson.registerTypeAdapter(User.class, new UserGsonSerializer());
        Gson parser = gson.create();
        return parser.toJson(db.findUser(newUser.getPersonalCode()));
    }

    @PutMapping(path = "/user/replace")
    public String replaceUser(@RequestBody User usr) throws SQLException, ClassNotFoundException {
        db.getAllUsers();
        db.updateUser(usr, "name", usr.getName());
        db.updateUser(usr, "surname", usr.getSurname());
        db.updateUser(usr, "password", usr.getPassword());
        db.updateUser(usr, "birthyear", String.valueOf(usr.getBirthYear()));
        gson.registerTypeAdapter(User.class, new UserGsonSerializer());
        Gson parser = gson.create();
        db.getAllUsers();
        return parser.toJson(db.findUser(usr.getPersonalCode()));
    }

    @DeleteMapping(path = "/user/delete/{personalcode}")
    public String deleteUser(@PathVariable(name = "personalcode") String personalcode) throws SQLException, ClassNotFoundException {
        db.deleteUser(db.findUser(personalcode));
        return ("Successfully deleted user with id: " + personalcode);
    }

}
