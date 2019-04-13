package dk.runs.runners.repositories;

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
        String routeSql = "INSERT INTO route (id, creator_id, title, location, date, distance, duration, description, status, max_participants, min_participants)" +
                "VALUES ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ?)";

        String waypointSql = "INSERT INTO waypoint (`index`, route_id, spatial_point)" +
                "VALUES ( ? , ? , ST_GeomFromText( ? , ? ))";
        executeCreateRouteQueryAsUnitOfWork(routeSql, waypointSql, route, creatorId);
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
        route.setWayPoints(getWaypoints(route.getId()));
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
        }
        return routes;
    }

    private List<WayPoint> getWaypoints(String routeId) {
        String sql = "SELECT `index`, ST_X(spatial_point) AS X, ST_Y(spatial_point) AS Y " +
                " FROM waypoint" +
                " WHERE waypoint.route_id = ?";
        return executeGetWaypointsQuery(sql, new LinkedList<WayPoint>(), routeId);
    }

    @Override
    public void updateRoute(Route route) {
        String deleteWaypointsSql = "DELETE FROM waypoint WHERE route_id = ?";;

        String routeSql = "UPDATE route" +
                     " SET title = ?, location = ?, date = ?, distance = ?, duration = ?,"+
                     " description = ?, status = ?, max_participants = ?, min_participants = ?" +
                " WHERE id = ?";

        String waypointSql = "INSERT INTO waypoint (`index`, route_id, spatial_point)" +
                "VALUES ( ? , ? , ST_GeomFromText( ? , ? ))";
        executeUpdateRouteQueryAsUnitOfWork(deleteWaypointsSql, routeSql, waypointSql, route);
    }

    private void executeUpdateRouteQueryAsUnitOfWork(String deleteWaypointsSql, String routeSql, String waypointSql, Route route) throws UpdateRouteException {
        Connection conn = null;
        PreparedStatement pstmtRoute = null;
        PreparedStatement pstmtWaypoint = null;
        PreparedStatement pstmtDeleteWaypoint = null;
        try{
            conn = DriverManager.getConnection(url);
            conn.setAutoCommit(false);

            pstmtDeleteWaypoint = conn.prepareStatement(deleteWaypointsSql);
            pstmtRoute = conn.prepareStatement(routeSql);
            pstmtWaypoint = conn.prepareStatement(waypointSql);

            //delete waypoints
            pstmtDeleteWaypoint.setString(1, route.getId());
            pstmtDeleteWaypoint.executeUpdate();

            //update route
            pstmtRoute.setString(1, route.getTitle());
            pstmtRoute.setString(2, route.getLocation());
            pstmtRoute.setLong(3, route.getDate().getTime() );
            pstmtRoute.setInt(4, route.getDistance());
            pstmtRoute.setLong(5, route.getDuration() );
            pstmtRoute.setString(6, route.getDescription() );
            pstmtRoute.setString(7, route.getStatus() );
            pstmtRoute.setInt(8, route.getMaxParticipants() );
            pstmtRoute.setInt(9, route.getMinParticipants() );
            pstmtRoute.setString(10, route.getId());
            int rowsEffected = pstmtRoute.executeUpdate();

            if(rowsEffected == 1){
                //create waypoints
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
        String sql = "SELECT route.id AS id, route.location AS location " +
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
                route.setLocation(rs.getString(2));
                routes.add(route);
            }
        } catch (SQLException se){
            throw new GetRoutesException(se.getMessage());
        } catch (Exception e){
            throw new GetRoutesException(e.getMessage());
        }
        return routes;
    }

    private void executeUpdateRouteQuery(String sql, Route route) throws UpdateRouteException {
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
    }

    private List<Route> executeGetRouteQuery(String sql, String creatorId) throws RouteNotFoundException {
        List<Route> routes = new LinkedList<>();
        try(Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt= conn.prepareStatement(sql)){
            pstmt.setString(1, creatorId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                Route route = new Route(rs.getString("id"));
                route.setTitle(rs.getString("title"));
                route.setLocation(rs.getString("location"));
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
            route.setLocation(rs.getString("location"));
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

    private void executeCreateRouteQueryAsUnitOfWork(String routeSql, String waypointSql, Route route, String creatorId) throws CreateRouteException {
        Connection conn = null;
        PreparedStatement pstmtRoute = null;
        PreparedStatement pstmtWaypoint = null;
        try{
            conn = DriverManager.getConnection(url);
            conn.setAutoCommit(false);

            pstmtRoute = conn.prepareStatement(routeSql);
            pstmtWaypoint = conn.prepareStatement(waypointSql);


            pstmtRoute.setString(1, route.getId());
            pstmtRoute.setString(2, creatorId);
            pstmtRoute.setString(3, route.getTitle());
            pstmtRoute.setString(4, route.getLocation());
            pstmtRoute.setLong(5, route.getDate().getTime() );
            pstmtRoute.setInt(6, route.getDistance());
            pstmtRoute.setLong(7, route.getDuration() );
            pstmtRoute.setString(8, route.getDescription() );
            pstmtRoute.setString(9, route.getStatus() );
            pstmtRoute.setInt(10, route.getMaxParticipants() );
            pstmtRoute.setInt(11, route.getMinParticipants() );
            int rowsEffected = pstmtRoute.executeUpdate();

            if(rowsEffected == 1){
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
    }

    public void deleteRouteOld(String id) throws DeleteRouteException {
        String sql = "DELETE FROM route WHERE id = ?";
        executeDeleteRouteQueryOld(sql, id);
    }

    public void deleteRoute(String routeId) throws DeleteRouteException {
        String routeSql = "DELETE FROM route WHERE id = ?";
        String waypointSql = "DELETE FROM waypoint WHERE route_id = ?";
        executeDeleteRouteQuery(routeSql, waypointSql, routeId);
    }

    private void executeDeleteRouteQuery(String routeSql, String waypointSql, String routeId) throws DeleteRouteException {
        Connection conn = null;
        PreparedStatement pstmtRoute = null;
        PreparedStatement pstmtWaypoint = null;
        try{
            conn = DriverManager.getConnection(url);
            conn.setAutoCommit(false);

            pstmtWaypoint = conn.prepareStatement(waypointSql);
            pstmtWaypoint.setString(1, routeId);
            pstmtWaypoint.executeUpdate();

            pstmtRoute = conn.prepareStatement(routeSql);
            pstmtRoute.setString(1, routeId);
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

    private void executeDeleteRouteQueryOld(String sql, String param01) throws DeleteRouteException {
        try(Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt= conn.prepareStatement(sql)){
            pstmt.setString(1, param01);
            pstmt.executeUpdate();
        }catch(SQLException se){
            throw new DeleteRouteException(se.getMessage());
        }catch(Exception e){
            throw new DeleteRouteException(e.getMessage());
        }
    }


}
