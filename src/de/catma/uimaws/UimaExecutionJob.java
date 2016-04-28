package de.catma.uimaws;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UimaExecutionJob implements Runnable {
	private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyMMddhhmm");
	
	private final String generatorPath;
	private final String logFolder;
	private final String corpusId;
	private final String tagsetIdentification;
	private final String identifier;
	private final String token;
	private final String apiURL;
	private final String sourceDocId;

	public UimaExecutionJob(String generatorPath, String logFolder,
			String corpusId, String tagsetIdentification, String identifier,
			String token, String apiURL, String sourceDocId) {
		super();
		this.generatorPath = generatorPath;
		this.logFolder = logFolder;
		this.corpusId = corpusId;
		this.tagsetIdentification = tagsetIdentification;
		this.identifier = identifier;
		this.token = token;
		this.apiURL = apiURL;
		this.sourceDocId = sourceDocId;
	}

	@Override
	public void run() {
		Logger logger = Logger.getLogger(UimaExecutionJob.class.getName());
		
		try {
			ProcessBuilder pb =
				   new ProcessBuilder(
						   generatorPath, 
						   corpusId, tagsetIdentification, 
						   identifier, token, apiURL
						   );
//							   ,sourceDocId);
			File log = new File(
					logFolder, 
					"AnnotationGenerator" + FORMATTER.format(new Date()) + ".log");
			pb.redirectErrorStream(true);
			pb.redirectOutput(Redirect.appendTo(log));
			Process proc = pb.start();
	
			int rc = proc.waitFor();
			if(rc != 0){
				logger.severe(
					"uima pipeline terminated with code" + rc);
			}
		}
		catch (InterruptedException | IOException e) {
			logger.log(Level.SEVERE,
					"uima pipeline terminated with error", e);
		}
		
	}

}
