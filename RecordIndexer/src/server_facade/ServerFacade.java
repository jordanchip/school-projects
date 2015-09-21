package server_facade;

import java.util.List;

import database.*;
import server.*;
import shared_model.*;
import shared_communication.DownloadBatch_Input;
import shared_communication.DownloadBatch_Result;
import shared_communication.DownloadFile_Input;
import shared_communication.DownloadFile_Result;
import shared_communication.GetProjects_Input;
import shared_communication.GetProjects_Result;
import shared_communication.GetSampleImage_Input;
import shared_communication.GetSampleImage_Result;
import shared_communication.Search_Input;
import shared_communication.Search_Result;
import shared_communication.SubmitBatch_Input;
import shared_communication.SubmitBatch_Result;
import shared_communication.ValidateUser_Input;
import shared_communication.ValidateUser_Result;
import shared_communication.GetFields_Input;
import shared_communication.GetFields_Result;


public class ServerFacade {
	
	
	public static void initialize() throws ServerException {		
		try {
			Database.initialize();		
		}
		catch (DatabaseException e) {
			throw new ServerException(e.getMessage(), e);
		}		
	}
	
	
	
	public static ValidateUser_Result validateUser(ValidateUser_Input params) throws ServerException {
		ValidateUser_Result resultUser = new ValidateUser_Result();
		try {
			Database db = new Database();
			db.startTransaction();
			UserDAO userDAO = db.getUserDAO();
			
			//Set up the parameters to the DAO
			User inputUser = new User(params.getUsername(), params.getPassword());
			
			User user = userDAO.validateUser(inputUser);
			resultUser.setUser(user);
			
			db.endTransaction(true);
			return resultUser;
			
		} catch (DatabaseException e) {
			throw new ServerException(e.getMessage(), e);
		}
	}
	
	/**
	 * Retrieves all projects available in the database.
	 * @param params an object which contains all the 
	 * @return a GetProjects_Result object which contains all projects in the database
	 */
	public static GetProjects_Result getProjects(GetProjects_Input params) throws ServerException  {
		GetProjects_Result resultProjects = new GetProjects_Result();
		try {
			Database db = new Database();
			db.startTransaction();
			ProjectDAO projectDAO = db.getProjectDAO();
			UserDAO userDAO = db.getUserDAO();
			
			User inputUser = new User(params.getUsername(), params.getPassword());
			inputUser = userDAO.validateUser(inputUser);
			//If inputUser's id is -1, it means an invalid user was passed in.
			if (inputUser.getID() == -1) {
				resultProjects.setHasFailed(true);
				db.endTransaction(false);
				return resultProjects;
			}
			
			resultProjects.setProjects(projectDAO.getAll());
			
			db.endTransaction(true);
			return resultProjects;
		} catch (DatabaseException e) {
			throw new ServerException(e.getMessage(), e);
		}
	}
	
	/**
	 * Retrieves one sample image for a given project.
	 * @param params A GetSampleImage_Input object which contains the project for which a batch is desired
	 * @return A GetSampleImage_Result object which contains the sample batch object.
	 */
	public static GetSampleImage_Result getSampleImage(GetSampleImage_Input params) throws ServerException  {
		GetSampleImage_Result resultImage = new GetSampleImage_Result();
		try {
			Database db = new Database();
			db.startTransaction();
			
			BatchDAO batchDAO = db.getBatchDAO();
			UserDAO userDAO = db.getUserDAO();
			
			//Set up the project object to pass the the DAO.
			Project project = new Project(params.getProjectID());
			
			User inputUser = new User(params.getUsername(), params.getPassword());
			inputUser = userDAO.validateUser(inputUser);
			//If inputUser's id is -1, it means an invalid user was passed in.
			if (inputUser.getID() == -1) {
				resultImage.setHasFailed(true);
				db.endTransaction(false);
				return resultImage;
			}
			Batch batch = batchDAO.getSampleBatch(project);
			//If the batch's ID is -1, either the project ID was invalid, or there are no
			//image's linked to the project.
			if (batch.getID() == -1) {
				resultImage.setHasFailed(true);
				db.endTransaction(false);
				return resultImage;
			}
			resultImage.setBatch(batch);
			
			db.endTransaction(true);
			return resultImage;
			
		} catch (DatabaseException e) {
			throw new ServerException(e.getMessage(), e);
		}
	}
	
	/**
	 * Retrieves an image for a project and assigns it to a user.  This will only 
	 * assign a batch to a user if there is an existing batch available.  If the user 
	 * already has a batch assigned to them, this operation will fail.
	 * @param params A DownloadBatch_Input object which contains the project for which a batch is desired.
	 * @return A DownloadBatch_Result object which contains the retrieved batch object.
	 */
	public static DownloadBatch_Result downloadBatch(DownloadBatch_Input params) throws ServerException  {
		DownloadBatch_Result results = new DownloadBatch_Result();
		try {
			Database db = new Database();
			db.startTransaction();
			
			BatchDAO batchDAO = db.getBatchDAO();
			ProjectDAO projectDAO = db.getProjectDAO();
			FieldDAO fieldDAO = db.getFieldDAO();
			UserDAO userDAO = db.getUserDAO();
			
			//Set up the project object to pass the the DAO.
			Project inputProject = new Project(params.getProjectID());
			User inputUser = new User(params.getUsername(), params.getPassword());
			
			inputUser = userDAO.validateUser(inputUser);
			//If inputUser's id is -1, it means an invalid user was passed in.
			if (inputUser.getID() == -1) {
				System.out.println("User not valid");
				results.setHasFailed(true);
				db.endTransaction(false);
				return results;
			}
			//If user's current assigned batch is not -1, it means they already have 
			//a batch assigned to them.
			else if (inputUser.getCurrentAssignedBatch() >= 1) {
				results.setHasFailed(true);
				db.endTransaction(false);
				return results;
			}
			
			Batch batch = batchDAO.getBatch(inputProject, inputUser);
			//If the batch's ID is -1, either the project ID was invalid, or there are no
			//image's linked to the project.
			if (batch.getID() == -1) {
				results.setHasFailed(true);
				db.endTransaction(false);
				return results;
			}
			
			results.setBatch(batch);
			
			List<Field> fields = fieldDAO.getAllFieldsForProject(inputProject);
			results.setFields(fields);
			
			Project returnProject = projectDAO.getProject(inputProject);
			results.setProject(returnProject);
			
			db.endTransaction(true);
			return results;
			
		} catch (DatabaseException e) {
			throw new ServerException(e.getMessage(), e);
		}
	}
	
