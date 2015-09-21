package testing;

import static org.junit.Assert.*;

import org.junit.*;

import shared_model.Batch;

public class BatchTest {

	private Batch defaultBatch;
	private Batch batch;
	
	@Before
	public void setUp() {
		defaultBatch = new Batch();
		batch = new Batch(22, 8, "/data/batches", true);
	}
	
	@After
	public void tearDown() {
		defaultBatch = null;
		batch = null;
	}
	
	@Test
	public void testDefaultConstructor() {
		assertEquals(defaultBatch.getID(), -1);
		assertEquals(defaultBatch.getPath(), null);
		assertEquals(defaultBatch.getProjectID(), -1);
	}
	
	@Test
	public void testValueConstructor() {
		assertEquals(batch.getID(), 22);
		assertEquals(batch.getProjectID(), 8);
		assertEquals(batch.getPath(), "/data/batches");
		assertEquals(batch.isInUse(), true);
	}
	
	@Test
	public void testSetters() {
		batch.setID(10);
		assertEquals(batch.getID(), 10);
		batch.setProjectID(66);
		assertEquals(batch.getProjectID(), 66);
		batch.setPath("/missionabort");
		assertEquals(batch.getPath(), "/missionabort");
		batch.setInUse(false);
		assertEquals(batch.isInUse(), false);
	}

}
