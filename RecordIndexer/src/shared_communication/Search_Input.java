package shared_communication;

import java.util.ArrayList;
import java.util.List;

import shared_model.Field;

/**
 * Contains all the input to the search method. 
 * Contains a username and password to validate, 
 * a list of fields to search through, and a list 
 * of keywords to search for.
 * @author jchip
 *
 */
public class Search_Input {

	private String username;
	private String password;
	private List<Integer> fields;
	private List<String> keywords;
	
	public Search_Input(String[] values) {
		if (values.length == 4) {
			username = values[0];
			password = values[1];
			buildFields(values[2]);
			buildKeywords(values[3]);
		}
	}
	public Search_Input() {
		
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<Integer> getFields() {
		return fields;
	}
	public void setFields(List<Integer> fields) {
		this.fields = fields;
	}
	public List<String> getKeywords() {
		return keywords;
	}
	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}
	
	//This function takes the Field values given, and parses them
	//to add each individual field ID to the fields list.
	private void buildFields(String values) {
		fields = new ArrayList<Integer>();
		int index = 0;
		StringBuilder fieldBuilder = new StringBuilder(values);
		int indexOfComma = 0;
		do {
			indexOfComma = fieldBuilder.indexOf(",", index);
			if (indexOfComma != -1) {
				String id = fieldBuilder.substring(index, indexOfComma);
				fields.add(Integer.parseInt(id));
				index = indexOfComma;

				while (!Character.isDigit(fieldBuilder.charAt(index))) {
					index++;
				}
			}
			else {
				String id = fieldBuilder.substring(index);
				fields.add(Integer.parseInt(id));
				break;
			}
		} while (indexOfComma != -1);
	}
	
	//This function takes the search values given, and parses them
	//to add each individual search String to the strings list.
	private void buildKeywords(String values) {
		keywords = new ArrayList<String>();
		int index = 0;
		StringBuilder fieldBuilder = new StringBuilder(values);
		int indexOfComma = 0;
		do {
			indexOfComma = fieldBuilder.indexOf(",", index);
			if (indexOfComma != -1) {
				String name = fieldBuilder.substring(index, indexOfComma);
				keywords.add(name.toLowerCase());
				index = indexOfComma;

				while (!Character.isLetterOrDigit(fieldBuilder.charAt(index))) {
					index++;
				}
			}
			else {
				String name = fieldBuilder.substring(index);
				keywords.add(name.toLowerCase());
				break;
			}
		} while (indexOfComma != -1);
	}
}
