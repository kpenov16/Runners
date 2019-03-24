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
    public void createRun(Run run, String creatorId) throws CreateRunException {
        String sql = "INSERT INTO run (id, creator_id, title, location, date, distance, duration, description, status)" +
                "VALUES ( ? , ? , ? , ? , ? , ? , ? , ? , ? )";
              executeCreateRunQuery(sql, run, creatorId);

/*        String sql = "INSERT INTO run (id, location)" +
                "VALUES ( ? , ? )";
        executeCreateRunQuery(sql, run.getId(), run.getLocation());*/
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
    public Run getRun(String id) throws RunNotFoundException {
        String sql = "SELECT * " +
                " FROM run" +
                " WHERE run.id = ?";

        /*
            * private String id;
            private String title;
            private String location;
            private Date date;
            private int distance;
            private long duration;
            private String description;
            private String status = "active";
                    * */
        return executeGetRunQuery(sql,new Run(id));
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
                Run run = new Run(rs.getString(1));
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

    private void executeUpdateRunQuery(String sql, String param01, String param02) throws UpdateRunException {
        try(Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt= conn.prepareStatement(sql)){
            pstmt.setString(1, param01);
            pstmt.setString(2, param02);
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
            pstmt.setString(1, run.getId());
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            run.setTitle(rs.getString("title"));
            run.setLocation(rs.getString("location"));
            run.setDate( new java.util.Date( rs.getLong("date") ));
            run.setDistance(rs.getInt("distance"));
            run.setDuration(rs.getLong("duration"));
            run.setDescription(rs.getString("description"));
            run.setStatus(rs.getString("status"));
            rs.close();
        }catch(SQLException se){
            throw new RunNotFoundException(se.getMessage());
        }catch(Exception e){
            throw new RunNotFoundException(e.getMessage());
        }
        return run;
    }

    private void executeCreateRunQuery(String sql, Run run, String creatorId) throws CreateRunException {
        try(Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt= conn.prepareStatement(sql)){
            pstmt.setString(1, run.getId());
            pstmt.setString(2, creatorId);
            pstmt.setString(3, run.getTitle());
            pstmt.setString(4, run.getLocation());
            pstmt.setLong(5, run.getDate().getTime() );
            pstmt.setInt(6, run.getDistance());
            pstmt.setLong(7, run.getDuration() );
            pstmt.setString(8, run.getDescription() );
            pstmt.setString(9, run.getStatus() );

            pstmt.executeUpdate();
        }catch(SQLException se){
            se.printStackTrace();
            throw new CreateRunException(se.getMessage());
        }catch(Exception e){
            e.printStackTrace();
            throw new CreateRunException(e.getMessage());
        }
    }

    public void deleteRun(String id) throws DeleteRunException {
        String sql = "DELETE FROM run WHERE id = ?";
        executeDeleteRunQuery(sql, id);
    }

    private void executeDeleteRunQuery(String sql, String param01) throws DeleteRunException {
        try(Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt= conn.prepareStatement(sql)){
            pstmt.setString(1, param01);
            pstmt.executeUpdate();
        }catch(SQLException se){
            throw new DeleteRunException(se.getMessage());
        }catch(Exception e){
            throw new DeleteRunException(e.getMessage());
        }
    }
}
