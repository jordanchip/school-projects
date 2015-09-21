package testing;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import database.Database;
import database.DatabaseException;
import database.UserDAO;
import shared_model.User;

public class UserDAOTest extends DAOTester {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		//Set up the database
		Database.initialize();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		return;
	}
	
	private Database db;
	private UserDAO userDAO;

	@Before
	public void setUp() throws Exception {
		db = new Database();
		db.startTransaction();
		
		List<User> users = db.getUserDAO().getAll();
		for (User u : users) {
			db.getUserDAO().deleteUser(u);
		}
		db.endTransaction(true);
		
		db = new Database();
		db.startTransaction();
		userDAO = db.getUserDAO();
	}

	@After
	public void tearDown() throws Exception {
		db.endTransaction(false);
		db = null;
		userDAO = null;
	}
	
	@Test
	public void testGetAll()  throws DatabaseException {
		List<User> users = userDAO.getAll();
		assertEquals(0, users.size());
		userDAO.addUser(new User("jchip", "pizza", "j", "chip", "jchip@gmail.com",
				0, -1));
		users = userDAO.getAll();
		assertEquals(1, users.size());
	}
	
	@Test
	public void testAddUser() throws DatabaseException {
		User temp = new User("jchip", "pizza", "j", "chip", "jchip@gmail.com",
							0, -1);
		User temp2 = new User("bob", "did", "not", "want", "this", 5, 2);
		
		userDAO.addUser(temp);
		userDAO.addUser(temp2);
		
		List<User> users = userDAO.getAll();
		
		boolean found1 = false;
		boolean found2 = false;
		
		for (User u : users) {
			
			assertFalse(u.getID() == -1);
			
			if (!found1) {
				found1 = areEqual(u, temp, false);
			}		
			if (!found2) {
				found2 = areEqual(u, temp2, false);
			}
		}
		
		assertTrue(found1 && found2);
	}
	
	@Test
	public void testValidateUser() throws DatabaseException {
		User temp = new User("jchip", "pizza", "j", "chip", "jchip@gmail.com",
							0, -1);
		
		userDAO.addUser(temp);
		
		User badInput = userDAO.validateUser(new User("bob", "did", "not", "want", "this", 5, 2));
		assertEquals(-1, badInput.getID());
		
		User goodInput = userDAO.validateUser(temp);
		assertEquals("jchip", goodInput.getUsername());
		assertEquals("pizza", goodInput.getPassword());
	}
	
	@Test
	public void testUpdate() throws DatabaseException {
		User temp = new User("jchip", "pizza", "j", "chip", "jchip@gmail.com",
				0, -1);
		User temp2 = new User("bob", "did", "not", "want", "this", 5, 2);
		
		userDAO.addUser(temp);
		userDAO.addUser(temp2);
		
		temp.setEmail("jimmer");
		temp.setFirstName("joe");
		temp.setLastName("manly");
		temp.setPassword("cheese");
		temp.setUsername("timmy");
		temp.setCurrentAssignedBatch(73);
		
		temp2.setEmail("kathy@kathy.com");
		temp2.setFirstName("kate");
		temp2.setLastName("stevenson");
		temp2.setPassword("12345thing");
		temp2.setUsername("katycat");
		temp2.setCurrentAssignedBatch(10);
		
		userDAO.updateUser(temp);
		userDAO.updateUser(temp2);
		
		List<User> users = userDAO.getAll();
		assertEquals(2, users.size());
		boolean found1 = false;
		boolean found2 = false;
		
		for (User u : users) {
		
		assertFalse(u.getID() == -1);
		
		if (!found1) {
			found1 = areEqual(u, temp, false);
		}		
		if (!found2) {
			found2 = areEqual(u, temp2, false);
		}
		}
		
		assertTrue(found1 && found2);
		
	}
	
	@Test
	public void testDeleteUser() throws DatabaseException {
		User temp = new User("jchip", "pizza", "j", "chip", "jchip@gmail.com",
				0, -1);
		User temp2 = new User("bob", "did", "not", "want", "this", 5, 2);
		
		userDAO.addUser(temp);
		userDAO.addUser(temp2);
		
		List<User> users = userDAO.getAll();
		userDAO.deleteUser(temp);
		userDAO.deleteUser(temp2);
		users = userDAO.getAll();
		assertEquals(0, users.size());
	}
	
	@Test(expected=DatabaseException.class)
	public void testInvalidAdd() throws DatabaseException{
		
		User invalidContact = new User();
		userDAO.addUser(invalidContact);
	}
	
	@Test(expected=DatabaseException.class)
	public void testInvalidDelete() throws DatabaseException{
		
		User temp = new User("jchip", "pizza", "j", "chip", "jchip@gmail.com",
				0, -1);
		userDAO.deleteUser(temp);
	}
	
	@Test(expected=DatabaseException.class)
	public void testInvalidUpdate() throws DatabaseException{
		
		User temp = new User("jchip", "pizza", "j", "chip", "jchip@gmail.com",
				0, -1);
		userDAO.addUser(temp);
		int ID = temp.getID();
		User temp2 = new User("jchip", "pizza", "j", "chip", "jchip@gmail.com",
				0, -1);
		temp2.setID(ID*10);
		userDAO.updateUser(temp2);
	}

	private boolean areEqual(User a, User b, boolean compareIDs) {
		if (compareIDs) {
			if (a.getID() != b.getID()) {
				return false;
			}
		}	
		return (safeEquals(a.getUsername(), b.getUsername()) &&
				safeEquals(a.getEmail(), b.getEmail()) &&
				safeEquals(a.getPassword(), b.getPassword()) &&
				safeEquals(a.getFirstName(), b.getFirstName()) &&
				safeEquals(a.getLastName(), b.getLastName()) &&
				safeEquals(a.getPassword(), b.getPassword()));
	}
	
	
}
