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
import database.FieldDAO;
import database.ProjectDAO;
import shared_model.Project;
import shared_model.Field;

public class FieldDAOTest extends DAOTester {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Database.initialize();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		return;
	}

	private Database db;
	private FieldDAO fieldDAO;
	private ProjectDAO projectDAO;
	
	@Before
	public void setUp() throws Exception {
		db = new Database();
		db.startTransaction();
		
		List<Field> fields = db.getFieldDAO().getAll();
		for (Field f : fields) {
			db.getFieldDAO().deleteField(f);
		}
		
		List<Project> projects = db.getProjectDAO().getAll();
		for (Project u : projects) {
			db.getProjectDAO().deleteProject(u);
		}
		
		db.endTransaction(true);
		
		db = new Database();
		db.startTransaction();
		fieldDAO = db.getFieldDAO();
		projectDAO = db.getProjectDAO();
	}

	@After
	public void tearDown() throws Exception {
		db.endTransaction(false);
		db = null;
		fieldDAO = null;
		projectDAO = null;
	}

	@Test
	public void testAddField() throws DatabaseException {
		Field temp = new Field("First Name", 2, 10, 5, "pie.html", "zoom.com");
		Field temp2 = new Field("Last Name", 3, 11, 10, "piez.html", "m.com");
		
		fieldDAO.addField(temp);
		fieldDAO.addField(temp2);
		
		List<Field> fields = fieldDAO.getAll();
		
		boolean found1 = false;
		boolean found2 = false;
		
		for (Field f : fields) {
			assertFalse(f.getID() == -1);
			
			if (!found1) {
				found1 = areEqual(f, temp, false);
			}		
			if (!found2) {
				found2 = areEqual(f, temp2, false);
			}
		}
		
		assertTrue(found1 && found2);
	}

	@Test
	public void testGetAll() throws DatabaseException {
		List<Field> fields = fieldDAO.getAll();
		int size = fields.size();
		assertEquals(0, fields.size());
		fieldDAO.addField(new Field("First Name", 3, 6, 2, "doe.com", "hi.cmm"));
		fieldDAO.addField(new Field("Last Name", 5, 2, 10, "m.com", "hi.ss"));
		fields = fieldDAO.getAll();
		assertEquals(size+2, fields.size());
	}

	@Test
	public void testGetAllFieldsForProject() throws DatabaseException  {
		
		Project goodProject = new Project(25, "1300 census", 30, 350, 10);
		Project badProject = new Project(29, "1500 census", 60, 40, 15);
		projectDAO.addProject(goodProject);
		projectDAO.addProject(badProject);
		Field goodField = new Field("First Name", goodProject.getID(), 6, 2, "doe.com", "hi.cmm");
		Field badField = new Field("Last Name", badProject.getID()*10, 2, 10, "m.com", "hi.ss");
		fieldDAO.addField(goodField);
		fieldDAO.addField(badField);

		List<Field> fields = fieldDAO.getAllFieldsForProject(goodProject);
		
		//Check that the goodField IS in the database, and
		//check that the badField ISNT in the database.
		boolean found1 = false;
		boolean found2 = true;
		
		for (Field f : fields) {
			assertFalse(f.getID() == -1);
			
			if (!found1) {
				found1 = areEqual(f, goodField, false);
			}		
			if (found2) {
				found2 = !areEqual(f, badField, false);
			}
		}

		assertTrue(found1 && found2);
	}

	@Test
	public void testUpdateField() throws DatabaseException  {
		Field temp = new Field("First Name", 2, 6, 2, "doe.com", "hi.cmm");
		Field temp2 = new Field("Last Name", 3, 2, 10, "m.com", "hi.ss");
		Field shouldntFindField = new Field("Last Name", 3, 2, 10, "m.com", "hi.ss");
		
		fieldDAO.addField(temp);
		fieldDAO.addField(temp2);
		
		temp.setName("Age");
		temp.setProjectID(25);
		temp.setxCoord(25);
		temp.setWidth(20);
		temp.setHelpHTMLPath("joey.com");
		temp.setKnownDataPath("ee.wq");
		
		temp2.setName("Age");
		temp2.setProjectID(25);
		temp2.setxCoord(25);
		temp2.setWidth(20);
		temp2.setHelpHTMLPath("joey.com");
		temp2.setKnownDataPath("ee.wq");
		
		fieldDAO.updateField(temp);
		fieldDAO.updateField(temp2);
		
		boolean found1 = false;
		boolean found2 = false;
		boolean found3 = true;
		
		List<Field> fields = fieldDAO.getAll();
		
		for (Field f : fields) {
			assertFalse(f.getID() == -1);
			
			if (!found1) {
				found1 = areEqual(f, temp, false);
			}		
			if (!found2) {
				found2 = areEqual(f, temp2, false);
			}
			if (found3) {
				found3 = !areEqual(f, shouldntFindField, false);
			}
		}
	}

	@Test
	public void testDeleteField() throws DatabaseException  {
		Field temp = new Field("First Name", 2, 10, 5, "pie.html", "zoom.com");
		Field temp2 = new Field("Last Name", 3, 11, 10, "piez.html", "m.com");
		
		fieldDAO.addField(temp);
		fieldDAO.addField(temp2);
		//We already know that add (supposedly) works, so lets
		//remove one of the fields and see if we can find it.
		fieldDAO.deleteField(temp2);
		List<Field> fields = fieldDAO.getAll();
		
		boolean found1 = false;
		boolean found2 = true;
		
		for (Field f : fields) {
			assertFalse(f.getID() == -1);
			
			if (!found1) {
				found1 = areEqual(f, temp, false);
			}		
			if (found2) {
				found2 = !areEqual(f, temp2, false);
			}
		}
		
		assertTrue(found1 && found2);
	}
	
	@Test(expected=DatabaseException.class)
	public void testInvalidAdd() throws DatabaseException{
		
		Field invalidContact = new Field();
		fieldDAO.addField(invalidContact);
	}
	
	@Test(expected=DatabaseException.class)
	public void testInvalidDelete() throws DatabaseException{
		Field goodField = new Field("First Name", 2, 10, 5, "pie.html", "zoom.com");
		Field badField = new Field("First Name", 2, 10, 5, "pie.html", "zoom.com");
		fieldDAO.addField(goodField);
		fieldDAO.deleteField(badField);
	}
	
	@Test(expected=DatabaseException.class)
	public void testInvalidUpdate() throws DatabaseException{
		
		Field temp = new Field("First Name", 2, 10, 5, "pie.html", "zoom.com");
		fieldDAO.addField(temp);
		int ID = temp.getID();
		Field temp2 = new Field("First Name", 2, 10, 5, "pie.html", "zoom.com");
		temp2.setID(ID*10);
		fieldDAO.updateField(temp2);
	}
	
	private boolean areEqual(Field a, Field b, boolean compareIDs) {
		if (compareIDs) {
			if (a.getID() != b.getID()) {
				return false;
			}
		}	
		return (safeEquals(a.getName(), b.getName()) &&
				safeEquals(a.getHelpHTMLPath(), b.getHelpHTMLPath()) &&
				safeEquals(a.getKnownDataPath(), b.getKnownDataPath()) &&
				safeEquals(a.getProjectID(), b.getProjectID()) &&
				safeEquals(a.getWidth(), b.getWidth()) && 
				safeEquals(a.getxCoord(), b.getxCoord()));
	}

}
