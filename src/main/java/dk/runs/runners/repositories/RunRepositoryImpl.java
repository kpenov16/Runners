package dk.runs.runners.repositories;

import dk.runs.runners.entities.Run;
import dk.runs.runners.usecases.RunRepository;

import java.sql.*;

public class RunRepositoryImpl implements RunRepository {

    private String url = "jdbc:mysql://ec2-52-30-211-3.eu-west-1.compute.amazonaws.com/s133967?"
            + "user=s133967&password=8JPOJuQcgUpUVIVHY4S2H";

//    private Connection createConnection() {
//        try {
//            return DriverManager.getConnection("jdbc:mysql://ec2-52-30-211-3.eu-west-1.compute.amazonaws.com/s133967?"
//                    + "user=s133967&password=8JPOJuQcgUpUVIVHY4S2H");
//        } catch (SQLException e) {
//            return null;
//        }
//    }

    @Override
    public void createRun(Run run) throws CreateRunException {
        String sql = "INSERT INTO run (id, location)" +
                "VALUES ( ? , ? )";
        executeCreateRunQuery(sql, run.getId(), run.getLocation());
    }

    @Override
    public Run getRun(int id) throws RunNotFoundException {
        String sql = "SELECT run.id AS id, run.location AS location" +
                " FROM run" +
                " WHERE run.id = ?";
        Run run = executeGetRunQuery(sql,new Run(id));
        return run;
    }


    private Run executeGetRunQuery(String sql, Run run) throws RunNotFoundException {


        try(Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt= conn.prepareStatement(sql);
            ){
            pstmt.setInt(1, run.getId());
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


    private void executeCreateRunQuery(String sql, int param01, String param02) throws CreateRunException {

        try(Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt= conn.prepareStatement(sql)){
            pstmt.setInt(1, param01);
            pstmt.setString(2, param02);
            pstmt.executeUpdate();
        }catch(SQLException se){
            throw new CreateRunException(se.getMessage());
        }catch(Exception e){
            throw new CreateRunException(e.getMessage());
        }

    }

    protected void deleteRun(int id) throws DeleteRunException {
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
