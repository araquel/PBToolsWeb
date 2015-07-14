package org.pbtools.analysis.result.view.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.pbtools.analysis.utilities.FileUtilities;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Div;
import org.zkoss.zul.Include;

import au.com.bytecode.opencsv.CSVReader;

public class CsvDataViewer {

	@Wire("#datagrid")
	Div divDatagrid;

	private String filePath;

	private List<String> columnList;
	private List<String[]> dataList = new ArrayList<String[]>();

	private String dataType;
	private CSVReader reader;
	private List<String[]> rawData;

	private String name;

	public CsvDataViewer() {
		// TODO Auto-generated constructor stub
	}

	public List<String[]> getDataList() {
		return dataList;
	}

	public void setDataList(List<String[]> dataList) {
		this.dataList = dataList;
	}

	public List<String> getColumnList() {

		return columnList;
	}

	public void setColumnList(List<String> columnList) {
		this.columnList = columnList;
	}

	public ArrayList<ArrayList<String>> getCsvData() {
		ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
		if (dataList.isEmpty())
			return result;
		for (int i = 0; i < dataList.size(); i++) {
			ArrayList<String> row = new ArrayList<String>();
			row.addAll(Arrays.asList(dataList.get(i)));
			result.add(row);
			row.add(0, "  ");
			// System.out.println(Arrays.toString(dataList.get(i)) + "ROW: " +
			// row.get(0));
		}
		return result;
	}

	@Init
	public void init(@ExecutionArgParam("rawData") List<String[]> rawData, @ExecutionArgParam("name") String name) throws IOException {
		this.name = name;
		populateCsvData(rawData);
	}

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		divDatagrid = (Div) view.getFellow("datagrid");
		includeDataGrid();
	}

	public void includeDataGrid() {
		/*
		 * if (!divDatagrid.getChildren().isEmpty())
		 * divDatagrid.getFirstChild().detach();
		 */
		Include incCSVData = new Include();
		incCSVData.setSrc("/analysis/csvgrid.zul");
		incCSVData.setParent(divDatagrid);
	}

	@Command
	public void exportRowData(@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx, @ContextParam(ContextType.VIEW) Component view) {
		FileUtilities.exportData(getColumnList(), getDataList(), name);

	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public void populateCsvData(List<String[]> rawData) {
//		try {

//			List<String[]> rawData = reader.readAll();
			columnList = new ArrayList<String>(Arrays.asList(rawData.get(0)));
			rawData.remove(0);
			dataList = new ArrayList<String[]>(rawData);
			// System.out.println(Arrays.toString(dataList.get(0)));
//		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			 TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}

	public void reloadCsvGrid() {

		if (!divDatagrid.getChildren().isEmpty())
			divDatagrid.getFirstChild().detach();
		Include incCSVData = new Include();
		incCSVData.setSrc("/analysis/updatestudy/csvdata.zul");
		incCSVData.setParent(divDatagrid);
	}

}
