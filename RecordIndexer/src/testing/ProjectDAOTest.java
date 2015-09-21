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
import database.ProjectDAO;
import shared_model.Project;

public class ProjectDAOTest extends DAOTester{

	private Database db;
	private ProjectDAO projectDAO;
	
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
		
		List<Project> projects = db.getProjectDAO().getAll();
		for (Project u : projects) {
			db.getProjectDAO().deleteProject(u);
		}
		db.endTransaction(true);
		
		db = new Database();
		db.startTransaction();
		projectDAO = db.getProjectDAO();
	}

	@After
	public void tearDown() throws Exception {
		db.endTransaction(false);
		db = null;
		projectDAO = null;
	}

	@Test
	public void testAddProject() throws DatabaseException {
		Project temp = new Project(25, "1300 census", 30, 350, 10);
		Project temp2 = new Project(29, "1500 census", 60, 40, 15);
		
		projectDAO.addProject(temp);
		projectDAO.addProject(temp2);
		
		List<Project> projects = projectDAO.getAll();
		
		boolean found1 = false;
		boolean found2 = false;
		
		for (Project p : projects) {
			assertFalse(p.getID() == -1);
			
			if (!found1) {
				found1 = areEqual(p, temp, false);
			}		
			if (!found2) {
				found2 = areEqual(p, temp2, false);
			}
		}
		
		assertTrue(found1 && found2);
	}

	@Test
	public void testGetAll() throws DatabaseException {
		List<Project> projects = projectDAO.getAll();
		assertEquals(0, projects.size());
		projectDAO.addProject(new Project(27, "2000 census", 20, 303, 50));
		projects = projectDAO.getAll();
		assertEquals(1, projects.size());
	}

	@Test
	public void testUpdateProject() throws DatabaseException {
		Project temp = new Project(25, "1300 census", 30, 350, 10);
		Project temp2 = new Project(29, "1500 census", 60, 40, 15);
		
		projectDAO.addProject(temp);
		projectDAO.addProject(temp2);
		
		temp.setName("future census");
		temp.setRecordsHeight(3);
		temp.setRecordsPerImage(5);
		temp.setFirstYCoord(80);
		
		temp2.setName("past census");
		temp2.setRecordsHeight(77);
		temp2.setRecordsPerImage(78);
		temp2.setFirstYCoord(79);
		
		projectDAO.updateProject(temp);
		projectDAO.updateProject(temp2);
		
		boolean found1 = false;
		boolean found2 = false;
		
		List<Project> projects = projectDAO.getAll();
		
		for (Project p : projects) {
			assertFalse(p.getID() == -1);
			
			if (!found1) {
				found1 = areEqual(p, temp, false);
			}		
			if (!found2) {
				found2 = areEqual(p, temp2, false);
			}
		}
		
		assertTrue(found1 && found2);
	}

	@Test
	public void testDeleteProject() throws DatabaseException {
		Project temp = new Project(25, "1300 census", 30, 350, 10);
		Project temp2 = new Project(29, "1500 census", 60, 40, 15);
		
		projectDAO.addProject(temp);
		projectDAO.addProject(temp2);
		
		List<Project> projects = projectDAO.getAll();
		
		assertEquals(2, projects.size());
		
		projectDAO.deleteProject(temp);
		projectDAO.deleteProject(temp2);
		projects = projectDAO.getAll();
		
		assertEquals(0, projects.size());
	}
	
	@Test(expected=DatabaseException.class)
	public void testInvalidAdd() throws DatabaseException{
		
		Project invalidContact = new Project();
		projectDAO.addProject(invalidContact);
	}
	
	@Test(expected=DatabaseException.class)
	public void testInvalidDelete() throws DatabaseException{
		Project goodProject = new Project(25, "1300 census", 30, 350, 10);
		Project badProject = new Project(25, "1300 census", 30, 350, 10);
		projectDAO.addProject(goodProject);
		projectDAO.deleteProject(badProject);
	}
	
	@Test(expected=DatabaseException.class)
	public void testInvalidUpdate() throws DatabaseException{
		
		Project temp = new Project(25, "1300 census", 30, 350, 10);
		projectDAO.addProject(temp);
		int ID = temp.getID();
		Project temp2 = new Project(25, "1300 census", 30, 350, 10);
		temp2.setID(ID*10);
		projectDAO.updateProject(temp2);
	}

	private boolean areEqual(Project a, Project b, boolean compareIDs) {
		if (compareIDs) {
			if (a.getID() != b.getID()) {
				return false;
			}
		}	
		return (safeEquals(a.getName(), b.getName()) &&
				safeEquals(a.getFirstYCoord(), b.getFirstYCoord()) &&
				safeEquals(a.getRecordsHeight(), b.getRecordsHeight()) &&
				safeEquals(a.getRecordsPerImage(), b.getRecordsPerImage()));
	}
	
}
