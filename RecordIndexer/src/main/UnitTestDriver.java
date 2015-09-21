package main;

import static org.junit.Assert.assertFalse;

import java.util.List;

import database.BatchDAO;
import database.CellDAO;
import database.Database;
import database.DatabaseException;
import database.FieldDAO;
import database.ProjectDAO;
import database.UserDAO;
import shared_model.Batch;
import shared_model.Project;
import shared_model.Field;
import shared_model.User;
import shared_model.Cell;

public class UnitTestDriver {

	public static void main(String[] args) {
		
		String[] testClasses = new String[] {
			"testing.BatchTest",
			"testing.CellTest",
			"testing.FieldTest",
			"testing.ProjectTest",
			"testing.RecordTest",
			"testing.UserTest",
			"testing.BatchDAOTest",
			"testing.CellDAOTest",
			"testing.FieldDAOTest",
			"testing.ProjectDAOTest",
			"testing.RecordDAOTest",
			"testing.UserDAOTest",
			"testing.DatabaseTest",
			"testing.ClientCommunicatorTest"
			
		};
		
//		try {
//			
//			Database.initialize();
//			Database db = new Database();
//			BatchDAO batchDAO = new BatchDAO(db);
//			ProjectDAO projectDAO = new ProjectDAO(db);
//			FieldDAO fieldDAO = new FieldDAO(db);
//			UserDAO userDAO = new UserDAO(db);
//			db.startTransaction();
//			
//			Project goodProj = new Project(25, "1300 census", 30, 350, 10);
//			Project badProj = new Project(29, "1500 census", 60, 40, 15);
//			Batch goodBatch = new Batch(5, "java/java.com", false);
//			Batch notLinkedBatch = new Batch(44, "watcher.com", false);
//			
//			projectDAO.addProject(goodProj);
//			projectDAO.addProject(badProj);
//			goodBatch.setProjectID(goodProj.getID());
//			notLinkedBatch.setProjectID(badProj.getID()*10);
//			batchDAO.addBatch(goodBatch);
//			batchDAO.addBatch(notLinkedBatch);
//			
//			Batch batch = batchDAO.getSampleBatch(goodProj);
//			
//			db.endTransaction(false);
//			
//			
//		} catch (DatabaseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		org.junit.runner.JUnitCore.main(testClasses);

	}

}
