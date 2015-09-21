package testing;

import static org.junit.Assert.*;

import org.junit.*;

import shared_model.Project;


public class ProjectTest {

	Project defaultConst;
	Project project;
	
	@Before
	public void setup() {
		defaultConst = new Project();
		project = new Project(1, "2000 Census", 10, 50, 5);
	}
	
	@After
	public void tearnDown() {
		defaultConst = null;
		project = null;
	}
	
	@Test
	public void testDefaultConstructor() {
		assertEquals(defaultConst.getID(), -1);
		assertEquals(defaultConst.getName(), null);
		assertEquals(defaultConst.getFirstYCoord(), -1);
		assertEquals(defaultConst.getRecordsHeight(), -1);
		assertEquals(defaultConst.getRecordsPerImage(), -1);
	}
	
	@Test
	public void testValueConstructor() {
		assertEquals(project.getID(), 1);
		assertEquals(project.getName(), "2000 Census");
		assertEquals(project.getRecordsPerImage(), 10);
		assertEquals(project.getFirstYCoord(), 50);
		assertEquals(project.getRecordsHeight(), 5);
	}

}
