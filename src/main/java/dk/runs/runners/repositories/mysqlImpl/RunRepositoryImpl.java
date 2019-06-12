package dk.runs.runners.repositories.mysqlImpl;

import dk.runs.runners.datasourceconfig.DataSource;
import dk.runs.runners.entities.Checkpoint;
import dk.runs.runners.entities.Route;
import dk.runs.runners.entities.Run;
import dk.runs.runners.entities.WayPoint;
import dk.runs.runners.services.interfaceRepositories.RouteRepository;
import dk.runs.runners.services.interfaceRepositories.RunRepository;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class RunRepositoryImpl implements RunRepository {

    public static final String RUN_ID_IS_NOT_DEFINED = "Run id is not defined";
    public static final String RUN_WITH_ID_S_IS_MISSING_ROUTE_OBJECT = "Run with id: %s is missing route object.";
    public static final String ROUTE_ID_S_HAS_REACHED_MAX_PARTICIPANTS_D = "Route Id: %s has reached max participants: %d";
    private final String url = "jdbc:mysql://ec2-52-30-211-3.eu-west-1.compute.amazonaws.com/s133967?"
            + "user=s133967&password=8JPOJuQcgUpUVIVHY4S2H";

    private RouteRepository routeRepository;

    public RunRepositoryImpl(RouteRepository routeRepository){
        this.routeRepository = routeRepository;
    }

    public void setRouteRepository(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    @Override
    public void createRun(Run run, String participantId) {
        validate(run);
        String sqlQuery = "INSERT INTO run (id, route_id, user_id) VALUES ( ? , ? , ? )";
        executeCreateRunQuery(sqlQuery,run, participantId);
    }

    private void validate(Run run) {
        if(run.getId()==null){
            throw new RunValidationException(RUN_ID_IS_NOT_DEFINED);
        }else if(run.getRoute()==null){
            throw new RunValidationException( String.format(RUN_WITH_ID_S_IS_MISSING_ROUTE_OBJECT, run.getId()) );
        }
    }

    private void executeCreateRunQuery(String sql, Run run, String paticipantId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try{
            conn = DataSource.getConnection();
            conn.setAutoCommit(false);

            pstmt = conn.prepareStatement(sql);

            Route route = routeRepository.getRoute( run.getRoute().getId() );
            if(route.getNumberOfParticipants() == route.getMaxParticipants() ){
                if(conn!=null){
                    conn.rollback();
                }
                throw new MaxParticipansReachedException(
                  String.format(ROUTE_ID_S_HAS_REACHED_MAX_PARTICIPANTS_D, route.getId(), route.getMaxParticipants())
                );
            }else {
                pstmt.setString(1, run.getId());
                pstmt.setString(2, run.getRoute().getId());
                pstmt.setString(3, paticipantId);
                int rowsEffected = pstmt.executeUpdate();
                conn.commit();
            }
        }catch (Throwable t){
            handleCreationExceptions(t, conn);
        }finally {
            try {
                if(pstmt != null) pstmt.close();
                if(conn != null) conn.close();
            } catch (SQLException e) {
                throw new CreateRunException(e.getMessage());
            }
        }
    }

    private void handleCreationExceptions(Throwable t, Connection conn) {
        if(t instanceof MaxParticipansReachedException){
            MaxParticipansReachedException e = (MaxParticipansReachedException)t;
            throw new MaxParticipansReachedException(e.getMessage());
        }else if(t instanceof SQLIntegrityConstraintViolationException){
            SQLIntegrityConstraintViolationException e =
                    (SQLIntegrityConstraintViolationException)t;
            try {
                if(conn!=null){
                    conn.rollback();
                }
                final String MSG = e.getMessage();
                if (MSG.contains("FOREIGN KEY (`route_id`)")){
                    throw new UnknownRouteException(MSG);
                }else if(MSG.contains("FOREIGN KEY (`user_id`)")){
                    throw new UnknownUserException(MSG);
                }
                throw new RunIdDuplicationException(MSG);
            }catch (SQLException rollBackException){
                rollBackException.printStackTrace();
            }
        }else if(t instanceof RouteRepository.RouteNotFoundException){
            if(conn!=null){
                try {
                    conn.rollback();
                    RouteRepository.RouteNotFoundException e =(RouteRepository.RouteNotFoundException)t;
                    throw new UnknownRouteException(e.getMessage());
                } catch (SQLException rollBackException) {
                    rollBackException.printStackTrace();
                }
            }
        }else if(t instanceof SQLException){
            SQLException se = (SQLException)t;
            try {
                if(conn!=null){
                    conn.rollback();
                }
                se.printStackTrace();
                throw new CreateRunException(se.getMessage());
            }catch (SQLException rollBackException){
                throw new CreateRunException(rollBackException.getMessage());
            }
        }else if(t instanceof Exception){
            Exception e = (SQLException)t;
            try {
                if(conn!=null){
                    conn.rollback();
                }
                e.printStackTrace();
                throw new CreateRunException(e.getMessage());
            }catch (SQLException rollBackException){
                throw new CreateRunException(rollBackException.getMessage());
            }
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
            conn = DataSource.getConnection();
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
        String sqlQuery = "INSERT INTO checkpoint (run_id, waypoint_index, visited_timestamp)" +
                          " SELECT run.id, `index`, now(6)" +
                          " FROM waypoint" +
                          " JOIN run ON waypoint.route_id = run.route_id" +
                          " WHERE run.id = ? AND " +
                          " ST_Distance(spatial_point, ST_GeomFromText( ? )) <= ?";
        executeInsertCheckpointQuery(sqlQuery, runId, currentX, currentY, precision);
    }

    @Override
    public Run getRunWithLastCheckpoints(String runId) {
        String sqlQueryRouteId = "SELECT route_id FROM run WHERE run.id = ?";
        String routeId = executeGetRouteIdQuery(sqlQueryRouteId, runId);
        String sqlQueryTime = "SELECT start_time, end_time FROM run WHERE run.id = ?";
        Run run = executeGetRun(sqlQueryTime, runId);
        //Route route = routeRepository.getRoute(runId);
        Route route = routeRepository.getRoute(routeId);
        //Run run = new Run();
        run.setId(runId);
        run.setRoute(route);
        List<Checkpoint> checkpoints = getLatestCheckpoints(runId, route.getWayPoints());
        run.setCheckpoints(checkpoints);
        return run;


    }

    private Run executeGetRun(String sqlQuery, String runId) {
        Run run = new Run();
        try(Connection conn = DataSource.getConnection();
            PreparedStatement pstmt= conn.prepareStatement(sqlQuery)){
            pstmt.setString(1, runId);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                run.setStartTime(new java.util.Date(rs.getLong("start_time")));
                run.setEndTime(new java.util.Date(rs.getLong("end_time")));
            }
            if(rs != null){  rs.close(); }
        }catch(SQLException se){
            se.printStackTrace();
            throw new GetRunsException(se.getMessage());
        }catch(Exception e){
            e.printStackTrace();
            throw new GetRunsException(e.getMessage());
        }
        return run;
    }

    private void executeInsertCheckpointQuery(String sqlQuery, String runId, double currentX, double currentY, int precision) {
        try(Connection conn = DataSource.getConnection();
            PreparedStatement pstmt= conn.prepareStatement(sqlQuery)){
            pstmt.setString(1, runId);
            pstmt.setString(2, "Point("+currentX+" "+currentY+")");
            pstmt.setInt(3, precision);
            pstmt.executeUpdate();
        }catch(SQLException se){
            se.printStackTrace();
            throw new CheckpointException(se.getMessage());
        }catch(Exception e){
            e.printStackTrace();
            throw new CheckpointException(e.getMessage());
        }
    }

    private void executeDeleteRunQuery(String runSqlQuery, String checkpointsSqlQuery, String runId) {
        Connection conn = null;
        PreparedStatement pstmtRun = null;
        PreparedStatement pstmtCheckpoint = null;
        try{
            conn = DataSource.getConnection();
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
        try(Connection conn = DataSource.getConnection();
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

    @Override
    public List<WayPoint> getMissingWaypoints(String runId) {
        List<WayPoint> wayPoints;
        String sqlQuery = "SELECT ST_X(spatial_point) AS X, ST_Y(spatial_point) AS Y, `index` " +
                "FROM waypoint JOIN run " +
                "ON waypoint.route_id = run.route_id " +
                "LEFT JOIN checkpoint ON waypoint.`index` = checkpoint.waypoint_index AND checkpoint.run_id = run.id " +
                "WHERE run.id = ? AND " +
                "visited_timestamp IS NULL";
        wayPoints = executeGetMissingWaypointsQuery(sqlQuery, runId);
        return wayPoints;
    }

    @Override
    public List<Run> getRuns(String userId) throws GetRunsException {
        List<Run> runs;
        String sqlQuery = "SELECT id FROM run WHERE user_id = ?";
        runs = executeGetRunsQuery(sqlQuery, userId);
        return runs;
    }

    @Override
    public void updateRun(Run runUpdated) {
        String sqlQuery = "UPDATE run SET start_time = ?, end_time = ? WHERE id = ?";
        executeUpdateRun(sqlQuery, runUpdated);
    }

    private void executeUpdateRun(String sqlQuery, Run runUpdated) {
        try(Connection conn = DataSource.getConnection();
            PreparedStatement pstmt= conn.prepareStatement(sqlQuery)){
            pstmt.setLong(1, runUpdated.getStartTime().getTime());
            pstmt.setLong(2, runUpdated.getEndTime().getTime());
            pstmt.setString(3, runUpdated.getId());
            int rowsEffected = pstmt.executeUpdate();
            if(rowsEffected < 1){
              throw new RunNotFound("Run with id:"+ runUpdated.getId() + " not exists.");
            }

        }catch(SQLException se){
            se.printStackTrace();
            throw new UpdateRunException(se.getMessage());
        }catch(Exception e){
            e.printStackTrace();
            throw new UpdateRunException(e.getMessage());
        }

    }

    private List<Run> executeGetRunsQuery(String sqlQuery, String userId) {
        List<Run> runs = new LinkedList<>();

        try(Connection conn = DataSource.getConnection();
            PreparedStatement pstmt= conn.prepareStatement(sqlQuery)){
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                String runId = rs.getString("id");
                Run run =getRunWithLastCheckpoints(runId);
                runs.add(run);
            }
            if(rs != null){  rs.close(); }
        }catch(SQLException se){
            se.printStackTrace();
            throw new GetRunsException(se.getMessage());
        }catch(Exception e){
            e.printStackTrace();
            throw new GetRunsException(e.getMessage());
        }

        return runs;
    }

    private List<WayPoint> executeGetMissingWaypointsQuery(String sqlQuery, String runId) {
        List<WayPoint> wayPoints = new LinkedList<>();
        try(Connection conn = DataSource.getConnection();
            PreparedStatement pstmt= conn.prepareStatement(sqlQuery)){
            pstmt.setString(1, runId);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                WayPoint wayPoint = new WayPoint();
                wayPoint.setX(rs.getDouble("X"));
                wayPoint.setY(rs.getDouble("Y"));
                wayPoint.setIndex(rs.getInt("index"));
                wayPoints.add(wayPoint);
            }
            if(rs != null){  rs.close(); }
        }catch(SQLException se){
            se.printStackTrace();
            throw new GetMissingWaypointException(se.getMessage());
        }catch(Exception e){
            e.printStackTrace();
            throw new GetMissingWaypointException(e.getMessage());
        }
        return wayPoints;
    }
}