	/**
	 * Sends all indexed fields to the database for storage, then flags the user as 
	 * having no currently assigned batches.  Increments the number of records indexed 
	 * for that user.
	 * @param params A SubmitBatch_Input object which contains the fields to be saved to the database.
	 */
	public static SubmitBatch_Result submitBatch(SubmitBatch_Input params) throws ServerException  {
		SubmitBatch_Result result = new SubmitBatch_Result();
		try {
			Database db = new Database();
			db.startTransaction();
			
			BatchDAO batchDAO = db.getBatchDAO();
			UserDAO userDAO = db.getUserDAO();
			
			//Set up the project object to pass the the DAO.
			Batch inputBatch = batchDAO.getBatch(params.getBatchID());
			Project project = db.getProjectDAO().getProjectFromBatch(inputBatch);
			//Check if the cells have the right number of "columns"
			if (params.getCells().length != project.getRecordsPerImage()) {
				result.setHasFailed(true);
				db.endTransaction(false);
				return result;
			}
			
			User inputUser = new User(params.getUsername(), params.getPassword());
			inputUser = userDAO.validateUser(inputUser);
			//If inputUser's id is -1, it means an invalid user was passed in.
			//If inputUser's assigned batch is 0 (or less), it means they didn't have a batch to submit
			if (inputUser.getID() == -1 || 
					inputUser.getCurrentAssignedBatch() != inputBatch.getID()) {
				result.setHasFailed(true);
				db.endTransaction(false);
				return result;
			}
			if (!batchDAO.updateBatch(inputBatch, params.getCells())) {
				result.setHasFailed(true);
				db.endTransaction(false);
				return result;
			}
			
			inputUser.setCurrentAssignedBatch(0);
			//Update the user's number of indexed records for each record in the batch.
			inputUser.setIndexedRecords(inputUser.getIndexedRecords() + project.getRecordsPerImage());
			userDAO.updateUser(inputUser);
			
			
			db.endTransaction(true);
			return result;
			
		} catch (DatabaseException e) {
			throw new ServerException(e.getMessage(), e);
		}
	}
	
	/**
	 * Gets all the fields for a given project. If no project is specified, returns 
	 * information about all of the fields for all projects in the database.
	 * @param params The batch which contains the desired fields.
	 * @return A getFields_Result object which contains all the found fields
	 */
	public static GetFields_Result getFields(GetFields_Input params) throws ServerException  {
		GetFields_Result returnFields = new GetFields_Result();
		try {
			Database db = new Database();
			db.startTransaction();
			
			FieldDAO fieldDAO = db.getFieldDAO();
			UserDAO userDAO = db.getUserDAO();
			//Set up the project object to pass the the DAO.
			Project project = new Project(params.getProjectID());
			
			User inputUser = new User(params.getUsername(), params.getPassword());
			inputUser = userDAO.validateUser(inputUser);
			//If inputUser's id is -1, it means an invalid user was passed in.
			if (inputUser.getID() == -1) {
				returnFields.setHasFailed(true);
				db.endTransaction(false);
				return returnFields;
			}
			
			List<Field> fields;
			//If the project's ID is -1 or 0, it means no ID was passed in.
			if (params.getProjectID() == 0 || params.getProjectID() == -1) {
				fields = fieldDAO.getAll();
			}
			//Otherwise, a project ID was given.
			else {
				fields = fieldDAO.getAllFieldsForProject(project);
				//If the fields returned are empty, it means that no fields were
				//linked to the given project ID.
				if (fields.size() == 0) {
					returnFields.setHasFailed(true);
					db.endTransaction(false);
					return returnFields;
				}
			}
			
			returnFields.setFields(fields);
			
			db.endTransaction(true);
			return returnFields;
			
		} catch (DatabaseException e) {
			throw new ServerException(e.getMessage(), e);
		}
	}
	
	/**
	 * Searches the database for specified strings
	 * @param params A Search_Input object which contains all the strings to be searched for.
	 * @return A Search_Result object containing lists of all found matches.
	 */
	public static Search_Result search(Search_Input params) throws ServerException  {
		Search_Result returnValues = new Search_Result();
		try {
			Database db = new Database();
			db.startTransaction();
			UserDAO userDAO = db.getUserDAO();
			SearchDAO searchDAO = db.getSearchDAO();
			
			User inputUser = new User(params.getUsername(), params.getPassword());
			inputUser = userDAO.validateUser(inputUser);
			//If inputUser's id is -1, it means an invalid user was passed in.
			if (inputUser.getID() == -1) {
				returnValues.setHasFailed(true);
				db.endTransaction(false);
				return returnValues;
			}
			
			
			List<Integer> fields = params.getFields();
			List<String> searchValues = params.getKeywords();
			returnValues = searchDAO.search(fields, searchValues);
			db.endTransaction(true);
			return returnValues;
		} catch (DatabaseException e) {
			throw new ServerException(e.getMessage(), e);
		}
	}

}
