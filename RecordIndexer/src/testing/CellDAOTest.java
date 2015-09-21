package testing;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import database.CellDAO;
import database.Database;
import database.DatabaseException;
import shared_model.Cell;

public class CellDAOTest extends DAOTester{

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Database.initialize();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		return;
	}

	private Database db;
	private CellDAO cellDAO;
	
	@Before
	public void setUp() throws Exception {
		db = new Database();
		db.startTransaction();
		
		List<Cell> cells = db.getCellDAO().getAll();
		for (Cell f : cells) {
			db.getCellDAO().deleteCell(f);
		}
		
		db.endTransaction(true);
		
		db = new Database();
		db.startTransaction();
		cellDAO = db.getCellDAO();
	}

	@After
	public void tearDown() throws Exception {
		db.endTransaction(false);
		cellDAO = null;
		db = null;
	}

	@Test
	public void testAddCell() throws DatabaseException {
		Cell temp = new Cell(2, 5, "male");
		Cell temp2 = new Cell(3, 10, "female");
		
		cellDAO.addCell(temp);
		cellDAO.addCell(temp2);
		
		List<Cell> cells = cellDAO.getAll();
		
		boolean found1 = false;
		boolean found2 = false;
		
		for (Cell c : cells) {
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
		List<Cell> cells = cellDAO.getAll();
		int size = cells.size();
		assertEquals(0, cells.size());
		cellDAO.addCell(new Cell(2, 5, "male"));
		cellDAO.addCell(new Cell(3, 10, "female"));
		cells = cellDAO.getAll();
		assertEquals(size+2, cells.size());
	}

	@Test
	public void testDeleteCell() throws DatabaseException {
		Cell temp = new Cell(2, 5, "male");
		Cell temp2 = new Cell(3, 10, "female");
		
		cellDAO.addCell(temp);
		cellDAO.addCell(temp2);
		
		//Assuming add works, we will see if a deleted cell
		//stays it the databse (it shouldn't)
		cellDAO.deleteCell(temp2);
		List<Cell> cells = cellDAO.getAll();
		
		boolean found1 = false;
		boolean found2 = true;
		
		for (Cell c : cells) {
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
		Cell goodCell = new Cell(2, 5, "male");
		Cell badCell = new Cell(2, 5, "male");
		cellDAO.addCell(goodCell);
		cellDAO.deleteCell(badCell);
	}
	
	private boolean areEqual(Cell a, Cell b, boolean compareIDs) {
		if (compareIDs) {
			if (a.getID() != b.getID()) {
				return false;
			}
		}	
		return (safeEquals(a.getRecordID(), b.getRecordID()) &&
				safeEquals(a.getFieldID(), b.getFieldID()) &&
				safeEquals(a.getValue(), b.getValue()));
	}

}
