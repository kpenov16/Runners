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
        run = new Run(1);
        run.setLocation("Copenhagen");
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
    public void givenRequestingRunByID_returnRunNotFoundException() {
        assertThrows(RunRepository.RunNotFoundException.class, () -> {
            runRepository.getRun(1);
        });
    }
}
