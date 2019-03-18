package dk.runs.runners.repositories;

import dk.runs.runners.entities.Run;
import dk.runs.runners.usecases.RunRepository;

import java.math.BigInteger;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class RunRepositoryImpl implements RunRepository {

    private final String url = "jdbc:mysql://ec2-52-30-211-3.eu-west-1.compute.amazonaws.com/s133967?"
            + "user=s133967&password=8JPOJuQcgUpUVIVHY4S2H";


    @Override
    public Run createRun(Run run, long creatorId) throws CreateRunException {
        String sql = "INSERT INTO run (creator_id, title, location, date, distance, duration, description, status)" +
                "VALUES ( ? , ? , ? , ? , ? , ? , ? , ? )";
              executeCreateRunQuery(sql, run, creatorId);
        return queryLastCreatedRun(creatorId);

/*        String sql = "INSERT INTO run (id, location)" +
                "VALUES ( ? , ? )";
        executeCreateRunQuery(sql, run.getId(), run.getLocation());*/
    }

    private Run queryLastCreatedRun(long creatorId) {
        /*String sql = "SELECT run.id AS id, run.location AS location" +
                     " FROM run" +
                     " WHERE run.id in (SELECT MAX(run.id) AS id" +
                                      " FROM run" +
                                      " WHERE run.creator_id = ?)";*/
        String sql = "SELECT MAX(run.id) AS id" +
                     " FROM run" +
                     " WHERE run.creator_id = ?";
        Run run = new Run();
        run.setId(executeGetIdQuery(sql, creatorId));
        executeGetIdQuery(sql, creatorId);
        return executeGetRunQuery(sql,run);
    }

    private long executeGetIdQuery(String sql, long creatorId) throws RunNotFoundException {
        long id  = -1 ;
        try(Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt= conn.prepareStatement(sql)){
            pstmt.setLong(1, creatorId);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            id = rs.getLong("id");
            rs.close();
        }catch(SQLException se){
            throw new RunNotFoundException(se.getMessage());
        }catch(Exception e){
            throw new RunNotFoundException(e.getMessage());
        }
        return id;
    }

    @Override
    public Run getRun(BigInteger id) throws RunNotFoundException {
        String sql = "SELECT run.id AS id, run.location AS location" +
                " FROM run" +
                " WHERE run.id = ?";
        Run run = executeGetRunQuery(sql,new Run(id));
        return run;
    }

    @Override
    public void updateRun(Run run) {
        String sql = "UPDATE run" +
                " SET location = ?" +
                " WHERE id = ?";
        executeUpdateRunQuery(sql, run.getLocation(), run.getId());
    }

    @Override
    public List<Run> getRunsList() {
        String sql = "SELECT run.id AS id, run.location AS location " +
                    "FROM run"; //TODO select only comming runs. That is where dato > now
        return executeGetRunsQuery(sql);
    }

    private List<Run> executeGetRunsQuery(String sql) {

        List<Run> runs = new LinkedList<>();

        try(Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()){

            while(rs.next()){
                Run run = new Run(rs.getInt(1));
                run.setLocation(rs.getString(2));
                runs.add(run);
            }
        } catch (SQLException se){
            throw new GetRunsException(se.getMessage());
        } catch (Exception e){
            throw new GetRunsException(e.getMessage());
        }
        return runs;
    }

    private void executeUpdateRunQuery(String sql, String param01, int param02) throws UpdateRunException {
        try(Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt= conn.prepareStatement(sql)){
            pstmt.setString(1, param01);
            pstmt.setInt(2, param02);
            pstmt.executeUpdate();
        }catch(SQLException se){
            throw new UpdateRunException(se.getMessage());
        }catch(Exception e){
            throw new UpdateRunException(e.getMessage());
        }
    }

    private Run executeGetRunQuery(String sql, Run run) throws RunNotFoundException {
        try(Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt= conn.prepareStatement(sql)){
            pstmt.setLong(1, run.getId());
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            String location = rs.getString("location");
            run.setLocation(location);
            rs.close();
        }catch(SQLException se){
            throw new RunNotFoundException(se.getMessage());
        }catch(Exception e){
            throw new RunNotFoundException(e.getMessage());
        }
        return run;
    }

    private void executeCreateRunQuery(String sql, Run run, long creatorId) throws CreateRunException {
        /*String sql = "INSERT INTO run (title, location, date, distance, duration, description, status)" +
                "VALUES ( ? , ? , ? , ? , ? , ? , ? )";*/
        try(Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt= conn.prepareStatement(sql)){
            pstmt.setLong(1, creatorId);
            pstmt.setString(2, run.getTitle());
            pstmt.setString(3, run.getLocation());
            pstmt.setDate(4, new Date( run.getDate().getTime()) );
            pstmt.setInt(5, run.getDistance());
            pstmt.setLong(6, run.getDuration() );
            pstmt.setString(7, run.getDescription() );
            pstmt.setString(8, run.getStatus() );

            pstmt.executeUpdate();
        }catch(SQLException se){
            throw new CreateRunException(se.getMessage());
        }catch(Exception e){
            throw new CreateRunException(e.getMessage());
        }

    }

    public void deleteRun(BigInteger id) throws DeleteRunException {
        String sql = "DELETE FROM run WHERE id = ?";
        executeDeleteRunQuery(sql, id);
    }

    private void executeDeleteRunQuery(String sql, int param01) throws DeleteRunException {
        try(Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt= conn.prepareStatement(sql)){
            pstmt.setInt(1, param01);
            pstmt.executeUpdate();
        }catch(SQLException se){
            throw new DeleteRunException(se.getMessage());
        }catch(Exception e){
            throw new DeleteRunException(e.getMessage());
        }
    }
}
