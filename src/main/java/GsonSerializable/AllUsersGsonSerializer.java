package GsonSerializable;

import com.google.gson.*;
import model.User;

import java.lang.reflect.Type;
import java.util.List;

public class AllUsersGsonSerializer implements JsonSerializer<List<User>> {

    @Override
    public JsonElement serialize(List<User> allUsers, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonArray jsonArray = new JsonArray();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(User.class, new UserGsonSerializer());
        Gson parser = gsonBuilder.create();

        for (User usr : allUsers) {
            jsonArray.add(parser.toJson(usr));
        }
        System.out.println(jsonArray);
        return jsonArray;
    }

}
