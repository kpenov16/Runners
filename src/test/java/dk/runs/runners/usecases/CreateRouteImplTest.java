package dk.runs.runners.usecases;

import dk.runs.runners.entities.Location;
import dk.runs.runners.entities.Route;
import dk.runs.runners.entities.User;
import dk.runs.runners.repositories.RouteRepositoryImpl;
import dk.runs.runners.repositories.UserRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class CreateRouteImplTest {
    private CreateRouteImpl createRoute;
    private UserRepositoryImpl userRepository;
    private RouteRepositoryImpl routeRepository;

    private final long ms = System.currentTimeMillis();
    private final long ONE_HOUR = 60*60*1_000;
    private final int DISTANCE = 5_000;
    private Route route;
    private User user;

    @BeforeEach
    public void beforeEach(){
        userRepository = new UserRepositoryImpl();
        routeRepository = new RouteRepositoryImpl();
        createRoute = new CreateRouteImpl(routeRepository, userRepository);

        route = new Route(UUID.randomUUID().toString());
        route.setTitle("Route three");
        route.setDescription("It is going to be very fun!!!");
        route.setDate(new Date(ms));
        route.setStatus("active");
        route.setDuration(ONE_HOUR);
        route.setDistance(DISTANCE);

        Location location = new Location(UUID.randomUUID().toString());
        location.setX(2.2123);
        location.setY(2.3123);
        location.setCity("Stockholm");
        location.setCountry("Sweden");
        location.setStreetName("Main street");
        location.setStreetNumber("5A");
        route.setLocation(location);

        user = new User(UUID.randomUUID().toString());
        user.setEmail("runner@runner.com");
        user.setUserName("BillGates");
        user.setPassword("bananas");
    }

    @Test
    public void givenCreateNewRoute_returnRouteCreated(){
        //arrange
        FakeRouteRepository fakeRouteRepository = new FakeRouteRepository();
        createRoute.setRouteRepository(fakeRouteRepository);

        //act
        createRoute.execute(route, user.getId());

        //assert
        assertEquals(route.getId(), fakeRouteRepository.routeId);
        assertEquals(user.getId(), fakeRouteRepository.creatorId);
    }

    class FakeRouteRepository implements RouteRepository{
        public String routeId;
        public String creatorId;

        @Override
        public void createRoute(Route route, String creatorId) throws CreateRouteException, RouteIdDuplicationException {
            this.routeId = route.getId();
            this.creatorId = creatorId;
        }

        @Override
        public Route getRoute(String routeId) throws RouteNotFoundException {
            return null;
        }

        @Override
        public List<Route> getRoutes(String creatorId) {
            return null;
        }

        @Override
        public void updateRoute(Route updatedRoute) {

        }

        @Override
        public List<Route> getRouteList(int count, Date since) {
            return null;
        }

        @Override
        public void deleteRoute(String routeId) throws DeleteRouteException {

        }

        @Override
        public List<Route> getMostPopular(int top, Date since) {
            return null;
        }
    }
}

