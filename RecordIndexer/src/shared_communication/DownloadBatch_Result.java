package shared_communication;

import java.util.List;
import shared_model.Project;
import shared_model.Field;
import shared_model.Batch;

/**
 * Contains the results to the downloadBatch method.   
 * Contains the project information, field information, 
 * and batch information about the file.  Also contains a toString 
 * method to view the result in String format.
 * @author jchip
 *
 */
public class DownloadBatch_Result {

	private String PATH_URL = "";
	
	private Project project;
	private List<Field> fields;
	private Batch batch = new Batch();
	private boolean hasFailed;
	
	public boolean isHasFailed() {
		return hasFailed;
	}
	public void setHasFailed(boolean hasFailed) {
		this.hasFailed = hasFailed;
	}
	/**
	 * 
	 */
	@Override
	public String toString() {
		if (hasFailed)
			return "FAILED";
		StringBuilder outputString = new StringBuilder();
		outputString.append(batch.getID() + "\n");
		outputString.append(project.getID() + "\n");
		outputString.append(PATH_URL + "/database/Records/" + batch.getPath() + "\n");
		outputString.append(project.getFirstYCoord() + "\n");
		outputString.append(project.getRecordsHeight() + "\n");
		outputString.append(project.getRecordsPerImage() + "\n");
		outputString.append(fields.size() + "\n");
		
		for (int i = 0; i < fields.size(); i++) {
			outputString.append(fields.get(i).getID() + "\n");
			int fieldNum = i+1;
			outputString.append(fieldNum);
			outputString.append("\n");
			outputString.append(fields.get(i).getName() + "\n");
			
			if (fields.get(i).getHelpHTMLPath() != "")
				outputString.append(PATH_URL + "/database/Records/" + fields.get(i).getHelpHTMLPath() + "\n");
			outputString.append(fields.get(i).getxCoord() + "\n");
			outputString.append(fields.get(i).getWidth() + "\n");
			System.out.println(fields.get(i).getKnownDataPath());
			
			//Known Datapath is optional.
			if (fields.get(i).getKnownDataPath().length() >= 1)
				outputString.append(PATH_URL + "/database/Records/" + fields.get(i).getKnownDataPath()+"\n");
		}
		return outputString.toString();
	}
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}
	public Batch getBatch() {
		return batch;
	}
	public void setBatch(Batch batch) {
		this.batch = batch;
	}
	public String getPATH_URL() {
		return PATH_URL;
	}
	public void setPATH_URL(String pATH_URL) {
		PATH_URL = pATH_URL;
	}
}
