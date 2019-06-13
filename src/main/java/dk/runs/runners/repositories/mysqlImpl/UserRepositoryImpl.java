package dk.runs.runners.repositories.mysqlImpl;

import dk.runs.runners.config.DataSourceConfig;
import dk.runs.runners.entities.Location;
import dk.runs.runners.entities.User;
import dk.runs.runners.services.interfaceRepositories.UserRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl extends BaseRunnersRepository implements UserRepository {
    private final String url = "jdbc:mysql://ec2-52-30-211-3.eu-west-1.compute.amazonaws.com/s133967?"
            + "user=s133967&password=8JPOJuQcgUpUVIVHY4S2H";

    @Override
    public void createUser(User user) {
        validateUser(user);

        String userSql = "INSERT INTO user (id, user_name, email, password)" +
                               "VALUES ( ? , ? , ? , ? )";

        String locationSql = "INSERT INTO user_location (id, user_id, street_name, street_number, city, country, spatial_point)" +
                "VALUES ( ? , ? , ? , ? , ? , ? , ST_GeomFromText( ? , ? ))";

        executeCreateUserQuery(userSql, locationSql, user);
    }

    private void validateUser(User user) {
        if(user.getLocations() == null || user.getLocations().size() == 0){
            throw new UserMissingLocationException("User with id: " + user.getId() + " is missing location.");
        }

        for(Location location : user.getLocations()){
            if(location.getId() == null || location.getId().isEmpty()){
                throw new UserMissingLocationException("User with id: " + user.getId() + " is missing location.");
            }
        }
    }

    private void executeCreateUserQuery(String userSql, String locationSql, User user){
        Connection conn = null;
        PreparedStatement pstmtUser = null;
        PreparedStatement pstmtLocation = null;
        try{
            conn = DataSourceConfig.getConnection();
            conn.setAutoCommit(false);

            pstmtLocation = conn.prepareStatement(locationSql);

            pstmtUser= conn.prepareStatement(userSql);
            pstmtUser.setString(1, user.getId());
            pstmtUser.setString(2, user.getUserName());
            pstmtUser.setString(3, user.getEmail());
            pstmtUser.setString(4, user.getPassword());

            int rowsEffected = pstmtUser.executeUpdate();

            if(rowsEffected == 1){
                super.executeCreateLocationQuery(user, pstmtLocation);
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
                if(conn!=null){
                    conn.rollback();
                }
                se.printStackTrace();
                throw new CreateUserException(se.getMessage());
            }catch (SQLException rollBackException){
                throw new CreateUserException(rollBackException.getMessage());
            }
        }catch(Exception e){
            try {
                if(conn!=null){
                    conn.rollback();
                }
                e.printStackTrace();
                throw new CreateUserException(e.getMessage());
            }catch (SQLException rollBackException){
                throw new CreateUserException(rollBackException.getMessage());
            }
        }finally {
            try {
                if(pstmtUser != null) pstmtUser.close();
                if(pstmtLocation != null) pstmtLocation.close();
                if(conn != null) conn.close();
            } catch (SQLException e) {
                throw new CreateUserException(e.getMessage());
            }
        }
    }

    @Override
    public User getUser(String userName) {
        String sql = "SELECT * " +
                " FROM user" +
                " WHERE user.user_name = ?";
        User user = executeGetUserQuery(sql, userName);
        user.setLocations( getLocations(user.getId()) ); //if only one location per user
        return user;
    }

    @Override
    public User getUserById(String userId) {
        String sql = "SELECT * " +
                " FROM user" +
                " WHERE user.id = ?";
        User user = executeGetUserQuery(sql, new User(userId));
        user.setLocations( getLocations(userId) );
        return user;
    }

    private List<Location> getLocations(String userId) {
        String sql = " SELECT id, street_name, street_number, city, country, ST_X(spatial_point) AS X, ST_Y(spatial_point) AS Y" +
                " FROM user_location" +
                " WHERE user_location.user_id = ? ";
        return executeGetLocationQuery(sql, userId);
    }

    private List<Location> executeGetLocationQuery(String locationSql, String userId) {
        List<Location> locations = new ArrayList<>();
        try(Connection conn = DataSourceConfig.getConnection();
            PreparedStatement pstmt= conn.prepareStatement(locationSql)){
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                Location location = new Location(rs.getString("id"));
                location.setStreetName(rs.getString("street_name"));
                location.setStreetNumber(rs.getString("street_number"));
                location.setCity(rs.getString("city"));
                location.setCountry(rs.getString("country"));
                location.setX(rs.getDouble("X"));
                location.setY(rs.getDouble("Y"));
                locations.add(location);
            }
            if(rs != null){  rs.close(); }
        }catch(SQLException se){
            throw new UserNotFoundException(se.getMessage());
        }catch(Exception e){
            throw new UserNotFoundException(e.getMessage());
        }
        return locations;
    }

    private User executeGetUserQuery(String sql, String userName) {
        boolean isUserFound = false;
        User user = new User();
        try(Connection conn = DataSourceConfig.getConnection();
            PreparedStatement pstmt= conn.prepareStatement(sql)){
            pstmt.setString(1, userName);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                isUserFound = true;
                user.setId(rs.getString("id"));
                user.setUserName(rs.getString("user_name"));
                user.setEmail(rs.getString("email"));
                user.setPassword( rs.getString("password") );
            }
            if(rs != null){  rs.close(); }
        }catch(SQLException se){
            throw new GetUserException(se.getMessage());
        }catch(Exception e){
            throw new GetUserException(e.getMessage());
        }finally {
            if(!isUserFound){
                throw new UserNotFoundException("User with userName: " + userName + " was not found");
            }
        }
        return user;
    }

    private User executeGetUserQuery(String sql, User user) {
        boolean isUserFound = false;
        try(Connection conn = DataSourceConfig.getConnection();
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
            throw new GetUserException(se.getMessage());
        }catch(Exception e){
            e.printStackTrace();
            throw new GetUserException(e.getMessage());
        }finally {
            if(!isUserFound){
                throw new UserNotFoundException("User with id: " + user.getId() + " was not found");
            }
        }
        return user;
    }

    @Override
    public void deleteUser(String userId){
        User user = getUserById(userId);

        String locationRouteSql = "DELETE FROM user_location WHERE user_id = ?";

        String userSql = "DELETE FROM user WHERE id = ?";

        executeDeleteUserQuery(locationRouteSql, userSql, user);
    }

    private void executeDeleteUserQuery(String locationUserSql,
                                         String userSql, User user)
            throws DeleteUserException {
        Connection conn = null;
        PreparedStatement pstmtLocationUser = null;
        PreparedStatement pstmtUser = null;
        try{
            conn = DataSourceConfig.getConnection();
            conn.setAutoCommit(false);

            pstmtLocationUser = conn.prepareStatement(locationUserSql);
            pstmtLocationUser.setString(1, user.getId());
            pstmtLocationUser.executeUpdate();

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

        String locationSql = "UPDATE user_location SET street_name = ? , street_number = ? ," +
                " city = ?, country = ?, spatial_point = ST_GeomFromText( ? , ? )" +
                "WHERE user_location.id = ?";

        executeUpdateUserQuery(userSql, locationSql, updatedUser);
    }

    private void executeUpdateUserQuery(String sql, String locationSql, User user) {
        boolean isUserFound = true;
        Connection conn = null;
        PreparedStatement pstmtUser = null;
        PreparedStatement pstmtLocation = null;
        try{
            conn = DataSourceConfig.getConnection();
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
                super.executeUpdateLocationQuery(user, pstmtLocation);
                conn.commit();
            }else {
                conn.rollback();
                isUserFound = false;
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
                throw new UpdateUserException(rollBackException.getMessage());
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
            if(!isUserFound){
                throw new UserNotFoundException("Update of user. " +"User with id: "+ user.getId() + " was not found.");
            }
        }
    }
}
