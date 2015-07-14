package org.pbtools.analysis.result.view.model;

public class FileModel {
    private final String filename;
    private final String foldername;
    private final String fileicon;
 
    public FileModel(String foldername) {
        this.foldername = foldername;
        this.filename = null;
        this.fileicon = null;
    }
 
    public FileModel(String filename, String fileicon) {
        this.filename = filename;
        this.fileicon = fileicon;
        this.foldername = null;
    }

	public String getFilename() {
		return filename;
	}

	public String getFoldername() {
		return foldername;
	}

	public String getFileicon() {
		return fileicon;
	}
 
   
}
