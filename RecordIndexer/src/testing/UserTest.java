package testing;

import static org.junit.Assert.*;

import org.junit.*;

import shared_model.User;

public class UserTest {

	private User defaultUser;
	private User user;
	
	@Before
	public void setUp() {
		defaultUser = new User();
		user = new User("jimmer", "iscool", "jim", "joe",
						"jimmer@jimjoe.com", 27, 92);
	}
	
	@After
	public void tearDown() {
		defaultUser = null;
		user = null;
	}
	
	@Test
	public void testDefaultConstructor() {
		assertEquals(-1, defaultUser.getID());
		assertEquals(null,defaultUser.getUsername());
		assertEquals(null, defaultUser.getPassword());
		assertEquals(null, defaultUser.getFirstName());
		assertEquals(null, defaultUser.getLastName());
		assertEquals(null, defaultUser.getEmail());
		assertEquals(0, defaultUser.getIndexedRecords());
		assertEquals(0, defaultUser.getCurrentAssignedBatch());
		
	}
	
	@Test
	public void testValueConstructor() {
		assertEquals("jimmer", user.getUsername());
		assertEquals("iscool", user.getPassword());
		assertEquals("jim", user.getFirstName());
		assertEquals("joe", user.getLastName());
		assertEquals("jimmer@jimjoe.com", user.getEmail());
		assertEquals(27, user.getIndexedRecords());
		assertEquals(92, user.getCurrentAssignedBatch());
	}
	
	@Test
	public void testSetters() {
		user.setID(16);
		assertEquals(user.getID(), 16);
		user.setUsername("pizza");
		assertEquals(user.getUsername(), "pizza");
		user.setPassword("cheese");
		assertEquals(user.getPassword(), "cheese");
		user.setFirstName("bob");
		assertEquals(user.getFirstName(), "bob");
		user.setLastName("doe");
		assertEquals(user.getLastName(), "doe");
		user.setEmail("hi.cs");
		assertEquals(user.getEmail(), "hi.cs");
		user.setIndexedRecords(7);
		assertEquals(user.getIndexedRecords(), 7);
		user.setCurrentAssignedBatch(25);
		assertEquals(user.getCurrentAssignedBatch(), 25);
	}

}
