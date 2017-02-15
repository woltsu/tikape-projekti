package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.Viestiketju;

public class ViestiketjuDao implements Dao<Viestiketju, Integer> {

    private Database database;

    public ViestiketjuDao(Database database) {
        this.database = database;
    }

    @Override
    public Viestiketju findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Viestiketju WHERE tunnus = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        int tunnus = rs.getInt("tunnus");
        int alue = rs.getInt("alue");
        String otsikko = rs.getString("otsikko");
        Timestamp aikaleima = new Aikaleima(rs.getString("aikaleima"));

        Viestiketju vk = new Viestiketju(tunnus, alue, otsikko, aikaleima);

        rs.close();
        stmt.close();
        connection.close();

        return vk;
    }

    @Override
    public List<Viestiketju> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Viestiketju");

        ResultSet rs = stmt.executeQuery();
        List<Viestiketju> viestiketjut = new ArrayList<>();
        while (rs.next()) {
            int tunnus = rs.getInt("tunnus");
            int alue = rs.getInt("alue");
            String otsikko = rs.getString("otsikko");
            Timestamp aikaleima = new Aikaleima(rs.getString("aikaleima"));

            viestiketjut.add(new Viestiketju(tunnus, alue, otsikko, aikaleima));
        }

        rs.close();
        stmt.close();
        connection.close();

        return viestiketjut;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM Viestiketju WHERE tunnus = ?");
        stmt.setObject(1, key);
        stmt.executeUpdate();

        stmt.close();
        connection.close();
    }

    @Override
    public Viestiketju create(Viestiketju t) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO Viestiketju VALUES(?, ?, ?, ?)");

        stmt.setObject(1, t.getTunnus());
        stmt.setObject(2, t.getAlue());
        stmt.setObject(3, t.getOtsikko());
        stmt.setObject(4, t.getAikaleima());
        stmt.executeUpdate();

        stmt.close();
        connection.close();

        return findOne(t.getTunnus());
    }

    @Override
    public void update(Integer key, Viestiketju t) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("UPDATE Viestiketju SET otsikko = ? WHERE tunnus = ?");

        stmt.setObject(1, t.getOtsikko());
        stmt.setObject(2, key);
        stmt.executeUpdate();

        stmt.close();
        connection.close();
    }

}
