package tikape.runko.domain;

import java.sql.Timestamp;

public class Vastaus {

    private Integer id;
    private int ketjuId;
    private Timestamp aikaleima;
    private String sisalto;
    private String lahettaja;

    public Vastaus(Integer id, int ketjuId, Timestamp aikaleima, String sisalto, String lahettaja) {
        this.id = id;
        this.ketjuId = ketjuId;
        this.aikaleima = aikaleima;
        this.sisalto = sisalto;
        this.lahettaja = lahettaja;
    }

    public Integer getId() {
        return id;
    }

    public int getKetjuId() {
        return ketjuId;
    }

    public String getLahettaja() {
        return lahettaja;
    }

    public Timestamp getAikaleima() {
        return aikaleima;
    }

    public String getSisalto() {
        return sisalto;
    }

    // Debuggausta varten
    @Override
    public String toString() {
        String s = "[Vastaus]" + "\n";
        s += "Id: " + id + "\n";
        s += "KetjuId: " + ketjuId + "\n";
        s += "Lähettäjä " + lahettaja + "\n";
        s += "Aikaleima: " + aikaleima + "\n";
        s += "Sisältö: " + sisalto;
        return s;
    }
}
