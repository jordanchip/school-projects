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
import database.RecordDAO;
import shared_model.Record;

public class RecordDAOTest extends DAOTester{

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Database.initialize();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		return;
	}

	private Database db;
	private RecordDAO recordDAO;
	
	@Before
	public void setUp() throws Exception {
		db = new Database();
		db.startTransaction();
		
		List<Record> records = db.getRecordDAO().getAll();
		for (Record f : records) {
			db.getRecordDAO().deleteRecord(f);
		}
		
		db.endTransaction(true);
		
		db = new Database();
		db.startTransaction();
		recordDAO = db.getRecordDAO();
	}

	@After
	public void tearDown() throws Exception {
		db.endTransaction(false);
		recordDAO = null;
		db = null;
	}

	@Test
	public void testAddRecord() throws DatabaseException {
		Record temp = new Record(5, 78);
		Record temp2 = new Record(3, 10);
		
		recordDAO.addRecord(temp);
		recordDAO.addRecord(temp2);
		
		List<Record> records = recordDAO.getAll();
		
		boolean found1 = false;
		boolean found2 = false;
		
		for (Record c : records) {
			assertFalse(c.getID() == -1);
			
			if (!found1) {
				found1 = areEqual(c, temp, false);
			}		
			if (!found2) {
				found2 = areEqual(c, temp2, false);
			}
		}
		
		assertTrue(found1 && found2);
	}

	@Test
	public void testGetAll() throws DatabaseException {
		List<Record> records = recordDAO.getAll();
		int size = records.size();
		assertEquals(0, records.size());
		recordDAO.addRecord(new Record(2, 5));
		recordDAO.addRecord(new Record(3, 10));
		records = recordDAO.getAll();
		assertEquals(size+2, records.size());
	}

	@Test
	public void testUpdateRecord() throws DatabaseException {
		Record temp = new Record(5, 78);
		Record temp2 = new Record(3, 10);
		
		recordDAO.addRecord(temp);
		recordDAO.addRecord(temp2);
		
		temp.setBatchID(10);
		temp.setRow(2);
		
		temp2.setBatchID(8);
		temp2.setRow(22);
		
		recordDAO.updateRecord(temp);
		recordDAO.updateRecord(temp2);
		
		List<Record> records = recordDAO.getAll();
		
		boolean found1 = false;
		boolean found2 = false;
		
		for (Record c : records) {
			assertFalse(c.getID() == -1);
			
			if (!found1) {
				found1 = areEqual(c, temp, false);
			}		
			if (!found2) {
				found2 = areEqual(c, temp2, false);
			}
		}
		
		assertTrue(found1 && found2);
	}

	@Test
	public void testDeleteRecord() throws DatabaseException {
		Record temp = new Record(5, 78);
		Record temp2 = new Record(3, 10);
		
		recordDAO.addRecord(temp);
		recordDAO.addRecord(temp2);
		recordDAO.deleteRecord(temp2);
		List<Record> records = recordDAO.getAll();
		
		boolean found1 = false;
		boolean found2 = true;
		
		for (Record c : records) {
			assertFalse(c.getID() == -1);
			
			if (!found1) {
				found1 = areEqual(c, temp, false);
			}		
			if (found2) {
				found2 = !areEqual(c, temp2, false);
			}
		}
		
		assertTrue(found1 && found2);
	}
	
	@Test(expected=DatabaseException.class)
	public void testInvalidDelete() throws DatabaseException{
		Record goodRecord = new Record(25, 10);
		Record badRecord = new Record(25, 10);
		recordDAO.addRecord(goodRecord);
		recordDAO.deleteRecord(badRecord);
	}
	
	@Test(expected=DatabaseException.class)
	public void testInvalidUpdate() throws DatabaseException{
		
		Record temp = new Record(25, 10);
		recordDAO.addRecord(temp);
		int ID = temp.getID();
		Record temp2 = new Record(25, 10);
		temp2.setID(ID*10);
		recordDAO.updateRecord(temp2);
	}
	
	private boolean areEqual(Record a, Record b, boolean compareIDs) {
		if (compareIDs) {
			if (a.getID() != b.getID()) {
				return false;
			}
		}	
		return (safeEquals(a.getBatchID(), b.getBatchID()) &&
				safeEquals(a.getRow(), b.getRow()));
	}

}
