package server;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.logging.*;

import com.sun.net.httpserver.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import shared_communication.*;
import server_facade.*;

public class GetProjectsHandler implements HttpHandler {

	private Logger logger = Logger.getLogger("recordindexer"); 
	
	private XStream xmlStream = new XStream(new DomDriver());	

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		
		GetProjects_Input params = (GetProjects_Input)xmlStream.fromXML(exchange.getRequestBody());
		GetProjects_Result result = new GetProjects_Result();
		try {
			result = ServerFacade.getProjects(params);
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			xmlStream.toXML(result, exchange.getResponseBody());
		}
		catch (ServerException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			return;
		}
		exchange.getResponseBody().close();
	}
}
