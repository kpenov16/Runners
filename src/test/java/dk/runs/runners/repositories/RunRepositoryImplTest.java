package dk.runs.runners.repositories;

import dk.runs.runners.entities.Run;
import dk.runs.runners.usecases.RunRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigInteger;
import java.util.Date;
import java.util.UUID;

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
    private final int DISTANCE = 5_000;
    private String creatorId = UUID.randomUUID().toString();

    @Before
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
        assertEquals("Run creation error!", run.toString(), returnedRun.toString());

        //clean up
        RunRepositoryImpl runRepositoryImpl = (RunRepositoryImpl)runRepository;
        runRepositoryImpl.deleteRun(run.getId());
     }

    @Test
    public void givenUserUpdatesExistingRunLocation_returnRunLocationUpdated() {
        //arrange
        runRepository.createRun(run, creatorId);

        Run updatedRun = run;
        updatedRun.setLocation("Sofia");

        //act
        runRepository.updateRun(updatedRun);

        //assert
        Run returnedRun = runRepository.getRun(run.getId());
        assertEquals("Update error", updatedRun.toString(), returnedRun.toString());

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
    public void givenGetRunsList_returnListIsNotEmpty(){
        runRepository.createRun(run, creatorId);
        runRepository.createRun(secondRun, creatorId);
        assertTrue(runRepository.getRunsList().size() > 0);
        //clean up
        RunRepositoryImpl runRepositoryImpl = (RunRepositoryImpl)runRepository;
        runRepositoryImpl.deleteRun(run.getId());
        runRepositoryImpl.deleteRun(secondRun.getId());
    }


    @Test
    public void nothing(){
        runRepository.createRun(run, "123");
    }
/*
    @Test
    public void givenRunMarkDeleted_returnRunMarkedDeleted(){

        //Arrange
        runRepository.createRun(run, creatorId);

    }*/



}
