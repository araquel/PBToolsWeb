package org.pbtools.filesystem.manager;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import org.apache.commons.io.FileUtils;
import org.zkoss.zk.ui.Sessions;

public class UserFileManager {
	public static String FILE_TYPE_GENOTYPE = "GENOTYPE"; 
	public static String FILE_TYPE_PHENOTYPE = "PHENOTYPE";
	public static String BASE_PATH = Sessions.getCurrent().getWebApp().getRealPath( File.separator + "UPLOADS" + File.separator );

	public void createNewFileFromUpload(int userid, int studyid, String realName,File dataFile, String fileType){
		File BASE_FOLDER = new File(buildUserPath(userid, studyid));
		if(!BASE_FOLDER.exists()) BASE_FOLDER.mkdirs();
		try {
			FileUtils.copyFileToDirectory(dataFile, new File(BASE_FOLDER.getAbsolutePath() + File.separator ),true);
			File movedFile = new File(BASE_FOLDER.getAbsolutePath() + File.separator + dataFile.getName() );
			File renamedFile = new File(BASE_FOLDER.getAbsolutePath() + File.separator + String.valueOf(Calendar.getInstance().getTimeInMillis()+ realName));
			movedFile.renameTo(renamedFile);
			File fileRec = new File(renamedFile.getAbsolutePath());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//		dataFile.renameTo(new File(BASE_FOLDER.getAbsolutePath() + dataFile.getName())); 
		System.out.println("File moved to: " + BASE_FOLDER.getAbsolutePath());
	}

	public String uploadFileForAnalysis(String realName,File dataFile){
		File movedFile= null;
		File renamedFile = null;
		File BASE_FOLDER = new File(BASE_PATH);
		if(!BASE_FOLDER.exists()) BASE_FOLDER.mkdirs();
		try {
			FileUtils.copyFileToDirectory(dataFile, new File(BASE_FOLDER.getAbsolutePath() + File.separator ),true);
			movedFile = new File(BASE_FOLDER.getAbsolutePath() + File.separator + dataFile.getName());
			renamedFile = new File(BASE_FOLDER.getAbsolutePath() + File.separator + String.valueOf(Calendar.getInstance().getTimeInMillis()+ realName));
			movedFile.renameTo(renamedFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//		dataFile.renameTo(new File(BASE_FOLDER.getAbsolutePath() + dataFile.getName())); 
		System.out.println("File moved to: " + BASE_FOLDER.getAbsolutePath());
		return renamedFile.getAbsolutePath();

	}

	public String uploadFileAndMoveToFolder(String realName,File dataFile, String resultFolderPath){
		File movedFile= null;
		File renamedFile = null;
		File BASE_FOLDER = new File(resultFolderPath);
		if(!BASE_FOLDER.exists()) BASE_FOLDER.mkdirs();
		try {
			FileUtils.copyFileToDirectory(dataFile, new File(BASE_FOLDER.getAbsolutePath() + File.separator ),true);
			movedFile = new File(BASE_FOLDER.getAbsolutePath() + File.separator + dataFile.getName());
			renamedFile = new File(BASE_FOLDER.getAbsolutePath() + File.separator + String.valueOf(Calendar.getInstance().getTimeInMillis()+ realName));
			movedFile.renameTo(renamedFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//		dataFile.renameTo(new File(BASE_FOLDER.getAbsolutePath() + dataFile.getName())); 
		System.out.println("File moved to: " + BASE_FOLDER.getAbsolutePath());
		return renamedFile.getAbsolutePath();

	}
	
	public String moveUploadedFileToOutputFolder(String resultFolderPath, String realName,File dataFile){

		System.out.println("move uploadedFile");
		String filePath = null;
		File movedFile= null;
		File renamedFile = null;
		try {
			FileUtils.copyFileToDirectory(dataFile, new File(resultFolderPath + File.separator ),true);
			movedFile = new File(resultFolderPath + File.separator + dataFile.getName());
			filePath = movedFile.getAbsolutePath();
				
			if(realName.contains(".csv")) renamedFile = new File(resultFolderPath+realName.replaceAll(".csv", "(dataset).csv"));
			else renamedFile = new File(resultFolderPath+realName+"(dataset).csv");
			movedFile.renameTo(renamedFile);

			System.out.println("resultFolderPath to:"+resultFolderPath);
			System.out.println("realname:"+realName);
			System.out.println("movedFile to:"+movedFile);
			System.out.println("renamed to:"+filePath);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//		dataFile.renameTo(new File(BASE_FOLDER.getAbsolutePath() + dataFile.getName())); 
		System.out.println("File moved to: " + resultFolderPath);
		return renamedFile.getAbsolutePath();
	}
	
	public static String buildUserPath(int userid, int studyid){

		String userBasePath =  userid + "_" + studyid;//Encryptions.encryptStringToNumber(new UserManagerImpl().getUserById(userid).getUsername(), userid); 
		String studyBasePath;
		if(studyid == 0){
			studyBasePath = "tmp";
		}
		else{
			studyBasePath = userid + "_" + studyid;//Encryptions.encryptStringToNumber(new StudyManagerImpl().getStudyById(studyid).getName(), studyid);
		}
		return BASE_PATH + File.separator  + userBasePath + File.separator  + studyBasePath + File.separator  ;
	}
}
