package dk.runs.runners.repositories;

import dk.runs.runners.entities.Location;
import dk.runs.runners.entities.User;
import dk.runs.runners.usecases.UserRepository;

import java.sql.*;

public class UserRepositoryImpl implements UserRepository {
    private final String url = "jdbc:mysql://ec2-52-30-211-3.eu-west-1.compute.amazonaws.com/s133967?"
            + "user=s133967&password=8JPOJuQcgUpUVIVHY4S2H";

    @Override
    public void createUser(User user) {
        validateUser(user);

        String userSql = "INSERT INTO user (id, user_name, email, password)" +
                               "VALUES ( ? , ? , ? , ? )";

        String locationSql = "INSERT INTO location (id, street_name, street_number, city, country, spatial_point)" +
                "VALUES ( ? , ? , ? , ? , ? , ST_GeomFromText( ? , ? ))";

        String locationUserSql = "INSERT INTO location_user ( location_id, user_id )" +
                "VALUES ( ? , ? )";

        executeCreateUserQuery(userSql, locationSql, locationUserSql, user);
    }

    private void validateUser(User user) {
        if(user.getLocation() == null || user.getLocation().getId() == null || user.getLocation().getId().isEmpty()){
            throw new UserMissingLocationException("User with id: " + user.getId() + " is missing location.");
        }
    }

    private void executeCreateUserQuery(String userSql, String locationSql, String locationUserSql, User user) throws CreateUserException {
        Connection conn = null;
        PreparedStatement pstmtUser = null;
        PreparedStatement pstmtLocation = null;
        PreparedStatement pstmtUserLocation = null;



        try{
            conn = DriverManager.getConnection(url);
            conn.setAutoCommit(false);

            pstmtLocation = conn.prepareStatement(locationSql);
            pstmtUserLocation = conn.prepareStatement(locationUserSql);

            pstmtUser= conn.prepareStatement(userSql);
            pstmtUser.setString(1, user.getId());
            pstmtUser.setString(2, user.getUserName());
            pstmtUser.setString(3, user.getEmail());
            pstmtUser.setString(4, user.getPassword());

            int rowsEffected = pstmtUser.executeUpdate();

            if(rowsEffected == 1){
                executeCreateLocationQuery(user, pstmtLocation);
                executeCreateLocationUserQuery(user, pstmtUserLocation);
                conn.commit();
            }else {
                conn.rollback();
            }
        }catch (SQLIntegrityConstraintViolationException e){

            try {
                if(conn!=null){
                    conn.rollback();
                }

                final String MSG = e.getMessage();
                if(MSG.contains("PRIMARY")){
                    throw new UserIdDuplicationException(MSG);
                } else if(MSG.contains("user_name")){
                    throw new UserNameDuplicationException(MSG);
                }else if(MSG.contains("email")){
                    throw new UserEmailDuplicationException(MSG);
                }else {
                    throw new CreateUserException(MSG);
                }

            }catch (SQLException rollBackException){
                //??
            }
        }catch(SQLException se){
            try {
                conn.rollback();
                se.printStackTrace();
                throw new CreateUserException(se.getMessage());
            }catch (SQLException rollBackException){
                throw new CreateUserException(rollBackException.getMessage());
            }
        }catch(Exception e){
            try {
                conn.rollback();
                e.printStackTrace();
                throw new CreateUserException(e.getMessage());
            }catch (SQLException rollBackException){
                throw new CreateUserException(rollBackException.getMessage());
            }
        }finally {
            try {
                if(pstmtUser != null) pstmtUser.close();
                if(pstmtLocation != null) pstmtLocation.close();
                if(pstmtUserLocation != null) pstmtUserLocation.close();
                if(conn != null) conn.close();
            } catch (SQLException e) {
                throw new CreateUserException(e.getMessage());
            }
        }
    }



