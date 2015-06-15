package com.flow.vaadin.osgi.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.List;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import com.vaadin.server.LegacyApplicationUIProvider;
import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UICreateEvent;
import com.vaadin.server.UIProvider;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.communication.ServletUIInitHandler;
import com.vaadin.server.communication.UIInitHandler;
import com.vaadin.shared.JsonConstants;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.shared.ui.ui.UIConstants;
import com.vaadin.ui.UI;

public class OSGIServletInitHandler extends ServletUIInitHandler {

	@Override
	public boolean synchronizedHandleRequest(VaadinSession session,
			VaadinRequest request, VaadinResponse response) throws IOException {
		StringWriter stringWriter = new StringWriter();

		try {
			assert UI.getCurrent() == null;

			// Update browser information from the request
			session.getBrowser().updateRequestDetails(request);

			UI uI = getBrowserDetailsUI(request, session);

			session.getCommunicationManager().repaintAll(uI);

			JSONObject params = new JSONObject();
			params.put(UIConstants.UI_ID_PARAMETER, uI.getUIId());
			String initialUIDL = getInitialUidl(request, uI);
			params.put("uidl", initialUIDL);

			stringWriter.write(params.toString());
		} catch (JSONException e) {
			throw new IOException("Error producing initial UIDL", e);
		} finally {
			stringWriter.close();
		}

		return commitJsonResponse(request, response, stringWriter.toString());
	}

	static boolean commitJsonResponse(VaadinRequest request,
            VaadinResponse response, String json) throws IOException {
        // The response was produced without errors so write it to the client
        response.setContentType(JsonConstants.JSON_CONTENT_TYPE);

        // Ensure that the browser does not cache UIDL responses.
        // iOS 6 Safari requires this (#9732)
        response.setHeader("Cache-Control", "no-cache");

        byte[] b = json.getBytes("UTF-8");
        response.setContentLength(b.length);

        OutputStream outputStream = response.getOutputStream();
        outputStream.write(b);
        // NOTE GateIn requires the buffers to be flushed to work
        outputStream.flush();

        return true;
    }
	
	private UI getBrowserDetailsUI(VaadinRequest request, VaadinSession session) {
		VaadinService vaadinService = request.getService();

		List<UIProvider> uiProviders = session.getUIProviders();

		UIClassSelectionEvent classSelectionEvent = new UIClassSelectionEvent(
				request);

		UIProvider provider = null;
		Class<? extends UI> uiClass = null;
		for (UIProvider p : uiProviders) {
			// Check for existing LegacyWindow
			if (p instanceof LegacyApplicationUIProvider) {
				LegacyApplicationUIProvider legacyProvider = (LegacyApplicationUIProvider) p;

				UI existingUi = legacyProvider
						.getExistingUI(classSelectionEvent);
				if (existingUi != null) {
					reinitUI(existingUi, request);
					return existingUi;
				}
			}

			uiClass = p.getUIClass(classSelectionEvent);
			if (uiClass != null) {
				provider = p;
				break;
			}
		}

		if (provider == null || uiClass == null) {
			return null;
		}

		// Check for an existing UI based on embed id

		String embedId = getEmbedId(request);

		UI retainedUI = session.getUIByEmbedId(embedId);
		if(retainedUI ==null){
			if(session.getUIs().size()>0){
				retainedUI = session.getUIs().iterator().next();
				Integer uiId = Integer.valueOf(session.getNextUIid());
				retainedUI.setSession(session);
				retainedUI.doInit(request, uiId.intValue(), embedId);
				session.addUI(retainedUI);
			}
		}
		if (retainedUI != null) {
			if (vaadinService.preserveUIOnRefresh(provider, new UICreateEvent(
					request, uiClass))) {
				if (uiClass.isInstance(retainedUI)) {
					reinitUI(retainedUI, request);
					return retainedUI;
				} else {
					getLogger().info(
							"Not using the preserved UI " + embedId
									+ " because it is of type "
									+ retainedUI.getClass() + " but " + uiClass
									+ " is expected for the request.");
				}
			}
			/*
			 * Previous UI without preserve on refresh will be closed when the
			 * new UI gets added to the session.
			 */
		}

		// No existing UI found - go on by creating and initializing one

		Integer uiId = Integer.valueOf(session.getNextUIid());

		// Explicit Class.cast to detect if the UIProvider does something
		// unexpected
		UICreateEvent event = new UICreateEvent(request, uiClass, uiId);
		UI ui = uiClass.cast(provider.createInstance(event));

		// Initialize some fields for a newly created UI
		if (ui.getSession() != session) {
			// Session already set for LegacyWindow
			ui.setSession(session);
		}

		PushMode pushMode = provider.getPushMode(event);
		if (pushMode == null) {
			pushMode = session.getService().getDeploymentConfiguration()
					.getPushMode();
		}
		ui.getPushConfiguration().setPushMode(pushMode);

		Transport transport = provider.getPushTransport(event);
		if (transport != null) {
			ui.getPushConfiguration().setTransport(transport);
		}

		// Set thread local here so it is available in init
		UI.setCurrent(ui);

		ui.doInit(request, uiId.intValue(), embedId);

		session.addUI(ui);

		// Warn if the window can't be preserved
		if (embedId == null
				&& vaadinService.preserveUIOnRefresh(provider, event)) {
			getLogger().warning(
					"There is no embed id available for UI " + uiClass
							+ " that should be preserved.");
		}

		return ui;
	}

	 private void reinitUI(UI ui, VaadinRequest request) {
	        UI.setCurrent(ui);
	        ui.doRefresh(request);
	 }
	 
	 private static final Logger getLogger() {
	        return Logger.getLogger(UIInitHandler.class.getName());
	    }
}
