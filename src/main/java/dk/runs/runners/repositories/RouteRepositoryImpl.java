package dk.runs.runners.repositories;

import dk.runs.runners.entities.Route;
import dk.runs.runners.usecases.RouteRepository;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class RouteRepositoryImpl implements RouteRepository {

    private final String url = "jdbc:mysql://ec2-52-30-211-3.eu-west-1.compute.amazonaws.com/s133967?"
            + "user=s133967&password=8JPOJuQcgUpUVIVHY4S2H";

    @Override
    public void createRoute(Route route, String creatorId) throws CreateRouteException {
        String sql = "INSERT INTO route (id, creator_id, title, location, date, distance, duration, description, status)" +
                "VALUES ( ? , ? , ? , ? , ? , ? , ? , ? , ? )";
              executeCreateRouteQuery(sql, route, creatorId);
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
    public Route getRoute(String id) throws RouteNotFoundException {
        String sql = "SELECT * " +
                " FROM route" +
                " WHERE route.id = ?";
        return executeGetRouteQuery(sql,new Route(id));
    }

    @Override
    public void updateRoute(Route route) {
        String sql = "UPDATE route" +
                " SET title = ?, location = ?, date = ?, distance = ?, duration = ?, description = ?, status = ?" +
                " WHERE id = ?";
        executeUpdateRouteQuery(sql, route);
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
            rs.close();
        }catch(SQLException se){
            throw new RouteNotFoundException(se.getMessage());
        }catch(Exception e){
            throw new RouteNotFoundException(e.getMessage());
        }
        return route;
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

    public void deleteRoute(String id) throws DeleteRouteException {
        String sql = "DELETE FROM route WHERE id = ?";
        executeDeleteRouteQuery(sql, id);
    }

    private void executeDeleteRouteQuery(String sql, String param01) throws DeleteRouteException {
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
