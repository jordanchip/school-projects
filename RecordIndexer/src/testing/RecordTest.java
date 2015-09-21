package testing;

import static org.junit.Assert.*;

import org.junit.*;

import shared_model.Record;

public class RecordTest {

	private Record defaultRecord;
	private Record record;
	
	@Before
	public void setUp() {
		defaultRecord = new Record();
		record = new Record(30, 72, 5);
	}
	
	@After
	public void tearDown() {
		defaultRecord = null;
		record = null;
	}
	
	@Test
	public void testDefaultConstructor() {
		assertEquals(defaultRecord.getID(), -1);
		assertEquals(defaultRecord.getBatchID(), -1);
		assertEquals(defaultRecord.getRow(), -1);
	}
	
	@Test
	public void testValueConstructor() {
		assertEquals(record.getID(), 30);
		assertEquals(record.getBatchID(), 72);
		assertEquals(record.getRow(), 5);
	}
	
	@Test
	public void testSetters() {
		record.setID(10);
		assertEquals(record.getID(), 10);
		record.setBatchID(34);
		assertEquals(record.getBatchID(), 34);
		record.setRow(9);
		assertEquals(record.getRow(), 9);
	}

}
