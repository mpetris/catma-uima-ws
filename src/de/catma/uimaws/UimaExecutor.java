package de.catma.uimaws;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

public class UimaExecutor extends ServerResource {
	private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyMMddhhmm");

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
		
		ProcessBuilder pb =
			   new ProcessBuilder(
					   generatorPath, 
					   corpusId, tagsetIdentification, 
					   identifier, token, apiURL);
		File log = new File(
				logFolder, 
				"AnnotationGenerator" + FORMATTER.format(new Date()) + ".log");
		pb.redirectErrorStream(true);
		pb.redirectOutput(Redirect.appendTo(log));
		
		try {
			Process proc = pb.start();
	
			int rc = proc.waitFor();
			if(rc != 0){
				throw new ResourceException(
					Status.SERVER_ERROR_INTERNAL, 
					"uima pipeline terminated with return code" + rc);
			}
		}
		catch (InterruptedException | IOException e) {
			throw new ResourceException(
					Status.SERVER_ERROR_INTERNAL, 
					"uima pipeline terminated with error", e);
		}
		
		return corpusId;
		
	}

}
