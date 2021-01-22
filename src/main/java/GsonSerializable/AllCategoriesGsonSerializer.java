package GsonSerializable;

import com.google.gson.*;
import model.Category;

import java.lang.reflect.Type;
import java.util.List;

public class AllCategoriesGsonSerializer implements JsonSerializer<List<Category>> {

    @Override
    public JsonElement serialize(List<Category> allCats, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonArray jsonArray = new JsonArray();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Category.class, new CategoryGsonSerializer());
        Gson parser = gsonBuilder.create();

        for (Category cat : allCats) {
            jsonArray.add(parser.toJson(cat));
        }
        System.out.println(jsonArray);
        return jsonArray;
    }

}
