package GsonSerializable;

import com.google.gson.*;
import model.Company;

import java.lang.reflect.Type;
import java.util.List;

public class AllCompaniesGsonSerializer implements JsonSerializer<List<Company>> {

    @Override
    public JsonElement serialize(List<Company> allCmps, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonArray jsonArray = new JsonArray();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Company.class, new CompanyGsonSerializer());
        Gson parser = gsonBuilder.create();

        for (Company cmp : allCmps) {
            jsonArray.add(parser.toJson(cmp));
        }
        System.out.println(jsonArray);
        return jsonArray;
    }

}
