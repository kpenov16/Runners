package dk.runs.runners.repositories;

import dk.runs.runners.entities.Run;
import dk.runs.runners.usecases.RunRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RunRepositoryImplTest {
    private RunRepository runRepository;
    private Run run;

    @Before
    public void beforeEach(){
        runRepository = new RunRepositoryImpl();
        run = new Run(2);
        run.setLocation("Paris");
    }

    @Test
     public void givenCreateRun_returnRunCreated() {
        //act
        runRepository.createRun(run);

        Run returnedRun = runRepository.getRun(run.getId());

        //assert
        assertEquals("Run creation error!", run.toString(), returnedRun.toString());

        //clean up
        RunRepositoryImpl runRepositoryImpl = (RunRepositoryImpl)runRepository;
        runRepositoryImpl.deleteRun(run.getId());
     }

    @Test
    public void givenUserUpdatesExistingRunLocation_returnRunLocationUpdated() {
        //arrange
        Run newRun = new Run(Integer.MAX_VALUE);
        newRun.setLocation("Copenhagen");
        runRepository.createRun(newRun);

        Run updatedRun = new Run(newRun.getId());
        updatedRun.setLocation("Sofia");

        //act
        runRepository.updateRun(updatedRun);

        //assert
        Run returnedRun = runRepository.getRun(newRun.getId());
        assertEquals("Update error", updatedRun.toString(), returnedRun.toString());

        //clean up
        RunRepositoryImpl runRepositoryImpl = (RunRepositoryImpl)runRepository;
        runRepositoryImpl.deleteRun(newRun.getId());
    }

    @Test
    public void givenRequestingNonExistingRunById_returnRunNotFoundException() {
        assertThrows(RunRepository.RunNotFoundException.class, () -> {
            runRepository.getRun(1);
        });
    }
}
