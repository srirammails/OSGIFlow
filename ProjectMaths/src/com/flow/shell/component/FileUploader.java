package com.flow.shell.component;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.logging.Logger;


import com.vaadin.server.FileResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FailedListener;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;

public class FileUploader extends CustomLayout implements  Serializable, Receiver, SucceededListener, FailedListener {

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(FileUploader.class.getName());
	
	
	public File file;
	public Embedded uploadImage;
	public Upload fileUpload;
	
	public String filepath="";
	public String filename;
	
	String uploadCaption = "";
	
	ByteArrayOutputStream outputStream;
	//Resource uploadResource;
	
	boolean isUploaded = false;
	
	boolean isImageOnly = false;
	
	
	public FileUploader(String caption, String filepath, boolean isimageonly) {
		this(caption,filepath);
		this.isImageOnly = isimageonly;
	}
	
	public FileUploader(String caption, String filepath) {
		this(caption);
		this.filepath = String.valueOf(filepath);
	}
	
	public FileUploader(String caption) {
		super("uploader");
		
		uploadCaption = caption;
		
		fileUpload = new Upload(null, this);
		fileUpload.setPrimaryStyleName("uploadlink");
		fileUpload.setImmediate(true);
	
		
		fileUpload.addSucceededListener(this);
		fileUpload.addFailedListener(this);
		
		uploadImage = new Embedded(uploadCaption, new ThemeResource("img/nofile.jpg"));
		uploadImage.setImmediate(true);
		uploadImage.setWidth("60px");
		uploadImage.setHeight("60px");
		uploadImage.setPrimaryStyleName("uploadimgframe");
		
		addComponent(uploadImage, "imageholder");
		addComponent(fileUpload, "uploadlink");
		
		log.info("FileUploader init -" + this.getId());
	}
	
	@Override
	public OutputStream receiveUpload(String name, String mimeType) {
		log.info("receiving to upload" + this.fileUpload.getId()+"  "+name+"   "+mimeType);
		//  uploadImage.get
		filename = name;
		FileOutputStream outputStream = null; 
		
		int k = name.lastIndexOf(".");
		String ext = null;
		if (k != -1) ext = name.substring(k + 1, name.length()).toLowerCase();
		
		if (ext.equals("png".toLowerCase())
				|| ext.equals("jpg")
				|| ext.equals("jpeg")
				|| ext.equals("ods")
				|| ext.equals("pdf")
				|| ext.equals("doc")
				|| ext.equals("docx")
				|| ext.equals("xls")
				|| ext.equals("xlsx")
				|| ext.equals("txt")
				|| ext.equals("csv")
				|| ext.equals("xps")
				|| ext.equals("ps")
				|| ext.equals("xml")
		){
		
			try {
				log.info("File to  upload path "+filepath);
			  file=	new File(filepath);
				if(!file.exists())
					file.mkdirs();
				
				file = new File(filepath + filename);
				log.info("File exist "+file.exists());
				if(!file.exists())
					file.createNewFile();
				
				outputStream = new FileOutputStream(file);
			}
			catch (final Exception e) {
				Notification.show("Could not open file", e.getMessage(), Notification.TYPE_ERROR_MESSAGE);
				return null;
			}
		
		}else{
			
			Notification.show("", "This " + ext + " format is not allowed !!", Type.ERROR_MESSAGE);
		}
		return outputStream;
	}
	
