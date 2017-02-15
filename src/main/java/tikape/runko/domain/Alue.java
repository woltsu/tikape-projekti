package tikape.runko.domain;

import java.sql.Timestamp;
import java.util.List;

public class Alue {

    private int id;
    private String nimi;
    private String kuvaus;
    private int viestienLkm;
    private Timestamp viimeisinTimestamp;
    private List<Viestiketju> viestiketjut;

    public Alue(int id, String nimi, String kuvaus, int viestienLkm, Timestamp viimeisinTimestamp, List<Viestiketju> viestiketjut) {
        this.id = id;
        this.nimi = nimi;
        this.kuvaus = kuvaus;
        this.viestienLkm = viestienLkm;
        this.viimeisinTimestamp = viimeisinTimestamp;
        this.viestiketjut = viestiketjut;
        //this.viestienLkm = AlueDao().getLkm();
    }

    public Alue(int id, String nimi, String kuvaus) {
        this.id = id;
        this.nimi = nimi;
        this.kuvaus = kuvaus;
        //this.viestienLkm = AlueDao().getLkm();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public int getViestienLkm() {
        return viestienLkm;
    }

    public void setViestienLkm(int viestienLkm) {
        this.viestienLkm = viestienLkm;
    }

    public String getKuvaus() {
        return this.kuvaus;
    }

    public void setKuvaus(String kuvaus) {
        this.kuvaus = kuvaus;
    }

    public int lisaaViesti() {
        viestienLkm++;
        return viestienLkm;
    }

    public Timestamp getViimeisinTimestamp() {
        return viimeisinTimestamp;
    }

    public void setViimeisinTimestamp(Timestamp viimeisinTimestamp) {
        this.viimeisinTimestamp = viimeisinTimestamp;
    }

    public String getViimeisin() {
        return viimeisinTimestamp.toString();
    }

    public List<Viestiketju> getViestiketjut() {
        return viestiketjut;
    }

    public void setViestiketjut(List<Viestiketju> viestiketjut) {
        this.viestiketjut = viestiketjut;
    }

}
