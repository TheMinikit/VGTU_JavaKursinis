package webControllers;

import GsonSerializable.AllCategoriesGsonSerializer;
import GsonSerializable.CategoryGsonSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.Category;
import model.Database;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.List;

@RestController
public class WebCategoryController {

    Database db = Database.Database();
    GsonBuilder gson = new GsonBuilder();

    @GetMapping(path = "/category/list")
    public String getAllCategories() throws SQLException, ClassNotFoundException {
        List<Category> allCategories = db.getAllCategories();
        gson.registerTypeAdapter(Category.class, new CategoryGsonSerializer());
        Gson parser = gson.create();
        parser.toJson(allCategories.get(0));
        Type categoryList = new TypeToken<List<Category>>() {
        }.getType();
        gson.registerTypeAdapter(categoryList, new AllCategoriesGsonSerializer());
        parser = gson.create();
        return parser.toJson(allCategories);
    }

    @GetMapping(path = "category/info/{id}")
    public String getCategoryInfo(@PathVariable(name = "id") int id) throws SQLException, ClassNotFoundException {
        db.getAllCategories();
        Category cat = db.findCategory(id);
        gson.registerTypeAdapter(Category.class, new CategoryGsonSerializer());
        Gson parser = gson.create();
        return parser.toJson(cat);
    }

    @PostMapping(path = "/category/create")
    public String newCategory(@RequestBody Category newCat) throws SQLException, ClassNotFoundException {
        db.addCategory(newCat);
        db.getAllCategories();
        gson.registerTypeAdapter(Category.class, new CategoryGsonSerializer());
        Gson parser = gson.create();
        return parser.toJson(db.findCategory(newCat.getId()));
    }

    @PutMapping(path = "/category/replace")
    public String replaceCategory(@RequestBody Category newCat) throws SQLException, ClassNotFoundException {
        db.getAllCategories();
        Category cat = db.findCategory(newCat.getId());
        db.updateCategory(cat, "name", newCat.getName());
        db.updateCategory(cat, "earning", String.valueOf(newCat.getEarning()));
        db.updateCategory(cat, "spending", String.valueOf(newCat.getSpending()));
        gson.registerTypeAdapter(Category.class, new CategoryGsonSerializer());
        Gson parser = gson.create();
        db.getAllCategories();
        return parser.toJson(db.findCategory(newCat.getId()));
    }

    @DeleteMapping(value = "/category/delete/{id}")
    public String deleteCategory(@PathVariable(name = "id") int id) throws SQLException, ClassNotFoundException {
        db.deleteCategory(db.findCategory(id));
        return ("Successfully deleted category with id: " + id);
    }
}
