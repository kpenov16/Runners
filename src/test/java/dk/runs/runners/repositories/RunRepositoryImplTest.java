package dk.runs.runners.repositories;

import dk.runs.runners.entities.Run;
import dk.runs.runners.usecases.RunRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RunRepositoryImplTest {
    private RunRepository runRepository;
    private Run run;
    private Run secondRun;
    private final long ms = System.currentTimeMillis();
    private final long ONE_HOUR = 60*60*1_000;
    private final int DISTANCE = 5_000;
    private String creatorId = UUID.randomUUID().toString();

    @BeforeEach
    public void beforeEach(){
        runRepository = new RunRepositoryImpl();
        run = new Run(UUID.randomUUID().toString());
        run.setTitle("Run three");
        run.setLocation("Stockholm");
        run.setDescription("It is going to be very fun!!!");
        run.setDate(new Date(ms));
        run.setStatus("active");
        run.setDuration(ONE_HOUR);
        run.setDistance(DISTANCE);

        secondRun = new Run(UUID.randomUUID().toString());
        secondRun.setTitle("Slow Run");
        secondRun.setLocation("Copenhagen");
        secondRun.setDescription("It is going to be very fun!!!");
        secondRun.setDate(new Date(ms));
        secondRun.setStatus("active");
        secondRun.setDuration(ONE_HOUR);
        secondRun.setDistance(DISTANCE);
    }

    @Test
     public void givenCreateRun_returnRunCreated() {
        //act
        runRepository.createRun(run, creatorId);

        Run returnedRun = runRepository.getRun(run.getId());

        //assert
        assertEquals(run.toString(), returnedRun.toString());

        //clean up
        RunRepositoryImpl runRepositoryImpl = (RunRepositoryImpl)runRepository;
        runRepositoryImpl.deleteRun(run.getId());
     }

    @Test
    public void givenRunUpdated_returnRunUpdated() {
        //arrange
        runRepository.createRun(run, creatorId);

        Run updatedRun = run;
        updatedRun.setTitle("new Title");
        updatedRun.setLocation("new Location");
        updatedRun.setDate(new Date());
        updatedRun.setDistance(1000);
        updatedRun.setDuration(2000);
        updatedRun.setDescription("new Dwscription");
        updatedRun.setStatus("new Status");

        //act
        runRepository.updateRun(updatedRun);

        //assert
        Run returnedRun = runRepository.getRun(run.getId());
        assertEquals(updatedRun.toString(), returnedRun.toString());

        //clean up
        RunRepositoryImpl runRepositoryImpl = (RunRepositoryImpl)runRepository;
        runRepositoryImpl.deleteRun(run.getId());
    }

    @Test
    public void givenRequestingNonExistingRunById_returnRunNotFoundException() {
        assertThrows(RunRepository.RunNotFoundException.class,
                () -> runRepository.getRun(UUID.randomUUID().toString())
        );
    }

    @Test
    public void givenCreateRunWithExistingId_returnRunIdDuplicationException() {
        //Arrange
        runRepository.createRun(run, creatorId);

        //Act, Assert
        assertThrows(RunRepository.RunIdDuplicationException.class,
                () -> runRepository.createRun(run, creatorId)
        );

    }

    @Test
    public void givenGetRunsList_returnListIsNotEmpty(){
        runRepository.createRun(run, creatorId);
        runRepository.createRun(secondRun, creatorId);
        assertTrue(runRepository.getRunsList().size() > 0);
        //clean up
        RunRepositoryImpl runRepositoryImpl = (RunRepositoryImpl)runRepository;
        runRepositoryImpl.deleteRun(run.getId());
        runRepositoryImpl.deleteRun(secondRun.getId());
    }
}
