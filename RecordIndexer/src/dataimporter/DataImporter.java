package dataimporter;

import java.io.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.*;
import org.xml.sax.SAXException;
import org.apache.commons.io.*;

import database.Database;
import database.DatabaseException;
import shared_model.*;

public class DataImporter {
	
	
	private static final String DESTINATIONFILE = "database" + File.separator + "Records";
	
	private Database db;
	
	//fieldIDs contains the unique IDs of the fields as they pertain to
	//an individual project.  This program uses this to link the cells to 
	//their respective fieldIDs later on.
	private int[] fieldIDs;

	public static void main(String[] args) {
		
		DataImporter DI = new DataImporter();
		DI.run(args[0]);
	}
	
	public void run(String args) {
		File F = new File(args);
		importFiles(F.getParentFile());
		importXMLToDatabase(F);
	}
	
	private void importFiles(File parentDir) {
		
		File emptydb = new File("database" + File.separator + "empty" + File.separator + "RecordIndexer.sqlite");
		File currentdb = new File("database" + File.separator + "RecordIndexer.sqlite");
		
		File dest = new File(DESTINATIONFILE);
		
		try
		{
			//	We make sure that the directory we are copying is not the the destination
			//	directory.  Otherwise, we delete the directories we are about to copy.
			if(!parentDir.getCanonicalPath().equals(dest.getCanonicalPath()))
				FileUtils.deleteDirectory(dest);
				
			//	Copy the directories (recursively) from our source to our destination.
			FileUtils.copyDirectory(parentDir, dest);
			
			//	Overwrite my existing *.sqlite database with an empty one.  Now, my
			//	database is completelty empty and ready to load with data.
			FileUtils.copyFile(emptydb, currentdb);
		}
		catch (IOException e)
		{
			//logger.log(Level.SEVERE, "Unable to deal with the IOException thrown", e);
			e.printStackTrace();
		}
		
		
	}
	
