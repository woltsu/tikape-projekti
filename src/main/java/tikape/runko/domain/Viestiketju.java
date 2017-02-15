package tikape.runko.domain;

import java.sql.Timestamp;
import java.util.List;

public class Viestiketju {

    private int tunnus;
    private int alue;
    private String otsikko;
    private Timestamp aikaleima;
    private List<Vastaus> vastaukset;

    public Viestiketju(int tunnus, int alue, String otsikko, Timestamp aikaleima, List<Vastaus> vastaukset) {
        this.tunnus = tunnus;
        this.alue = alue;
        this.otsikko = otsikko;
        this.aikaleima = aikaleima;
        this.vastaukset = vastaukset;
    }

    public int getTunnus() {
        return tunnus;
    }

    public int getAlue() {
        return alue;
    }

    public String getOtsikko() {
        return otsikko;
    }

    public Timestamp getAikaleima() {
        return aikaleima;
    }

    public void setOtsikko(String otsikko) {
        this.otsikko = otsikko;
    }

    public void setAikaleima(Timestamp aikaleima) {
        this.aikaleima = aikaleima;
    }

    public List<Vastaus> getVastaukset() {
        return vastaukset;
    }

    public void setVastaukset(List<Vastaus> vastaukset) {
        this.vastaukset = vastaukset;
    }

}
