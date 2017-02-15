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

        lista.add("INSERT INTO Alue (id, kuvaus, nimi) VALUES (1, 'ohi aiheesta', 'offtopic')");
        lista.add("INSERT INTO Alue (id, kuvaus, nimi) VALUES (2, 'pelaamista', 'pelit')");
        lista.add("INSERT INTO Alue (id, kuvaus, nimi) VALUES (3, 'pingviini', 'linux')");
        lista.add("INSERT INTO Alue (id, kuvaus, nimi) VALUES (4, 'stallman', 'gnu')");

        lista.add("INSERT INTO Viestiketju (tunnus, alue, otsikko) VALUES (1, 3, 'Osaavatko pingviinit lentää? Entä onko niillä polvia?')");
//        lista.add("INSERT INTO Vastaus VALUES(1, 'tommi', 1, null, 'joo ja ei')");
//        lista.add("INSERT INTO Vastaus VALUES(2, 'pekka', 1, null, 'ei ja joo')");
        lista.add("INSERT INTO Viestiketju (tunnus, alue, otsikko) VALUES (2, 1, 'morjens')");
        lista.add("INSERT INTO Viestiketju (tunnus, alue, otsikko) VALUES (3, 2, 'wowi nurkka')");
        lista.add("INSERT INTO Viestiketju (tunnus, alue, otsikko) VALUES (4, 3, 'miten poistun vimistä??')");
        lista.add("INSERT INTO Viestiketju (tunnus, alue, otsikko) VALUES (5, 4, 'hurd')");

        lista.add("INSERT INTO Vastaus (tunnus, aikaleima, nimimerkki, viestiketju, sisältö) VALUES (1, datetime('1980-01-01 23:30:25'), 'foorumi_ukko518', 2, 'moikka kaikki')");
        lista.add("INSERT INTO Vastaus (tunnus, aikaleima, nimimerkki, viestiketju, sisältö) VALUES (2, datetime('1990-09-09 09:30:25'), 'ammattilais_peluri', 3, 'moi')");
        lista.add("INSERT INTO Vastaus (tunnus, aikaleima, nimimerkki, viestiketju, sisältö) VALUES (3, datetime('2022-09-09 09:30:25'), 'linux_man', 4, 'help')");
        lista.add("INSERT INTO Vastaus (tunnus, aikaleima, nimimerkki, viestiketju, sisältö) VALUES (4, datetime('2001-09-09 09:30:25'), 'rms', 5, 'jåå')");

        return lista;
    }
}
