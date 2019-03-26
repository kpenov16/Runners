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
    void givenGetRuns_expectedException_returnNull() {
      //  when(runService.getRunsList()).thenThrow(new RunService.RunServiceException(""));

        doThrow(new RunService.RunServiceException("")).when(runService).getRunsList();
        assertThrows(RunService.RunServiceException.class,
                () -> controller.getRuns()
        );
    }

    @Test
    void givenGetRuns_returnEmptyList() {
        when(runService.getRunsList()).thenReturn(new LinkedList<Run>());
        assertEquals(new LinkedList<Run>(), controller.getRuns());
    }

    @Test
    void givenIdToDelete_InvokeDeleteMethodInRunService(){
        controller.deleteRun("0000");
        verify(runService).deleteRun("0000");
    }
}