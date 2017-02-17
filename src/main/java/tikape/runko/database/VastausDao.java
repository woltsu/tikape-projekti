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
            String query = "INSERT INTO Vastaus (nimimerkki, viestiketju, sisältö) VALUES (?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, t.getLahettaja());
            ps.setInt(2, t.getKetjuId());
            ps.setString(3, t.getSisalto());
            ps.execute();
            ps.close();
        }

        return t;
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

    /**
     * Hae vastaukset viestiketjun mukaan. Järjestetty vanhimmasta uusimpaan.
     *
     * @param viestiketjuTunnus
     * @return
     * @throws SQLException
     */
    public List<Vastaus> findAllByViestiketju(int viestiketjuTunnus) throws SQLException {
        List<Vastaus> vastaukset = new ArrayList<>();
        try (Connection connection = database.getConnection()) {
            PreparedStatement st = connection.prepareStatement("SELECT * FROM Vastaus WHERE viestiketju = ? ORDER BY aikaleima");
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

    /**
     * Hae vastaukset alueen mukaan. Järjestetty uusimmasta vanhimpaan.
     *
     * @param alueId alueen tunnus/id.
     * @return lista alueen viesteistä.
     * @throws SQLException
     */
    public List<Vastaus> findAllByAlue(int alueId) throws SQLException {
        List<Vastaus> vastaukset = new ArrayList<>();
        String query = "SELECT * FROM Vastaus "
                + "WHERE viestiketju IN (SELECT tunnus FROM Viestiketju WHERE alue = ?) "
                + "ORDER BY aikaleima DESC";
        try (Connection connection = database.getConnection()) {
            PreparedStatement st = connection.prepareStatement(query);
            st.setInt(1, alueId);
            ResultSet s = st.executeQuery();
            while (s.next()) {
                Timestamp aikaleima = new Aikaleima(s.getString("aikaleima"));
                vastaukset.add(new Vastaus(s.getInt("tunnus"), s.getInt("viestiketju"), aikaleima, s.getString("sisältö"), s.getString("nimimerkki")));
            }
            s.close();
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
