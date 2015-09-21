package testing;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import shared_communication.Search_Result;
import shared_model.*;
import database.*;
import dataimporter.*;

public class SearchDAOTester {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Database.initialize();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		return;
	}
	
	private Database db;
	private BatchDAO batchDAO;
	private FieldDAO fieldDAO;
	private ProjectDAO projectDAO;
	private UserDAO userDAO;
	private RecordDAO recordDAO;
	private SearchDAO searchDAO;
	
	@Before
	public void setUp() throws Exception {
		db = new Database();
		db.startTransaction();
		
		List<Batch> batchs = db.getBatchDAO().getAll();
		for (Batch u : batchs) {
			db.getBatchDAO().deleteBatch(u);
		}
		
		List<Field> fields = db.getFieldDAO().getAll();
		for (Field f : fields) {
			db.getFieldDAO().deleteField(f);
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
		fieldDAO = db.getFieldDAO();
		recordDAO = db.getRecordDAO();
		searchDAO = db.getSearchDAO();
	}

	@After
	public void tearDown() throws Exception {
		db.endTransaction(false);
		db = null;
		batchDAO = null;
		projectDAO = null;
		userDAO = null;
		fieldDAO = null;
		recordDAO = null;
		searchDAO = null;
	}

	@Test
	public void testSearch() throws DatabaseException {
		List<String> searchValues = new ArrayList<String>();
		DataImporter DI = new DataImporter();
		DI.run("importData/Records.xml");
		
//		fieldDAO.addField(new Field("Last Name", 10, 20, 15,
//				"record.txt", "record1.txt"));
//		fieldDAO.addField(new Field("First Name", 10, 20, 15,
//				"record.txt", "record1.txt"));
		searchValues.add("FOX");
		searchValues.add("JARVIS");
//		batchDAO.addBatch(new Batch(10, "image.url"));
//		recordDAO.addRecord(new Record(10, 2));

		List<Field> fields1 = fieldDAO.getAll();
		List<Integer> fields = new ArrayList<Integer>();
		for (Field f : fields1)
			fields.add(f.getID());
		Search_Result SR = searchDAO.search(fields, searchValues);
		assertEquals(2, SR.getBatchIDs().size());
		assertEquals(2, SR.getImageURLs().size());
		assertEquals(2, SR.getRowNumbers().size());
		assertEquals(2, SR.getFieldIDs().size());
		System.out.println(SR.toString());
		
	}

}
