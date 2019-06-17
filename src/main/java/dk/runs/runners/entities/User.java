package dk.runs.runners.entities;

import java.util.LinkedList;
import java.util.List;

public class User implements Locational{
    private String id = "";
    private String userName = "";
    private String email = "";
    private String password = "";
    private List<Route> routes = new LinkedList<>();
    private List<Location> locations;

    public User(){}
    public User(String id){
        setId(id);
    }
    @Override
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

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    @Override
    public List<Location> getLocations() {
        return locations;
    }

    public User(String id, String userName, String email, String password, List<Route> routes, List<Location> locations) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.routes = routes;
        this.locations = locations;
    }
}
