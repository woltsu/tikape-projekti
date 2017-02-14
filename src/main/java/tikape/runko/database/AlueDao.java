package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.Alue;

public class AlueDao implements Dao<Alue, Integer> {

    private Database database;

    public AlueDao(Database database) {
        this.database = database;
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
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Alue");

        ResultSet rs = stmt.executeQuery();
        List<Alue> alueet = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("id");
            String nimi = rs.getString("nimi");
            String kuvaus = rs.getString("kuvaus");
            alueet.add(new Alue(id, nimi, kuvaus));
        }

        rs.close();
        stmt.close();
        connection.close();

        return alueet;
    }

}
