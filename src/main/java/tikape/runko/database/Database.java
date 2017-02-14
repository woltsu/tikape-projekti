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
        
//        lista.add("INSERT INTO Opiskelija (nimi) VALUES ('Platon');");
//        lista.add("INSERT INTO Opiskelija (nimi) VALUES ('Aristoteles');");
//        lista.add("INSERT INTO Opiskelija (nimi) VALUES ('Homeros');");
        lista.add("INSERT INTO Alue (id, kuvaus, nimi) VALUES (1, 'ohi aiheesta', 'offtopic')");
        lista.add("INSERT INTO Alue (id, kuvaus, nimi) VALUES (2, 'pelaamista', 'pelit')");
        lista.add("INSERT INTO Alue (id, kuvaus, nimi) VALUES (3, 'pingviini', 'linux')");
        lista.add("INSERT INTO Alue (id, kuvaus, nimi) VALUES (4, 'stallman', 'gnu')");
        
        lista.add("INSERT INTO Viestiketju (tunnus, alue, otsikko, aikaleima) VALUES (1, 3, 'Osaavatko pingviinit lentää? Entä onko niillä polvia?', null)");
//        lista.add("INSERT INTO Vastaus VALUES(1, 'tommi', 1, null, 'joo ja ei')");
//        lista.add("INSERT INTO Vastaus VALUES(2, 'pekka', 1, null, 'ei ja joo')");

        return lista;
    }
}
