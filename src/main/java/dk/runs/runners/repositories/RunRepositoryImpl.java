package dk.runs.runners.repositories;

import dk.runs.runners.entities.Checkpoint;
import dk.runs.runners.entities.Route;
import dk.runs.runners.entities.Run;
import dk.runs.runners.entities.WayPoint;
import dk.runs.runners.usecases.RouteRepository;
import dk.runs.runners.usecases.RunRepository;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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
    public Run getRunWithAllCheckpoints(String runId) {
        String sqlQuery = "SELECT route_id FROM run WHERE run.id = ?";

        String routeId = executeGetRouteIdQuery(sqlQuery, runId);

        Route route = routeRepository.getRoute(routeId);

        Run run = new Run();
        run.setId(runId);
        run.setRoute(route);

        List<Checkpoint> checkpoints = getCheckpoints(runId, route.getWayPoints());

        run.setCheckpoints(checkpoints);

        return run;
    }

    private List<Checkpoint> getLatestCheckpoints(String runId, List<WayPoint> waypoints) {
        String sql = "SELECT MAX(visited_timestamp) AS visited_timestamp" +
                " FROM checkpoint " +
                " WHERE checkpoint.run_id = ? AND checkpoint.waypoint_index = ?" +
                " GROUP BY checkpoint.waypoint_index";
        return executeGetCheckpointsQuery(sql, new LinkedList<Checkpoint>(), runId, waypoints);
    }

    private List<Checkpoint> getCheckpoints(String runId, List<WayPoint> waypoints) {
        String sql = "SELECT visited_timestamp" +
                " FROM checkpoint " +
                " WHERE checkpoint.run_id = ? AND checkpoint.waypoint_index = ?";
        return executeGetCheckpointsQuery(sql, new LinkedList<Checkpoint>(), runId, waypoints);
    }

    private List<Checkpoint> executeGetCheckpointsQuery(String sql, LinkedList<Checkpoint> checkpoints, String runId, List<WayPoint> waypoints) {
        Connection conn = null;
        PreparedStatement pstmtCheckpoint = null;
        try{
            conn = DriverManager.getConnection(url);
            conn.setAutoCommit(false);

            pstmtCheckpoint = conn.prepareStatement(sql);
            pstmtCheckpoint.setString(1, runId);

            for(WayPoint wayPoint: waypoints){
                pstmtCheckpoint.setInt(2, wayPoint.getIndex());
                ResultSet rs = pstmtCheckpoint.executeQuery();
                while(rs.next()){
                    long timestamp= rs.getTimestamp("visited_timestamp").getTime();
                    Checkpoint checkpoint = new Checkpoint(wayPoint);
                    checkpoint.setVisitedTimestamp(timestamp);
                    checkpoints.add(checkpoint);
                }
                if(rs != null){  rs.close(); }
            }

            conn.commit(); //TODO do we need commit here?
        }catch(SQLException se){
            try {
                conn.rollback();
                throw new CheckpointException(se.getMessage());
            }catch (SQLException rollBackException){
                throw new CheckpointException(rollBackException.getMessage());
            }
        }catch(Exception e){
            try {
                conn.rollback();
                throw new CheckpointException(e.getMessage());
            }catch (SQLException rollBackException){
                throw new CheckpointException(rollBackException.getMessage());
            }
        }finally {
            try {
                if(pstmtCheckpoint != null) pstmtCheckpoint.close();
                if(conn != null) conn.close();
            } catch (SQLException e) {
                throw new CheckpointException(e.getMessage());
            }
        }

        return checkpoints;
    }


    @Override
    public void deleteRun(String runId) {
        String runSqlQuery = "DELETE FROM run WHERE run.id = ?";
        String checkpointsSqlQuery = "DELETE FROM checkpoint WHERE run_id = ?";

        executeDeleteRunQuery(runSqlQuery, checkpointsSqlQuery, runId);
    }

    @Override
    public void addCheckpointIfValid(String runId, double currentX, double currentY, int precision) {
        waitToAvoidDuplicateTimestamps(500);
        String sqlQuery = "INSERT INTO checkpoint (run_id, waypoint_index, visited_timestamp)" +
                          " SELECT run.id, `index`, now()" +
                          " FROM waypoint" +
                          " JOIN run ON waypoint.route_id = run.route_id" +
                          " WHERE run.id = ? AND " +
                          " ST_Distance(spatial_point, ST_GeomFromText( ? )) <= ?";
        executeInsertCheckpointQuery(sqlQuery, runId, currentX, currentY, precision);
    }

    private void waitToAvoidDuplicateTimestamps(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) { }
    }

    @Override
    public Run getRunWithLastCheckpoints(String runId) {

        String sqlQuery = "SELECT route_id FROM run WHERE run.id = ?";

        String routeId = executeGetRouteIdQuery(sqlQuery, runId);

        Route route = routeRepository.getRoute(routeId);

        Run run = new Run();
        run.setId(runId);
        run.setRoute(route);

        List<Checkpoint> checkpoints = getLatestCheckpoints(runId, route.getWayPoints());

        run.setCheckpoints(checkpoints);

        return run;
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

    private void executeDeleteRunQuery(String runSqlQuery, String checkpointsSqlQuery, String runId) {
        Connection conn = null;
        PreparedStatement pstmtRun = null;
        PreparedStatement pstmtCheckpoint = null;
        try{
            conn = DriverManager.getConnection(url);
            conn.setAutoCommit(false);

            pstmtCheckpoint = conn.prepareStatement(checkpointsSqlQuery);
            pstmtCheckpoint.setString(1, runId);
            pstmtCheckpoint.executeUpdate();

            pstmtRun = conn.prepareStatement(runSqlQuery);
            pstmtRun.setString(1, runId);
            pstmtRun.executeUpdate();

            conn.commit();
        }catch(SQLException se){
            try {
                conn.rollback();
                throw new DeleteRunException(se.getMessage());
            }catch (SQLException rollBackException){
                throw new DeleteRunException(rollBackException.getMessage());
            }
        }catch(Exception e){
            try {
                conn.rollback();
                throw new DeleteRunException(e.getMessage());
            }catch (SQLException rollBackException){
                throw new DeleteRunException(rollBackException.getMessage());
            }
        }finally {
            try {
                if(pstmtRun != null) pstmtRun.close();
                if(pstmtCheckpoint != null) pstmtCheckpoint.close();
                if(conn != null) conn.close();
            } catch (SQLException e) {
                throw new DeleteRunException(e.getMessage());
            }
        }
    }

    private String executeGetRouteIdQuery(String sqlQuery, String runId) {
        String routeId = null;
        try(Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt= conn.prepareStatement(sqlQuery)){
            pstmt.setString(1, runId);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                routeId = rs.getString("route_id");
            }
            if(rs != null){  rs.close(); }
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
