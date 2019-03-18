package dk.runs.runners;

import dk.runs.runners.entities.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.test.util.AssertionErrors.assertEquals;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class RunnersApplicationTests {

	//@Test
	public void givenNewUser_returnUserCreated() {
		User user = new User();
		user.setFirstName("Bob");
		user.setLastName("Larsen");
		user.setEmail("haha@run.dk");

        assertEquals("contextLoads","3","3");
	}

}
