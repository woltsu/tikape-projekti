package tikape.runko;

import java.sql.Timestamp;
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

        // asetetaan portti jos heroku antaa PORT-ympäristömuuttujan
        if (System.getenv("PORT") != null) {
            port(Integer.valueOf(System.getenv("PORT")));
        }

        // käytetään oletuksena paikallista sqlite-tietokantaa
        String jdbcOsoite = "jdbc:sqlite:foorumi.db";
        // jos heroku antaa käyttöömme tietokantaosoitteen, otetaan se käyttöön
        if (System.getenv("DATABASE_URL") != null) {
            jdbcOsoite = System.getenv("DATABASE_URL");
        }

        Database database = new Database(jdbcOsoite);

        //luodaan tietokantaolio
        //Database database = new Database("jdbc:sqlite:foorumi.db");
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

        get("/:alue/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            int alueid = Integer.parseInt(req.params(":id"));
            int sivunumero = Integer.parseInt(req.queryParams("sivu"));
            int offset = sivunumero * 10 - 10;
            int id = Integer.parseInt(req.params(":id"));
            Alue a = alueDao.findOne(id);
            map.put("sivuedellinen", sivunumero - 1);
            map.put("sivuseuraava", sivunumero + 1);
            map.put("sivunykyinen", sivunumero);
            map.put("nimi", a.getNimi());
            map.put("alue", a);
            map.put("viestiketjut", viestiketjuDao.findTenByAlue(alueid, offset));
            map.put("aluenimi", alueDao.findOne(id).getNimi());
            System.out.println("alueidmetodi");
            return new ModelAndView(map, "alue");
        }, new ThymeleafTemplateEngine());

        get("/:alue/:alueid/:id/:sivunumero", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("viestiketju", viestiketjuDao.findOne(Integer.parseInt(req.params(":id"))));
            int offset = 10 * Integer.parseInt(req.params(":sivunumero")) - 10;
            map.put("vastaukset", vastausDao.findTenByViestiketju(Integer.parseInt(req.params(":id")), offset));
            map.put("alue", alueDao.findOne(Integer.parseInt(req.params(":alueid"))));
            map.put("sivunumero", req.params(":sivunumero"));
            return new ModelAndView(map, "viestiketju");
        }, new ThymeleafTemplateEngine());

        get("/:alue/:alueid/:id/:sivunumero/next", (req, res) -> {
            int uusiSivunumero = Integer.parseInt(req.params(":sivunumero")) + 1;
            if (vastausDao.findTenByViestiketju(Integer.parseInt(req.params(":id")), uusiSivunumero * 10 - 10).size() == 0) {
                uusiSivunumero--;
            }
            String alue = req.params(":alue");
            String alueId = req.params(":alueid");
            String id = req.params(":id");
            res.redirect("/" + alue + "/" + alueId + "/" + id + "/" + uusiSivunumero);
            return "ok";
        });

        get("/:alue/:alueid/:id/:sivunumero/prev", (req, res) -> {
            int uusiSivunumero = Integer.parseInt(req.params(":sivunumero")) - 1;
            if (uusiSivunumero < 1) {
                uusiSivunumero++;
            }
            String alue = req.params(":alue");
            String alueId = req.params(":alueid");
            String id = req.params(":id");
            res.redirect("/" + alue + "/" + alueId + "/" + id + "/" + uusiSivunumero);
            return "ok";
        });

        post("/:alue/:alueId/:id/:sivunumero", (req, res) -> {
            String nimi = req.queryParams("nimi");
            String viesti = req.queryParams("viesti");
            vastausDao.create(new Vastaus(null, Integer.parseInt(req.params(":id")), null, viesti, nimi));

            String alue = req.params(":alue");
            String alueId = req.params(":alueId");
            String id = req.params(":id");
            String sivunumero = req.params(":sivunumero");

            res.redirect("/" + alue + "/" + alueId + "/" + id + "/" + sivunumero);

            return "";
        });


        get("/:alue/:alueid/:id/:sivunumero/next", (req, res) -> {
            int uusiSivunumero = Integer.parseInt(req.params(":sivunumero")) + 1;
            if (vastausDao.findTenByViestiketju(Integer.parseInt(req.params(":id")), uusiSivunumero * 10 - 10).size() == 0) {
                uusiSivunumero--;
            }
            String alue = req.params(":alue");
            String alueId = req.params(":alueid");
            String id = req.params(":id");
            res.redirect("/" + alue + "/" + alueId + "/" + id + "/" + uusiSivunumero);
            return "ok";
        });

        get("/:alue/:alueid/:id/:sivunumero/prev", (req, res) -> {
            int uusiSivunumero = Integer.parseInt(req.params(":sivunumero")) - 1;
            if (uusiSivunumero < 1) {
                uusiSivunumero++;
            }
            String alue = req.params(":alue");
            String alueId = req.params(":alueid");
            String id = req.params(":id");
            res.redirect("/" + alue + "/" + alueId + "/" + id + "/" + uusiSivunumero);
            return "ok";
        });

        post("/:alue/:alueId/:id/:sivunumero", (req, res) -> {
            String nimi = req.queryParams("nimi");
            String viesti = req.queryParams("viesti");
            vastausDao.create(new Vastaus(null, Integer.parseInt(req.params(":id")), null, viesti, nimi));

            String alue = req.params(":alue");
            String alueId = req.params(":alueId");
            String id = req.params(":id");
            String sivunumero = req.params(":sivunumero");

            res.redirect("/" + alue + "/" + alueId + "/" + id + "/" + sivunumero);

            return "";
        });

        post("/alue/:id", (req, res) -> {
            String aihe = req.queryParams("aihe");
            int alueId = Integer.parseInt(req.params((":id")));
            viestiketjuDao.create(new Viestiketju(alueId, aihe));
            res.redirect("/alue/" + req.params(":id") + "?sivu=1");
            return "";
        });
    }
}
