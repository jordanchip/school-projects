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
import shared_model.User;

public class DatabaseTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Database.initialize();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		return;
	}

	@Before
	public void setUp() throws Exception {
		Database db = new Database();
		db.startTransaction();
		List<User> users = db.getUserDAO().getAll();
		for (User u : users)
			db.getUserDAO().deleteUser(u);
		db.endTransaction(true);
	}

	@After
	public void tearDown() throws Exception {
		return;
	}

	@Test
	public void testCommit() {
		try {
			addUser(true);
			Database db = new Database();
			db.startTransaction();
			List<User> users = db.getUserDAO().getAll();
			assertEquals(2, users.size());
			db.endTransaction(false);
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testRollback() {
		try {
			addUser(false);
			Database db = new Database();
			db.startTransaction();
			List<User> users = db.getUserDAO().getAll();
			assertEquals(0, users.size());
			db.endTransaction(false);
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
	}
	
	private void addUser(boolean commit) {
		Database db = new Database();
		try {
			db.startTransaction();
			List<User> users = db.getUserDAO().getAll();
			assertEquals(0, users.size());
			
			db.getUserDAO().addUser(new User("Jimmy", "joe", "sue", "sally", "j@j.com", 0));
			db.getUserDAO().addUser(new User("bobby", "tim", "you", "pizza", "a@a.net", 2));
			users = db.getUserDAO().getAll();
			assertEquals(2, users.size());
			
			db.endTransaction(commit);
			
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
