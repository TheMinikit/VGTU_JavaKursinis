package GsonSerializable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import model.User;

import java.lang.reflect.Type;

public class UserGsonSerializer implements JsonSerializer<User> {

    @Override
    public JsonElement serialize(User user, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject usrJson = new JsonObject();
        usrJson.addProperty("name", user.getName());
        usrJson.addProperty("surname", user.getSurname());
        usrJson.addProperty("password", user.getPassword());
        usrJson.addProperty("birthyear", user.getBirthYear());
        usrJson.addProperty("personalcode", user.getPersonalCode());
        return usrJson;
    }
}