    private void executeCreateLocationQuery(User user, PreparedStatement pstmtLocation) throws SQLException {
        /*"INSERT INTO location (route_id, street_name, street_number, city, country, spatial_point)" +
                "VALUES ( ? , ? , ? , ? , ? , ST_GeomFromText( ? , ? ))";*/
        final Location location = user.getLocation();
        if (location != null) {
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

    private void executeCreateLocationUserQuery(User user, PreparedStatement pstmtLocationUser) throws SQLException {
        final Location location = user.getLocation();
        if (location != null) {
            pstmtLocationUser.setString(1, location.getId());
            pstmtLocationUser.setString(2, user.getId());
            pstmtLocationUser.executeUpdate();
        }
    }





    @Override
    public User getUser(String userId) {
        String sql = "SELECT * " +
                " FROM user" +
                " WHERE user.id = ?";
        User user = executeGetUserQuery(sql, new User(userId));
        user.setLocation(getLocation(userId));
        return user;
    }

    private Location getLocation(String userId) {
        String sql = " SELECT id, street_name, street_number, city, country, ST_X(spatial_point) AS X, ST_Y(spatial_point) AS Y" +
                " FROM location JOIN location_user" +
                " ON location_user.location_id = location.id" +
                " WHERE location_user.user_id = ? ";
        return executeGetLocationQuery(sql, userId);
    }

    private Location executeGetLocationQuery(String locationSql, String userId) throws UserNotFoundException {
        Location location = null;
        try(Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt= conn.prepareStatement(locationSql)){
            pstmt.setString(1, userId);
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
            if(rs != null){  rs.close(); }
        }catch(SQLException se){
            throw new UserNotFoundException(se.getMessage());
        }catch(Exception e){
            throw new UserNotFoundException(e.getMessage());
        }
        return location;
    }

    private User executeGetUserQuery(String sql, User user) throws UserNotFoundException {
        boolean isUserFound = false;
        try(Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt= conn.prepareStatement(sql)){
            pstmt.setString(1, user.getId());
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                isUserFound = true;
                user.setUserName(rs.getString("user_name"));
                user.setEmail(rs.getString("email"));
                user.setPassword( rs.getString("password") );
            }
            if(rs != null){  rs.close(); }
        }catch(SQLException se){
            se.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            if(!isUserFound){
                throw new UserNotFoundException("User with id: " + user.getId() + " was not found");
            }
        }
        return user;
    }

    @Override
    public void deleteUser(String userId) throws DeleteUserException {
        User user = getUser(userId);
        String locationRouteSql = "DELETE FROM location_user WHERE user_id = ?";
        String locationSql = "DELETE FROM location WHERE id = ?";
        String userSql = "DELETE FROM user WHERE id = ?";

        executeDeleteUserQuery(locationRouteSql, locationSql, userSql, user);
    }
   /* private void executeDeleteUserQuery(String sql, String userId) throws DeleteUserException {
        try(Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt= conn.prepareStatement(sql)){
            pstmt.setString(1, userId);
            pstmt.executeUpdate();
        }catch(SQLException se){
            throw new DeleteUserException(se.getMessage());
        }catch(Exception e){
            throw new DeleteUserException(e.getMessage());
        }
    }*/

    private void executeDeleteUserQuery(String locationRouteSql, String locationSql,
                                         String userSql, User user)
            throws DeleteUserException {
        Connection conn = null;
        PreparedStatement pstmtLocationUser = null;
        PreparedStatement pstmtLocation = null;
        PreparedStatement pstmtUser = null;
        try{
            conn = DriverManager.getConnection(url);
            conn.setAutoCommit(false);

            pstmtLocationUser = conn.prepareStatement(locationRouteSql);
            pstmtLocationUser.setString(1, user.getId());
            pstmtLocationUser.executeUpdate();

            pstmtLocation = conn.prepareStatement(locationSql);
            pstmtLocation.setString(1, user.getLocation().getId());
            pstmtLocation.executeUpdate();

            pstmtUser = conn.prepareStatement(userSql);
            pstmtUser.setString(1, user.getId());
            pstmtUser.executeUpdate();

            conn.commit();
        }catch(SQLException se){
            try {
                conn.rollback();
                throw new DeleteUserException(se.getMessage());
            }catch (SQLException rollBackException){
                throw new DeleteUserException(rollBackException.getMessage());
            }
        }catch(Exception e){
            try {
                conn.rollback();
                throw new DeleteUserException(e.getMessage());
            }catch (SQLException rollBackException){
                throw new DeleteUserException(rollBackException.getMessage());
            }
        }finally {
            try {
                if(pstmtUser != null) pstmtUser.close();
                if(pstmtLocation != null) pstmtLocation.close();
                if(pstmtLocationUser != null) pstmtLocationUser.close();
                if(conn != null) conn.close();
            } catch (SQLException e) {
                throw new DeleteUserException(e.getMessage());
            }
        }
    }



    @Override
    public void updateUser(User updatedUser) {
        validateUser(updatedUser);
        String userSql = "UPDATE user" +
                " SET user_name = ?, email = ?, password = ?" +
                " WHERE id = ?";
        String locationSql = "UPDATE location SET street_name = ? , street_number = ? ," +
                " city = ?, country = ?, spatial_point = ST_GeomFromText( ? , ? )" +
                "WHERE location.id = ?";
        executeUpdateUserQuery(userSql, locationSql, updatedUser);
    }
    private void executeUpdateUserQuery(String sql, String locationSql, User user) throws UpdateUserException {

        Connection conn = null;
        PreparedStatement pstmtUser = null;
        PreparedStatement pstmtLocation = null;
        try{
            conn = DriverManager.getConnection(url);
            conn.setAutoCommit(false);

            pstmtUser = conn.prepareStatement(sql);
            pstmtLocation = conn.prepareStatement(locationSql);

            pstmtUser.setString(1, user.getUserName());
            pstmtUser.setString(2, user.getEmail());
            pstmtUser.setString(3, user.getPassword() );
            pstmtUser.setString(4, user.getId());

            int rowsEffected = pstmtUser.executeUpdate();

            if(rowsEffected == 1){
                //update location
                executeUpdateLocationQuery(user, pstmtLocation);
                conn.commit();
            }else {
                conn.rollback();
            }

        }catch (SQLIntegrityConstraintViolationException e){

            try {
                if(conn!=null){
                    conn.rollback();
                }
                final String MSG = e.getMessage();
                if (MSG.contains("user_name")) {
                    throw new UserNameDuplicationException(MSG);
                } else if (MSG.contains("email")) {
                    throw new UserEmailDuplicationException(MSG);
                } else {
                    throw new UpdateUserException(MSG);
                }
            }catch (SQLException rollBackException){
                //??
            }
        }catch(SQLException se){
            try {
                conn.rollback();
                se.printStackTrace();
                throw new UpdateUserException(se.getMessage());
            }catch (SQLException rollBackException){
                throw new UpdateUserException(rollBackException.getMessage());
            }
        }catch(Exception e){
            try {
                conn.rollback();
                e.printStackTrace();
                throw new UpdateUserException(e.getMessage());
            }catch (SQLException rollBackException){
                throw new UpdateUserException(rollBackException.getMessage());
            }
        }finally {
            try {
                if(pstmtUser != null) pstmtUser.close();
                if(pstmtLocation != null) pstmtLocation.close();
                if(conn != null) conn.close();
            } catch (SQLException e) {
                throw new UpdateUserException(e.getMessage());
            }
        }
    }

    private void executeUpdateLocationQuery(User user, PreparedStatement pstmtLocation) throws SQLException {

        final Location location = user.getLocation();
        pstmtLocation.setString(1, location.getStreetName());
        pstmtLocation.setString(2, location.getStreetNumber());
        pstmtLocation.setString(3, location.getCity());
        pstmtLocation.setString(4, location.getCountry());
        pstmtLocation.setString(5, "POINT(" + location.getX() + " " + location.getY() + ")");
        pstmtLocation.setInt(6, location.getSRID());
        pstmtLocation.setString(7, location.getId());
        pstmtLocation.executeUpdate();

    }


}
