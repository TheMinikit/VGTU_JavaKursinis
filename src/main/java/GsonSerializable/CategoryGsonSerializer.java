package GsonSerializable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import model.Category;

import java.lang.reflect.Type;

public class CategoryGsonSerializer implements JsonSerializer<Category> {

    @Override
    public JsonElement serialize(Category category, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject catJson = new JsonObject();
        catJson.addProperty("name", category.getName());
        catJson.addProperty("id", category.getId());
        if(category.getParent() == null) {
            catJson.addProperty("parent", "No Parent");
        }
        else catJson.addProperty("parent", category.getParent().getName());
        //catJson.addProperty("children", category);
        catJson.addProperty("earning", category.getEarning());
        catJson.addProperty("spending", category.getSpending());
        if (category.getOwner() == null) {
            catJson.addProperty("owner", "No Owner");
        }
        else catJson.addProperty("owner", category.getOwner().getName());
        //catJson.addProperty("userswithaccess", category);

        return catJson;
    }

}