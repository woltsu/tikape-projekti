package tikape.runko.domain;

import java.sql.Timestamp;
import java.util.List;

public class Viestiketju {

    private Integer tunnus;
    private int alue;
    private String otsikko;
    private Timestamp aikaleima;
    private Timestamp uusinAikaleima;
    private List<Vastaus> vastaukset;

    public Viestiketju(Integer tunnus, int alue, String otsikko, Timestamp aikaleima, Timestamp uusinAikaleima, List<Vastaus> vastaukset) {
        this.tunnus = tunnus;
        this.otsikko = otsikko;
        this.aikaleima = aikaleima;
        this.uusinAikaleima = uusinAikaleima;
        this.vastaukset = vastaukset;
        this.alue = alue;
    }

    public Viestiketju(int alue, String otsikko) {
        this(null, alue, otsikko, null, null, null);
    }

    public Timestamp getUusinAikaleima() {
        return uusinAikaleima;
    }

    public int getId() {
        return tunnus;
    }

    public int getTunnus() {
        return tunnus;
    }

    public int getAlueId() {
        return this.alue;
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

    // Debuggausta varten
    @Override
    public String toString() {
        String s = "[Viestiketju]" + "\n";
        s += "Tunnus: " + tunnus + "\n";
        s += "Otsikko: " + otsikko + "\n";
        s += "Alueen tunnus: " + alue + "\n";
        s += "Aikaleima: " + aikaleima + "\n";
        s += "Vastaukset:" + "\n";

        for (Vastaus v : vastaukset) {
            for (String rivi : v.toString().split("\n")) {
                s += "    " + rivi + "\n";
            }
            s += "\n";
        }
        return s;
    }

}
