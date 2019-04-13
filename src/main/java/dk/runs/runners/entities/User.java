package dk.runs.runners.entities;

import java.util.LinkedList;
import java.util.List;

public class User {
    private String id = "";
    private String userName = "";
    private String email = "";
    private String password = "";
    private List<Route> routes = new LinkedList<>();

    public User(){}
    public User(String id){
        setId(id);
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public List<Route> getRoutes() {
        return this.routes;
    }
}
