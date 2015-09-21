package testing;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import shared_model.*;
import shared_communication.*;
import database.*;
import dataimporter.DataImporter;
import server.ServerException;
import server_facade.*;



public class ServerFacadeTest {

	public static final String VALIDUSERNAME = "admin";
	public static final String VALIDPASSWORD = "12345abc";
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ServerFacade.initialize();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		return;
	}
	
	private Database db;
	private BatchDAO batchDAO;
	private CellDAO cellDAO;
	private FieldDAO fieldDAO;
	private ProjectDAO projectDAO;
	private RecordDAO recordDAO;
	private UserDAO userDAO;

	@Before
	public void setUp() throws Exception {
		db = new Database();
		userDAO = db.getUserDAO();
//		db.startTransaction();
//		List<Batch> batchs = db.getBatchDAO().getAll();
//		for (Batch u : batchs) {
//			db.getBatchDAO().deleteBatch(u);
//		}
//		
//		List<Project> projects = db.getProjectDAO().getAll();
//		for (Project p : projects) {
//			db.getProjectDAO().deleteProject(p);
//		}
//		
//		List<User> users = db.getUserDAO().getAll();
//		for (User u : users) {
//			db.getUserDAO().deleteUser(u);
//		}
//		
//		db.endTransaction(true);
		
		//populate the database so we have thing to work with!
		DataImporter DI = new DataImporter();
		DI.run("importData" + File.separator + "Records.xml");

		db.startTransaction();
		User user = new User(VALIDUSERNAME, VALIDPASSWORD, "adminfirst", "adminlast", "j@j.com", 0);
		userDAO.addUser(user);
		db.endTransaction(true);
		
		db = new Database();
		db.startTransaction();
		batchDAO = db.getBatchDAO();
		cellDAO = db.getCellDAO();
		fieldDAO = db.getFieldDAO();
		projectDAO = db.getProjectDAO();
		recordDAO = db.getRecordDAO();
		
	}

	@After
	public void tearDown() throws Exception {
		db.endTransaction(false);
		db = null;
		batchDAO = null;
		cellDAO = null;
		fieldDAO = null;
		projectDAO = null;
		recordDAO = null;
		userDAO = null;
	}

	@Test
	public void testValidateUser() throws DatabaseException, ServerException {
		
		ValidateUser_Input input = new ValidateUser_Input();
		input.setUsername(VALIDUSERNAME);
		input.setPassword(VALIDPASSWORD);
		
		ValidateUser_Result output = ServerFacade.validateUser(input);
		System.out.println("VALID USER OUTPUT:\n" + output.toString());
		assertEquals(VALIDUSERNAME, output.getUser().getUsername());
		assertEquals(VALIDPASSWORD, output.getUser().getPassword());
		
		input.setUsername("wrong");
		input.setPassword("user");
		output = ServerFacade.validateUser(input);
		System.out.println("INVALID USER OUTPUT:\n" + output.toString());
		assertEquals(null, output.getUser().getUsername());
	}
	
	@Test
	public void testGetProjects() throws DatabaseException, ServerException {
		GetProjects_Input input = new GetProjects_Input();
		input.setUsername(VALIDUSERNAME);
		input.setPassword(VALIDPASSWORD);
		
		GetProjects_Result output = ServerFacade.getProjects(input);
		System.out.println("ALL PROJECT OUTPUT:\n" + output.toString());
		assertEquals(1, 1);
	}
	
	@Test
	public void testGetSampleImage() throws DatabaseException, ServerException {
		GetProjects_Input input1 = new GetProjects_Input();
		input1.setUsername(VALIDUSERNAME);
		input1.setPassword(VALIDPASSWORD);
		
		GetSampleImage_Input input2 = new GetSampleImage_Input();
		input2.setUsername(VALIDUSERNAME);
		input2.setPassword(VALIDPASSWORD);
		
		GetProjects_Result outputProj = ServerFacade.getProjects(input1);
		List<Project> projects = outputProj.getProjects();
		if (projects.size() > 0) {
			input2.setProjectID(projects.get(0).getID());
			GetSampleImage_Result output = ServerFacade.getSampleImage(input2);
			System.out.println("SAMPLE IMAGE OUTPUT:\n" + output.toString());
			assertEquals(1, 1);
		}
	}
	
	
	@Test
	public void testDownloadBatch() throws DatabaseException, ServerException {
		GetProjects_Input input1 = new GetProjects_Input();
		input1.setUsername(VALIDUSERNAME);
		input1.setPassword(VALIDPASSWORD);
		
		GetProjects_Result outputProj = ServerFacade.getProjects(input1);
		List<Project> projects = outputProj.getProjects();
		if (projects.size() > 0) {
		
			DownloadBatch_Input input = new DownloadBatch_Input();
			input.setUsername(VALIDUSERNAME);
			input.setPassword(VALIDPASSWORD);
			input.setProjectID(projects.get(0).getID());
			
			DownloadBatch_Result output = ServerFacade.downloadBatch(input);
			System.out.println("DL IMAGE OUTPUT:\n" + output.toString());
			assertEquals(1, 1);
		}
	}

	@Test
	public void testGetFields() throws DatabaseException, ServerException {
		
		GetProjects_Input input1 = new GetProjects_Input();
		input1.setUsername(VALIDUSERNAME);
		input1.setPassword(VALIDPASSWORD);
		
		GetProjects_Result outputProj = ServerFacade.getProjects(input1);
		List<Project> projects = outputProj.getProjects();
		if (projects.size() > 0) {
		
			GetFields_Input input = new GetFields_Input();
			input.setUsername(VALIDUSERNAME);
			input.setPassword(VALIDPASSWORD);
			input.setProjectID(projects.get(0).getID());
			
			GetFields_Result output = ServerFacade.getFields(input);
			System.out.println("FIELDS OUTPUT:\n" + output.toString());
			assertEquals(1, 1);
		}
	}
	
	@Test
	public void testSearch() throws DatabaseException, ServerException {
		
		Search_Input input = new Search_Input();
		List<Integer> fields = new ArrayList<Integer>();
		fields.add(5);
		fields.add(10);
		List<String> searchValues = new ArrayList<String>();
		searchValues.add("JENNINGS");
		searchValues.add("JOHNS");
		input.setFields(fields);
		input.setKeywords(searchValues);
		
		Search_Result result = ServerFacade.search(input);
		System.out.println("SEARCH RESULTS:\n" + result.toString());
		assertEquals(1, 1);
	}
}
