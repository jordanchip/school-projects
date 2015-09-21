package server;

import java.io.*;
import java.net.*;
import java.util.logging.*;

import com.sun.net.httpserver.*;

import server_facade.*;

public class Server {

	private static int SERVER_PORT_NUMBER = 8080;
	private static final int MAX_WAITING_CONNECTIONS = 10;
	
	private static Logger logger;
	
	static {
		try {
			initLog();
		}
		catch (IOException e) {
			System.out.println("Could not initialize log: " + e.getMessage());
		}
	}
	
	private static void initLog() throws IOException {
		
		Level logLevel = Level.FINE;
		
		logger = Logger.getLogger("RecordIndexer"); 
		logger.setLevel(logLevel);
		logger.setUseParentHandlers(false);
		
		Handler consoleHandler = new ConsoleHandler();
		consoleHandler.setLevel(logLevel);
		consoleHandler.setFormatter(new SimpleFormatter());
		logger.addHandler(consoleHandler);

		FileHandler fileHandler = new FileHandler("log.txt", false);
		fileHandler.setLevel(logLevel);
		fileHandler.setFormatter(new SimpleFormatter());
		logger.addHandler(fileHandler);
	}

	
	private HttpServer server;
	
	public Server() {
		return;
	}
	
	public void run() {
		
		logger.info("Initializing Model");
		
		try {
			ServerFacade.initialize();		
		}
		catch (ServerException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			return;
		}
		
		logger.info("Initializing HTTP Server");
		
		try {
			server = HttpServer.create(new InetSocketAddress(SERVER_PORT_NUMBER),
											MAX_WAITING_CONNECTIONS);
		} 
		catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);			
			return;
		}

		server.setExecutor(null); // use the default executor

		server.createContext("/DownloadBatch", downloadBatchHandler);
		server.createContext("/", downloadFileHandler);
		server.createContext("/GetFields", getFieldsHandler);
		server.createContext("/GetProjects", getProjectsHandler);
		server.createContext("/GetSampleImage", getSampleImageHandler);
		server.createContext("/Search", searchHandler);
		server.createContext("/SubmitBatch", submitBatchHandler);
		server.createContext("/ValidateUser", validateUserHandler);
		
		logger.info("Starting HTTP Server");

		server.start();
	}
	private HttpHandler downloadBatchHandler = new DownloadBatchHandler();
	private HttpHandler downloadFileHandler = new DownloadFileHandler();
	private HttpHandler getFieldsHandler = new GetFieldsHandler();
	private HttpHandler getProjectsHandler = new GetProjectsHandler();
	private HttpHandler getSampleImageHandler = new GetSampleImageHandler();
	private HttpHandler searchHandler = new SearchHandler();
	private HttpHandler submitBatchHandler = new SubmitBatchHandler();
	private HttpHandler validateUserHandler = new ValidateUserHandler();
	
	public static void main(String[] args) {
		Server server = new Server();
		if (args.length > 0)
			server.initPortNum(Integer.parseInt(args[0]));
		else
			server.initPortNum(8080);
		new Server().run();
	}

	private void initPortNum(int port) {
		SERVER_PORT_NUMBER = port;
	}
	
}