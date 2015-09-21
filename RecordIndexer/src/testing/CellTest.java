package testing;

import static org.junit.Assert.*;

import org.junit.*;

import shared_model.Cell;

public class CellTest {

	private Cell defaultCell;
	private Cell cell;
	
	@Before
	public void setUp() {
		defaultCell = new Cell();
		cell = new Cell(40, 30, "hello");
	}
	
	@After
	public void tearDown() {
		defaultCell = null;
		cell = null;
	}
	
	@Test
	public void testDefaultConstructor() {
		assertEquals(defaultCell.getRecordID(), -1);
		assertEquals(defaultCell.getFieldID(), -1);
		assertEquals(defaultCell.getValue(), null);
	}
	
	@Test
	public void testValueConstructor() {
		assertEquals(cell.getRecordID(), 40);
		assertEquals(cell.getFieldID(), 30);
		assertEquals(cell.getValue(), "hello");
	}
	
	@Test
	public void testSetters() {
		cell.setFieldID(10);
		assertEquals(cell.getFieldID(), 10);
		cell.setRecordID(66);
		assertEquals(cell.getRecordID(), 66);
		cell.setValue("bonny");
		assertEquals(cell.getValue(), "bonny");
	}

}
