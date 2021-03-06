package dk.runs.runners.repositories.mysqlImpl;

import dk.runs.runners.entities.Location;
import dk.runs.runners.entities.Locational;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class BaseRunnersRepository {

    protected void executeCreateLocationsQuery(Locational locational, PreparedStatement pstmtLocation) throws SQLException {
        for(Location location : locational.getLocations()){
            if (location != null) {
                pstmtLocation.setString(1, location.getId());
                pstmtLocation.setString(2, locational.getId());

                pstmtLocation.setString(3, location.getStreetName());
                pstmtLocation.setString(4, location.getStreetNumber());
                pstmtLocation.setString(5, location.getCity());
                pstmtLocation.setString(6, location.getCountry());
                pstmtLocation.setString(7, "POINT(" + location.getX() + " " + location.getY() + ")");
                pstmtLocation.setInt(8, location.getSRID());
                pstmtLocation.setString(9, location.getTitle());
                pstmtLocation.executeUpdate();
            }
        }
    }

    protected void executeUpdateLocationQuery(Locational locational, PreparedStatement pstmtLocation) throws SQLException {
        for(Location location : locational.getLocations()) {
            pstmtLocation.setString(1, location.getStreetName());
            pstmtLocation.setString(2, location.getStreetNumber());
            pstmtLocation.setString(3, location.getCity());
            pstmtLocation.setString(4, location.getCountry());
            pstmtLocation.setString(5, "POINT(" + location.getX() + " " + location.getY() + ")");
            pstmtLocation.setInt(6, location.getSRID());
            pstmtLocation.setString(7, location.getTitle());
            pstmtLocation.setString(8, location.getId());
            pstmtLocation.executeUpdate();
        }
    }

    protected void executeDeleteLocationsQuery(Locational locational, PreparedStatement pstmtDeleteLocations) throws SQLException {
        pstmtDeleteLocations.setString(1, locational.getId());
        pstmtDeleteLocations.executeUpdate();
    }
}
