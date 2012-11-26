package edu.ncsu.csc574.module;

import edu.ncsu.csc574.emailserver.commlayer.CommunicationServiceFactory;
import edu.ncsu.csc574.emailserver.commlayer.ICommunicationService;
import edu.ncsu.csc574.emailserver.exceptions.InitalizationException;
import edu.ncsu.csc574.emailserver.workflowmanager.IRequestProcessor;

/**
 * This class launches application args: port passphrase isMutAuthRequired
 * Moduleimp class
 * 
 * @author Harsha
 * 
 */
public class AppLauncher implements Runnable {
	private int port;
	private String passphrase;
	private boolean isMutAuthRequired;
	private String moduleClass;

	private ICommunicationService commService;
	
	public AppLauncher(int port, String passphrase, boolean isMutAuthRequired, String moduleClass) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		// parse arguments
		this.port = port;
		this.passphrase = passphrase;
		this.isMutAuthRequired = isMutAuthRequired;
		this.moduleClass = moduleClass;
		
		// Instantiate communication service object
		ICommunicationService commService = CommunicationServiceFactory.getCommServiceInstance();
		
		// Instantiate module class
		IModule module = (IModule) Class.forName(moduleClass).newInstance();

		// Get requestProcessor
		IRequestProcessor reqProcessor = module.getRequestProcessorInstance();

		// register module with communication service
		commService.registerModule(reqProcessor);
	}
	
	public static void main(String args[]) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		if (args.length != 4) {
			System.out
					.println("Usage java AppLauncher <port> <passphrase> <isMuthAuthRequired> <ModuleImpClass>");
			return;
		}

		
		int port = Integer.parseInt(args[0]);
		String passphrase = args[1];
		boolean isMutAuthRequired = Boolean.parseBoolean(args[2]);
		String moduleClass = args[3];

		AppLauncher app = new AppLauncher(port, passphrase, isMutAuthRequired, moduleClass);
		Thread launch = new Thread(app); 
		launch.start();

	}

	@Override
	public void run() {
		// start communication service is blocking call
		try {
			commService.startService(port, passphrase, isMutAuthRequired);
		} catch (InitalizationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
