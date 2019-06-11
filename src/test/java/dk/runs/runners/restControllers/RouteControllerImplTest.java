package dk.runs.runners.restControllers;

import dk.runs.runners.entities.Route;
import dk.runs.runners.services.interfaceServices.RouteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.LinkedList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RouteControllerImplTest {
    private RouteService routeService;
    private RouteControllerImpl controller;

    @BeforeEach
    void setUp(){
        routeService = Mockito.mock(RouteService.class);
        controller = new RouteControllerImpl(routeService);
    }
//
//    @Test
//    void givenCreateRunWithExistingId_returnRunServiceException() {
//        // Arrange
//        RouteRequest routeRequest = new RouteRequest();
//        Route route = new Route();
//        routeRequest.setRoute(route);
//        routeRequest.setCreatorId("");
//
//        doThrow(new RouteService.RouteServiceException("")).when(routeService).createRoute(routeRequest.getRoute(), routeRequest.getCreatorId()); //  have to be same object as in  controller addRun call
//
//        // Act & Assert
//        assertThrows(RouteService.RouteServiceException.class,
//                () -> controller.addRoute(routeRequest)
//        );
//    }



    @Test
    void givenGetRuns_expectedException_returnNull() {

        // Arrange
        doThrow(new RouteService.RouteServiceException("")).when(routeService).getRoutesList();
        // Act & Assert
        assertThrows(RouteService.RouteServiceException.class,
                () -> controller.getRoutes()
        );
    }

    @Test
    void givenGetRuns_returnEmptyList() {
        // Arrange
        when(routeService.getRoutesList()).thenReturn(new LinkedList<Route>());
        // Act & Assert
        assertEquals(new LinkedList<Route>(), controller.getRoutes());
    }

    @Test
    void givenIdToDelete_InvokeDeleteMethodInRunService(){
        // Act
        controller.deleteRoute("0000");
        // Assert
        verify(routeService).deleteRoute("0000");
    }
}