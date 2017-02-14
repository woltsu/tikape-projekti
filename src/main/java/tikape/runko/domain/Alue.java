package tikape.runko.domain;

public class Alue {

    private int id;
    private String nimi;
    private String kuvaus;
    private int viestienLkm;

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
    
    public int lisaaViesti() {
        viestienLkm++;
        return viestienLkm;
    }
}
