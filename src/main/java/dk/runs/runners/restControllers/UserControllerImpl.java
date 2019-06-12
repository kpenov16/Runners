package dk.runs.runners.restControllers;

import dk.runs.runners.entities.User;
import dk.runs.runners.services.interfaceServices.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@CrossOrigin(origins="http://localhost:4200")
@RestController
public class UserControllerImpl {
    private UserService userService;
    public UserControllerImpl(UserService userService){
        this.userService = userService;
    }

    @GetMapping("users/hello")
    public ResponseEntity<String> hello(){
        return new ResponseEntity<String>("Hello Kaloyan", HttpStatus.OK);
    }

    @PostMapping(path = "/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        userService.createUser(user);
        User createdUser = userService.getUserById(user.getId());
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdUser.getId()).toUri();
        return ResponseEntity.created(uri).body(createdUser);
        //return new ResponseEntity<User>(createdUser, HttpStatus.OK);
    }

    @GetMapping("/users/{userName}")
    public User getUser(@PathVariable String userName){
        return userService.getUser(userName);
    }
    /*
    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable String id){
        return userService.getUser(id);
    }
    */
}
