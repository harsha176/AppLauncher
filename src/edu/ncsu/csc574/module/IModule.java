package edu.ncsu.csc574.module;

import edu.ncsu.csc574.emailserver.workflowmanager.IRequestProcessor;

public interface IModule {
	public IRequestProcessor getRequestProcessorInstance();
	public String getName();
}
