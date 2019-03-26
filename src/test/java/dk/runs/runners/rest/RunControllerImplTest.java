package dk.runs.runners.rest;

import dk.runs.runners.entities.Run;
import dk.runs.runners.resources.RunRequest;
import dk.runs.runners.services.run.RunService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.LinkedList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RunControllerImplTest {
    private RunService runService;
    private RunControllerImpl controller;

    @BeforeEach
    void setUp(){
        runService = Mockito.mock(RunService.class);
        controller = new RunControllerImpl(runService);
    }

    @Test
    void givenCreateRunWithExistingId_returnRunServiceException() {
        // Arrange
        RunRequest runRequest = new RunRequest();
        Run run = new Run();
        runRequest.setRun(run);
        runRequest.setCreatorId("");

        doThrow(new RunService.RunServiceException("")).when(runService).createRun(runRequest.getRun(), runRequest.getCreatorId()); //  have to be same object as in  controller addRun call

        // Act & Assert
        assertThrows(RunService.RunServiceException.class,
                () -> controller.addRun(runRequest)
        );
    }



    @Test
    void givenGetRuns_expectedException_returnNull() {

        // Arrange
        doThrow(new RunService.RunServiceException("")).when(runService).getRunsList();
        // Act & Assert
        assertThrows(RunService.RunServiceException.class,
                () -> controller.getRuns()
        );
    }

    @Test
    void givenGetRuns_returnEmptyList() {
        // Arrange
        when(runService.getRunsList()).thenReturn(new LinkedList<Run>());
        // Act & Assert
        assertEquals(new LinkedList<Run>(), controller.getRuns());
    }

    @Test
    void givenIdToDelete_InvokeDeleteMethodInRunService(){
        // Act
        controller.deleteRun("0000");
        // Assert
        verify(runService).deleteRun("0000");
    }
}