package dk.runs.runners.repositories;

import dk.runs.runners.entities.Route;
import dk.runs.runners.entities.Run;
import dk.runs.runners.usecases.RouteRepository;
import dk.runs.runners.usecases.RunRepository;

import java.sql.*;

public class RunRepositoryImpl implements RunRepository {

    private final String url = "jdbc:mysql://ec2-52-30-211-3.eu-west-1.compute.amazonaws.com/s133967?"
            + "user=s133967&password=8JPOJuQcgUpUVIVHY4S2H";

    private RouteRepository routeRepository;

    public void setRouteRepository(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    @Override
    public void createRun(Run run, String routeId, String participantId) {
        String sqlQuery = "INSERT INTO run VALUES ( ? , ? , ? )";
        executeCreateRunQuery(sqlQuery,run, participantId);
    }
    private void executeCreateRunQuery(String sql, Run run, String paticipantId) throws CreateRunException {
        try(Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt= conn.prepareStatement(sql)){
            pstmt.setString(1, run.getId());
            pstmt.setString(2, run.getRoute().getId());
            pstmt.setString(3, paticipantId);
            pstmt.executeUpdate();
        }catch (SQLIntegrityConstraintViolationException e){
            throw new RunIdDuplicationException(e.getMessage());
        }catch(SQLException se){
            se.printStackTrace();
            throw new CreateRunException(se.getMessage());
        }catch(Exception e){
            e.printStackTrace();
            throw new CreateRunException(e.getMessage());
        }
    }


    @Override
    public Run getRun(String runId) {
        String sqlQuery = "SELECT route_id FROM run WHERE run.id = ?";

        String routeId = executeGetRouteIdQuery(sqlQuery, runId);

        Route route = routeRepository.getRoute(routeId);

        Run run = new Run();
        run.setId(runId);
        run.setRoute(route);

        return run;
    }

    @Override
    public void deleteRun(String runId) {
        String sqlQuery = "DELETE FROM run WHERE run.id = ?";
        executeDeleteRunQuery(sqlQuery, runId);
    }

    @Override
    public void addCheckpointIfValid(String runId, double currentX, double currentY, int precision) {
        String sqlQuery = "INSERT INTO checkpoint (run_id, waypoint_index, visited_timestamp)" +
                          " SELECT run.id, `index`, now()" +
                          " FROM waypoint" +
                          " JOIN run ON waypoint.route_id = run.route_id" +
                          " WHERE run.id = ? AND " +
                          " ST_Distance(spatial_point, ST_GeomFromText( ? )) <= ?";
        executeInsertCheckpointQuery(sqlQuery, runId, currentX, currentY, precision);
    }

    private void executeInsertCheckpointQuery(String sqlQuery, String runId, double currentX, double currentY, int precision) {
        try(Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt= conn.prepareStatement(sqlQuery)){
            pstmt.setString(1, runId);
            pstmt.setString(2, "Point("+currentX+" "+currentY+")");
            pstmt.setInt(3, precision);
            pstmt.executeUpdate();
        }catch(SQLException se){
            se.printStackTrace();
            throw new DeleteRunException(se.getMessage());
        }catch(Exception e){
            e.printStackTrace();
            throw new DeleteRunException(e.getMessage());
        }
    }

    private void executeDeleteRunQuery(String sqlQuery, String runId) {
        try(Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt= conn.prepareStatement(sqlQuery)){
            pstmt.setString(1, runId);
            pstmt.executeUpdate();
        }catch(SQLException se){
            se.printStackTrace();
            throw new DeleteRunException(se.getMessage());
        }catch(Exception e){
            e.printStackTrace();
            throw new DeleteRunException(e.getMessage());
        }
    }

    private String executeGetRouteIdQuery(String sqlQuery, String runId) {
        String routeId;

        try(Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt= conn.prepareStatement(sqlQuery)){
            pstmt.setString(1, runId);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            routeId = rs.getString("route_id");
        }catch (SQLIntegrityConstraintViolationException e){
            throw new RunIdDuplicationException(e.getMessage());
        }catch(SQLException se){
            se.printStackTrace();
            throw new CreateRunException(se.getMessage());
        }catch(Exception e){
            e.printStackTrace();
            throw new CreateRunException(e.getMessage());
        }
        return routeId;
    }


}
