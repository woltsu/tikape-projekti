package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.Vastaus;

public class VastausDao implements Dao<Vastaus, Integer> {

    private Database database;

    public VastausDao(Database database) {
        this.database = database;
    }

    @Override
    public Vastaus create(Vastaus t) throws SQLException {
        try (Connection connection = database.getConnection()) {
            String query = "INSERT INTO Vastaus (, title, status) VALUES (?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, t.getId());
            ps.execute();
            ps.close();
        }

        return findOne(t.getId());
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
                Timestamp timestamp = new Aikaleima(rs.getString("aikaleima"));
                String sisalto = rs.getString("sisältö");
                String nimimerkki = rs.getString("nimimerkki");
                vastaus = new Vastaus(tunnus, viestiketjuTunnus, timestamp, sisalto, nimimerkki);
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
                Timestamp timestamp = new Aikaleima(rs.getString("aikaleima"));
                String sisalto = rs.getString("sisältö");
                String nimimerkki = rs.getString("nimimerkki");
                vastaukset.add(new Vastaus(tunnus, viestiketjuTunnus, timestamp, sisalto, nimimerkki));
            }
            rs.close();
        }

        return vastaukset;
    }

    public List<Vastaus> findAllByViestiketju(int viestiketjuTunnus) throws SQLException {
        List<Vastaus> vastaukset = new ArrayList<>();
        try (Connection connection = database.getConnection()) {
            PreparedStatement st = connection.prepareStatement("SELECT * FROM Vastaus WHERE viestiketju = ?");
            st.setInt(1, viestiketjuTunnus);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int viestinTunnus = rs.getInt("tunnus");
                Timestamp timestamp = new Aikaleima(rs.getString("aikaleima"));
                String sisalto = rs.getString("sisältö");
                String nimimerkki = rs.getString("nimimerkki");
                vastaukset.add(new Vastaus(viestinTunnus, viestiketjuTunnus, timestamp, sisalto, nimimerkki));
            }
            rs.close();
            st.close();
        }

        return vastaukset;
    }

    @Override
    public void update(Integer key, Vastaus t) throws SQLException {
        try (Connection connection = database.getConnection()) {
            String query = "UPDATE Vastaus SET viestiketju = ?, aikaleima = ?, sisältö = ? WHERE tunnus = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, t.getKetjuId());
            ps.setTimestamp(2, t.getAikaleima());
            ps.setString(3, t.getSisalto());
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
