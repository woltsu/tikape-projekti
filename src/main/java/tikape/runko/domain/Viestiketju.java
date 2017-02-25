package tikape.runko.domain;

import java.sql.Timestamp;
import java.util.List;

public class Viestiketju {

<<<<<<< HEAD
    private int tunnus;
    private int alueId;
=======
    private Integer tunnus;
    private int alue;
>>>>>>> origin/master
    private String otsikko;
    private Timestamp aikaleima;
    private List<Vastaus> vastaukset;
<<<<<<< HEAD
    private Alue alue;
    
    public Viestiketju(int tunnus, int alueId, String otsikko, Timestamp aikaleima, List<Vastaus> vastaukset, Alue alue) {
=======

    public Viestiketju(Integer tunnus, int alue, String otsikko, Timestamp aikaleima, List<Vastaus> vastaukset) {
>>>>>>> origin/master
        this.tunnus = tunnus;
        this.alueId = alueId;
        this.otsikko = otsikko;
        this.aikaleima = aikaleima;
        this.vastaukset = vastaukset;
        this.alue = alue;
    }
    
    public int getId() {
        return tunnus;
    }

    public int getTunnus() {
        return tunnus;
    }

    public int getAlueId() {
        return alueId;
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
    
    public Alue getAlue() {
        return this.alue;
    }

    // Debuggausta varten
    @Override
    public String toString() {
        String s = "[Viestiketju]" + "\n";
        s += "Tunnus: " + tunnus + "\n";
        s += "Otsikko: " + otsikko + "\n";
        s += "Alueen tunnus: " + alueId + "\n";
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
