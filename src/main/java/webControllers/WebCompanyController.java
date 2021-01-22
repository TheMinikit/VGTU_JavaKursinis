package webControllers;

import GsonSerializable.AllCompaniesGsonSerializer;
import GsonSerializable.CompanyGsonSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.Category;
import model.Company;
import model.Database;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Path;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.List;

@RestController
public class WebCompanyController {

    Database db = Database.Database();
    GsonBuilder gson = new GsonBuilder();

    @GetMapping(path = "/company/list")
    public String getAllCompanies() throws SQLException, ClassNotFoundException {
        List<Company> allCompanies = db.getAllCompanies();
        gson.registerTypeAdapter(Category.class, new CompanyGsonSerializer());
        Gson parser = gson.create();
        parser.toJson(allCompanies.get(0));
        Type companyList = new TypeToken<List<Company>>() {
        }.getType();
        gson.registerTypeAdapter(companyList, new AllCompaniesGsonSerializer());
        parser = gson.create();
        return parser.toJson(allCompanies);
    }

    @GetMapping(path = "company/info/{id}")
    public String getCompanyInfo(@PathVariable(name = "id") int id) throws SQLException, ClassNotFoundException {
        db.getAllCompanies();
        Company cmp = db.findCompany(id);
        gson.registerTypeAdapter(Company.class, new CompanyGsonSerializer());
        Gson parser = gson.create();
        return parser.toJson(cmp);
    }

    @PostMapping(path = "/company/create")
    public String newCompany(@RequestBody Company newCmp) throws SQLException, ClassNotFoundException {
        db.addCompany(newCmp);
        db.getAllCompanies();
        gson.registerTypeAdapter(Company.class, new CompanyGsonSerializer());
        Gson parser = gson.create();
        return parser.toJson(db.findCompany(newCmp.getId()));
    }

    @PutMapping(path = "/company/replace")
    public String replaceCompany(@RequestBody Company cmp) throws SQLException, ClassNotFoundException {
        db.getAllCompanies();
        db.updateCompany(cmp, "name", cmp.getName());
        gson.registerTypeAdapter(Company.class, new CompanyGsonSerializer());
        Gson parser = gson.create();
        db.getAllCompanies();
        return parser.toJson(db.findCompany(cmp.getId()));
    }

    @DeleteMapping(path = "/company/delete/{id}")
    public String deleteCompany(@PathVariable(name = "id") int id) throws SQLException, ClassNotFoundException {
        db.deleteCompany(db.findCompany(id));
        return ("Successfully deleted company with id: " + id);
    }

}
