package dk.runs.runners.usecases;

import dk.runs.runners.entities.Route;
import dk.runs.runners.entities.User;
import dk.runs.runners.repositories.RouteRepositoryImpl;
import dk.runs.runners.repositories.UserRepositoryImpl;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ShowUserCreatedRoutesImplTest {


    private ShowUserCreatedRoutesImpl showUserCreatedRoutes;
    private FakeUserRepo userRepository;
    private FakeRouteRepo routeRepository;

    @Test
    public void givenRequestingUserWithRoute_returnUserWithRoute(){
        showUserCreatedRoutes = new ShowUserCreatedRoutesImpl();
        userRepository = new FakeUserRepo();
        userRepository.timesCalled = 0;
        showUserCreatedRoutes.setUserRepository(userRepository);
        routeRepository = new FakeRouteRepo();
        routeRepository.timesCalled = 0;
        routeRepository.fakeRouteId = UUID.randomUUID().toString();
        showUserCreatedRoutes.setRouteRepository(routeRepository);

        User user = showUserCreatedRoutes.execute("user_id");

        assertEquals("user_id", userRepository.userId);
        assertEquals(1, userRepository.timesCalled);

        assertEquals("user_id", routeRepository.creatorId);
        assertEquals(1, routeRepository.timesCalled);

         //we check if the route was added to the user
         assertEquals(routeRepository.fakeRouteId, user.getRoutes().get(0).getId());
    }

    class FakeRouteRepo implements  RouteRepository{
        public String creatorId;
        public String fakeRouteId;
        public int timesCalled;

        @Override
        public void createRoute(Route route, String creatorId) throws CreateRouteException, RouteIdDuplicationException {

        }

        @Override
        public Route getRoute(String routeId) throws RouteNotFoundException {
            return null;
        }

        @Override
        public List<Route> getRoutes(String creatorId) {
            this.timesCalled++;
            this.creatorId = creatorId;
            return new LinkedList<Route>(){{add(new Route(fakeRouteId));}};
        }

        @Override
        public void updateRoute(Route updatedRoute) {

        }

        @Override
        public List<Route> getRouteList() {
            return null;
        }

        @Override
        public void deleteRoute(String routeId) throws DeleteRouteException {

        }
    }

    class FakeUserRepo implements UserRepository{
        public String  userId ;
        public int timesCalled;

        @Override
        public void createUser(User user) {

        }

        @Override
        public User getUser(String userId) {
            this.userId = userId;
            this.timesCalled++;
            return new User(userId);
        }

        @Override
        public void deleteUser(String userId) {

        }

        @Override
        public void updateUser(User updatedUser) {

        }
    }

}
