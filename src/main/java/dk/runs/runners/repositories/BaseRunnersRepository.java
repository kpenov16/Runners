package dk.runs.runners.repositories;

import dk.runs.runners.entities.Location;
import dk.runs.runners.entities.Locational;
import dk.runs.runners.entities.User;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class BaseRunnersRepository {

    protected void executeCreateLocationQuery(Locational locational, PreparedStatement pstmtLocation) throws SQLException {
        final Location location = locational.getLocation();
        if (location != null) {
            pstmtLocation.setString(1, location.getId());
            pstmtLocation.setString(2, location.getStreetName());
            pstmtLocation.setString(3, location.getStreetNumber());
            pstmtLocation.setString(4, location.getCity());
            pstmtLocation.setString(5, location.getCountry());
            pstmtLocation.setString(6, "POINT(" + location.getX() + " " + location.getY() + ")");
            pstmtLocation.setInt(7, location.getSRID());
            pstmtLocation.executeUpdate();
        }
    }


}
