package GsonSerializable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import model.Company;

import java.lang.reflect.Type;

public class CompanyGsonSerializer implements JsonSerializer<Company> {

    @Override
    public JsonElement serialize(Company company, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject cmpJson = new JsonObject();
        cmpJson.addProperty("name", company.getName());
        cmpJson.addProperty("id", company.getId());
        //catJson.addProperty("companyusers", company);
        return cmpJson;
    }

}
