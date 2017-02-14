package tikape.runko.domain;

import java.sql.Timestamp;

public class Vastaus {

    private int id;
    private int ketjuId;
    private Timestamp aikaleima;
    private String sisalto;
    private String lahettaja;

    public Vastaus(int id, int ketjuId, Timestamp aikaleima, String sisalto, String lahettaja) {
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
}
