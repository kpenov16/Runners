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
            pstmtLocation.setString(2, locational.getId());

            pstmtLocation.setString(3, location.getStreetName());
            pstmtLocation.setString(4, location.getStreetNumber());
            pstmtLocation.setString(5, location.getCity());
            pstmtLocation.setString(6, location.getCountry());
            pstmtLocation.setString(7, "POINT(" + location.getX() + " " + location.getY() + ")");
            pstmtLocation.setInt(8, location.getSRID());
            pstmtLocation.executeUpdate();
        }
    }

    protected void executeUpdateLocationQuery(Locational locational, PreparedStatement pstmtLocation) throws SQLException {
        final Location location = locational.getLocation();
        pstmtLocation.setString(1, location.getStreetName());
        pstmtLocation.setString(2, location.getStreetNumber());
        pstmtLocation.setString(3, location.getCity());
        pstmtLocation.setString(4, location.getCountry());
        pstmtLocation.setString(5, "POINT(" + location.getX() + " " + location.getY() + ")");
        pstmtLocation.setInt(6, location.getSRID());
        pstmtLocation.setString(7, location.getId());
        pstmtLocation.executeUpdate();

        String locationSql = "UPDATE route_location SET street_name = ?, street_number = ?," +
                " city = ?, country = ?, spatial_point = ST_GeomFromText( ? , ? )" +
                " WHERE route_location.id = ? ";

    }

    protected void executeCreateLocationReferenceQuery(Locational locational, PreparedStatement pstmtLocationUser) throws SQLException {
        final Location location = locational.getLocation();
        if (location != null) {
            pstmtLocationUser.setString(1, location.getId());
            pstmtLocationUser.setString(2, locational.getId());
            pstmtLocationUser.executeUpdate();
        }
    }

}
