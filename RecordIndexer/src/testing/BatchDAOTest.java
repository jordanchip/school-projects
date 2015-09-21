package testing;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import database.BatchDAO;
import database.Database;
import database.DatabaseException;
import database.ProjectDAO;
import database.UserDAO;
import shared_model.Batch;
import shared_model.Project;
import shared_model.User;

public class BatchDAOTest extends DAOTester{

	private Database db;
	private BatchDAO batchDAO;
	private ProjectDAO projectDAO;
	private UserDAO userDAO;
	
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
		db = new Database();
		db.startTransaction();
		
		List<Batch> batchs = db.getBatchDAO().getAll();
		for (Batch u : batchs) {
			db.getBatchDAO().deleteBatch(u);
		}
		
		List<Project> projects = db.getProjectDAO().getAll();
		for (Project p : projects) {
			db.getProjectDAO().deleteProject(p);
		}
		
		List<User> users = db.getUserDAO().getAll();
		for (User u : users) {
			db.getUserDAO().deleteUser(u);
		}
		
		db.endTransaction(true);
		
		db = new Database();
		db.startTransaction();
		batchDAO = db.getBatchDAO();
		projectDAO = db.getProjectDAO();
		userDAO = db.getUserDAO();
	}

	@After
	public void tearDown() throws Exception {
		db.endTransaction(false);
		db = null;
		batchDAO = null;
		projectDAO = null;
		userDAO = null;
	}
	
	@Test
	public void testAddBatch() throws DatabaseException {

		Batch temp = new Batch(10, 5, "java/java.com", false);
		Batch temp2 = new Batch(2, 3, "youtube.com", true);
		
		batchDAO.addBatch(temp);
		batchDAO.addBatch(temp2);
		
		List<Batch> batches = batchDAO.getAll();
		
		boolean found1 = false;
		boolean found2 = false;
		
		for (Batch u : batches) {
			
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

//	@Test
//	public void testUpdateBatch() throws DatabaseException {
//		Batch temp = new Batch(10, 5, "java/java.com", false);
//		batchDAO.addBatch(temp);
//		List<Batch> batches = batchDAO.getAll();
//		for (Batch b : batches) {
//		
//			assertFalse(b.isInUse());
//		}
//		
//		temp.setInUse(true);
//		batchDAO.updateBatch(temp);
//		batches = batchDAO.getAll();
//		
//		//Now we will see if the new batch in the database contains the correct info.
//		
//		assertEquals(1, batches.size());
//		for (Batch b : batches) {
//		
//			assertTrue(b.isInUse());
//		}
//	}

	@Test
	public void testGetSampleBatch() throws DatabaseException {
		
		Project goodProj = new Project(25, "1300 census", 30, 350, 10);
		Project badProj = new Project(29, "1500 census", 60, 40, 15);
		Batch goodBatch = new Batch(5, "java/java.com", false);
		Batch notLinkedBatch = new Batch(44, "watcher.com", false);
		
		projectDAO.addProject(goodProj);
		projectDAO.addProject(badProj);
		goodBatch.setProjectID(goodProj.getID());
		notLinkedBatch.setProjectID(badProj.getID()*10);
		batchDAO.addBatch(goodBatch);
		batchDAO.addBatch(notLinkedBatch);
		
		Batch batch = batchDAO.getSampleBatch(goodProj);
		assertEquals(batch.getProjectID(), goodProj.getID());
		assertEquals(batch.getID(), goodBatch.getID());
		
		Batch badBatch = batchDAO.getSampleBatch(badProj);
		assertEquals(-1, badBatch.getID());
		
	}

	@Test
	public void testGetBatch() throws DatabaseException {
		Project goodProj = new Project(25, "1300 census", 30, 350, 10);
		Project badProj = new Project(29, "1500 census", 60, 40, 15);
		User user = new User("jchip", "pizza", "j", "chip", "jchip@gmail.com",
				0, -1);
		Batch goodBatch = new Batch(5, "java/java.com", false);
		Batch notLinkedBatch = new Batch(44, "watcher.com", false);
		
		projectDAO.addProject(goodProj);
		projectDAO.addProject(badProj);
		goodBatch.setProjectID(goodProj.getID());
		notLinkedBatch.setProjectID(badProj.getID()*10);
		batchDAO.addBatch(goodBatch);
		batchDAO.addBatch(notLinkedBatch);
		
		userDAO.addUser(user);
		
		Batch batch = batchDAO.getBatch(goodProj, user);
		//Ensure the batch is linked to the project
		assertEquals(batch.getProjectID(), goodProj.getID());
		//Ensure the batch has been flagged as in Use
		assertTrue(batch.isInUse());
		//Ensure that the user is correctly linked to the batch.
		assertEquals(batch.getID(), user.getCurrentAssignedBatch());
	}

	@Test
	public void testDeleteBatch() throws DatabaseException {
		Batch goodBatch = new Batch(5, "java/java.com", false);
		batchDAO.addBatch(goodBatch);
		List<Batch> batches = batchDAO.getAll();
		int size = batches.size();
		batchDAO.deleteBatch(goodBatch);
		batches = batchDAO.getAll();
		assertEquals(size-1, batches.size());
	}
	
	@Test(expected=DatabaseException.class)
	public void testInvalidAdd() throws DatabaseException{
		
		Batch invalidContact = new Batch();
		batchDAO.addBatch(invalidContact);
	}
	
	@Test(expected=DatabaseException.class)
	public void testInvalidDelete() throws DatabaseException{
		Batch goodBatch = new Batch(5, "java/java.com", false);
		Batch badBatch = new Batch(5, "java/java.com", false);
		batchDAO.addBatch(goodBatch);
		batchDAO.deleteBatch(badBatch);
	}
	
//	@Test(expected=DatabaseException.class)
//	public void testInvalidUpdate() throws DatabaseException{
//		
//		Batch temp = new Batch(5, "java/java.com", false);
//		batchDAO.addBatch(temp);
//		int ID = temp.getID();
//		Batch temp2 = new Batch(5, "java/java.com", false);
//		temp2.setID(ID*10);
//		batchDAO.updateBatch(temp2);
//	}

	private boolean areEqual(Batch a, Batch b, boolean compareIDs) {
		if (compareIDs) {
			if (a.getID() != b.getID()) {
				return false;
			}
		}	
		return (safeEquals(a.getProjectID(), b.getProjectID()) &&
				safeEquals(a.getPath(), b.getPath()) &&
				safeEquals(a.isInUse(), b.isInUse()));
	}
	
}
