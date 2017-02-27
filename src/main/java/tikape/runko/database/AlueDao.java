package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.Alue;
import tikape.runko.domain.Vastaus;
import tikape.runko.domain.Viestiketju;

public class AlueDao implements Dao<Alue, Integer> {

    private Database database;
    private ViestiketjuDao viestiketjuDao;
    private VastausDao vastausDao;

    public AlueDao(Database database) {
        this.database = database;
        this.viestiketjuDao = new ViestiketjuDao(database);
        this.vastausDao = new VastausDao(database);
    }

    @Override
    public Alue create(Alue t) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO Alue VALUES(?, ?, ?)");

        stmt.setObject(1, t.getId());
        stmt.setObject(2, t.getNimi());
        stmt.setObject(3, t.getKuvaus());
        stmt.executeUpdate();

        stmt.close();
        connection.close();

        return findOne(t.getId());
    }

    @Override
    public Alue findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Alue WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        int id = rs.getInt("id");
        String nimi = rs.getString("nimi");
        String kuvaus = rs.getString("kuvaus");

        Alue a = new Alue(id, nimi, kuvaus);

        rs.close();
        stmt.close();
        connection.close();

        return a;
    }

    /**
     * Etsi alue sen nimen perusteella. Case-INsensitive!
     *
     * @param name etsittävän alueen nimi.
     * @return alue tai null, jos ei löydy.
     * @throws SQLException
     */
    public Alue findOneByName(String name) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Alue WHERE upper(nimi) = ?");
        stmt.setString(1, name.toUpperCase());

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        int id = rs.getInt("id");
        String nimi = rs.getString("nimi");
        String kuvaus = rs.getString("kuvaus");
        List<Vastaus> vastaukset = vastausDao.findAllByAlue(id);
        List<Viestiketju> viestiketjut = viestiketjuDao.findAllByAlue(id);
        int viestienLkm = vastaukset.size();
        Timestamp uusin = vastaukset.get(0).getAikaleima();

        Alue alue = new Alue(id, nimi, kuvaus, viestienLkm, uusin, viestiketjut);

        rs.close();
        stmt.close();
        connection.close();

        return alue;
    }

    @Override
    public void update(Integer key, Alue t) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("UPDATE Alue SET kuvaus = ? WHERE id = ?");

        stmt.setObject(1, t.getKuvaus());
        stmt.setObject(2, key);
        stmt.executeUpdate();

        stmt.close();
        connection.close();
    }

    @Override
    public void delete(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM Alue WHERE id = ?");
        stmt.setObject(1, key);
        stmt.executeUpdate();

        stmt.close();
        connection.close();
    }

    @Override
    public List<Alue> findAll() throws SQLException {
        List<Alue> alueet = new ArrayList<>();
        try (Connection connection = database.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Alue ORDER BY nimi");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String kuvaus = rs.getString("kuvaus");
                String nimi = rs.getString("nimi");
                List<Vastaus> vastaukset = vastausDao.findAllByAlue(id);
                int viestienlkm = vastaukset.size();
                Timestamp timestamp = viestienlkm == 0 ? null : vastaukset.get(0).getAikaleima();
                List<Viestiketju> viestiketjut = viestiketjuDao.findAllByAlue(id);
                alueet.add(new Alue(id, nimi, kuvaus, viestienlkm, timestamp, viestiketjut));
            }
            rs.close();
            stmt.close();
        }

        return alueet;
    }

}
