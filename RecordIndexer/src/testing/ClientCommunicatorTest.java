package testing;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import client_facade.ClientCommunicator;
import client_facade.ClientException;
import dataimporter.*;
import server.*;
import shared_communication.*;

public class ClientCommunicatorTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		String[] args = new String[0];
		Server.main(args);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	
	private ClientCommunicator comm;

	@Before
	public void setUp() throws Exception {
		String args[] = new String[1];
		args[0] = "importData/Records.xml";
		/*
		 * We are going to run the data importer before EACH test so that
		 * The database is filled with set values each time.  This is so
		 * each test doesn't affect one another, and so that we already have
		 * a filled database to run tests on.
		 */
		DataImporter.main(args);
		comm = new ClientCommunicator();
		comm.setHostAndPort("localhost", 8080);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testValidateUser() throws ClientException {
		ValidateUser_Input params = new ValidateUser_Input();
		params.setUsername("sheila");
		params.setPassword("parker");
		ValidateUser_Result result = comm.validateUser(params);
		
		//Check that the user has an id greater than 1
		assertTrue(result.getUser().getID() >= 0);
		assertEquals("Sheila", result.getUser().getFirstName());
		assertEquals("Parker", result.getUser().getLastName());
		assertEquals(0, result.getUser().getIndexedRecords());
	}
	
	@Test
	public void testGetProjects() throws ClientException {
		GetProjects_Input params = new GetProjects_Input();
		params.setUsername("sheila");
		params.setPassword("parker");
		GetProjects_Result result = comm.getProjects(params);
		
		assertEquals(result.getProjects().size(), 3);
	}

	@Test
	public void testGetSampleImage() throws ClientException {
		GetSampleImage_Input params = new GetSampleImage_Input();
		params.setUsername("sheila");
		params.setPassword("parker");
		params.setProjectID(1);
		GetSampleImage_Result result = comm.getSampleImage(params);
		
		assertTrue(result.getBatch().getID() >= 0);
		
		ValidateUser_Input params2 = new ValidateUser_Input();
		params.setUsername("sheila");
		params.setPassword("parker");
		ValidateUser_Result result2 = comm.validateUser(params2);
		
		//Ensure the user has not be flagged as working on a batch yet.
		assertTrue(result2.getUser().getCurrentAssignedBatch() <= 0);
	}

	@Test
	public void testDownloadBatch() throws ClientException {
		ValidateUser_Input params = new ValidateUser_Input();
		params.setUsername("sheila");
		params.setPassword("parker");
		ValidateUser_Result result = comm.validateUser(params);
		
		assertTrue(result.getUser().getCurrentAssignedBatch() <= 0);
		
		DownloadBatch_Input params2 = new DownloadBatch_Input();
		params2.setUsername("sheila");
		params2.setPassword("parker");
		params2.setProjectID(1);
		DownloadBatch_Result result2 = comm.downloadBatch(params2);
		
		assertEquals(result2.getProject().getID(), 1);
		assertEquals(result2.getProject().getName(), "1890 Census");
		assertEquals(result2.getProject().getRecordsPerImage(), 8);
		assertEquals(result2.getProject().getFirstYCoord(), 199);
		assertEquals(result2.getProject().getRecordsHeight(), 60);
		
		assertFalse(result2.getFields().size() == 0);
		result = comm.validateUser(params);
		//Ensure the user is linked up to project 1
		assertTrue(result.getUser().getCurrentAssignedBatch() == 1);
	}

	@Test
	public void testGetFields() throws ClientException {
		GetFields_Input params = new GetFields_Input();
		params.setUsername("sheila");
		params.setPassword("parker");
		params.setProjectID(1);
		GetFields_Result result = comm.getFields(params);
		
		assertEquals(result.getFields().size(), 4);
		
		params.setProjectID(0);
		result = comm.getFields(params);
		
		assertEquals(result.getFields().size(), 13);
	}

	@Test
	public void testSubmitBatch() throws ClientException {
		testDownloadBatch();
		
		SubmitBatch_Input params = new SubmitBatch_Input();
		params.setUsername("sheila");
		params.setPassword("parker");
		params.setBatchID(1);
		String parameters = "tim, joe, M, 10; SAL, JEFF, F, 15; me, you, M, 99;"
				+ " m,m,m,10;y,y,y,11;p,p,p,15;p,p,p,18;p,p,p,20";
		params.assignCells(parameters);
		SubmitBatch_Result result = comm.submitBatch(params);
		
		assertFalse(result.isHasFailed());
	}
	
	@Test
	public void testSearch() throws ClientException {
		String[] args = new String[4];
		args[0] = "sheila";
		args[1] = "parker";
		args[2] = "10,11,12,13";
		args[3] = "FOX, hector, 30";
		Search_Input params = new Search_Input(args);
		Search_Result result = comm.search(params);
		
		assertEquals(result.getBatchIDs().size(), 14);
		assertEquals(result.getFieldIDs().size(), 14);
		assertEquals(result.getImageURLs().size(), 14);
		assertEquals(result.getRowNumbers().size(), 14);
		
	}
	
	@Test
	public void testInvalidUserValidateUser() throws ClientException {
		ValidateUser_Input params = new ValidateUser_Input();
		params.setUsername("sheila1");
		params.setPassword("parker");
		ValidateUser_Result result = comm.validateUser(params);
		
		//Check that the user has an id of -1, that is,
		//that it has not been found
		assertEquals(result.getUser().getID(), -1);
	}
	
	@Test
	public void testInvalidPassValidateUser() throws ClientException {
		ValidateUser_Input params = new ValidateUser_Input();
		params.setUsername("sheila");
		params.setPassword("parker1");
		ValidateUser_Result result = comm.validateUser(params);
		
		//Check that the user has an id of -1, that is,
		//that it has not been found
		assertEquals(result.getUser().getID(), -1);
	}
	
	@Test
	public void testInvalidGetProjects() throws ClientException {
		GetProjects_Input params = new GetProjects_Input();
		params.setUsername("sheila3");
		params.setPassword("parker3");
		GetProjects_Result result = comm.getProjects(params);
		
		assertEquals(result.getProjects(), null);
	}
	
	@Test
	public void testInvalidGetSampleImage() throws ClientException {
		GetSampleImage_Input params = new GetSampleImage_Input();
		params.setUsername("timtextra");
		params.setPassword("timtextra");
		params.setProjectID(1);
		GetSampleImage_Result result = comm.getSampleImage(params);
		
		assertTrue(result.getBatch().getID() <= 0);
		
		ValidateUser_Input params2 = new ValidateUser_Input();
		params.setUsername("sheila");
		params.setPassword("parker");
		ValidateUser_Result result2 = comm.validateUser(params2);
		
		//Ensure the user has not be flagged as working on a batch yet.
		assertTrue(result2.getUser().getID() <= 0);
	}
	
	@Test
	public void testInvalidAssignmentDownloadBatch() throws ClientException {
		//Set up a user as already having a batch.
		testDownloadBatch();
		
		DownloadBatch_Input params = new DownloadBatch_Input();
		params.setUsername("sheila");
		params.setPassword("parker");
		params.setProjectID(1);
		DownloadBatch_Result result = comm.downloadBatch(params);
		
		assertTrue(result.isHasFailed());
	}
	
	@Test
	public void testInvalidProjectIDDownloadBatch() throws ClientException {
		
		DownloadBatch_Input params = new DownloadBatch_Input();
		params.setUsername("sheila");
		params.setPassword("parker");
		params.setProjectID(4);
		DownloadBatch_Result result = comm.downloadBatch(params);
		
		assertTrue(result.isHasFailed());
	}
	
	@Test
	public void testInvalidGetFields() throws ClientException {
		GetFields_Input params = new GetFields_Input();
		params.setUsername("sheila");
		params.setPassword("parker");
		params.setProjectID(4);
		GetFields_Result result = comm.getFields(params);
		
		assertTrue(result.isHasFailed());
	}
	
	@Test
	public void testInvalidParamsSubmitBatch() throws ClientException {
		testDownloadBatch();
		
		SubmitBatch_Input params = new SubmitBatch_Input();
		params.setUsername("sheila");
		params.setPassword("parker");
		params.setBatchID(1);
		//This following variable only has 3 values in the last section, when it should
		//have 4.
		String parameters = "tim, joe, M, 10; SAL, JEFF, F, 15; me, you, M, 99;"
				+ " m,m,m,10;y,y,y,11;p,p,p,15;p,p,p,18;p,p,p";
		params.assignCells(parameters);
		SubmitBatch_Result result = comm.submitBatch(params);
		
		assertTrue(result.isHasFailed());
	}
	
	@Test
	public void testInvalidBatchSubmitBatch() throws ClientException {
		testDownloadBatch();
		
		SubmitBatch_Input params = new SubmitBatch_Input();
		params.setUsername("sheila");
		params.setPassword("parker");
		//This is an invalid batch iD, as the user hasn't been assigned to
		//this batch.
		params.setBatchID(2);
		String parameters = "tim, joe, M, 10; SAL, JEFF, F, 15; me, you, M, 99;"
				+ " m,m,m,10;y,y,y,11;p,p,p,15;p,p,p,18;p,p,p,20";
		params.assignCells(parameters);
		SubmitBatch_Result result = comm.submitBatch(params);
		
		assertTrue(result.isHasFailed());
	}
	
}
