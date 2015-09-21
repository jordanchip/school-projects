package servertester.controllers;

import java.util.*;

import servertester.views.*;
import client_facade.ClientCommunicator;
import shared_communication.*;


public class Controller implements IController {

	private IView _view;
	
	public Controller() {
		return;
	}
	
	public IView getView() {
		return _view;
	}
	
	public void setView(IView value) {
		_view = value;
	}
	
	// IController methods
	//
	
	@Override
	public void initialize() {
		getView().setHost("localhost");
		getView().setPort("8080");
		operationSelected();
	}

	@Override
	public void operationSelected() {
		ArrayList<String> paramNames = new ArrayList<String>();
		paramNames.add("User");
		paramNames.add("Password");
		
		switch (getView().getOperation()) {
		case VALIDATE_USER:
			break;
		case GET_PROJECTS:
			break;
		case GET_SAMPLE_IMAGE:
			paramNames.add("Project");
			break;
		case DOWNLOAD_BATCH:
			paramNames.add("Project");
			break;
		case GET_FIELDS:
			paramNames.add("Project");
			break;
		case SUBMIT_BATCH:
			paramNames.add("Batch");
			paramNames.add("Record Values");
			break;
		case SEARCH:
			paramNames.add("Fields");
			paramNames.add("Search Values");
			break;
		default:
			assert false;
			break;
		}
		
		getView().setRequest("");
		getView().setResponse("");
		getView().setParameterNames(paramNames.toArray(new String[paramNames.size()]));
	}

	@Override
	public void executeOperation() {
		switch (getView().getOperation()) {
		case VALIDATE_USER:
			validateUser();
			break;
		case GET_PROJECTS:
			getProjects();
			break;
		case GET_SAMPLE_IMAGE:
			getSampleImage();
			break;
		case DOWNLOAD_BATCH:
			downloadBatch();
			break;
		case GET_FIELDS:
			getFields();
			break;
		case SUBMIT_BATCH:
			submitBatch();
			break;
		case SEARCH:
			search();
			break;
		default:
			assert false;
			break;
		}
	}
	
	private void validateUser() {
		ClientCommunicator communicator = startComm();
		String[] values = getView().getParameterValues();
		ValidateUser_Input input = new ValidateUser_Input(values);
		
		try {
			ValidateUser_Result output = communicator.validateUser(input);
			getView().setResponse(output.toString());
		} catch (Exception e) {
			getView().setResponse("FAILED\n");
		}
	}
	
	private void getProjects() {
		ClientCommunicator communicator = startComm();
		String[] values = getView().getParameterValues();
		GetProjects_Input input = new GetProjects_Input(values);
		
		try {
			GetProjects_Result output = communicator.getProjects(input);
			getView().setResponse(output.toString());
		} catch (Exception e) {
			getView().setResponse("FAILED\n");
		}
	}
	
	private void getSampleImage() {
		ClientCommunicator communicator = startComm();
		String[] values = getView().getParameterValues();
		GetSampleImage_Input input = new GetSampleImage_Input(values);
		
		try {
			GetSampleImage_Result output = communicator.getSampleImage(input);
			output.setPATH_URL(communicator.getURLPrefix());
			getView().setResponse(output.toString());
		} catch (Exception e) {
			getView().setResponse("FAILED\n");
		}
	}
	
	private void downloadBatch() {
		ClientCommunicator communicator = startComm();
		String[] values = getView().getParameterValues();
		DownloadBatch_Input input = new DownloadBatch_Input(values);
		
		try {
			DownloadBatch_Result output = communicator.downloadBatch(input);
			output.setPATH_URL(communicator.getURLPrefix());
			getView().setResponse(output.toString());
		} catch (Exception e) {
			getView().setResponse("FAILED\n");
		}
	}
	
	private void getFields() {
		ClientCommunicator communicator = startComm();
		String[] values = getView().getParameterValues();
		GetFields_Input input = new GetFields_Input(values);
		
		try {
			GetFields_Result output = communicator.getFields(input);
			getView().setResponse(output.toString());
		} catch (Exception e) {
			getView().setResponse("FAILED\n");
		}
	}
	
	private void submitBatch() {
		ClientCommunicator communicator = startComm();
		String[] values = getView().getParameterValues();
		SubmitBatch_Input input = new SubmitBatch_Input(values);
		
		try {
			SubmitBatch_Result output = communicator.submitBatch(input);
			getView().setResponse(output.toString());
		} catch (Exception e) {
			getView().setResponse("FAILED\n");
		}
	}
	
	private void search() {
		ClientCommunicator communicator = startComm();
		String[] values = getView().getParameterValues();
		Search_Input input = new Search_Input(values);
		
		try {
			Search_Result output = communicator.search(input);
			output.setPATH_URL(communicator.getURLPrefix());
			getView().setResponse(output.toString());
		} catch (Exception e) {
			getView().setResponse("FAILED\n");
		}
	}
	
	private ClientCommunicator startComm() {
		ClientCommunicator communicator = new ClientCommunicator();
		String host = getView().getHost();
		int port = Integer.parseInt(getView().getPort());
		communicator.setHostAndPort(host, port);
		return communicator;
	}

}