	@Override
	public void uploadSucceeded(SucceededEvent event) {
		log.info("success:" + filename);
		// Show the uploaded file in the image viewer
		try {
			File f = new File(event.getFilename());
			String name = f.getName();
			int k = name.lastIndexOf(".");
			String ext = null;
			if (k != -1) ext = name.substring(k + 1, name.length());
			
			if (isImageOnly) {
				if (ext.toLowerCase().equals("png".toLowerCase())
						|| ext.toLowerCase().equals("jpg".toLowerCase())
						|| ext.toLowerCase().equals("jpeg".toLowerCase())
						|| ext.toLowerCase().equals("gif".toLowerCase())
						|| ext.toLowerCase().equals("ods".toLowerCase())) {
					FileResource fRes = new FileResource(file);
					fRes.setCacheTime(10);
					uploadImage.setSource(fRes);
					uploadImage.requestRepaint();
					uploadImage.setImmediate(true);
				} else if (ext.equals(" ")) {
					Notification.show("", " File Format is incorrect",
							Type.ERROR_MESSAGE);
				} else {
					Notification.show("", "This " + ext
							+ " format is not allowed !!", Type.ERROR_MESSAGE);
				}
			}else{
				
				if (ext.toLowerCase().equals("png".toLowerCase())
						|| ext.toLowerCase().equals("jpg".toLowerCase())
						|| ext.toLowerCase().equals("jpeg".toLowerCase())
						|| ext.toLowerCase().equals("ods".toLowerCase())
						) 
				{
					FileResource fRes = new FileResource(file);
					fRes.setCacheTime(10);
					uploadImage.setSource(fRes);
					uploadImage.requestRepaint();
					uploadImage.setImmediate(true);
					
				} else if (ext.equals("")
						|| ext.toLowerCase().equals("pdf".toLowerCase())
						|| ext.toLowerCase().equals("doc".toLowerCase())
						|| ext.toLowerCase().equals("docx".toLowerCase())
						|| ext.toLowerCase().equals("xls".toLowerCase())
						|| ext.toLowerCase().equals("xlsx".toLowerCase())
						|| ext.toLowerCase().equals("txt".toLowerCase())
						|| ext.toLowerCase().equals("csv".toLowerCase())
						|| ext.toLowerCase().equals("xps".toLowerCase())
						|| ext.toLowerCase().equals("ps".toLowerCase())
						|| ext.toLowerCase().equals("xml".toLowerCase())
						) 
				{
					this.uploadImage.setSource(new ThemeResource("img/doc_file.png"));
					uploadImage.requestRepaint();
					uploadImage.setImmediate(true);
				
				} else {
					Notification.show("", "This " + ext
							+ " format is not allowed !!", Type.ERROR_MESSAGE);
				}
				
			}
			
		}
		catch (Exception e) {
			log.info("file formate function ");
			Notification.show("", " File Format is incorrect", Type.ERROR_MESSAGE);
			e.printStackTrace();
		}
		this.isUploaded = true;
		log.info("file name on session:" + VaadinSession.getCurrent().getSession().getAttribute("UPLOAD_FILE_NAME"));
	}
	
	@Override
	public void uploadFailed(FailedEvent event) {
		log.info("failed" + event.getReason());
	}
	
//	public void setImage(Resource res){
//		if(res!=null){
//			this.uploadResource = res;
//			setImage(this.uploadResource.getExternalRefrence());
//		}
//	}
	
	private void setImage(String imagesrc) {
		try {
			
			imagesrc = imagesrc.toLowerCase();
			
			log.info("Setting image to uploader");
			
			if (imagesrc != null) {
				this.filepath = imagesrc;

				if (imagesrc.endsWith(".jpeg") || imagesrc.endsWith(".gif")
						|| imagesrc.endsWith(".jpg")
						|| imagesrc.endsWith(".png")) {
					this.uploadImage.setSource(new FileResource(new File(
							this.filepath)));
				} else {
					this.uploadImage
							.setSource(new ThemeResource("img/doc_file.png"));
				}
			}
		} catch (Exception ex) {
			log.info("Setting image failed");
		}
	}
	
	public String getFileAccesspath() {
		if(filepath!=null){
			if(filename!=null)
				return this.filepath+this.filename;
			else
				return this.filepath;
				
		}
		else 
			return null;
	}

//	public Resource getResource() {
//		if(uploadResource==null){
//			uploadResource = new Resource();
//			uploadResource.setExternalRefrence(this.filepath+this.filename);
//			ResourceDao resourceDao = new ResourceDao();
//			uploadResource = resourceDao.saveResource(uploadResource);
//			log.info("resource added");
//			if(uploadResource!=null)
//				return uploadResource;
//			else
//			{
//				Notification.show("Error reading image", Type.WARNING_MESSAGE);
//				return null;
//			}
//		}
//		else{
//			return uploadResource;
//		}
//		
//	}
	
	
}
