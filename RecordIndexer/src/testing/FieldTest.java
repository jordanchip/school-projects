package testing;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import shared_model.Field;

public class FieldTest {

	private Field defaultField;
	private Field field;
	
	@Before
	public void setUp() {
		defaultField = new Field();
		field = new Field(5, "First Name", 3, 10, 25, 
							"/src.test.fields", "jkd.java");
	}
	
	@After
	public void tearDown() {
		defaultField = null;
		field = null;
	}
	
	@Test
	public void testDefaultConstructor() {
		assertEquals(defaultField.getName(), null);
		assertEquals(defaultField.getID(), -1);
		assertEquals(defaultField.getProjectID(), -1);
		assertEquals(defaultField.getxCoord(), -1);
		assertEquals(defaultField.getWidth(), -1);
		assertEquals(defaultField.getHelpHTMLPath(), null);
		assertEquals(defaultField.getKnownDataPath(), null);
	}
	
	@Test
	public void testValueConstructor() {
		assertEquals(field.getName(), "First Name");
		assertEquals(field.getID(), 5);
		assertEquals(field.getProjectID(), 3);
		assertEquals(field.getxCoord(), 10);
		assertEquals(field.getWidth(), 25);
		assertEquals(field.getHelpHTMLPath(), "/src.test.fields");
		assertEquals(field.getKnownDataPath(), "jkd.java");
	}
	
	@Test
	public void testSetters() {
		field.setName("Age");
		assertEquals(field.getName(), "Age");
		field.setID(4);
		assertEquals(field.getID(), 4);
		field.setProjectID(8);
		assertEquals(field.getProjectID(), 8);
		field.setxCoord(87);
		assertEquals(field.getxCoord(), 87);
		field.setWidth(12);
		assertEquals(field.getWidth(), 12);
		field.setHelpHTMLPath("path/user/me");
		assertEquals(field.getHelpHTMLPath(), "path/user/me");
		field.setKnownDataPath("java.java");
		assertEquals(field.getKnownDataPath(), "java.java");
	}

}
