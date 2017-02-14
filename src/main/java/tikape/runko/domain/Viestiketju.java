package tikape.runko.domain;

import java.sql.Timestamp;

public class Viestiketju {

    private int tunnus;
    private int alue;
    private String otsikko;
    private Timestamp aikaleima;

    public Viestiketju(int tunnus, int alue, String otsikko, Timestamp aikaleima) {
        this.tunnus = tunnus;
        this.alue = alue;
        this.otsikko = otsikko;
        this.aikaleima = aikaleima;
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

}
