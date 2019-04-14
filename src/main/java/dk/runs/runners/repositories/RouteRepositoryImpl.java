package dk.runs.runners.repositories;

import dk.runs.runners.entities.Location;
import dk.runs.runners.entities.Route;
import dk.runs.runners.entities.WayPoint;
import dk.runs.runners.usecases.RouteRepository;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class RouteRepositoryImpl implements RouteRepository {

    private final String url = "jdbc:mysql://ec2-52-30-211-3.eu-west-1.compute.amazonaws.com/s133967?"
            + "user=s133967&password=8JPOJuQcgUpUVIVHY4S2H";

    @Override
    public void createRoute(Route route, String creatorId) throws CreateRouteException {
        String routeSql = "INSERT INTO route (id, creator_id, title, date, distance, duration, description, status, max_participants, min_participants)" +
                "VALUES ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? )";

        String locationSql = "INSERT INTO location (id, street_name, street_number, city, country, spatial_point)" +
                "VALUES ( ? , ? , ? , ? , ? , ST_GeomFromText( ? , ? ))";

        String locationRouteSql = "INSERT INTO location_route ( location_id, route_id )" +
                                  "VALUES ( ? , ? )";

        String waypointSql = "INSERT INTO waypoint (`index`, route_id, spatial_point)" +
                "VALUES ( ? , ? , ST_GeomFromText( ? , ? ))";

        executeCreateRouteQueryAsUnitOfWork(routeSql, waypointSql, locationSql, locationRouteSql, route, creatorId);
    }

    private long executeGetIdQuery(String sql, long creatorId) throws RouteNotFoundException {
        long id  = -1 ;
        try(Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt= conn.prepareStatement(sql)){
            pstmt.setLong(1, creatorId);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            id = rs.getLong("id");
            rs.close();
        }catch(SQLException se){
            throw new RouteNotFoundException(se.getMessage());
        }catch(Exception e){
            throw new RouteNotFoundException(e.getMessage());
        }
        return id;
    }

    @Override
    public Route getRoute(String routeId) throws RouteNotFoundException {
        String sql = "SELECT * " +
                " FROM route" +
                " WHERE route.id = ?";
        Route route = executeGetRouteQuery(sql,new Route(routeId));
        route.setWayPoints( getWaypoints(route.getId()) );
        route.setLocation( getLocation(route.getId()) );
        return route;
    }

    @Override
    public List<Route> getRoutes(String creatorId) {
        String sql = "SELECT * " +
                     " FROM route" +
                     " WHERE route.creator_id = ?";
        List<Route> routes = executeGetRouteQuery( sql, creatorId );
        for(Route r : routes){
            r.setWayPoints( getWaypoints(r.getId()) );
            r.setLocation(getLocation(r.getId()));
        }
        return routes;
    }

    private List<WayPoint> getWaypoints(String routeId) {
        String sql = "SELECT `index`, ST_X(spatial_point) AS X, ST_Y(spatial_point) AS Y " +
                " FROM waypoint" +
                " WHERE waypoint.route_id = ?";
        return executeGetWaypointsQuery(sql, new LinkedList<WayPoint>(), routeId);
    }

    private Location getLocation(String routeId) {
        String sql = " SELECT id, street_name, street_number, city, country, ST_X(spatial_point) AS X, ST_Y(spatial_point) AS Y" +
                     " FROM location JOIN location_route" +
                     " ON location_route.location_id = location.id" +
                     " WHERE location_route.route_id = ? ";
        return executeGetLocationQuery(sql, routeId);
    }

    @Override
    public void updateRoute(Route route) {
        String deleteWaypointsSql = "DELETE FROM waypoint WHERE route_id = ?";

        String routeSql = "UPDATE route" +
                     " SET title = ?, date = ?, distance = ?, duration = ?,"+
                     " description = ?, status = ?, max_participants = ?, min_participants = ?" +
                " WHERE id = ?";

        String waypointSql = "INSERT INTO waypoint (`index`, route_id, spatial_point)" +
                "VALUES ( ? , ? , ST_GeomFromText( ? , ? ))";

        String locationSql = "UPDATE location SET street_name = ? , street_number = ? ," +
                                " city = ?, country = ?, spatial_point = ST_GeomFromText( ? , ? )" +
                                "WHERE location.id = ?";
        executeUpdateRouteQueryAsUnitOfWork(deleteWaypointsSql, routeSql, waypointSql, locationSql, route);
    }

    private void executeUpdateRouteQueryAsUnitOfWork(String deleteWaypointsSql, String routeSql,
                                                     String waypointSql, String locationSql, Route route)
                                                    throws UpdateRouteException {
        Connection conn = null;
        PreparedStatement pstmtRoute = null;
        PreparedStatement pstmtWaypoint = null;
        PreparedStatement pstmtDeleteWaypoint = null;
        PreparedStatement pstmtRouteLocation = null;
        try{
            conn = DriverManager.getConnection(url);
            conn.setAutoCommit(false);

            pstmtDeleteWaypoint = conn.prepareStatement(deleteWaypointsSql);
            pstmtRoute = conn.prepareStatement(routeSql);
            pstmtWaypoint = conn.prepareStatement(waypointSql);
            pstmtRouteLocation = conn.prepareStatement(locationSql);

            //delete waypoints
            pstmtDeleteWaypoint.setString(1, route.getId());
            pstmtDeleteWaypoint.executeUpdate();

            //update route
            pstmtRoute.setString(1, route.getTitle());
            pstmtRoute.setLong(2, route.getDate().getTime() );
            pstmtRoute.setInt(3, route.getDistance());
            pstmtRoute.setLong(4, route.getDuration() );
            pstmtRoute.setString(5, route.getDescription() );
            pstmtRoute.setString(6, route.getStatus() );
            pstmtRoute.setInt(7, route.getMaxParticipants() );
            pstmtRoute.setInt(8, route.getMinParticipants() );
            pstmtRoute.setString(9, route.getId());
            int rowsEffected = pstmtRoute.executeUpdate();

            if(rowsEffected == 1){
                //create waypoints
                executeCreateWaypointsQuery(route, pstmtWaypoint);

                //update location
                executeUpdateLocationQuery(route, pstmtRouteLocation);

                conn.commit();
            }else {
                conn.rollback();
            }

            //


        }catch (SQLIntegrityConstraintViolationException e){
            try {
                if(conn!=null){
                    conn.rollback();
                }
                throw new RouteIdDuplicationException(e.getMessage());
            }catch (SQLException rollBackException){
                //??
            }
        }catch(SQLException se){
            try {
                conn.rollback();
                se.printStackTrace();
                throw new UpdateRouteException(se.getMessage());
            }catch (SQLException rollBackException){
                throw new UpdateRouteException(rollBackException.getMessage());
            }
        }catch(Exception e){
            try {
                conn.rollback();
                e.printStackTrace();
                throw new UpdateRouteException(e.getMessage());
            }catch (SQLException rollBackException){
                throw new UpdateRouteException(rollBackException.getMessage());
            }
        }finally {
            try {
                if(pstmtRoute != null) pstmtRoute.close();
                if(pstmtWaypoint != null) pstmtWaypoint.close();
                if(conn != null) conn.close();
            } catch (SQLException e) {
                throw new UpdateRouteException(e.getMessage());
            }
        }
    }



    @Override
    public List<Route> getRouteList() {
        String sql = "SELECT route.id AS id " +
                    "FROM route"; //TODO select only comming runs. That is where dato > now
        return executeGetRoutesQuery(sql);
    }

    private List<Route> executeGetRoutesQuery(String sql) {

        List<Route> routes = new LinkedList<>();

        try(Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()){

            while(rs.next()){
                Route route = new Route(rs.getString(1));
                //route.setLocation(rs.getString(2));
                route.setLocation(getLocation(route.getId()));
                routes.add(route);
            }
        } catch (SQLException se){
            throw new GetRoutesException(se.getMessage());
        } catch (Exception e){
            throw new GetRoutesException(e.getMessage());
        }
        return routes;
    }

 /*   private void executeUpdateRouteQuery(String sql, Route route) throws UpdateRouteException {
        try(Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt= conn.prepareStatement(sql)){
            pstmt.setString(1, route.getTitle());
            pstmt.setString(2, route.getLocation());
            pstmt.setLong(3, route.getDate().getTime() );
            pstmt.setInt(4, route.getDistance());
            pstmt.setLong(5, route.getDuration() );
            pstmt.setString(6, route.getDescription() );
            pstmt.setString(7, route.getStatus() );
            pstmt.setString(8, route.getId() );
            pstmt.executeUpdate();
        }catch(SQLException se){
            throw new UpdateRouteException(se.getMessage());
        }catch(Exception e){
            throw new UpdateRouteException(e.getMessage());
        }
    }*/

    private List<Route> executeGetRouteQuery(String sql, String creatorId) throws RouteNotFoundException {
        List<Route> routes = new LinkedList<>();
        try(Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt= conn.prepareStatement(sql)){
            pstmt.setString(1, creatorId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                Route route = new Route(rs.getString("id"));
                route.setTitle(rs.getString("title"));
                route.setDate( new java.util.Date( rs.getLong("date") ));
                route.setDistance(rs.getInt("distance"));
                route.setDuration(rs.getLong("duration"));
                route.setDescription(rs.getString("description"));
                route.setStatus(rs.getString("status"));
                route.setMaxParticipants(rs.getInt("max_participants"));
                route.setMinParticipants(rs.getInt("min_participants"));
                routes.add(route);
            }
            rs.close();
        }catch(SQLException se){
            throw new RouteNotFoundException(se.getMessage());
        }catch(Exception e){
            throw new RouteNotFoundException(e.getMessage());
        }
        return routes;
    }


    private Route executeGetRouteQuery(String sql, Route route) throws RouteNotFoundException {
        try(Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt= conn.prepareStatement(sql)){
            pstmt.setString(1, route.getId());
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            route.setTitle(rs.getString("title"));
            route.setDate( new java.util.Date( rs.getLong("date") ));
            route.setDistance(rs.getInt("distance"));
            route.setDuration(rs.getLong("duration"));
            route.setDescription(rs.getString("description"));
            route.setStatus(rs.getString("status"));
            route.setMaxParticipants(rs.getInt("max_participants"));
            route.setMinParticipants(rs.getInt("min_participants"));
            rs.close();
        }catch(SQLException se){
            throw new RouteNotFoundException(se.getMessage());
        }catch(Exception e){
            throw new RouteNotFoundException(e.getMessage());
        }
        return route;
    }

    private Location executeGetLocationQuery(String locationSql, String routeId) throws RouteNotFoundException {
        Location location = null;
        try(Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt= conn.prepareStatement(locationSql)){
            pstmt.setString(1, routeId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                location = new Location(rs.getString("id"));
                location.setStreetName(rs.getString("street_name"));
                location.setStreetNumber(rs.getString("street_number"));
                location.setCity(rs.getString("city"));
                location.setCountry(rs.getString("country"));
                location.setX(rs.getDouble("X"));
                location.setY(rs.getDouble("Y"));
            }
            rs.close();
        }catch(SQLException se){
            throw new RouteNotFoundException(se.getMessage());
        }catch(Exception e){
            throw new RouteNotFoundException(e.getMessage());
        }
        return location;
    }


    private List<WayPoint> executeGetWaypointsQuery(String sql, List<WayPoint> wayPoints, String routeId) throws RouteNotFoundException {

        try(Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt= conn.prepareStatement(sql)){
            pstmt.setString(1, routeId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                int index = rs.getInt(1);
                double x = rs.getDouble(2);
                double y = rs.getDouble(3);
                wayPoints.add( new WayPoint(x, y, index) );
            }
            rs.close();
        }catch(SQLException se){
            throw new RouteNotFoundException(se.getMessage());
        }catch(Exception e){
            throw new RouteNotFoundException(e.getMessage());
        }
        return wayPoints;
    }

    private void executeCreateRouteQueryAsUnitOfWork(String routeSql, String waypointSql, String locationSql,
                                                     String locationRouteSql, Route route, String creatorId) throws CreateRouteException {
        Connection conn = null;
        PreparedStatement pstmtRoute = null;
        PreparedStatement pstmtWaypoint = null;
        PreparedStatement pstmtLocation = null;
        PreparedStatement pstmtLocationRoute = null;

        try{
            conn = DriverManager.getConnection(url);
            conn.setAutoCommit(false);

            pstmtRoute = conn.prepareStatement(routeSql);
            pstmtWaypoint = conn.prepareStatement(waypointSql);
            pstmtLocation = conn.prepareStatement(locationSql);
            pstmtLocationRoute = conn.prepareStatement(locationRouteSql);


            pstmtRoute.setString(1, route.getId());
            pstmtRoute.setString(2, creatorId);
            pstmtRoute.setString(3, route.getTitle());
            pstmtRoute.setLong(4, route.getDate().getTime() );
            pstmtRoute.setInt(5, route.getDistance());
            pstmtRoute.setLong(6, route.getDuration() );
            pstmtRoute.setString(7, route.getDescription() );
            pstmtRoute.setString(8, route.getStatus() );
            pstmtRoute.setInt(9, route.getMaxParticipants() );
            pstmtRoute.setInt(10, route.getMinParticipants() );
            int rowsEffected = pstmtRoute.executeUpdate();

            if(rowsEffected == 1){
                executeCreateLocationQuery(route, pstmtLocation);

                executeCreateLocationRouteQuery(route, pstmtLocationRoute);

                executeCreateWaypointsQuery(route, pstmtWaypoint);

                conn.commit();
            }else {
                conn.rollback();
            }
        }catch (SQLIntegrityConstraintViolationException e){
            try {
                if(conn!=null){
                    conn.rollback();
                }
                throw new RouteIdDuplicationException(e.getMessage());
            }catch (SQLException rollBackException){
                //??
            }
        }catch(SQLException se){
            try {
                conn.rollback();
                se.printStackTrace();
                throw new CreateRouteException(se.getMessage());
            }catch (SQLException rollBackException){
                throw new CreateRouteException(rollBackException.getMessage());
            }
        }catch(Exception e){
            try {
                conn.rollback();
                e.printStackTrace();
                throw new CreateRouteException(e.getMessage());
            }catch (SQLException rollBackException){
                throw new CreateRouteException(rollBackException.getMessage());
            }
        }finally {
            try {
                if(pstmtRoute != null) pstmtRoute.close();
                if(pstmtWaypoint != null) pstmtWaypoint.close();
                if(conn != null) conn.close();
            } catch (SQLException e) {
                throw new CreateRouteException(e.getMessage());
            }
        }
    }

    private void executeCreateWaypointsQuery(Route route, PreparedStatement pstmtWaypoint) throws SQLException {
        for (WayPoint wayPoint : route.getWayPoints()) {
            pstmtWaypoint.setInt(1, wayPoint.getIndex());
            pstmtWaypoint.setString(2, route.getId());
            pstmtWaypoint.setString(3, "POINT(" + wayPoint.getX() + " " + wayPoint.getY() + ")");
            pstmtWaypoint.setInt(4, wayPoint.getSRID());
            pstmtWaypoint.executeUpdate();
        }
    }
    private void executeUpdateLocationQuery(Route route, PreparedStatement pstmtRouteLocation) throws SQLException {

        /*String locationSql = "UPDATE location SET street_name = ? , street_number = ? ," +
                " city = ?, country = ?, spatial_point = ST_GeomFromText( ? , ? )" +
                "WHERE location.id = ?";*/

        final Location location = route.getLocation();
        pstmtRouteLocation.setString(1, location.getStreetName());
        pstmtRouteLocation.setString(2, location.getStreetNumber());
        pstmtRouteLocation.setString(3, location.getCity());
        pstmtRouteLocation.setString(4, location.getCountry());
        pstmtRouteLocation.setString(5, "POINT(" + location.getX() + " " + location.getY() + ")");
        pstmtRouteLocation.setInt(6, location.getSRID());
        pstmtRouteLocation.setString(7, location.getId());
        pstmtRouteLocation.executeUpdate();

    }

    private void executeCreateLocationRouteQuery(Route route, PreparedStatement pstmtLocationRoute) throws SQLException {
        final Location location = route.getLocation();
        if (location != null) {
            pstmtLocationRoute.setString(1, location.getId());
            pstmtLocationRoute.setString(2, route.getId());
            pstmtLocationRoute.executeUpdate();
        }
    }

    private void executeCreateLocationQuery(Route route, PreparedStatement pstmtLocation) throws SQLException {
        /*"INSERT INTO location (route_id, street_name, street_number, city, country, spatial_point)" +
                "VALUES ( ? , ? , ? , ? , ? , ST_GeomFromText( ? , ? ))";*/
        final Location location = route.getLocation();
        if (location !=null) {
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
/*
    private void executeCreateRouteQuery(String sql, Route route, String creatorId) throws CreateRouteException {
        try(Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt= conn.prepareStatement(sql)){
            pstmt.setString(1, route.getId());
            pstmt.setString(2, creatorId);
            pstmt.setString(3, route.getTitle());
            pstmt.setString(4, route.getLocation());
            pstmt.setLong(5, route.getDate().getTime() );
            pstmt.setInt(6, route.getDistance());
            pstmt.setLong(7, route.getDuration() );
            pstmt.setString(8, route.getDescription() );
            pstmt.setString(9, route.getStatus() );
            pstmt.executeUpdate();
        }catch (SQLIntegrityConstraintViolationException e){
            throw new RouteIdDuplicationException(e.getMessage());
        }catch(SQLException se){
            se.printStackTrace();
            throw new CreateRouteException(se.getMessage());
        }catch(Exception e){
            e.printStackTrace();
            throw new CreateRouteException(e.getMessage());
        }
    }*/

   /* public void deleteRouteOld(String id) throws DeleteRouteException {
        String sql = "DELETE FROM route WHERE id = ?";
        executeDeleteRouteQueryOld(sql, id);
    }*/

    public void deleteRoute(String routeId) throws DeleteRouteException {
        Route route = getRoute(routeId);
        String locationRouteSql = "DELETE FROM location_route WHERE route_id = ?";
        String locationSql = "DELETE FROM location WHERE id = ?";
        String routeSql = "DELETE FROM route WHERE id = ?";
        String waypointSql = "DELETE FROM waypoint WHERE route_id = ?";
        executeDeleteRouteQuery(locationRouteSql, locationSql, routeSql, waypointSql, route);
    }

    private void executeDeleteRouteQuery(String locationRouteSql, String locationSql,
                                         String routeSql, String waypointSql, Route route)
                                        throws DeleteRouteException {
        Connection conn = null;
        PreparedStatement pstmtLocationRoute = null;
        PreparedStatement pstmtLocation = null;
        PreparedStatement pstmtRoute = null;
        PreparedStatement pstmtWaypoint = null;
        try{
            conn = DriverManager.getConnection(url);
            conn.setAutoCommit(false);

            pstmtLocationRoute = conn.prepareStatement(locationRouteSql);
            pstmtLocationRoute.setString(1, route.getId());
            pstmtLocationRoute.executeUpdate();

            pstmtLocation = conn.prepareStatement(locationSql);
            pstmtLocation.setString(1, route.getLocation().getId());
            pstmtLocation.executeUpdate();

            pstmtWaypoint = conn.prepareStatement(waypointSql);
            pstmtWaypoint.setString(1, route.getId());
            pstmtWaypoint.executeUpdate();

            pstmtRoute = conn.prepareStatement(routeSql);
            pstmtRoute.setString(1, route.getId());
            pstmtRoute.executeUpdate();

            conn.commit();
        }catch(SQLException se){
            try {
                conn.rollback();
                throw new DeleteRouteException(se.getMessage());
            }catch (SQLException rollBackException){
                throw new DeleteRouteException(rollBackException.getMessage());
            }
        }catch(Exception e){
            try {
                conn.rollback();
                throw new DeleteRouteException(e.getMessage());
            }catch (SQLException rollBackException){
                throw new DeleteRouteException(rollBackException.getMessage());
            }
        }finally {
            try {
                if(pstmtRoute != null) pstmtRoute.close();
                if(pstmtWaypoint != null) pstmtWaypoint.close();
                if(conn != null) conn.close();
            } catch (SQLException e) {
                throw new DeleteRouteException(e.getMessage());
            }
        }
    }

 /*   private void executeDeleteRouteQueryOld(String sql, String param01) throws DeleteRouteException {
        try(Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt= conn.prepareStatement(sql)){
            pstmt.setString(1, param01);
            pstmt.executeUpdate();
        }catch(SQLException se){
            throw new DeleteRouteException(se.getMessage());
        }catch(Exception e){
            throw new DeleteRouteException(e.getMessage());
        }
    }*/


}