	private Document getInitializedDoc(File F) {
		DocumentBuilder docB;
		Document doc = null;
		try {
			docB = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			return docB.parse(F);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return doc;
	}
	
	private void importXMLToDatabase(File F) {
		try {
			
			Document doc = getInitializedDoc(F);
			doc.getDocumentElement().normalize();
			
			Database.initialize();
			db = new Database();
			db.startTransaction();
			
			getUsers(doc);
			getProjects(doc);
			
			db.endTransaction(true);
			
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
	}
	
	private void getUsers(Document doc) {
		NodeList nL = doc.getElementsByTagName("user");
		for (int i = 0; i < nL.getLength(); i++) {
			
			Element userElement = (Element)nL.item(i);
			Element userElementUsername = (Element)userElement.getElementsByTagName("username").item(0);
			Element userElementPassword = (Element)userElement.getElementsByTagName("password").item(0);
			Element userElementFirstname = (Element)userElement.getElementsByTagName("firstname").item(0);
			Element userElementLastname = (Element)userElement.getElementsByTagName("lastname").item(0);
			Element userElementEmail = (Element)userElement.getElementsByTagName("email").item(0);
			Element userElementIndexedRecords = (Element)userElement.getElementsByTagName("indexedrecords").item(0);
			
			String username = userElementUsername.getTextContent();
			String password = userElementPassword.getTextContent();
			String firstName = userElementFirstname.getTextContent();
			String lastName = userElementLastname.getTextContent();
			String email = userElementEmail.getTextContent();
			int indexedRecords = Integer.parseInt(userElementIndexedRecords.getTextContent());
		
			User user = new User(username, password, firstName, lastName, email, indexedRecords);
			addUser(user);
		}
	}
	
	private User addUser(User user) {
		try {
			db.getUserDAO().addUser(user);
			return user;
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		return user;
	}
	
	
	private void getProjects(Document doc) {
		NodeList nL = doc.getElementsByTagName("project");
		for (int i = 0; i < nL.getLength(); i++) {
			
			Element projElement = (Element)nL.item(i);
			Element projElementTitle = (Element)projElement.getElementsByTagName("title").item(0);
			Element projElementRecords = (Element)projElement.getElementsByTagName("recordsperimage").item(0);
			Element projElementFirstY = (Element)projElement.getElementsByTagName("firstycoord").item(0);
			Element projElementRecordHeight = (Element)projElement.getElementsByTagName("recordheight").item(0);
			
			String title = projElementTitle.getTextContent();
			int records = Integer.parseInt(projElementRecords.getTextContent());
			int firstY = Integer.parseInt(projElementFirstY.getTextContent());
			int recHeight = Integer.parseInt(projElementRecordHeight.getTextContent());
			
			Project project = new Project(title, records, firstY, recHeight);
			addProject(project);
			
			getFields(projElement, project.getID());
			getImages(projElement, project.getID());
		}
	}
	
	private Project addProject(Project project) {
		try {
			db.getProjectDAO().addProject(project);
			return project;
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		return project;
	}
	
	private void getFields(Element projElement, int projectID) {
		NodeList nL = projElement.getElementsByTagName("field");
		fieldIDs = new int[nL.getLength()];
		for (int i = 0; i < nL.getLength(); i++) {
			
			Element fieldElement = (Element)nL.item(i);
			Element fieldElementTitle = (Element)fieldElement.getElementsByTagName("title").item(0);
			Element fieldElementXCoord = (Element)fieldElement.getElementsByTagName("xcoord").item(0);
			Element fieldElementWidth = (Element)fieldElement.getElementsByTagName("width").item(0);
			Element fieldElementHelp = (Element)fieldElement.getElementsByTagName("helphtml").item(0);
			Element fieldElementKnown = (Element)fieldElement.getElementsByTagName("knowndata").item(0);
			
			String title = fieldElementTitle.getTextContent();
			int records = Integer.parseInt(fieldElementXCoord.getTextContent());
			int width = Integer.parseInt(fieldElementWidth.getTextContent());
			String help = "";
			if (fieldElementHelp != null)
				help = fieldElementHelp.getTextContent();
			String knownData = "";
			if (fieldElementKnown != null)
				knownData = fieldElementKnown.getTextContent();
			
			Field field = new Field(title, projectID, records, width, help, knownData);
			addField(field);
			fieldIDs[i] = field.getID();
		}
	}
	
	private Field addField(Field field) {
		try {
			db.getFieldDAO().addField(field);
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		return field;
	}
	
	private void getImages(Element projElement, int projectID) {
		NodeList nL = projElement.getElementsByTagName("image");
		for (int i = 0; i < nL.getLength(); i++) {
			
			Element imageElement = (Element)nL.item(i);
			Element imageElementFile = (Element)imageElement.getElementsByTagName("file").item(0);
			String file = imageElementFile.getTextContent();
			
			Batch batch = new Batch(projectID, file);
			addBatch(batch);
			
			getRecords(imageElement, batch.getID());
		}
			
	}
	
	private Batch addBatch(Batch batch) {
		try {
			db.getBatchDAO().addBatch(batch);
		}
		catch (DatabaseException e) {
			e.printStackTrace();
		}
		return batch;
	}
	
	private void getRecords(Element imageElement, int batchID) {
		NodeList nL = imageElement.getElementsByTagName("record");
		for (int i = 0; i < nL.getLength(); i++) {
			
			Element recElement = (Element)nL.item(i);
			Record record = new Record(batchID, i);
			addRecord(record);
			
			getCells(recElement, record.getID());
		}
	}
	private void addRecord(Record record) {
		try {
			db.getRecordDAO().addRecord(record);
		}
		catch (DatabaseException e) {
			e.printStackTrace();
		}
	}
	
	private void getCells(Element recElement, int recID) {
		NodeList nL = recElement.getElementsByTagName("values");
		for (int i = 0; i < nL.getLength(); i++) {
			
			Element cellElement = (Element)nL.item(i);
			NodeList nL2 = cellElement.getElementsByTagName("value");
			for (int j = 0; j < nL2.getLength(); j++) {
				
				Element cellValue = (Element)nL2.item(j);
				String value = cellValue.getTextContent().toLowerCase();
				Cell cell = new Cell(recID, fieldIDs[j], value);
				addCell(cell);
			}
		}
	}
	
	private void addCell(Cell cell) {
		try {
			db.getCellDAO().addCell(cell);
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
	}
	
}
