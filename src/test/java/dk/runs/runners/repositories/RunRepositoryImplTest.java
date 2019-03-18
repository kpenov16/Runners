package dk.runs.runners.repositories;

import dk.runs.runners.entities.Run;
import dk.runs.runners.usecases.RunRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RunRepositoryImplTest {
    private RunRepository runRepository;
    private Run run;
    private Run secondRun;
    private final long ms = System.currentTimeMillis();
    private final long ONE_HOUR = 60*60*1_000;

    @Before
    public void beforeEach(){
        runRepository = new RunRepositoryImpl();
        run = new Run(3);
        run.setTitle("Run three");
        run.setLocation("Stockholm");
        run.setDescription("It is going to be very fun!!!");
        run.setDate(new Date(ms));
        run.setStatus("active");
        run.setDuration(ONE_HOUR);
        secondRun = new Run(4);
        secondRun.setLocation("Berlin");
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
        Run newRun = new Run(Integer.MAX_VALUE);  //TODO Why creating new runs here if there is in beforeEach?
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
    @Test
    public void givenGetRunsList_returnListIsNotEmpty(){
        runRepository.createRun(run);
        runRepository.createRun(secondRun);
        assertTrue(runRepository.getRunsList().size() > 0);
        //clean up
        RunRepositoryImpl runRepositoryImpl = (RunRepositoryImpl)runRepository;
        runRepositoryImpl.deleteRun(run.getId());
        runRepositoryImpl.deleteRun(secondRun.getId());
    }

    @Test
    public void givenRunMarkDeleted_returnRunMarkedDeleted(){

        //Arrange
        runRepository.createRun(run);

    }

}
