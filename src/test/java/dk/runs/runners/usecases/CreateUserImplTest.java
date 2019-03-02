package dk.runs.runners.usecases;

import dk.runs.runners.entities.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CreateUserImplTest {

    @Test
    public void givenNonExistingUser_returnUserWasCreated(){

    }

    @Test
    public void givenAnExistingUser_returnUserAlreadyCreatedException(){

    }

}
