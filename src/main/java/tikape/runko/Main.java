package tikape.runko;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.AlueDao;
import tikape.runko.database.Database;
import tikape.runko.database.ViestiketjuDao;
import tikape.runko.domain.Alue;
import tikape.runko.domain.Viestiketju;

public class Main {

    public static void main(String[] args) throws Exception {
        Database database = new Database("jdbc:sqlite:foorumi.db");
        database.init();

        ViestiketjuDao viestiketjuDao = new ViestiketjuDao(database);
        AlueDao alueDao = new AlueDao(database);

        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("alueet", alueDao.findAll());

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        get("/alue", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("viestiketjut", viestiketjuDao.findAll());

            return new ModelAndView(map, "alue");
        }, new ThymeleafTemplateEngine());

        get("/alue/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("viestiketju", viestiketjuDao.findOne(Integer.parseInt(req.params("tunnus"))));

            return new ModelAndView(map, "viestiketju");
        }, new ThymeleafTemplateEngine());

        get("/index.html", (req, res) -> {
            HashMap map = new HashMap<>();
            List<Alue> alueet = new ArrayList<>();
            alueet.add(new Alue(0, "Koodaus", "Koodaamista"));
            alueet.add(new Alue(1, "Pelit", "Pelaamista"));
            alueet.add(new Alue(2, "Linux", "Pingviini"));
            map.put("alueet", alueet);

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        get("/viestiketju", (req, res) -> {
            HashMap data = new HashMap<>();
            
            data.put("otsikko", "autot on kivoja");
            
            return new ModelAndView(data, "viestiketju");
        }, new ThymeleafTemplateEngine());
    }
}
