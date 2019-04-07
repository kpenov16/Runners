package dk.runs.runners.repositories;

import dk.runs.runners.entities.User;
import dk.runs.runners.usecases.UserRepository;

import java.sql.*;

public class UserRepositoryImpl implements UserRepository {
    private final String url = "jdbc:mysql://ec2-52-30-211-3.eu-west-1.compute.amazonaws.com/s133967?"
            + "user=s133967&password=8JPOJuQcgUpUVIVHY4S2H";

    @Override
    public void createUser(User user) {
        String sql = "INSERT INTO user (id, user_name, email, password)" +
                               "VALUES ( ? , ? , ? , ? )";
        executeCreateRunQuery(sql, user);
    }

    private void executeCreateRunQuery(String sql, User user) throws CreateUserException {
        try(Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt= conn.prepareStatement(sql)){
            pstmt.setString(1, user.getId());
            pstmt.setString(2, user.getUserName());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getPassword());
            pstmt.executeUpdate();
        }catch (SQLIntegrityConstraintViolationException e){
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
        }catch(SQLException se){
            se.printStackTrace();
            throw new CreateUserException(se.getMessage());
        }catch(Exception e){
            e.printStackTrace();
            throw new CreateUserException(e.getMessage());
        }
    }

    @Override
    public User getUser(String userId) {
        String sql = "SELECT * " +
                " FROM user" +
                " WHERE user.id = ?";
        return executeGetUserQuery(sql, new User(userId));
    }

    private User executeGetUserQuery(String sql, User user) throws UserNotFoundException {
        try(Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt= conn.prepareStatement(sql)){
            pstmt.setString(1, user.getId());
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            user.setUserName(rs.getString("user_name"));
            user.setEmail(rs.getString("email"));
            user.setPassword( rs.getString("password") );
            rs.close();
        }catch(SQLException se){
            throw new UserNotFoundException(se.getMessage());
        }catch(Exception e){
            throw new UserNotFoundException(e.getMessage());
        }
        return user;
    }

    @Override
    public void deleteUser(String userId) throws DeleteUserException {
        String sql = "DELETE FROM user WHERE id = ?";
        executeDeleteUserQuery(sql, userId);
    }
    private void executeDeleteUserQuery(String sql, String userId) throws DeleteUserException {
        try(Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt= conn.prepareStatement(sql)){
            pstmt.setString(1, userId);
            pstmt.executeUpdate();
        }catch(SQLException se){
            throw new DeleteUserException(se.getMessage());
        }catch(Exception e){
            throw new DeleteUserException(e.getMessage());
        }
    }

    @Override
    public void updateUser(User updatedUser) {
        String sql = "UPDATE user" +
                " SET user_name = ?, email = ?, password = ?" +
                " WHERE id = ?";
        executeUpdateUserQuery(sql, updatedUser);
    }
    private void executeUpdateUserQuery(String sql, User user) throws UpdateUserException {
        try(Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt= conn.prepareStatement(sql)){
            pstmt.setString(1, user.getUserName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword() );
            pstmt.setString(4, user.getId());
            pstmt.executeUpdate();
    }   catch (SQLIntegrityConstraintViolationException e) {
            final String MSG = e.getMessage();
            if (MSG.contains("user_name")) {
                throw new UserNameDuplicationException(MSG);
            } else if (MSG.contains("email")) {
                throw new UserEmailDuplicationException(MSG);
            } else {
                throw new UpdateUserException(MSG);
            }
        }catch(SQLException se){
            throw new UpdateUserException(se.getMessage());
        }catch(Exception e){
            throw new UpdateUserException(e.getMessage());
        }
    }

}
