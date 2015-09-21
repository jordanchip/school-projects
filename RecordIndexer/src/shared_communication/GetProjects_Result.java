package shared_communication;

import java.util.List;
import shared_model.Project;

/**
 * Contains the results to the getProjects method.  Contains a list  
 * of all available projects.  Also contains a toString 
 * method to view the result in String format.
 * @author jchip
 *
 */
public class GetProjects_Result {

	private List<Project> projects;
	private boolean hasFailed;
	
	public boolean isHasFailed() {
		return hasFailed;
	}
	public void setHasFailed(boolean hasFailed) {
		this.hasFailed = hasFailed;
	}
	
	@Override
	public String toString() {
		if (hasFailed)
			return "FAILED";
		
		StringBuilder result = new StringBuilder();
		for (Project p : projects) {
			result.append(p.getID() + "\n");
			result.append(p.getName() + "\n");
		}
		return result.toString();
	}
	public List<Project> getProjects() {
		return projects;
	}
	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}
}
