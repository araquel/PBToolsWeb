package org.pbtools.analysis.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.analysis.webservice.manager.RServeManager;
import org.apache.commons.io.input.ReaderInputStream;
import org.zkoss.bind.BindContext;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Div;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Toolbarbutton;

public class FileUtilities {

	RServeManager rServeManager;
	public static void uploadFile(String filepath, InputStream file) {
		try {
			OutputStream out = new java.io.FileOutputStream(filepath);
			InputStream in = file;
			int read = 0;
			byte[] bytes = new byte[1024];
			while ((read = in.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			in.close();
			out.flush();
			out.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	static public File zipFolder(String srcFolder) throws Exception {
		ZipOutputStream zip = null;
		FileOutputStream fileWriter = null;

		File destZipFile = File.createTempFile(new File(srcFolder).getName(), ".zip");

		fileWriter = new FileOutputStream(destZipFile.getAbsolutePath());
		zip = new ZipOutputStream(fileWriter);

		addFolderToZip("", srcFolder, zip);
		zip.flush();
		zip.close();
		return destZipFile;
	}

	static private void addFileToZip(String path, String srcFile, ZipOutputStream zip) throws Exception {

		File folder = new File(srcFile);
		if (folder.isDirectory()) {
			addFolderToZip(path, srcFile, zip);
		} else {
			byte[] buf = new byte[1024];
			int len;
			FileInputStream in = new FileInputStream(srcFile);
			zip.putNextEntry(new ZipEntry(path + "/" + folder.getName()));
			while ((len = in.read(buf)) > 0) {
				zip.write(buf, 0, len);
			}
		}
	}

	static private void addFolderToZip(String path, String srcFolder, ZipOutputStream zip) throws Exception {
		File folder = new File(srcFolder);

		for (String fileName : folder.list()) {
			if (path.equals("")) {
				addFileToZip(folder.getName(), srcFolder + "/" + fileName, zip);
			} else {
				addFileToZip(path + "/" + folder.getName(), srcFolder + "/" + fileName, zip);
			}
		}
	}

	public static File getFileFromUpload(BindContext ctx, Component view) {

		UploadEvent event = (UploadEvent) ctx.getTriggerEvent();

		// System.out.println(event.getMedia().getStringData());

		String name = event.getMedia().getName();
		File tempFile = null;
		try {
			tempFile = File.createTempFile(name, ".tmp");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// if (!name.endsWith(".csv")) {
		// Messagebox.show("Error: File must be a text-based csv format",
		// "Upload Error", Messagebox.OK, Messagebox.ERROR);
		// return null;
		// }

		InputStream in = event.getMedia().isBinary() ? event.getMedia().getStreamData() : new ReaderInputStream(event.getMedia().getReaderData());
		FileUtilities.uploadFile(tempFile.getAbsolutePath(), in);
		return tempFile;
	}
//	public String isNumeric(String absolutePath, String columnName){
//		// TODO Auto-generated method stub
//		
//		System.out.println("called isNumeric");
//		String isNumeric = "FALSE";
//		try{
//			rServeManager = new RServeManager();
//			isNumeric = rServeManager.isColumnNumeric(absolutePath.replaceAll("\\\\","/"), columnName);
//		}
//		catch(NullPointerException npe){
////			isNumeric = ProjectExplorerView.rJavaManager.getRJavaDataManipulationManager().isColumnNumeric(absolutePath.replaceAll("\\\\","/"), columnName.replaceAll(" ","."));
//			System.out.println("caught Exception from isNumericable");
//		}
//
//		return isNumeric;
//	}

	
//	public static String[] getLevels(String varName, File file) {
//		int envtColumn = 0;
//		
//		for (int i = 0; i < table.getColumnCount(); i++) {
//			if (table.getColumn(i).getText()
//					.equals(facEnvt)) {
//				envtColumn = i;
//			}
//		}
//		ArrayList<String> envts = new ArrayList<String>();
//		for (int j = 0; j < table.getItemCount(); j++) {
//			String level = table.getItem(j).getText(
//					envtColumn);
//			if (!envts.contains(level)&& !level.isEmpty()) {
//				envts.add(level);
//			}
//		}
//
//		String[] envtLevels = new String[envts.size()];
//		for (int k = 0; k < envts.size(); k++) {
//			envtLevels[k] = (String) envts.get(k);
//		}
//		
//		return envtLevels;
//	}
	
	public static void exportData2(List<String> columns, List<ArrayList<String>> rows, String outputFileName) {
		List<ArrayList<String>> grid = rows;

		StringBuffer sb = new StringBuffer();

		System.out.println("creating File...");
		int ctr = 0;
		for (String s : columns) {
			ctr++;
			sb.append(s);
			if (ctr != columns.size())
				sb.append(",");
		}
		sb.append("\n");

		for (ArrayList<String> row : grid) {
			ctr = 0;
			for (String s : row) {
				ctr++;
				sb.append(s);
				if (ctr != row.size())
					sb.append(",");
			}
			sb.append("\n");
		}

		System.out.println("downloading File...");
		Filedownload.save(sb.toString().getBytes(), "text/plain", outputFileName + ".csv");
	}

	public static void exportGridData(Columns columns, Rows rows, String fileName) {
		// TODO Auto-generated method stub

		StringBuffer sb = new StringBuffer();

		System.out.println("creating File...");
		int ctr = 0;
		for (Component comp : columns.getChildren()) {
			Column c = (Column) comp;
			ctr++;
			sb.append(c.getLabel());
			if (ctr != columns.getChildren().size())
				sb.append(",");
		}
		sb.append("\n");

		for (Component row : rows.getChildren()) {
			ctr = 0;
			Row r = (Row) row;
			for (Component comp : r.getChildren()) {
				Label l;
				StringBuffer miniSb = new StringBuffer();
				try {
					l = (Label) comp;
					ctr++;
					sb.append(l.getValue());
				} catch (ClassCastException npe) {
					Div d = (Div) comp;
					miniSb.append("\"");
					for (Component toolbarButtons : comp.getChildren()) {
						Toolbarbutton tb = (Toolbarbutton) toolbarButtons;
						miniSb.append(tb.getLabel());
						if (!toolbarButtons.equals(comp.getLastChild()))
							miniSb.append(", ");
					}
					miniSb.append("\"");
					sb.append(miniSb.toString());
				}

				if (ctr != r.getChildren().size())
					sb.append(",");
			}
			sb.append("\n");
		}

		System.out.println("downloading File...");
		Filedownload.save(sb.toString().getBytes(), "text/plain", fileName + ".csv");
	}

	public static File createFileFromDatabase(List<String> columns, List<String[]> rows, String filePath) {
		// TODO Auto-generated method stub
		List<String[]> grid = rows;

		StringBuffer sb = new StringBuffer();

		System.out.println("creating File...");
		int ctr = 0;
		for (String s : columns) {
			ctr++;
			sb.append(s);
			if (ctr != columns.size())
				sb.append(",");
		}
		sb.append("\n");

		for (String[] row : grid) {
			ctr = 0;
			for (String s : row) {
				ctr++;
				sb.append(s);
				if (ctr != row.length)
					sb.append(",");
			}
			sb.append("\n");
		}

		FileWriter writer = null;
		try {
			writer = new FileWriter(filePath);
			writer.write(sb.toString());
			if (writer != null) {
				writer.close();
			}
		} catch (Exception e) {
			System.out.println(e);
		}

		return new File(filePath);
	}

	public static void exportData(List<String> columnList, List<String[]> dataList, String name) {
		List<String[]> grid = dataList;

		StringBuffer sb = new StringBuffer();

		System.out.println("creating File...");
		int ctr = 0;
		for (String s : columnList) {
			ctr++;
			sb.append(s);
			if (ctr != columnList.size())
				sb.append(",");
		}
		sb.append("\n");

		for (String[] row : grid) {
			ctr = 0;
			for (String s : row) {
				ctr++;
				sb.append(s);
				if (ctr != row.length)
					sb.append(",");
			}
			sb.append("\n");
		}

		System.out.println("downloading File...");
		Filedownload.save(sb.toString().getBytes(), "text/plain", name + ".csv");

	}

	public static File getFileFromUploadAsDatasetCsv(BindContext ctx, Component view) {

		UploadEvent event = (UploadEvent) ctx.getTriggerEvent();

		// System.out.println(event.getMedia().getStringData());

		String name = event.getMedia().getName();
		File tempFile = null;
		try {
			tempFile = File.createTempFile(name.replaceAll(".csv", ""), "(dataset).csv");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// if (!name.endsWith(".csv")) {
		// Messagebox.show("Error: File must be a text-based csv format",
		// "Upload Error", Messagebox.OK, Messagebox.ERROR);
		// return null;
		// }

		InputStream in = event.getMedia().isBinary() ? event.getMedia().getStreamData() : new ReaderInputStream(event.getMedia().getReaderData());
		FileUtilities.uploadFile(tempFile.getAbsolutePath(), in);
		return tempFile;
	}

}
