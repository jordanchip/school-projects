package server;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.logging.*;

import com.sun.net.httpserver.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import shared_communication.*;
import server_facade.*;

public class SubmitBatchHandler implements HttpHandler {

	private Logger logger = Logger.getLogger("contactmanager"); 
	
	private XStream xmlStream = new XStream(new DomDriver());	

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		
		SubmitBatch_Input params = (SubmitBatch_Input)xmlStream.fromXML(exchange.getRequestBody());
		SubmitBatch_Result result = new SubmitBatch_Result();
		try {
			result = ServerFacade.submitBatch(params);
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			xmlStream.toXML(result, exchange.getResponseBody());
			exchange.getResponseBody().close();
		}
		catch (ServerException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			return;
		}
	}
}
