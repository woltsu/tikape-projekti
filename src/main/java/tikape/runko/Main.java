package tikape.runko;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.*;
import tikape.runko.domain.*;

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

        //luodaan tietokantaolio
        Database database = new Database(jdbcOsoite);
        database.init();

        //luodaan tietokannan käsittelyoliot
        ViestiketjuDao viestiketjuDao = new ViestiketjuDao(database);
        AlueDao alueDao = new AlueDao(database);
        VastausDao vastausDao = new VastausDao(database);

        // Estä html syöttäminen
        before("*", (req, res) -> {
            Pattern regex = Pattern.compile("<.*>", Pattern.DOTALL);
            for (String param : req.queryParams()) {
                Matcher matcher = regex.matcher(req.queryParams(param));
                if (matcher.find()) {
                    halt(403, "Älä syötä html:ää kenttiin.");
                }
            }
        });

        // Etusivu
        get("/", (req, res) -> {
            HashMap<String, Object> data = new HashMap<>();
            data.put("alueet", alueDao.findAll());

            return new ModelAndView(data, "index");
        }, new ThymeleafTemplateEngine());

        // Alue
        get("/:alue/:id", (req, res) -> {
            HashMap<String, Object> data = new HashMap<>();
            int alueid = Integer.parseInt(req.params(":id"));
            int sivunumero = Integer.parseInt(req.queryParams("sivu"));
            int offset = sivunumero * 10 - 10;
            int id = Integer.parseInt(req.params(":id"));
            Alue alue = alueDao.findOne(id);
            data.put("sivunumero", sivunumero);
            data.put("alue", alue);
            data.put("viestiketjut", viestiketjuDao.findTenByAlue(alueid, offset));
            return new ModelAndView(data, "alue");
        }, new ThymeleafTemplateEngine());

        // Viestiketju
        get("/:alue/:alueid/:id", (req, res) -> {
            HashMap<String, Object> data = new HashMap<>();
            int sivunumero = Integer.parseInt(req.queryParams(("sivu")));
            int vkId = Integer.parseInt(req.params(":id"));
            data.put("viestiketju", viestiketjuDao.findOne(vkId));
            int offset = 10 * sivunumero - 10;
            data.put("vastaukset", vastausDao.findTenByViestiketju(vkId, offset));
            data.put("alue", alueDao.findOne(Integer.parseInt(req.params(":alueid"))));
            data.put("sivunumero", sivunumero);
            return new ModelAndView(data, "viestiketju");
        }, new ThymeleafTemplateEngine());

        // Vastauksen lisääminen
        post("/:alue/:alueId/:id", (req, res) -> {
            String nimi = req.queryParams("nimi");
            String viesti = req.queryParams("viesti");
            vastausDao.create(new Vastaus(Integer.parseInt(req.params(":id")), viesti, nimi));

            res.redirect(req.pathInfo() + "?sivu=" + req.queryParams("sivu"));
            return "";
        });

        // Viestiketjun lisääminen
        post("/:alue/:id", (req, res) -> {
            String aihe = req.queryParams("aihe");
            int alueId = Integer.parseInt(req.params((":id")));
            Viestiketju uusiVk = viestiketjuDao.create(new Viestiketju(alueId, aihe));
            res.redirect(req.pathInfo() + "/" + uusiVk.getId() + "?sivu=1");
            return "";
        });
    }
}
