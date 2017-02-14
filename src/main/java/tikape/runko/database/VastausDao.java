package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class VastausDao implements Dao<Vastaus, Integer> {

    private Database database;

    public VastausDao(Database database) {
        this.database = database;
    }

    @Override
    public Vastaus create(Vastaus t) throws SQLException {
        try (Connection connection = database.getConnection()) {
            String query = "INSERT INTO Book (isbn, title, status) VALUES (?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, key);
            ps.execute();
            ps.close();
        }
    }

    @Override
    public Vastaus findOne(Integer key) throws SQLException {
        Vastaus vastaus = null;
        try (Connection connection = database.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Vastaus WHERE tunnus = ?");
            ps.setInt(1, key);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int tunnus = rs.getInt("tunnus");
                int viestiketjuTunnus = rs.getInt("viestiketju");
                Timestamp timestamp = rs.getTimestamp("aikaleima");
                String sisalto = rs.getString("sisältö");
                vastaus = new Vastaus();
            }

            rs.close();
            ps.close();
        }
        return vastaus;
    }

    @Override
    public List<Vastaus> findAll() throws SQLException {
        List<Vastaus> vastaukset;
        try (Connection connection = database.getConnection()) {
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM Vastaus");
            vastaukset = new ArrayList<>();
            while (rs.next()) {
                int tunnus = rs.getInt("tunnus");
                int viestiketjuTunnus = rs.getInt("viestiketju");
                Timestamp timestamp = rs.getTimestamp("aikaleima");
                String sisalto = rs.getString("sisältö");
                vastaukset.add(new Vastaus());
            }
            rs.close();
        }

        return vastaukset;
    }

    @Override
    public void update(Integer key, Vastaus t) throws SQLException {
        try (Connection connection = database.getConnection()) {
            String query = "UPDATE Vastaus SET viestiketju = ?, aikaleima = ?, sisältö = ? WHERE tunnus = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, t.getViestiketju());
            ps.setInt(2, t.getAikaleima());
            ps.setInt(3, t.getSisalto());
            ps.setInt(4, key);
            ps.executeUpdate();
            ps.close();
        }
    }

    @Override
    public void delete(Integer key) throws SQLException {
        try (Connection connection = database.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM Vastaus WHERE tunnus = ?");
            ps.setInt(1, key);
            ps.execute();
            ps.close();
        }
    }

}
