package dk.runs.runners.services.run;

import dk.runs.runners.entities.Run;
import dk.runs.runners.usecases.RunRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RunServiceImplTest {
    RunService runService = null;
    RunRepository runRepository = null;

    @BeforeEach
    public void beforeEach(){
        //runRepository = new RunRepositoryImpl();
        //runService = new RunServiceImpl(runRepository);
    }

    @Test
    public void givenCreateRunWithExistingId_returnRunIdDuplicationException() {
        //Arrange
        runRepository = new FakeRunRepository();
        runService = new RunServiceImpl(runRepository);

        //Act, Assert
        assertThrows(RunRepository.RunIdDuplicationException.class,
                () -> runService.createRun(new Run(), "creatorId")
        );
        assertEquals(1,((FakeRunRepository)runRepository).numberOfCallsTo_createRun);
    }

    class FakeRunRepository implements RunRepository{
        protected int numberOfCallsTo_createRun;
        @Override
        public void createRun(Run run, String creatorId) throws CreateRunException {
            numberOfCallsTo_createRun++;
            throw new RunIdDuplicationException("");
        }
        @Override
        public Run getRun(String id) throws RunNotFoundException {
            return null;
        }
        @Override
        public void updateRun(Run updatedRun) {

        }
        @Override
        public List<Run> getRunsList() {
            return null;
        }
        @Override
        public void deleteRun(String id) throws DeleteRunException {

        }
    }

}