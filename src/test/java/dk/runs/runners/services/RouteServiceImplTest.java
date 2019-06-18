package dk.runs.runners.services;

import dk.runs.runners.entities.Location;
import dk.runs.runners.entities.Route;
import dk.runs.runners.entities.User;
import dk.runs.runners.services.interfaceServices.RouteService;
import dk.runs.runners.services.serviceImpl.RouteServiceImpl;
import dk.runs.runners.services.interfaceRepositories.RouteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RouteServiceImplTest {
    RouteService routeService = null;
    RouteRepository routeRepository = null;

    @BeforeEach
    public void beforeEach(){
        //routeRepository = new RouteRepositoryImpl();
        //routeService = new RouteServiceImpl(routeRepository);
    }

    @Test
    public void givenCreateRunWithExistingId_returnRunIdDuplicationException() {
        //Arrange
        routeRepository = new FakeRouteRepository();
        routeService = new RouteServiceImpl(routeRepository);
        Location location = new Location();
        Route route = new Route();
        route.setLocation(location);

        //Act, Assert
        RouteService.RouteServiceException ex = assertThrows(RouteService.RouteServiceException.class,
                () -> routeService.createRoute(route, "creatorId")
        );
        assertEquals("Route already created!",ex.getMessage());
        assertEquals(1,((FakeRouteRepository) routeRepository).numberOfCallsTo_createRun);
    }
    class FakeRouteRepository implements RouteRepository {
        protected int numberOfCallsTo_createRun;
        @Override
        public void createRoute(Route route, String creatorId) throws CreateRouteException {
            numberOfCallsTo_createRun++;
            throw new RouteIdDuplicationException("PRIMARY KEY");
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

        @Override
        public List<User> getRouteParticipants(String routeId) throws RouteNotFoundException {
            return null;
        }
    }
}