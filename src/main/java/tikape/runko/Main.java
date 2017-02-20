package tikape.runko;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.AlueDao;
import tikape.runko.database.Database;
import tikape.runko.database.VastausDao;
import tikape.runko.database.ViestiketjuDao;
import tikape.runko.domain.Alue;
import tikape.runko.domain.Vastaus;
import tikape.runko.domain.Viestiketju;

public class Main {

    public static void main(String[] args) throws Exception {
        //luodaan tietokantaolio
        Database database = new Database("jdbc:sqlite:foorumi.db");
        database.init();

        //luodaan tietokannan käsittelyoliot
        ViestiketjuDao viestiketjuDao = new ViestiketjuDao(database);
        AlueDao alueDao = new AlueDao(database);
        VastausDao vastausDao = new VastausDao(database);

        //testailua
        Viestiketju viestiketju = viestiketjuDao.findOne(1);
        List<Vastaus> vastaukset = vastausDao.findAll();

        //luodaan hashmap alueista
        Map<String, Alue> alueet = new HashMap<>();

        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("alueet", alueDao.findAll());

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        get("/alue/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            int id = Integer.parseInt(req.params(":id"));
            Alue a = alueDao.findOne(id);
            map.put("nimi", a.getNimi());
            map.put("viestiketjut", viestiketjuDao.findAllByAlue(Integer.parseInt(req.params(":id"))));

            return new ModelAndView(map, "alue");
        }, new ThymeleafTemplateEngine());

        get("/viestiketju/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("viestiketju", viestiketjuDao.findOne(Integer.parseInt(req.params(":id"))));
            map.put("vastaukset", vastausDao.findAllByViestiketju(Integer.parseInt(req.params(":id"))));
            return new ModelAndView(map, "viestiketju");
        }, new ThymeleafTemplateEngine());

//        "/viestiketju":n sijaan käytetään varmaankin viestiketjun id:tä, kuten /:id tai /:alue/:id
        get("/viestiketju", (req, res) -> {
            HashMap data = new HashMap<>();

            data.put("viestiketju", viestiketju);
            data.put("vastaukset", vastaukset);

            return new ModelAndView(data, "viestiketju");
        }, new ThymeleafTemplateEngine());

        post("/viestiketju/:id", (req, res) -> {
            String nimi = req.queryParams("nimi");
            String viesti = req.queryParams("viesti");
            vastausDao.create(new Vastaus(null, Integer.parseInt(req.params(":id")), null, viesti, nimi));
            res.redirect("/viestiketju/" + req.params(":id"));
            return "";
        });
    }
}
