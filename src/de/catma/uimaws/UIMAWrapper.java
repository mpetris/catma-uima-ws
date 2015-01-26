package de.catma.uimaws;

import java.io.FileInputStream;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.data.Reference;
import org.restlet.routing.Router;

public class UIMAWrapper extends Application {

	public UIMAWrapper() {
		this.getStatusService().setHomeRef(new Reference("http://www.catma.de"));
	}
	
	@Override
	public Restlet createInboundRoot() {
		try {
			ServletContext servletContext = 
			(ServletContext)getContext().getAttributes().get(
					"org.restlet.ext.servlet.ServletContext");
	
			Properties properties = new Properties();
			try (FileInputStream fis = new FileInputStream(
					servletContext.getRealPath("uimawrapper.properties"))) {
				properties.load(fis); 
			}

			getContext().getAttributes().put(
					Parameter.uimawrapper_properties.name(), properties);
			Router router = new Router(getContext());
			router.attach("/", Info.class);
	        router.attach("/uimaexecutor", UimaExecutor.class);

	        return router;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
