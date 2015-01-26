package de.catma.uimaws;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class Info extends ServerResource {

	@Get
	public String info() {
		return "This is the CATMA UIMA integration webservice";
	}
}
