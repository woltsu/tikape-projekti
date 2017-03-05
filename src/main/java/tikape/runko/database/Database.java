package tikape.runko.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private String databaseAddress;

    public Database(String databaseAddress) throws ClassNotFoundException {
        this.databaseAddress = databaseAddress;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(databaseAddress);
    }

    public void init() {
        List<String> lauseet = sqliteLauseet();

        // "try with resources" sulkee resurssin automaattisesti lopuksi
        try (Connection conn = getConnection()) {
            Statement st = conn.createStatement();

            // suoritetaan komennot
            for (String lause : lauseet) {
                System.out.println("Running command >> " + lause);
                st.executeUpdate(lause);
            }

        } catch (Throwable t) {
            // jos tietokantataulu on jo olemassa, ei komentoja suoriteta
            System.out.println("Error >> " + t.getMessage());
        }
    }

    private List<String> sqliteLauseet() {
        ArrayList<String> lista = new ArrayList<>();

        // tietokantataulujen luomiseen tarvittavat komennot suoritusjärjestyksessä
        lista.add("CREATE TABLE Vastaus\n"
                + "(\n"
                + "	tunnus INTEGER PRIMARY KEY,\n"
                + "	nimimerkki VARCHAR(30) NOT NULL,\n"
                + "	viestiketju INTEGER NOT NULL,\n"
                + "	aikaleima TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n"
                + "	sisältö VARCHAR(1000) NOT NULL,\n"
                + "	FOREIGN KEY(viestiketju) REFERENCES Viestiketju(tunnus)\n"
                + ");");
        lista.add("CREATE TABLE Alue\n"
                + "(\n"
                + "	id INTEGER PRIMARY KEY,\n"
                + "	kuvaus VARCHAR(200),\n"
                + "	nimi VARCHAR(50) NOT NULL\n"
                + ");");
        lista.add("CREATE TABLE Viestiketju\n"
                + "(\n"
                + "	tunnus INTEGER PRIMARY KEY,\n"
                + "	alue INTEGER NOT NULL,\n"
                + "	otsikko VARCHAR(30) NOT NULL,\n"
                + "	aikaleima TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n"
                + "	FOREIGN KEY(alue) REFERENCES Alue(tunnus)\n"
                + ");");

        lista.add(alueSql(1, "ohi aiheesta", "offtopic"));
        lista.add(alueSql(2, "pelaamista", "pelit"));
        lista.add(alueSql(3, "pingviini", "linux"));
        lista.add(alueSql(4, "stallman", "gnu"));
        lista.add(alueSql(5, "tärkeää asiaa", "tiedotukset"));

        lista.add(viestiketjuSql(1, 3, "Osaavatko pingviinit lentää? Entä onko niillä polvia?"));
        lista.add(viestiketjuSql(2, 1, "morjens"));
        lista.add(viestiketjuSql(3, 2, "wowi nurkka"));
        lista.add(viestiketjuSql(4, 3, "miten poistun vimistä??"));
        lista.add(viestiketjuSql(5, 4, "hurd"));
        lista.add(viestiketjuSql(6, 4, "Hälsning"));
        lista.add(viestiketjuSql(7, 5, "Tärkeä muistutus"));

        lista.add(vastausSql("1980-01-01 23:30:25", "foorumi_ukko518", 2, "moikka kaikki"));
        lista.add(vastausSql("1990-09-09 09:30:25", "ammattilais_peluri", 3, "moi"));
        lista.add(vastausSql("2016-09-09 09:30:25", "linux_man", 4, "help"));
        lista.add(vastausSql("0001-01-09 09:30:25", "rms", 5, "jåå"));
        lista.add(vastausSql("2001-02-09 09:30:25", "rms", 5, "jåå"));
        lista.add(vastausSql("2001-03-09 09:30:25", "rms", 5, "jåå"));
        lista.add(vastausSql("2001-04-09 09:30:25", "rms", 5, "jåå"));
        lista.add(vastausSql("2001-05-09 09:30:25", "rms", 5, "jåå"));
        lista.add(vastausSql("2001-06-09 09:30:25", "rms", 5, "jåå"));
        lista.add(vastausSql("2001-07-09 09:30:25", "rms", 5, "jåå"));
        lista.add(vastausSql("2001-08-09 09:30:25", "rms", 5, "jåå"));
        lista.add(vastausSql("2001-09-09 09:30:25", "rms", 5, "jåå"));
        lista.add(vastausSql("2001-10-09 09:30:25", "rms", 5, "jåå"));
        lista.add(vastausSql("2001-11-09 09:30:25", "rms", 5, "jåå"));
        lista.add(vastausSql("2001-12-09 09:30:25", "rms", 5, "jåå"));
        lista.add(vastausSql("1653-01-09 09:30:25", "Gustav", 6, "hej"));
        lista.add(vastausSql("2017-01-09 09:30:25", "admin", 7, "Älkää puhuko asiattomuuksia"));

        return lista;
    }

    private String alueSql(int tunnus, String kuvaus, String nimi) {
        return String.format("INSERT INTO Alue (id, kuvaus, nimi) "
                + "VALUES (%d, '%s', '%s')", tunnus, kuvaus, nimi);
    }

    private String viestiketjuSql(int tunnus, int alue, String otsikko) {
        return String.format("INSERT INTO Viestiketju (tunnus, alue, otsikko) "
                + "VALUES (%d, %d, '%s')", tunnus, alue, otsikko);

    }

    private String vastausSql(String isodate, String nick, int viestiketju, String viesti) {
        return String.format("INSERT INTO Vastaus (aikaleima, nimimerkki, viestiketju, sisältö) "
                + "VALUES (datetime('%s'), '%s', %d, '%s')", isodate, nick, viestiketju, viesti);
    }
}
