package tikape.runko.domain;

import java.sql.Timestamp;

public class Vastaus {
    private int id;
    private int ketju_id;
    private Timestamp aikaleima;
    private String sisalto;
    private String lahettaja;
    

    public Vastaus(int id, int ketju_id, Timestamp aikaleima, String sisalto, String lahettaja) {
        this.id = id;
        this.ketju_id = ketju_id;
        this.aikaleima = aikaleima;
        this.sisalto = sisalto;
        this.lahettaja = lahettaja;
    }
    
    public Integer getId() {
        return id;
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
