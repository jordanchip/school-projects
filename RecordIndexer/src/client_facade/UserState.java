package client_facade;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;


import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class UserState {

	private final String USER_STATE_PATH = "database" + File.separator + "Users";
	
	
	private XStream xmlStream = new XStream(new DomDriver());
	
	public UserState() {
	}
	
	public void save(BatchState batchState, String username) {
		String fileName = USER_STATE_PATH + File.separator + username + ".xml";
		File f = new File(fileName);
		if(f.exists() && !f.isDirectory()) {
			f.delete();
		}
		try {
			PrintWriter writer = new PrintWriter(fileName, "UTF-8");
			xmlStream.toXML(batchState, writer);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public BatchState getStateForUser(String username) {
		File f = new File(USER_STATE_PATH + File.separator + username + ".xml");
		if (f.exists() && !f.isDirectory()) {
			Path path = FileSystems.getDefault().getPath(USER_STATE_PATH, username + ".xml");
		    BufferedReader reader;
			try {
				reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
				BatchState batchState = (BatchState)xmlStream.fromXML(reader);
				batchState.resetListeners();
				return batchState;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return new BatchState();
	}
}
