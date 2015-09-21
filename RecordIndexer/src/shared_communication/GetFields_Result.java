package shared_communication;

import java.util.List;
import shared_model.Project;
import java.util.Map;

import shared_model.Field;

/**
 * Contains the results to the getFields method.  Contains 
 * a list of the requested fields mapped to their respective 
 * projects. Also contains a toString 
 * method to view the result in String format.
 * @author jchip
 *
 */
public class GetFields_Result {

	private Map<Project,List<Field>> fieldMap;
	private List<Field> fields;
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
		
		StringBuilder returnString = new StringBuilder();
		for (Field f : fields) {
			returnString.append(f.getProjectID() + "\n");
			returnString.append(f.getID() + "\n");
			returnString.append(f.getName() + "\n");
		}
		return returnString.toString();
	}

	public Map<Project, List<Field>> getFieldMap() {
		return fieldMap;
	}

	public void setFieldMap(Map<Project, List<Field>> fieldMap) {
		this.fieldMap = fieldMap;
	}

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

	

}
