package server;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.logging.*;

import com.sun.net.httpserver.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import shared_communication.*;
import server_facade.*;

public class ValidateUserHandler implements HttpHandler {

	private Logger logger = Logger.getLogger("recordindexer"); 
	
	private XStream xmlStream = new XStream(new DomDriver());	

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		
		ValidateUser_Input params = (ValidateUser_Input)xmlStream.fromXML(exchange.getRequestBody());
		ValidateUser_Result result = new ValidateUser_Result();
		try {
			result = ServerFacade.validateUser(params);
			
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			xmlStream.toXML(result, exchange.getResponseBody());
			exchange.getResponseBody().close();
		}
		catch (ServerException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
			return;
		}
	}
}
