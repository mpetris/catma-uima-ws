package de.catma.uimaws;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.restlet.data.Form;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class UimaExecutor extends ServerResource {
	private static final ExecutorService EXECUTORSERVICE = Executors.newFixedThreadPool(4);
	
	@Get
	public String execute() {
		
		Properties properties = 
				(Properties) getContext().getAttributes().get(
					Parameter.uimawrapper_properties.name());
		
		String generatorPath = 
				properties.getProperty(PropertyKey.AnnotationGeneratorPath.name());
		String logFolder = properties.getProperty(PropertyKey.LogFolder.name());	

		Form form = getRequest().getResourceRef().getQueryAsForm();
		String corpusId = form.getFirstValue(Parameter.cid.name());
		String tagsetIdentification = form.getFirstValue(Parameter.tid.name());
		String identifier = form.getFirstValue(Parameter.id.name());
		String token = form.getFirstValue(Parameter.token.name());
		String apiURL = form.getFirstValue(Parameter.api.name());
		String sourceDocId = form.getFirstValue(Parameter.sid.name());

		
		EXECUTORSERVICE.submit(
			new UimaExecutionJob(
				generatorPath, logFolder,
				corpusId, tagsetIdentification, 
				identifier, token, apiURL, sourceDocId));

		return corpusId;
		
	}

}
