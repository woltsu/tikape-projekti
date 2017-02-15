package tikape.runko.database;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * Sqlite3 ei ilmeisesti tallenna aikaleimoja standardien mukaan tjsp?
 * rs.getTimestamp() ei siis toimi.
 */
public class Aikaleima extends Timestamp {

    /**
     * Luo uusi aikaleima käyttäen sqlite3:n omaa muotoa ajalle.
     *
     * @param timestampString sqlite3 aikaleima merkkijonomuodossa.
     */
    public Aikaleima(String timestampString) {
        super(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime aika = LocalDateTime.parse(timestampString, formatter);
        super.setTime(aika.toEpochSecond(ZoneOffset.UTC) * 1000);
    }

}
