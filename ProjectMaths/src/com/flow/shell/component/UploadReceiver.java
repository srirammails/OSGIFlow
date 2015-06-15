package com.flow.shell.component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;

public class UploadReceiver implements  Upload.Receiver, SucceededListener{

	private static final long serialVersionUID = 1L;


	
	@Override
	public OutputStream receiveUpload(String filename, String mimeType) {
		FileOutputStream fos = null; 
        try {
            File file = new File("/www/uploads/" + filename);
            fos = new FileOutputStream(file);
        } catch (final java.io.FileNotFoundException e) {
            new Notification("Could not open file<br/>",
                             e.getMessage(),
                             Notification.Type.ERROR_MESSAGE)
                .show(Page.getCurrent());
            return null;
        }
        return fos;
	}
	
	@Override
	public void uploadSucceeded(SucceededEvent event) {
		
	}

}
