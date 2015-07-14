package org.analysis.webservice.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.pbtools.analysis.utilities.AnalysisUtils;
import org.pbtools.analysis.utilities.InputTransform;
import org.pbtools.analysis.view.model.GeneticSimilarityModel;
import org.pbtools.analysis.view.model.GenomicSelectionModel;
import org.pbtools.analysis.view.model.MultiSiteAnalysisModel;
import org.pbtools.analysis.view.model.QTLAnalysisModel;
import org.pbtools.analysis.view.model.SingleSiteAnalysisModel;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Messagebox;

import com.google.gson.Gson;
public class WebServiceManager {
	private RConnection rConnection;

	private InputTransform inputTransform;
	private StringBuilder rscriptCommand;
	private String errorMessage;
	private static String BSLASH = "\\";
	private static String FSLASH = "/";
	private static String OUTPUTFOLDER_PATH;  //Sessions.getCurrent().getWebApp().getRealPath("resultanalysis")+ System.getProperty("file.separator");
	public static String DATA_PATH = System.getProperty("user.dir")+ System.getProperty("file.separator") + "sample_datasets" + System.getProperty("file.separator");

	public WebServiceManager(){
//		try {
//			rConnection	= new RConnection();
//			inputTransform = new InputTransform();
//			System.out.println("started rserve connection");
//			rConnection.eval("library(PBTools)");
			//			rConnection.eval("library(STAR)"); // not yet 3.0.2
//		} catch (RserveException e) {
			// TODO Auto-generated catch block
//			errorMessage="RConnection refused.\nRServe Library was not initialized, please contact your administrator.";
//			e.printStackTrace();
//			Messagebox.show(errorMessage);
//		}
	}

	private void readData(String dataFileName){
		String readData = "dataRead <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\",\"-\",\"\"), blank.lines.skip=TRUE, sep = \",\")";
		System.out.println(readData);
		try {
			rConnection.eval(readData);
		} catch (RserveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//		rConnection.close();
	}

	public ArrayList<String> getVariableInfo(String fileName, int fileFormat,String separator) {
		String funcGetVarInfo;

		ArrayList<String> toreturn=new ArrayList<String>();
		if (fileFormat == 2)  
			funcGetVarInfo = "varsAndTypes <- getVarInfo(fileName = \"" + fileName + "\", fileFormat = 2, separator = \"" + separator + "\")";
		else funcGetVarInfo = "varsAndTypes <- getVarInfo(fileName = \"" + fileName + "\", fileFormat = " + fileFormat + ", separator = NULL)"; 
		//		String writeTempData = "tryCatch(write.table(varsAndTypes,file =\"" + tempFileName + "\",sep=\":\",row.names=FALSE), error=function(err) \"notRun\")";

		System.out.println(funcGetVarInfo);
		//		System.out.println(writeTempData);

		String[] vars;

		try {
			rConnection.eval(funcGetVarInfo);
			//			rConnection.eval(writeTempData);
			vars = rConnection.eval("as.vector(varsAndTypes$Variable)").asStrings();
			String[] types = rConnection.eval("as.vector(varsAndTypes$Type)").asStrings();
			for (int i = 0; i < vars.length; i++){
				toreturn.add(vars[i]+":"+types[i]);
			}
			for(String s:toreturn){
				System.out.println(s);
			}
		} catch (RserveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (REXPMismatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		end();
		//		rConnection.close();
		return toreturn;
	}

	public void doSingleEnvironmentAnalysis(SingleSiteAnalysisModel ssaModel) {
		try{
			Gson gson = new Gson();
			String json = gson.toJson(ssaModel);
			
			System.out.println(json);

			Client c = ClientBuilder.newClient();
			WebTarget target= c.target(AnalysisUtils.WEB_SERVICE_ADDRESS+"/WS-RS/rest/SingleTrial/analyze");
//			WebTarget target= c.target(AnalysisUtils.WEB_SERVICE_ADDRESS+"/WS-RS/rest/SingleTrial/run");
//			WebTarget target= c.target("http://localhost:8080/WS-RS/rest/SingleTrial/analyze");
			Response response = target.request().post(Entity.json(json));

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
			}

			String output = response.getEntity().toString();
			System.out.println("Server response .... \n");
			System.out.println(response.readEntity(String.class));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	public void getFilesFromWS(String outputFolderPath) {
//
//		try{
//
//			Gson gson = new Gson();
//			String json = gson.toJson(ssaModel);
//			
//			System.out.println(json);
//
//			Client c = ClientBuilder.newClient();
//			WebTarget target= c.target("http://172.29.4.166:8080/WS-RS/rest/SingleTrial/analyze");
////			WebTarget target= c.target("http://localhost:8080/WS-RS/rest/SingleTrial/run");
////			WebTarget target= c.target("http://localhost:8080/WS-RS/rest/SingleTrial/analyze");
//			Response response = target.request().post(Entity.json(json));
//
//			if (response.getStatus() != 200) {
//				throw new RuntimeException("Failed : HTTP error code : "
//						+ response.getStatus());
//			}
//
//			String output = response.getEntity().toString();
//			System.out.println("Server response .... \n");
//			System.out.println(response.readEntity(String.class));
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	private String getDisplayName(String dataFileName) {
		// TODO Auto-generated method stub
		String[] newFile = dataFileName.split(FSLASH);
		String displayName=newFile[newFile.length-2] + FSLASH + newFile[newFile.length-1];
		System.out.println(displayName);
		return displayName ;
	}

	public void doSingleEnvironmentAnalysis2(SingleSiteAnalysisModel ssaModel) {
		// rjava manager for single site analysisn for DMAS
		String resultFolderPath = ssaModel.getResultFolderPath().replace(BSLASH, FSLASH);
		String outFileName = ssaModel.getOutFileName().replace(BSLASH, FSLASH);
		String dataFileName = ssaModel.getDataFileName().replace(BSLASH, FSLASH);
		int designIndex = ssaModel.getDesign();
		String[] respvars = ssaModel.getRespvars();
		String environment = ssaModel.getEnvironment();
		String[] environmentLevels = ssaModel.getEnvironmentLevels();
		String genotype = ssaModel.getGenotype();
		String block = ssaModel.getBlock();
		String rep = ssaModel.getRep();
		String row = ssaModel.getRow();
		String column = ssaModel.getColumn();
		boolean descriptiveStat = ssaModel.isDescriptiveStat();
		boolean varianceComponents = ssaModel.isVarianceComponents();
		boolean boxplotRawData = ssaModel.isBoxplotRawData();
		boolean histogramRawData = ssaModel.isHistogramRawData();
		boolean heatmapResiduals = ssaModel.isHeatmapResiduals();
		String heatmapRow = ssaModel.getHeatmapRow();
		String heatmapColumn = ssaModel.getHeatmapColumn();
		boolean diagnosticPlot = ssaModel.isDiagnosticPlot();
		boolean genotypeFixed = ssaModel.isGenotypeFixed();
		boolean performPairwise = ssaModel.isPerformPairwise();
		String pairwiseAlpha = ssaModel.getPairwiseAlpha();
		String[] genotypeLevels = ssaModel.getGenotypeLevels();
		String[] controlLevels = ssaModel.getControlLevels();
		boolean compareControl = ssaModel.isCompareControl();
		boolean performAllPairwise = ssaModel.isPerformAllPairwise();
		boolean genotypeRandom = ssaModel.isGenotypeRandom();
		boolean excludeControls = ssaModel.isExcludeControls();
		boolean genoPhenoCorrelation = ssaModel.isGenoPhenoCorrelation();
		boolean specifiedContrast = ssaModel.isSpecifiedContrast();
		String contrastFileName = ssaModel.getContrastFileName();

		String respvarVector= inputTransform.createRVector(respvars);
		//		String genotypeLevelsVector= inputTransform.createRVector(genotypeLevels);
		String controlLevelsVector= inputTransform.createRVector(controlLevels);
		boolean runningFixedSuccess =true;
		boolean runningRandomSuccess =true;
		boolean printAllOutputFixed =true;
		boolean printAllOutputRandom =true;

		try {				
			String designUsed = new String();
			String design = new String();
			switch (designIndex) {
			case 0: {
				designUsed = "Randomized Complete Block (RCB)"; 
				design = "RCB"; 
				break;
			}
			case 1: {
				designUsed = "Augmented RCB"; 
				design = "AugRCB";
				break;
			}
			case 2: {
				designUsed = "Augmented Latin Square"; 
				design = "AugLS";
				break;
			}
			case 3: {
				designUsed = "Alpha-Lattice"; 
				design = "Alpha";
				break;
			}
			case 4: {
				designUsed = "Row-Column"; 
				design = "RowCol";
				break;
			}
			case 5: {
				designUsed = "Latinized Alpha-Lattice"; 
				design = "LatinAlpha";
				break;
			}
			case 6: {
				designUsed = "Latinized Row-Column"; 
				design = "LatinRowCol";
				break;
			}
			default: {
				designUsed = "Randomized Complete Block (RCB)"; 
				design = "RCB";
				break;
			}
			}

			String readData = "data <- read.csv(\"" + dataFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\",\" \",\"\"), blank.lines.skip=TRUE, sep = \",\")";
			System.out.println(readData);
			rConnection.eval(readData);
			String runSuccessData = rConnection.eval("data").toString();

			if (runSuccessData != null && runSuccessData.equals("notRun")) {	
				System.out.println("error");
				rConnection.eval("capture.output(cat(\"\n***Error reading data.***\n\"),file=\"" + outFileName + "\",append = FALSE)"); 
			}
			else {
				String setWd = "setwd(\"" + resultFolderPath + "\")";
				System.out.println(setWd);
				rConnection.eval(setWd);
			}
			String dataFileNameDisplay = getDisplayName(dataFileName);
			String usedData = "capture.output(cat(\"\nDATA FILE: " + dataFileNameDisplay + "\n\",file=\"" + outFileName + "\"))";
			String outFile = "capture.output(cat(\"\nSINGLE-ENVIRONMENT ANALYSIS\n\"),file=\"" + outFileName + "\",append = TRUE)";
			String usedDesign = "capture.output(cat(\"\nDESIGN: " + designUsed + "\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
			String sep = "capture.output(cat(\"------------------------------\"),file=\"" + outFileName + "\",append = TRUE)";
			String sep2 = "capture.output(cat(\"==============================\n\"),file=\"" + outFileName + "\",append = TRUE)";
			String outspace = "capture.output(cat(\"\n\"),file=\"" + outFileName + "\",append = TRUE)"; 

			rConnection.eval(usedData);
			rConnection.eval(outFile);
			rConnection.eval(usedDesign);

			// OUTPUT
			// Genotype Fixed
			if (genotypeFixed) {
				String funcSsaFixed = null;
				String groupVars = null;
				if (environment == "NULL") {
					if (designIndex == 0 || designIndex == 1){
						funcSsaFixed = "ssa1 <- try(ssa.test(\"" + design + "\",data,"+ respvarVector + ",\"" + genotype + "\",\"" + block+ "\",column=NULL, rep=NULL," + environment+ ", is.random = FALSE), silent = TRUE)";
						groupVars = "c(\"" + genotype + "\", \"" + block + "\")";
					} else if (designIndex == 2) {
						funcSsaFixed = "ssa1 <- try(ssa.test(\"" + design + "\",data,"+ respvarVector + ",\"" + genotype + "\",\"" + row+ "\",\"" + column + "\", rep=NULL," + environment+ ", is.random = FALSE), silent = TRUE)";
						groupVars = "c(\"" + genotype + "\", \"" + row + "\", \"" + column + "\")";
					} else if (designIndex == 3 || designIndex == 5) {
						funcSsaFixed = "ssa1 <- try(ssa.test(\"" + design + "\",data,"+ respvarVector + ",\"" + genotype + "\",\"" + block+ "\",column=NULL,\"" + rep + "\"," + environment+ ", is.random = FALSE), silent = TRUE)";
						groupVars = "c(\"" + genotype + "\", \"" + block + "\", \"" + rep + "\")";
					} else if (designIndex == 4 || designIndex == 6) {
						funcSsaFixed = "ssa1 <- try(ssa.test(\"" + design + "\",data,"+ respvarVector + ",\"" + genotype + "\",\"" + row+ "\",\"" + column + "\",\"" + rep + "\","+ environment + ", is.random = FALSE), silent = TRUE)";
						groupVars = "c(\"" + genotype + "\", \"" + rep + "\", \"" + row + "\", \"" + column + "\")";
					}
				} else {
					if (designIndex == 0 || designIndex == 1){
						funcSsaFixed = "ssa1 <- try(ssa.test(\"" + design + "\",data,"+ respvarVector + ",\"" + genotype + "\",\"" + block+ "\",column=NULL, rep=NULL,\"" + environment+ "\", is.random = FALSE), silent = TRUE)";
						groupVars = "c(\"" + genotype + "\", \"" + block + "\")";
					} else if (designIndex == 2) {
						funcSsaFixed = "ssa1 <- try(ssa.test(\"" + design + "\",data,"+ respvarVector + ",\"" + genotype + "\",\"" + row+ "\",\"" + column + "\", rep=NULL,\"" + environment+ "\", is.random = FALSE), silent = TRUE)";
						groupVars = "c(\"" + genotype + "\", \"" + row + "\", \"" + column + "\")";
					} else if (designIndex == 3 || designIndex == 5) {
						funcSsaFixed = "ssa1 <- try(ssa.test(\"" + design + "\",data,"+ respvarVector + ",\"" + genotype + "\",\"" + block+ "\",column=NULL,\"" + rep + "\",\"" + environment+ "\", is.random = FALSE), silent = TRUE)";
						groupVars = "c(\"" + genotype + "\", \"" + block + "\", \"" + rep + "\")";
					} else if (designIndex == 4 || designIndex == 6) {
						funcSsaFixed = "ssa1 <- try(ssa.test(\"" + design + "\",data,"+ respvarVector + ",\"" + genotype + "\",\"" + row+ "\",\"" + column + "\",\"" + rep + "\",\""+ environment + "\", is.random = FALSE), silent = TRUE)";
						groupVars = "c(\"" + genotype + "\", \"" + rep + "\", \"" + row + "\", \"" + column + "\")";
					}
				}
				String fixedHead = "capture.output(cat(\"GENOTYPE AS: Fixed\n\"),file=\""+ outFileName + "\",append = TRUE)";
				rConnection.eval(funcSsaFixed);
				rConnection.eval(sep2);
				rConnection.eval(fixedHead);
				rConnection.eval(sep2);
				rConnection.eval(outspace);

				System.out.println(funcSsaFixed);

				String runSuccess = rConnection.eval("class(ssa1)").asString();
				if (runSuccess != null && runSuccess.equals("try-error")) {	
					System.out.println("ssa.test: error");
					String checkError = "msg <- trimStrings(strsplit(ssa1, \":\")[[1]])";
					String checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
					String checkError3 ="msg <- gsub(\"\\\"\", \"\", msg)";
					String checkError4 = "capture.output(cat(\"*** \nERROR in ssa.test function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
					rConnection.eval(checkError);
					rConnection.eval(checkError2);
					rConnection.eval(checkError3);
					rConnection.eval(checkError4);

					runningFixedSuccess=false;

				}
				else {
					for (int k = 0; k < respvars.length; k++) {
						int i = k + 1; // 1-relative index;
						String respVarHead = "capture.output(cat(\"\nRESPONSE VARIABLE: " + respvars[k] + "\n\"),file=\"" + outFileName + "\",append = TRUE)";
						rConnection.eval(sep);
						rConnection.eval(respVarHead);
						rConnection.eval(sep);
						rConnection.eval(outspace);

						// optional output: descriptive statistics
						if (descriptiveStat) {
							String funcDesc = null;
							if (environment=="NULL") {
								funcDesc = "outDesc <- try(DescriptiveStatistics(data, \"" + respvars[k] + "\", " + environment + "), silent=TRUE)";
							} else {
								funcDesc = "outDesc <- try(DescriptiveStatistics(data, \"" + respvars[k] + "\", \"" + environment + "\"), silent=TRUE)";
							}
							System.out.println(funcDesc);
							rConnection.eval(funcDesc);

							String outDescStat = "capture.output(cat(\"DESCRIPTIVE STATISTICS:\n\n\"),file=\"" + outFileName + "\",append = TRUE)"; 
							String outDescStat2 = "capture.output(outDesc,file=\"" + outFileName + "\",append = TRUE)";

							String runSuccessDescStat = rConnection.eval("class(outDesc)").asString();	
							if (runSuccessDescStat != null && runSuccessDescStat.equals("try-error")) {	
								System.out.println("desc stat: error");
								String checkError = "msg <- trimStrings(strsplit(outDesc, \":\")[[1]])";
								String checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
								String checkError3 ="msg <- gsub(\"\\\"\", \"\", msg)";
								String checkError4 = "capture.output(cat(\"*** \nERROR in DescriptiveStatistics function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
								rConnection.eval(checkError);
								rConnection.eval(checkError2);
								rConnection.eval(checkError3);
								rConnection.eval(checkError4);
							} 
							else {
								rConnection.eval(outspace);
								rConnection.eval(outDescStat);
								rConnection.eval(outDescStat2);
								rConnection.eval(outspace);
							}
						}
						int envLevelsLength=0;
						if (environment == "NULL") {
							envLevelsLength = 1;
						} else {
							envLevelsLength = environmentLevels.length;
						}

						for (int m = 0; m < envLevelsLength; m++) { // no of envts or sites
							printAllOutputFixed=true;
							int j = m + 1; // 1-relative index;
							if (environment != "NULL") {
								String envtHead = "capture.output(cat(\"\nANALYSIS FOR: "+ environment + "\", \" = \" ,ssa1$output[[" + i	+ "]]$site[[" + j + "]]$env,\"\n\"),file=\""+ outFileName + "\",append = TRUE)";
								rConnection.eval(sep);
								rConnection.eval(envtHead);
								rConnection.eval(sep);
								rConnection.eval(outspace);
							}

							//check if the data has too many missing observation
							double nrowData=rConnection.eval("ssa1$output[[" + i + "]]$site[[" + j + "]]$responseRate").asDouble();
							if (nrowData < 0.80) {
								String allNAWarning = rConnection.eval("ssa1$output[[" + i + "]]$site[[" + j + "]]$manyNAWarning").asString();
								String printError1 = "capture.output(cat(\"***\\n\"), file=\"" + outFileName + "\",append = TRUE)";
								String printError2 = "capture.output(cat(\"ERROR:\\n\"), file=\"" + outFileName + "\",append = TRUE)";
								String printError3 = "capture.output(cat(\"" + allNAWarning + "\\n\"), file=\"" + outFileName + "\",append = TRUE)";

								rConnection.eval(outspace);
								rConnection.eval(printError1);
								rConnection.eval(printError2);
								rConnection.eval(printError3);
								rConnection.eval(printError1);
								rConnection.eval(outspace);
								rConnection.eval(outspace);
								printAllOutputFixed=false;

							} else {
								String lmerRun=rConnection.eval("ssa1$output[[" + i + "]]$site[[" + j + "]]$lmerRun").asString();
								if (lmerRun.equals("ERROR")) {
									String lmerError = rConnection.eval("ssa1$output[[" + i + "]]$site[[" + j + "]]$lmerError").asString();
									String printError1 = "capture.output(cat(\"***\\n\"), file=\"" + outFileName + "\",append = TRUE)";
									String printError2 = "capture.output(cat(\"ERROR:\\n\"), file=\"" + outFileName + "\",append = TRUE)";
									String printError3 = "capture.output(cat(\"" + lmerError + "\\n\"), file=\"" + outFileName + "\",append = TRUE)";

									rConnection.eval(outspace);
									rConnection.eval(printError1);
									rConnection.eval(printError2);
									rConnection.eval(printError3);
									rConnection.eval(printError1);
									rConnection.eval(outspace);
									rConnection.eval(outspace);
									printAllOutputFixed=false;
								}
							}

							if (printAllOutputFixed) {
								// default output: trial summary
								String funcTrialSum = "funcTrialSum <- try(class.information(" + groupVars + ",ssa1$output[[" + i + "]]$site[[" + j + "]]$data), silent=TRUE)";
								String trialSumHead = "capture.output(cat(\"\nDATA SUMMARY:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
								String trialObsRead = "capture.output(cat(\"Number of observations read: \", ssa1$output[["	+ i	+ "]]$site[[" + j + "]]$obsread[[1]],\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
								String trialObsUsed = "capture.output(cat(\"Number of observations used: \", ssa1$output[["	+ i	+ "]]$site[[" + j + "]]$obsused[[1]],\"\n\n\"),file=\""	+ outFileName + "\",append = TRUE)";
								String trialSum = "capture.output(funcTrialSum,file=\"" + outFileName + "\",append = TRUE)";

								rConnection.eval(funcTrialSum);
								//								System.out.println(funcTrialSum);

								String runSuccessTS = rConnection.eval("class(funcTrialSum)").asString();
								if (runSuccessTS != null && runSuccessTS.equals("try-error")) {	
									System.out.println("class info: error");
									String checkError = "msg <- trimStrings(strsplit(funcTrialSum, \":\")[[1]])";
									String checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
									String checkError3 ="msg <- gsub(\"\\\"\", \"\", msg)";
									String checkError4 = "capture.output(cat(\"*** \nERROR in class.information function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
									rConnection.eval(checkError);
									rConnection.eval(checkError2);
									rConnection.eval(checkError3);
									rConnection.eval(checkError4);
								}
								else {
									rConnection.eval(trialSumHead);
									rConnection.eval(trialObsRead);
									rConnection.eval(trialObsUsed);
									rConnection.eval(trialSum);
									rConnection.eval(outspace);
								}	

								// optional output: variance components
								if (varianceComponents) {
									String outVarComp = "capture.output(cat(\"\nVARIANCE COMPONENTS TABLE:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
									String outVarComp2 = "capture.output(ssa1$output[[" + i + "]]$site[[" + j + "]]$varcomp.table,file=\"" + outFileName + "\",append = TRUE)";

									rConnection.eval(outVarComp);
									rConnection.eval(outVarComp2);
									rConnection.eval(outspace);
								}

								//default output: test for genotypic effect
								String outAnovaTable1 = "capture.output(cat(\"\nTESTING FOR THE SIGNIFICANCE OF GENOTYPIC EFFECT:\n\"),file=\"" + outFileName + "\",append = TRUE)";
								String outAnovaTable2 = "library(lmerTest)";
								String outAnovaTable3 = "model1b <- lmer(formula(ssa1$output[[" + i + "]]$site[[" + j + "]]$formula1), data = ssa1$output[[" + i + "]]$site[[" + j + "]]$data, REML = T)";
								String outAnovaTable4 = "a.table <- anova(model1b)";
								String outAnovaTable5 = "pvalue <- formatC(as.numeric(format(a.table[1,6], scientific=FALSE)), format=\"f\")";
								String outAnovaTable6 = "a.table<-cbind(round(a.table[,c(\"NumDF\", \"Sum Sq\", \"Mean Sq\", \"F.value\", \"DenDF\")], digits=4),pvalue)";
//								String outAnovaTable6 = "a.table<-cbind(round(a.table[,1:5], digits=4),pvalue)";
								String outAnovaTable7 = "colnames(a.table)<-c(\"Df\", \"Sum Sq\", \"Mean Sq\", \"F value\", \"Denom\", \"Pr(>F)\")";
								String outAnovaTable8 = "capture.output(cat(\"Analysis of Variance Table with Satterthwaite Denominator Df\n\"),file=\"" + outFileName + "\",append = TRUE)";
								String outAnovaTable9 = "capture.output(a.table,file=\"" + outFileName + "\",append = TRUE)";
								String outAnovaTable10 = "detach(\"package:lmerTest\")";

								rConnection.eval(outAnovaTable1);
								rConnection.eval(outAnovaTable2);
								rConnection.eval(outAnovaTable3);
								rConnection.eval(outAnovaTable4);
								rConnection.eval(outAnovaTable5);
								rConnection.eval(outAnovaTable6);
								rConnection.eval(outAnovaTable7);
								rConnection.eval(outspace);
								rConnection.eval(outAnovaTable8);
								rConnection.eval(outAnovaTable9);
								rConnection.eval(outspace);
								rConnection.eval(outAnovaTable10);

								// default output: test for genotypic effect
								//								String outAnovaTable1b = "capture.output(cat(\"\nTESTING FOR THE SIGNIFICANCE OF GENOTYPIC EFFECT:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
								//								String outAnovaTable2b = "library(pbkrtest)";
								//								String outAnovaTable3b = "model1b <- lmer(formula(ssa1$output[[" + i + "]]$site[[" + j + "]]$formula1), data = ssa1$output[[" + i + "]]$site[[" + j + "]]$data, REML = T)";
								//								String outAnovaTable4b = "model2b <- lmer(formula(ssa1$output[[" + i + "]]$site[[" + j + "]]$formula2), data = ssa1$output[[" + i + "]]$site[[" + j + "]]$data, REML = T)";
								//								String outAnovaTable5b = "anova.table1 <- KRmodcomp(model1b, model2b)[[1]][1,]";
								//								String outAnovaTable6b = "anova.table1 <- anova.table1[-c(4)]";
								//								String outAnovaTable7b = "rownames(anova.table1) <- " + genotype;
								//								String outAnovaTable8b = "colnames(anova.table1) <- c(\"F value\", \"Num df\", \"Denom df\", \"Pr(>F)\")";
								//								String outAnovaTable9b = "anova.table1[1, \"F value\"] <- format(round(anova.table1[1, \"F value\"],2), digits=2, nsmall=2, scientific=FALSE)";
								//								String outAnovaTable10b = "anova.table1[1, \"Pr(>F)\"] <- formatC(as.numeric(format(anova.table1[1, \"Pr(>F)\"], scientific=FALSE)), format=\"f\")";
								//								String outAnovaTable11b = "capture.output(anova.table1,file=\"" + outFileName + "\",append = TRUE)";
								//								String outAnovaTable12b = "detach(\"package:pbkrtest\")";
								//								
								//								rConnection.eval(outAnovaTable1b);
								//								rConnection.eval(outAnovaTable2b);
								//								rConnection.eval(outAnovaTable3b);
								//								rConnection.eval(outAnovaTable4b);
								//								rConnection.eval(outAnovaTable5b);
								//								rConnection.eval(outAnovaTable6b);
								//								rConnection.eval(outAnovaTable7b);
								//								rConnection.eval(outAnovaTable8b);
								//								rConnection.eval(outAnovaTable9b);
								//								rConnection.eval(outAnovaTable10b);
								//								rConnection.eval(outAnovaTable11b);
								//								rConnection.eval(outAnovaTable12b);
								//								rConnection.eval(outspace);

								//								String outAnovaTable1b = "capture.output(cat(\"\nTESTING FOR THE SIGNIFICANCE OF GENOTYPIC EFFECT:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
								//								String outAnovaTable2b = "capture.output(ssa1$output[[" + i + "]]$site[[" + j + "]]$model.comparison,file=\"" + outFileName + "\",append = TRUE)";
								//								
								//								rConnection.eval(outAnovaTable1b);
								//								rConnection.eval(outAnovaTable2b);
								//								rConnection.eval(outspace);

								//default output: LSMeans
								String outDescStat = "capture.output(cat(\"\nGENOTYPE LSMEANS AND STANDARD ERRORS:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
								String outDescStat2 = "capture.output(ssa1$output[[" + i + "]]$site[[" + j + "]]$summary.statistic,file=\"" + outFileName + "\",append = TRUE)"; 

								rConnection.eval(outDescStat);
								rConnection.eval(outDescStat2);
								rConnection.eval(outspace);

								//default output: standard error of the differences
								String outsedTable = "capture.output(cat(\"\nSTANDARD ERROR OF THE DIFFERENCE (SED):\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
								String outsedTable2 = "capture.output(ssa1$output[[" + i + "]]$site[[" + j + "]]$sedTable,file=\"" + outFileName + "\",append = TRUE)";

								rConnection.eval(outsedTable);
								rConnection.eval(outsedTable2);
								rConnection.eval(outspace);

								if (compareControl || specifiedContrast) {
									double pairwiseSig = Double.valueOf(pairwiseAlpha);

									if (compareControl) {
										String compareCtrl = "cntrl <- try(contrastAnalysis(model = ssa1$output[["+ i +"]]$site[["+j+"]]$model, contrastOpt = \"control\", controlLevel = "+ controlLevelsVector +", alpha = "+ pairwiseSig +"), silent = TRUE)";
										System.out.println(compareCtrl);
										rConnection.eval(compareCtrl);

										String runSuccessCompareCtrl = rConnection.eval("class(cntrl)").asString();
										if (runSuccessCompareCtrl != null && runSuccessCompareCtrl.equals("try-error")) {
											System.out.println("compare with control: error");
											String checkError = "msg <- trimStrings(strsplit(cntrl, \":\")[[1]])";
											String checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
											String checkError3 ="msg <- gsub(\"\\\"\", \"\", msg)";
											String checkError4 = "capture.output(cat(\"*** \nERROR in contrastAnalysis function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
											rConnection.eval(checkError);
											rConnection.eval(checkError2);
											rConnection.eval(checkError3);
											rConnection.eval(checkError4);

										} else {
											String outCompareControl = "if (nrow(cntrl) != 0) {\n";
											outCompareControl = outCompareControl + "     capture.output(cat(\"\nSIGNIFICANT LINEAR CONTRAST AT ALPHA = "+ pairwiseSig+":\n\n\"),file=\"" + outFileName	+ "\",append = TRUE)\n";
											outCompareControl = outCompareControl + "     capture.output(cntrl,file=\"" + outFileName	+ "\",append = TRUE)\n";
											outCompareControl = outCompareControl + "     capture.output(cat(\"\n\n\"),file=\"" + outFileName	+ "\",append = TRUE)\n";
											outCompareControl = outCompareControl + "} else {";
											outCompareControl = outCompareControl + "     capture.output(cat(\"\n NO SIGNIFICANT LINEAR CONTRAST AT ALPHA = "+ pairwiseSig+":\n\n\"),file=\"" + outFileName	+ "\",append = TRUE)\n";
											outCompareControl = outCompareControl + "     capture.output(cat(\"\n\n\"),file=\"" + outFileName	+ "\",append = TRUE)\n";
											outCompareControl = outCompareControl + "}";
											System.out.println(outCompareControl);
											rConnection.eval(outCompareControl);	
										}	
									} // end stmt if (compareControl)

									if (specifiedContrast) {
										String readContrastData = "contrastData <- read.csv(\"" + contrastFileName + "\", header = TRUE, na.strings = c(\"NA\",\".\",\" \",\"\"), blank.lines.skip=TRUE, sep = \",\")";
										System.out.println(readContrastData);
										rConnection.eval(readContrastData);
										String runSuccessContrastData = rConnection.eval("contrastData").asString();

										if (runSuccessContrastData != null && runSuccessContrastData.equals("notRun")) {	
											System.out.println("error");
											rConnection.eval("capture.output(cat(\"\n***Error reading contrast data.***\n\"),file=\"" + outFileName + "\",append = FALSE)"); 
										} else {
											String userContrast = "userContrast <- try(contrastAnalysis(model = ssa1$output[["+ i +"]]$site[["+j+"]]$model, contrastOpt = \"user\", contrast = contrastData, alpha = "+ pairwiseSig +"), silent = TRUE)";
											System.out.println(userContrast);
											rConnection.eval(userContrast);

											String runSuccessUserCtrl = rConnection.eval("class(userContrast)").asString();
											if (runSuccessUserCtrl != null && runSuccessUserCtrl.equals("try-error")) {
												System.out.println("compare with control: error");
												String checkUError = "msg <- trimStrings(strsplit(userContrast, \":\")[[1]])";
												String checkUError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
												String checkUError3 ="msg <- gsub(\"\\\"\", \"\", msg)";
												String checkUError4 = "capture.output(cat(\"*** \nERROR in contrastAnalysis function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
												rConnection.eval(checkUError);
												rConnection.eval(checkUError2);
												rConnection.eval(checkUError3);
												rConnection.eval(checkUError4);

											} else {
												String outCompareControl = "if (nrow(userContrast) != 0) {\n";
												outCompareControl = outCompareControl + "     capture.output(cat(\"\nSIGNIFICANT LINEAR CONTRAST AT ALPHA = "+ pairwiseSig+":\n\n\"),file=\"" + outFileName	+ "\",append = TRUE)\n";
												outCompareControl = outCompareControl + "     capture.output(userContrast,file=\"" + outFileName	+ "\",append = TRUE)\n";
												outCompareControl = outCompareControl + "     capture.output(cat(\"\n\n\"),file=\"" + outFileName	+ "\",append = TRUE)\n";
												outCompareControl = outCompareControl + "} else {";
												outCompareControl = outCompareControl + "     capture.output(cat(\"\n NO SIGNIFICANT LINEAR CONTRAST AT ALPHA = "+ pairwiseSig+":\n\n\"),file=\"" + outFileName	+ "\",append = TRUE)\n";
												outCompareControl = outCompareControl + "     capture.output(cat(\"\n\n\"),file=\"" + outFileName	+ "\",append = TRUE)\n";
												outCompareControl = outCompareControl + "}";
												System.out.println(outCompareControl);
												rConnection.eval(outCompareControl);	
											}	

										} // end stmt if-else (runSuccessContrastData != null && runSuccessContrastData.equals("notRun")) 

									} // end stmt if (specifiedContrast)

								} // end stmt if (compareControl || specifiedContrast) 	


								//								if (performPairwise) {
								//									
								//									double pairwiseSig = Double.valueOf(pairwiseAlpha);
								//									
								////									rConnection.rniAssign("trmt.levels",	rConnection.rniPutStringArray(genotypeLevels),0); // a string array from OptionsPage
								//									
								//										if (compareControl) {
								////											rConnection.rniAssign("controlLevels",rConnection.rniPutStringArray(controlLevels),0); // a string array from OptionsPage
								//																		
								//											String funcPwC = "pwControl <- try(ssa.pairwise(ssa1$output[[" + i + "]]$site[["	+ j	+ "]]$model, type = \"Dunnett\", alpha = "	+ pairwiseSig + ", control.level = " + controlLevelsVector + "), silent=TRUE)";
								//											String outCompareControl = "capture.output(cat(\"\nSIGNIFICANT PAIRWISE COMPARISONS (IF ANY): \nCompared with control(s)\n\n\"),file=\"" + outFileName	+ "\",append = TRUE)";
								//											String outCompareControl2n = "capture.output(pwControl$result,file=\""	+ outFileName	+ "\",append = TRUE)";
								//											String outCompareControl3n = "capture.output(cat(\"\n\n\"),file=\"" + outFileName	+ "\",append = TRUE)";
								//											System.out.println(funcPwC);
								//											rConnection.eval(funcPwC);
								//											rConnection.eval(outCompareControl);
								//											
								//											
								//					  						String runSuccessPwC = rConnection.eval("class(pwControl)").toString();	
								//											if (runSuccessPwC != null && runSuccessPwC.equals("try-error")) {	
								//												System.out.println("compare with control: error");
								//												String checkError = "msg <- trimStrings(strsplit(pwControl, \":\")[[1]])";
								//												String checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
								//												String checkError3 ="msg <- gsub(\"\\\"\", \"\", msg)";
								//												String checkError4 = "capture.output(cat(\"*** \nERROR in ssa.pairwise function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
								//												rConnection.eval(checkError);
								//												rConnection.eval(checkError2);
								//												rConnection.eval(checkError3);
								//												rConnection.eval(checkError4);
								//											}
								//											else {
								//												
								//												rConnection.eval(outCompareControl2n);
								//												
								//												// display warning generated by checkTest in ssa.test
								//												String warningControlTest = rConnection.eval("pwControl$controlTestWarning").toString();
								//												
								//												if (!warningControlTest.equals("NONE")) {
								//													String warningCheckTest2 = "capture.output(cat(\"----- \nNOTE:\\n\"), file=\"" + outFileName + "\",append = TRUE)";
								//													String warningCheckTest3 = "capture.output(cat(\"" + warningControlTest + "\\n\"), file=\"" + outFileName + "\",append = TRUE)";
								//													
								//													rConnection.eval(warningCheckTest2);
								//													rConnection.eval(warningCheckTest3);
								//												}
								//												
								//												rConnection.eval(outCompareControl3n);
								//												
								//												System.out.println("pairwise control test:" + warningControlTest); 
								//												
								//											}
								//										} else if (performAllPairwise) {
								//											String outPerformAllPairwise = "capture.output(cat(\"\nSIGNIFICANT PAIRWISE COMPARISONS (IF ANY): \n\n\"),file=\""	+ outFileName	+ "\",append = TRUE)";
								//											rConnection.eval(outPerformAllPairwise);
								//											if (genotypeLevels.length > 0 && genotypeLevels.length < 16) {
								//												String funcPwAll = "pwAll <- try(ssa.pairwise(ssa1$output[[" + i + "]]$site[[" + j + "]]$model, type = \"Tukey\", alpha = "+ pairwiseSig + ", control.level = NULL), silent=TRUE)";
								//												String outPerformAllPairwise2 = "capture.output(pwAll$result,file=\"" + outFileName + "\",append = TRUE)";
								//												String outPerformAllPairwise3 = "capture.output(cat(\"\n\"),file=\""	+ outFileName	+ "\",append = TRUE)";
								//												rConnection.eval(funcPwAll);
								////												System.out.println(funcPwAll);
								//		
								//												String runSuccessPwAll = rConnection.eval("class(pwAll)").toString();
								//												if (runSuccessPwAll != null && runSuccessPwAll.equals("try-error")) {	
								//													System.out.println("all pairwise: error");
								//													String checkError = "msg <- trimStrings(strsplit(pwAll, \":\")[[1]])";
								//													String checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
								//													String checkError3 ="msg <- gsub(\"\\\"\", \"\", msg)";
								//													String checkError4 = "capture.output(cat(\"*** \nERROR in ssa.pairwise function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
								//													rConnection.eval(checkError);
								//													rConnection.eval(checkError2);
								//													rConnection.eval(checkError3);
								//													rConnection.eval(checkError4);
								//												}
								//												else {
								//													rConnection.eval(outPerformAllPairwise2);
								//													rConnection.eval(outPerformAllPairwise3);
								//												}
								//											} else {
								//												String nLevelsLarge = "capture.output(cat(\"***\nExceeded maximum number of genotypes that can be compared. \n***\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
								//												rConnection.eval(nLevelsLarge);
								//											}
								//										}
								//								} else {
								//									rConnection.eval(outspace);
								//								}
							}

						} // end of for loop for diff envts
					}

					//default output: save the means, standard error of the mean, variance and no. of reps in a file
					String checkMeanSSE = rConnection.eval("ssa1$meansseWarning").asString();
					String checkVarRep = rConnection.eval("ssa1$varrepWarning").asString();
					System.out.println("checkMeanSSE: " + checkMeanSSE);
					System.out.println("checkVarRep: " + checkVarRep);

					if (checkMeanSSE.equals("empty") | checkVarRep.equals("empty")) {
						System.out.println("Saving means not done.");
					} else {
						String meansFileName = "meansFileName <- paste(\"" + resultFolderPath + "\",\"summaryStats.csv\", sep=\"\")";
						String funcSaveSesVarRep=null;
						if (environment=="NULL") {
							funcSaveSesVarRep = "meansVar <- merge(ssa1$meansse,ssa1$varrep, by = \"EnvLevel\")";
						} else {
							funcSaveSesVarRep = "meansVar <- merge(ssa1$meansse,ssa1$varrep, by = \"" + environment + "\")";
						}
						String funcSaveSesVarRepCsv = "saveMeans <- try(write.table(meansVar,file = meansFileName ,sep=\",\",row.names=FALSE), silent=TRUE)";

						rConnection.eval(meansFileName);
						rConnection.eval(funcSaveSesVarRep);
						rConnection.eval(funcSaveSesVarRepCsv);

						String runSuccessSaveMeansSes = rConnection.eval("class(saveMeans)").asString();
						if (runSuccessSaveMeansSes != null && runSuccessSaveMeansSes.equals("try-error")) {	
							System.out.println("saving means file: error");
							String checkError = "msg <- trimStrings(strsplit(saveMeans, \":\")[[1]])";
							String checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
							String checkError3 ="msg <- gsub(\"\\\"\", \"\", msg)";
							String checkError4 = "capture.output(cat(\"*** \nERROR in saving means file:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
							rConnection.eval(checkError);
							rConnection.eval(checkError2);
							rConnection.eval(checkError3);
							rConnection.eval(checkError4);
						} 
					}

					//diagnostic plots for genotype fixed
					if (diagnosticPlot) {
						String diagPlotsFunc=null;
						if (environment=="NULL") {
							diagPlotsFunc = "diagPlots <- try(graph.sea.diagplots(data, " + respvarVector + ", env = " + environment + ", is.random = FALSE, ssa1), silent=TRUE)";
						} else {
							diagPlotsFunc = "diagPlots <- try(graph.sea.diagplots(data, " + respvarVector + ", env = \"" + environment + "\", is.random = FALSE, ssa1), silent=TRUE)";
						}
						System.out.println(diagPlotsFunc);
						rConnection.eval(diagPlotsFunc);

						String runSuccessDiagPlots = rConnection.eval("class(diagPlots)").asString();
						if (runSuccessDiagPlots != null && runSuccessDiagPlots.equals("try-error")) {	
							System.out.println("diagnostic plots(genotype fixed): error");
							String checkError = "msg <- trimStrings(strsplit(diagPlots, \":\")[[1]])";
							String checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
							String checkError3 ="msg <- gsub(\"\\\"\", \"\", msg)";
							String checkError4 = "capture.output(cat(\"*** \nERROR in graph.sea.diagplots function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
							rConnection.eval(checkError);
							rConnection.eval(checkError2);
							rConnection.eval(checkError3);
							rConnection.eval(checkError4);
						}
					}
				}  
			} // end of if fixed


			// Genotype Random
			if (genotypeRandom == true) {
				String funcSsaRandom = null;
				String groupVars = null;

				if (excludeControls) {
					if (environment == "NULL") {
						if (designIndex == 0){
							funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL, rep=NULL," + environment + ", is.random = TRUE, excludeCheck=TRUE, checkList= " + controlLevelsVector + "), silent=TRUE)";
							groupVars = "c(\"" + genotype + "\", \"" + block + "\")";
						} else if (designIndex == 1){
							funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL, rep=NULL," + environment + ", is.random = TRUE, excludeCheck=TRUE, checkList= NULL), silent=TRUE)";
							groupVars = "c(\"" + genotype + "\", \"" + block + "\")";
						} else if (designIndex == 2) {
							funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\", rep=NULL," + environment + ", is.random = TRUE, excludeCheck=TRUE, checkList= NULL), silent=TRUE)";
							groupVars = "c(\"" + genotype + "\", \"" + row + "\", \"" + column + "\")";
						} else if (designIndex == 3 || designIndex == 5) {
							funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL,\"" + rep + "\"," + environment + ", is.random = TRUE, excludeCheck=TRUE, checkList= " + controlLevelsVector + "), silent=TRUE)";
							groupVars = "c(\"" + genotype + "\", \"" + block + "\", \"" + rep + "\")";
						} else if (designIndex == 4 || designIndex == 6) {
							funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\",\"" + rep + "\"," + environment + ", is.random = TRUE, excludeCheck=TRUE, checkList= " + controlLevelsVector + "), silent=TRUE)";
							groupVars = "c(\"" + genotype + "\", \"" + rep + "\", \"" + row + "\", \"" + column + "\")";
						}
					} else {
						if (designIndex == 0){
							funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL, rep=NULL,\"" + environment + "\", is.random = TRUE, excludeCheck=TRUE, checkList= " + controlLevelsVector + "), silent=TRUE)";
							groupVars = "c(\"" + genotype + "\", \"" + block + "\")";
						} else if (designIndex == 1) {
							funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL, rep=NULL,\"" + environment + "\", is.random = TRUE, excludeCheck=TRUE, checkList= NULL), silent=TRUE)";
							groupVars = "c(\"" + genotype + "\", \"" + block + "\")";
						} else if (designIndex == 2) {
							funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\", rep=NULL,\"" + environment + "\", is.random = TRUE, excludeCheck=TRUE, checkList= NULL), silent=TRUE)";
							groupVars = "c(\"" + genotype + "\", \"" + row + "\", \"" + column + "\")";
						} else if (designIndex == 3 || designIndex == 5) {
							funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL,\"" + rep + "\",\"" + environment + "\", is.random = TRUE, excludeCheck=TRUE, checkList= " + controlLevelsVector + "), silent=TRUE)";
							groupVars = "c(\"" + genotype + "\", \"" + block + "\", \"" + rep + "\")";
						} else if (designIndex == 4 || designIndex == 6) {
							funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\",\"" + rep + "\",\"" + environment + "\", is.random = TRUE, excludeCheck=TRUE, checkList= " + controlLevelsVector + "), silent=TRUE)";
							groupVars = "c(\"" + genotype + "\", \"" + rep + "\", \"" + row + "\", \"" + column + "\")";
						}
					}
				} else {
					if (environment == "NULL") {
						if (designIndex == 0 || designIndex == 1) {
							funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL, rep=NULL," + environment + ", is.random = TRUE), silent=TRUE)";
							groupVars = "c(\"" + genotype + "\", \"" + block + "\")";
						} else if (designIndex == 2) {
							funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\", rep=NULL," + environment + ", is.random = TRUE), silent=TRUE)";
							groupVars = "c(\"" + genotype + "\", \"" + row + "\", \"" + column + "\")";
						} else if (designIndex == 3 || designIndex == 5) {
							funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL,\"" + rep + "\"," + environment + ", is.random = TRUE), silent=TRUE)";
							groupVars = "c(\"" + genotype + "\", \"" + block + "\", \"" + rep + "\")";
						} else if (designIndex == 4 || designIndex == 6) {
							funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\",\"" + rep + "\"," + environment + ", is.random = TRUE), silent=TRUE)";
							groupVars = "c(\"" + genotype + "\", \"" + rep + "\", \"" + row + "\", \"" + column + "\")";
						}
					} else {
						if (designIndex == 0 || designIndex == 1) {
							funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL, rep=NULL,\"" + environment + "\", is.random = TRUE), silent=TRUE)";
							groupVars = "c(\"" + genotype + "\", \"" + block + "\")";
						} else if (designIndex == 2) {
							funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\", rep=NULL,\"" + environment + "\", is.random = TRUE), silent=TRUE)";
							groupVars = "c(\"" + genotype + "\", \"" + row + "\", \"" + column + "\")";
						} else if (designIndex == 3 || designIndex == 5) {
							funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL,\"" + rep + "\",\"" + environment + "\", is.random = TRUE), silent=TRUE)";
							groupVars = "c(\"" + genotype + "\", \"" + block + "\", \"" + rep + "\")";
						} else if (designIndex == 4 || designIndex == 6) {
							funcSsaRandom = "ssa2 <- try(ssa.test(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\",\"" + rep + "\",\"" + environment + "\", is.random = TRUE), silent=TRUE)";
							groupVars = "c(\"" + genotype + "\", \"" + rep + "\", \"" + row + "\", \"" + column + "\")";
						}
					}
				}
				String randomHead = "capture.output(cat(\"GENOTYPE AS: Random\n\"),file=\"" + outFileName + "\",append = TRUE)";
				rConnection.eval(funcSsaRandom);
				rConnection.eval(sep2);
				rConnection.eval(randomHead);
				rConnection.eval(sep2);
				rConnection.eval(outspace);
				System.out.println(funcSsaRandom);

				String runSuccess2 = rConnection.eval("class(ssa2)").asString();	
				if (runSuccess2 != null && runSuccess2.equals("try-error")) {	
					System.out.println("ssa2: error");
					String checkError = "msg <- trimStrings(strsplit(ssa2, \":\")[[1]])";
					String checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
					String checkError3 ="msg <- gsub(\"\\\"\", \"\", msg)";
					String checkError4 = "capture.output(cat(\"*** \nERROR in ssa.test function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
					rConnection.eval(checkError);
					rConnection.eval(checkError2);
					rConnection.eval(checkError3);
					rConnection.eval(checkError4);

					runningRandomSuccess=false;
				}
				else {

					for (int k = 0; k < respvars.length; k++) {
						int i = k + 1; // 1-relative index;
						String respVarHead = "capture.output(cat(\"\nRESPONSE VARIABLE: " + respvars[k] + "\n\"),file=\"" + outFileName + "\",append = TRUE)";
						rConnection.eval(sep);
						rConnection.eval(respVarHead);
						rConnection.eval(sep);
						rConnection.eval(outspace);

						// optional output: descriptive statistics
						if (descriptiveStat) {
							String funcDesc = null;
							if (environment == "NULL") {
								funcDesc = "outDesc <- try(DescriptiveStatistics(data, \"" + respvars[k] + "\", " + environment + "), silent=TRUE)";
							} else {
								funcDesc = "outDesc <- try(DescriptiveStatistics(data, \"" + respvars[k] + "\", \"" + environment + "\"), silent=TRUE)";
							}
							rConnection.eval(funcDesc);
							System.out.println(funcDesc);
							String outDescStat = "capture.output(cat(\"DESCRIPTIVE STATISTICS:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
							String outDescStat2 = "capture.output(outDesc,file=\"" + outFileName + "\",append = TRUE)"; 

							String runSuccessDescStat = rConnection.eval("class(outDesc)").asString();
							if (runSuccessDescStat != null && runSuccessDescStat.equals("try-error")) {	
								System.out.println("desc stat: error");
								String checkError = "msg <- trimStrings(strsplit(outDesc, \":\")[[1]])";
								String checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
								String checkError3 ="msg <- gsub(\"\\\"\", \"\", msg)";
								String checkError4 = "capture.output(cat(\"*** \nERROR in DescriptiveStatistics function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
								rConnection.eval(checkError);
								rConnection.eval(checkError2);
								rConnection.eval(checkError3);
								rConnection.eval(checkError4);
							} 
							else {
								rConnection.eval(outspace);
								rConnection.eval(outDescStat);
								rConnection.eval(outDescStat2);
								rConnection.eval(outspace);
							}	
						}
						int envLevelsLength2 = 0;
						if (environment == "NULL") {
							envLevelsLength2 = 1;
						} else {
							envLevelsLength2 = environmentLevels.length;
						}
						for (int m = 0; m < envLevelsLength2; m++) { // no of envts or sites
							printAllOutputRandom=true;
							int j = m + 1; // 1-relative index;

							if (environment != "NULL") {
								String envtHead = "capture.output(cat(\"\nANALYSIS FOR: "+ environment + "\", \" = \" ,ssa2$output[[" + i	+ "]]$site[[" + j + "]]$env,\"\n\"),file=\""+ outFileName + "\",append = TRUE)";
								rConnection.eval(sep);
								rConnection.eval(envtHead);
								rConnection.eval(sep);
								rConnection.eval(outspace);
							}

							//check if the data has too many missing observations
							double responseRate=rConnection.eval("ssa2$output[[" + i + "]]$site[[" + j + "]]$responseRate").asDouble();
							if (responseRate < 0.8) {
								String allNAWarning2 = rConnection.eval("ssa2$output[[" + i + "]]$site[[" + j + "]]$manyNAWarning").asString();
								String printError1 = "capture.output(cat(\"***\\n\"), file=\"" + outFileName + "\",append = TRUE)";
								String printError2 = "capture.output(cat(\"ERROR:\\n\"), file=\"" + outFileName + "\",append = TRUE)";
								String printError3 = "capture.output(cat(\"" + allNAWarning2 + "\\n\"), file=\"" + outFileName + "\",append = TRUE)";

								rConnection.eval(outspace);
								rConnection.eval(printError1);
								rConnection.eval(printError2);
								rConnection.eval(printError3);
								rConnection.eval(printError1);
								rConnection.eval(outspace);
								rConnection.eval(outspace);
								printAllOutputRandom=false;
							} else {
								String lmerRun=rConnection.eval("ssa2$output[[" + i + "]]$site[[" + j + "]]$lmerRun").asString();
								if (lmerRun.equals("ERROR")) {
									String lmerError = rConnection.eval("ssa2$output[[" + i + "]]$site[[" + j + "]]$lmerError").asString();
									String printError1 = "capture.output(cat(\"***\\n\"), file=\"" + outFileName + "\",append = TRUE)";
									String printError2 = "capture.output(cat(\"ERROR:\\n\"), file=\"" + outFileName + "\",append = TRUE)";
									String printError3 = "capture.output(cat(\"" + lmerError + "\\n\"), file=\"" + outFileName + "\",append = TRUE)";

									rConnection.eval(outspace);
									rConnection.eval(printError1);
									rConnection.eval(printError2);
									rConnection.eval(printError3);
									rConnection.eval(printError1);
									rConnection.eval(outspace);
									rConnection.eval(outspace);
									printAllOutputRandom=false;
								}
								System.out.println("lmerRun:"+lmerRun);	
							}

							if (printAllOutputRandom) {
								// display warning generated by checkTest in ssa.test
								String warningCheckTest = rConnection.eval("ssa2$output[[" + i	+ "]]$site[[" + j + "]]$checkTestWarning").asString();

								if (!warningCheckTest.equals("NONE")) {
									String warningCheckTest2 = "capture.output(cat(\"\n*** \nWARNING:\\n\"), file=\"" + outFileName + "\",append = TRUE)";
									String warningCheckTest3 = "capture.output(cat(\"" + warningCheckTest + "\"), file=\"" + outFileName + "\",append = TRUE)";
									String warningCheckTest4 = "capture.output(cat(\"\n*** \\n\"), file=\"" + outFileName + "\",append = TRUE)";

									rConnection.eval(warningCheckTest2);
									rConnection.eval(warningCheckTest3);
									rConnection.eval(warningCheckTest4);
								} 
								System.out.println("check test:" + warningCheckTest);

								// default output: trial summary
								String funcTrialSum = "funcTrialSum <- try(class.information(" + groupVars + ",ssa2$output[[" + i + "]]$site[[" + j + "]]$data), silent=TRUE)";
								String trialSumHead = "capture.output(cat(\"\nDATA SUMMARY:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
								String trialObsRead = "capture.output(cat(\"Number of observations read: \", ssa2$output[["	+ i	+ "]]$site[[" + j + "]]$obsread[[1]],\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
								String trialObsUsed = "capture.output(cat(\"Number of observations used: \", ssa2$output[["	+ i	+ "]]$site[[" + j + "]]$obsused[[1]],\"\n\n\"),file=\""	+ outFileName + "\",append = TRUE)";
								String trialSum = "capture.output(funcTrialSum,file=\"" + outFileName + "\",append = TRUE)";

								rConnection.eval(funcTrialSum);

								String runSuccessTS = rConnection.eval("class(funcTrialSum)").asString();
								System.out.println("runSuccessTS:"+runSuccessTS);
								if (runSuccessTS != null && runSuccessTS.equals("try-error")) {	
									System.out.println("class info: error");
									String checkError = "msg <- trimStrings(strsplit(funcTrialSum, \":\")[[1]])";
									String checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
									String checkError3 ="msg <- gsub(\"\\\"\", \"\", msg)";
									String checkError4 = "capture.output(cat(\"*** \nERROR in class.information function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
									rConnection.eval(checkError);
									rConnection.eval(checkError2);
									rConnection.eval(checkError3);
									rConnection.eval(checkError4);
								}
								else {
									rConnection.eval(trialSumHead);
									rConnection.eval(trialObsRead);
									rConnection.eval(trialObsUsed);
									rConnection.eval(trialSum);
									rConnection.eval(outspace);
								}

								// optional output: variance components
								if (varianceComponents) {
									String outVarComp = "capture.output(cat(\"\nVARIANCE COMPONENTS TABLE:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
									String outVarComp2 = "capture.output(ssa2$output[[" + i + "]]$site[[" + j + "]]$varcomp.table,file=\"" + outFileName + "\",append = TRUE)";

									rConnection.eval(outVarComp);
									rConnection.eval(outVarComp2);
									rConnection.eval(outspace);
								}

								//default output: test genotypic effect
								String outTestGen1 = "capture.output(cat(\"\nTESTING FOR THE SIGNIFICANCE OF GENOTYPIC EFFECT USING -2 LOGLIKELIHOOD RATIO TEST:\n\"),file=\"" + outFileName + "\",append = TRUE)";
								String outTestGen2 = "capture.output(cat(\"\nFormula for Model1: \", ssa2$output[["	+ i	+ "]]$site[[" + j + "]]$formula1,\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
								String outTestGen3 = "capture.output(cat(\"Formula for Model2: \", ssa2$output[["	+ i	+ "]]$site[[" + j + "]]$formula2,\"\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
								String outTestGen4 = "capture.output(ssa2$output[[" + i + "]]$site[[" + j + "]]$models.table,file=\"" + outFileName + "\",append = TRUE)";
								rConnection.eval(outTestGen1);
								rConnection.eval(outTestGen2);
								rConnection.eval(outTestGen3);
								rConnection.eval(outTestGen4);
								rConnection.eval(outspace);

								//default output: test for check effect
								String newExcludeCheck = rConnection.eval("ssa2$output[[" + i + "]]$site[[" + j + "]]$newExcludeCheck").asString();
								System.out.println("newExcludeCheck: " + newExcludeCheck);

								if (newExcludeCheck.equals("TRUE")) {
									String outAnovaTable1 = "capture.output(cat(\"\nTESTING FOR THE SIGNIFICANCE OF CHECK EFFECT:\n\"),file=\"" + outFileName + "\",append = TRUE)";
									String outAnovaTable2 = "library(lmerTest)";
									String outAnovaTable3 = "model2b <- lmer(formula(ssa2$output[[" + i + "]]$site[[" + j + "]]$formula1), data = ssa2$output[[" + i + "]]$site[[" + j + "]]$data, REML = T)";
									String outAnovaTable4 = "a.table <- anova(model2b)";
									String outAnovaTable5 = "pvalue <- formatC(as.numeric(format(a.table[1,6], scientific=FALSE)), format=\"f\")";
									String outAnovaTable6 = "a.table<-cbind(round(a.table[,1:5], digits=4),pvalue)";
									String outAnovaTable7 = "colnames(a.table)<-c(\"Df\", \"Sum Sq\", \"Mean Sq\", \"F value\", \"Denom\", \"Pr(>F)\")";
									String outAnovaTable8 = "capture.output(cat(\"Analysis of Variance Table with Satterthwaite Denominator Df\n\"),file=\"" + outFileName + "\",append = TRUE)";
									String outAnovaTable9 = "capture.output(a.table,file=\"" + outFileName + "\",append = TRUE)";
									String outAnovaTable10 = "detach(\"package:lmerTest\")";

									//								rConnection.eval(outspace);
									rConnection.eval(outAnovaTable1);
									rConnection.eval(outAnovaTable2);
									rConnection.eval(outAnovaTable3);
									rConnection.eval(outAnovaTable4);
									rConnection.eval(outAnovaTable5);
									rConnection.eval(outAnovaTable6);
									rConnection.eval(outAnovaTable7);
									rConnection.eval(outspace);
									rConnection.eval(outAnovaTable8);
									rConnection.eval(outAnovaTable9);
									rConnection.eval(outspace);
									rConnection.eval(outAnovaTable10);
								}

								//default output: predicted means
								String outPredMeans = "capture.output(cat(\"\nPREDICTED MEANS:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
								String outPredMeans2 = "capture.output(ssa2$output[[" + i + "]]$site[[" + j + "]]$summary.statistic,file=\"" + outFileName + "\",append = TRUE)"; 
								rConnection.eval(outPredMeans);
								rConnection.eval(outPredMeans2);
								rConnection.eval(outspace);

								//default output: lsmeans of checks
								if (excludeControls) {
									int newCheckListLength = rConnection.eval("ssa2$output[[" + i	+ "]]$site[[" + j + "]]$newCheckListLength").asInteger();

									if (newCheckListLength > 0) {
										String outLSMeansCheck = "capture.output(cat(\"\nCHECK/CONTROL LSMEANS:\n\n\"),file=\"" + outFileName + "\",append = TRUE)";
										String outLSMeansCheck2 = "capture.output(ssa2$output[[" + i + "]]$site[[" + j + "]]$lsmeans.checks,file=\"" + outFileName + "\",append = TRUE)"; 
										rConnection.eval(outLSMeansCheck);
										rConnection.eval(outLSMeansCheck2);
										rConnection.eval(outspace);
									}
								}

								//default output: estimate heritability
								String outEstHerit = "capture.output(cat(\"\nHERITABILITY:\n\"),file=\"" + outFileName + "\",append = TRUE)";
								String outEstHerit2 = "capture.output(ssa2$output[[" + i + "]]$site[[" + j + "]]$heritability,file=\""	+ outFileName + "\",append = TRUE)";
								String outEstHerit3 = "capture.output(cat(\"\n\"),file=\"" + outFileName + "\",append = TRUE)";
								rConnection.eval(outEstHerit);
								rConnection.eval(outEstHerit2);
								rConnection.eval(outEstHerit3);
								rConnection.eval(outspace);
							}

						}
					}

					//optional output: estimate genotypic and phenotypic correlations
					if (genoPhenoCorrelation) {
						rConnection.eval(sep2);
						String funcEstCorr = null;
						if (excludeControls) {
							if (environment == "NULL") {
								if (designIndex == 0)
									funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL, rep=NULL," + environment + ", excludeLevels=TRUE, excludeList = " + controlLevelsVector + "), silent=TRUE)";
								else if (designIndex == 1)
									funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL, rep=NULL," + environment + ", excludeLevels=TRUE, excludeList = NULL), silent=TRUE)";
								else if (designIndex == 2)
									funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\", rep=NULL," + environment + ", excludeLevels=TRUE, excludeList = NULL), silent=TRUE)";
								else if (designIndex == 3)
									funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL,\"" + rep + "\"," + environment + ", excludeLevels=TRUE, excludeList = " + controlLevelsVector + "), silent=TRUE)";
								else if (designIndex == 4)
									funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\",\"" + rep + "\"," + environment + ", excludeLevels=TRUE, excludeList = " + controlLevelsVector + "), silent=TRUE)";
								else if (designIndex == 5)
									funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL,\"" + rep + "\"," + environment + ", excludeLevels=TRUE, excludeList = " + controlLevelsVector + "), silent=TRUE)";
								else if (designIndex == 6)
									funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\",\"" + rep + "\"," + environment + ", excludeLevels=TRUE, excludeList = " + controlLevelsVector + "), silent=TRUE)";
							} else {
								if (designIndex == 0)
									funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL, rep=NULL,\"" + environment + "\", excludeLevels=TRUE, excludeList = " + controlLevelsVector + "), silent=TRUE)";
								else if (designIndex == 1)
									funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL, rep=NULL,\"" + environment + "\", excludeLevels=TRUE, excludeList = NULL), silent=TRUE)";
								else if (designIndex == 2)
									funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\", rep=NULL,\"" + environment + "\", excludeLevels=TRUE, excludeList = NULL), silent=TRUE)";
								else if (designIndex == 3)
									funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL,\"" + rep + "\",\"" + environment + "\", excludeLevels=TRUE, excludeList = " + controlLevelsVector + "), silent=TRUE)";
								else if (designIndex == 4)
									funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\",\"" + rep + "\",\"" + environment + "\", excludeLevels=TRUE, excludeList = " + controlLevelsVector + "), silent=TRUE)";
								else if (designIndex == 5)
									funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL,\"" + rep + "\",\"" + environment + "\", excludeLevels=TRUE, excludeList = " + controlLevelsVector + "), silent=TRUE)";
								else if (designIndex == 6)
									funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\",\"" + rep + "\",\"" + environment + "\", excludeLevels=TRUE, excludeList = " + controlLevelsVector + "), silent=TRUE)";
							}
						} else {
							if (environment == "NULL") {
								if (designIndex == 0 || designIndex == 1)
									funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL, rep=NULL," + environment + "), silent=TRUE)";
								else if (designIndex == 2)
									funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\", rep=NULL," + environment + "), silent=TRUE)";
								else if (designIndex == 3)
									funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL,\"" + rep + "\"," + environment + "), silent=TRUE)";
								else if (designIndex == 4)
									funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\",\"" + rep + "\"," + environment + "), silent=TRUE)";
								else if (designIndex == 5)
									funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL,\"" + rep + "\"," + environment + "), silent=TRUE)";
								else if (designIndex == 6)
									funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\",\"" + rep + "\"," + environment + "), silent=TRUE)";
							} else {
								if (designIndex == 0 || designIndex == 1)
									funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL, rep=NULL,\"" + environment + "\"), silent=TRUE)";
								else if (designIndex == 2)
									funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\", rep=NULL,\"" + environment + "\"), silent=TRUE)";
								else if (designIndex == 3)
									funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL,\"" + rep + "\",\"" + environment + "\"), silent=TRUE)";
								else if (designIndex == 4)
									funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\",\"" + rep + "\",\"" + environment + "\"), silent=TRUE)";
								else if (designIndex == 5)
									funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + block + "\",column=NULL,\"" + rep + "\",\"" + environment + "\"), silent=TRUE)";
								else if (designIndex == 6)
									funcEstCorr = "gpcorr <- try(genoNpheno.corr(\"" + design + "\",data," + respvarVector + ",\"" + genotype + "\",\"" + row + "\",\"" + column + "\",\"" + rep + "\",\"" + environment + "\"), silent=TRUE)";
							}
						}

						System.out.println(funcEstCorr);
						rConnection.eval(funcEstCorr);	

						String runSuccessGPCorr = rConnection.eval("class(gpcorr)").asString();
						if (runSuccessGPCorr != null && runSuccessGPCorr.equals("try-error")) {	
							System.out.println("geno pheno corr: error");
							String checkError = "msg <- trimStrings(strsplit(gpcorr, \":\")[[1]])";
							String checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
							String checkError3 ="msg <- gsub(\"\\\"\", \"\", msg)";
							String checkError4 = "capture.output(cat(\"*** \nERROR in genoNpheno.corr function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
							rConnection.eval(checkError);
							rConnection.eval(checkError2);
							rConnection.eval(checkError3);
							rConnection.eval(checkError4);
						}
						else {
							String outEstGenoCorr = "capture.output(cat(\"\nGENOTYPIC CORRELATIONS:\n\"),file=\"" + outFileName + "\",append = TRUE)";
							rConnection.eval(outEstGenoCorr);

							int envLevelsLength = 0;
							if (environment == "NULL") {
								envLevelsLength = 1;
							} else {
								envLevelsLength = environmentLevels.length;
							}

							for (int m = 0; m < envLevelsLength; m++) { // no of envts or sites
								int j = m + 1; // 1-relative index;
								if (environment != "NULL") {
									String outEstGenoCorr2 = "capture.output(cat(\"" + environment + " = \", gpcorr$EnvLevels[[" + j + "]]),file=\"" + outFileName + "\",append = TRUE)";
									rConnection.eval(outspace);
									rConnection.eval(outEstGenoCorr2);
									rConnection.eval(outspace);
								}
								String outEstGenoCorr2b = "capture.output(gpcorr$GenoCorr[[" + j + "]],file=\"" + outFileName + "\",append = TRUE)";
								rConnection.eval(outspace);
								rConnection.eval(outEstGenoCorr2b);
								rConnection.eval(outspace);
							}

							String outEstPhenoCorr = "capture.output(cat(\"\nPHENOTYPIC CORRELATIONS:\n\"),file=\"" + outFileName + "\",append = TRUE)";
							rConnection.eval(outEstPhenoCorr);

							for (int m = 0; m < envLevelsLength; m++) { // no of envts or sites
								int j = m + 1; // 1-relative index;
								if (environment != "NULL") {
									String outEstPhenoCorr2 = "capture.output(cat(\"" + environment + " = \", gpcorr$EnvLevels[[" + j + "]]),file=\"" + outFileName + "\",append = TRUE)";
									rConnection.eval(outspace);
									rConnection.eval(outEstPhenoCorr2);
									rConnection.eval(outspace);
								}
								String outEstPhenoCorr2b = "capture.output(gpcorr$PhenoCorr[[" + j + "]],file=\"" + outFileName + "\",append = TRUE)";
								rConnection.eval(outspace);
								rConnection.eval(outEstPhenoCorr2b);
								rConnection.eval(outspace);
							}
						} //end of else for if runSuccessGPCorr	
					}

					//default option: save predicted means to a file
					String checkPredMean = rConnection.eval("ssa2$meansWarning").asString();
					System.out.println("checkPredMean: " + checkPredMean);

					if (checkPredMean.equals("empty")) {
						System.out.println("Saving predicted means not done.");
					} else {
						String meansFileName2 = "meansFileName2 <- paste(\"" + resultFolderPath + "\",\"predictedMeans.csv\", sep=\"\")";
						String funcSavePredMeansCsv = "saveDataB1 <- try(write.table(ssa2$means,file = meansFileName2 ,sep=\",\",row.names=FALSE), silent=TRUE)";
						rConnection.eval(meansFileName2);
						rConnection.eval(funcSavePredMeansCsv);

						String runSuccessSavePredMeans = rConnection.eval("class(saveDataB1)").asString();
						if (runSuccessSavePredMeans != null && runSuccessSavePredMeans.equals("try-error")) {	
							System.out.println("save pred means: error");
							String checkError = "msg <- trimStrings(strsplit(saveDataB1, \":\")[[1]])";
							String checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
							String checkError3 ="msg <- gsub(\"\\\"\", \"\", msg)";
							String checkError4 = "capture.output(cat(\"*** \nERROR in saving predicted means to a file:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
							rConnection.eval(checkError);
							rConnection.eval(checkError2);
							rConnection.eval(checkError3);
							rConnection.eval(checkError4);
						}
					}

					//optional output: diagnostic plots for genotype random
					if (diagnosticPlot) {
						String diagPlotsFunc = null;
						if (environment == "NULL") {
							diagPlotsFunc = "diagPlots <- try(graph.sea.diagplots(data, " + respvarVector + ", env = " + environment + ", is.random = TRUE, ssa2), silent=TRUE)";
						} else {
							diagPlotsFunc = "diagPlots <- try(graph.sea.diagplots(data, " + respvarVector + ", env = \"" + environment + "\", is.random = TRUE, ssa2), silent=TRUE)";
						}
						System.out.println(diagPlotsFunc);
						rConnection.eval(diagPlotsFunc);

						String runSuccessDiagPlots = rConnection.eval("class(diagPlots)").asString();
						if (runSuccessDiagPlots != null && runSuccessDiagPlots.equals("try-error")) {	
							System.out.println("diagnostic plots (genotype random): error");
							String checkError = "msg <- trimStrings(strsplit(diagPlots, \":\")[[1]])";
							String checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
							String checkError3 ="msg <- gsub(\"\\\"\", \"\", msg)";
							String checkError4 = "capture.output(cat(\"*** \nERROR in graph.sea.diagplots function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
							rConnection.eval(checkError);
							rConnection.eval(checkError2);
							rConnection.eval(checkError3);
							rConnection.eval(checkError4);
						}
					}

					//				if (SeaOptions.sConstructInt == true) {
					////					graph.sea.predint <- function(data, respvar, env, result) 
					//					String predIntPlotsSeaFunc = "predIntPlotsSea <- tryCatch(graph.sea.predint(data, " + respvarVector + ", env = \"" + environment + "\", geno = \"" + genotype + "\", ssa2), error=function(err) \"notRun\")";
					//					System.out.println("predIntPlotsSeaFunc: " + predIntPlotsSeaFunc);
					//					rConnection.eval(predIntPlotsSeaFunc);
					//					
					//					String runSuccesspredIntPlotsSea = rConnection.eval("predIntPlotsSea").toString();
					//					System.out.println("runSuccesspredIntPlotsSea: " + runSuccesspredIntPlotsSea);
					//					//generate warning if error occurred	
					//					if (runSuccesspredIntPlotsSea != null && runSuccesspredIntPlotsSea.equals("notRun")) {	
					//						System.out.println("error");
					//						rConnection.eval("capture.output(cat(\"\n***An error has occurred.***\n***Prediction interval plots not created.***\n\"),file=\"" + outFileName + "\",append = TRUE)"); //append to output file?
					//					}
					////					else {
					////					}
					//				}

				} // end of else for if (runSuccess == "notRun") 
			} // end of if random

			//default output: save residuals to a file
			if (runningFixedSuccess & runningRandomSuccess) {
				String residFileNameFixed = "residFileNameFixed <- paste(\"" + resultFolderPath + "\",\"residuals_fixed.csv\", sep=\"\")";
				String residFileNameRandom = "residFileNameRandom <- paste(\"" + resultFolderPath + "\",\"residuals_random.csv\", sep=\"\")";
				if ((genotypeFixed) & (genotypeRandom == false)) {
					String runSsaResid1 = null;
					if (environment == "NULL") {
						runSsaResid1 = "resid_f <- try(ssa.resid(data, ssa1, " + respvarVector + ", env = " + environment + ", is.genoRandom = FALSE), silent=TRUE)";
					} else {
						runSsaResid1 = "resid_f <- try(ssa.resid(data, ssa1, " + respvarVector + ", env = \"" + environment + "\", is.genoRandom = FALSE), silent=TRUE)";
					}
					System.out.println(runSsaResid1);
					rConnection.eval(runSsaResid1);

					String runSuccessDiagPlots = rConnection.eval("class(resid_f)").asString();
					if (runSuccessDiagPlots != null && runSuccessDiagPlots.equals("try-error")) {	
						System.out.println("ssa.resid (genotype fixed): error");
						String checkError = "msg <- trimStrings(strsplit(resid_f, \":\")[[1]])";
						String checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
						String checkError3 ="msg <- gsub(\"\\\"\", \"\", msg)";
						String checkError4 = "capture.output(cat(\"*** \nERROR in ssa.resid function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
						rConnection.eval(checkError);
						rConnection.eval(checkError2);
						rConnection.eval(checkError3);
						rConnection.eval(checkError4);
					} else {
						String checkResid1 = rConnection.eval("resid_f$residWarning").asString();
						System.out.println("checkResid1: " + checkResid1);
						if (checkResid1.equals("empty")) {
							System.out.println("Saving resid (fixed) not done.");
						} else {
							String func1SaveResidualsCsv = "saveResid <- try(write.table(resid_f$residuals, file = residFileNameFixed ,sep=\",\",row.names=FALSE), silent=TRUE)";
							rConnection.eval(residFileNameFixed);
							rConnection.eval(func1SaveResidualsCsv);

							String runSuccessSaveResid = rConnection.eval("class(saveResid)").asString();
							if (runSuccessSaveResid != null && runSuccessSaveResid.equals("try-error")) {	
								System.out.println("save residuals: error");
								String checkError = "msg <- trimStrings(strsplit(saveResid, \":\")[[1]])";
								String checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
								String checkError3 ="msg <- gsub(\"\\\"\", \"\", msg)";
								String checkError4 = "capture.output(cat(\"*** \nERROR in saving residuals to a file:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
								rConnection.eval(checkError);
								rConnection.eval(checkError2);
								rConnection.eval(checkError3);
								rConnection.eval(checkError4);
							}

							//generate heatmap
							if (heatmapResiduals) {
								String funcHeat=null;
								if (environment == "NULL") {
									funcHeat = "heat1 <- try(Heatmap(resid_f$residuals, genAs=\"fixed\", \"" + heatmapRow + "\", \"" + heatmapColumn + "\", " + respvarVector + ", \"" + designUsed + "\", " + environment + "), silent=TRUE)";
								} else {
									funcHeat = "heat1 <- try(Heatmap(resid_f$residuals, genAs=\"fixed\", \"" + heatmapRow + "\", \"" + heatmapColumn + "\", " + respvarVector + ", \"" + designUsed + "\", \"" + environment + "\"), silent=TRUE)";
								}
								System.out.println(funcHeat);
								rConnection.eval(funcHeat);

								String runSuccessHeat = rConnection.eval("class(heat1)").asString();
								if (runSuccessHeat != null && runSuccessHeat.equals("try-error")) {	
									System.out.println("heatmap (fixed): error");
									String checkError = "msg <- trimStrings(strsplit(heat1, \":\")[[1]])";
									String checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
									String checkError3 ="msg <- gsub(\"\\\"\", \"\", msg)";
									String checkError4 = "capture.output(cat(\"*** \nERROR in Heatmap function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
									rConnection.eval(checkError);
									rConnection.eval(checkError2);
									rConnection.eval(checkError3);
									rConnection.eval(checkError4);
								} else {
									for (int k = 0; k < respvars.length; k++) {
										int i = k + 1; // 1-relative index;

										String envLevelsCommand = "length(heat1[[" + i + "]]$site)";
										int envLevels = rConnection.eval(envLevelsCommand).asInteger();
										for (int m = 0; m < envLevels; m++) { 
											int j = m + 1; // 1-relative index;

											String warningListCommand = "heat1[[" + i + "]]$site[["+ j + "]]";
											String warningList = rConnection.eval(warningListCommand).asString();

											if (warningList.equals("empty")) {

											} else if (warningList.equals("unique")) {

											} else {
												String trialObsUsed = "capture.output(cat(\"\nERROR:\", heat1[[" + i + "]]$site[["+ j + "]],\"\n\"),file=\""	+ outFileName + "\",append = TRUE)";
												rConnection.eval(trialObsUsed);
											}
										}
									}
								} //end (heat1 is not error)
							}
						}
					}
				}
				else if ((genotypeFixed == false) & (genotypeRandom)) {
					String runSsaResid2 = null;
					if (environment == "NULL") {
						runSsaResid2 = "resid_r <- try(ssa.resid(data, ssa2, " + respvarVector + ", env = " + environment + ", is.genoRandom = TRUE), silent=TRUE)";
					} else {
						runSsaResid2 = "resid_r <- try(ssa.resid(data, ssa2, " + respvarVector + ", env = \"" + environment + "\", is.genoRandom = TRUE), silent=TRUE)";
					}
					System.out.println(runSsaResid2);
					rConnection.eval(runSsaResid2);

					String runSuccessDiagPlots = rConnection.eval("class(resid_r)").asString();
					if (runSuccessDiagPlots != null && runSuccessDiagPlots.equals("try-error")) {	
						System.out.println("ssa.resid (genotype random): error");
						String checkError = "msg <- trimStrings(strsplit(resid_r, \":\")[[1]])";
						String checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
						String checkError3 ="msg <- gsub(\"\\\"\", \"\", msg)";
						String checkError4 = "capture.output(cat(\"*** \nERROR in ssa.resid function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
						rConnection.eval(checkError);
						rConnection.eval(checkError2);
						rConnection.eval(checkError3);
						rConnection.eval(checkError4);
					} else {
						String checkResid1 = rConnection.eval("resid_r$residWarning").asString();
						System.out.println("checkResid1: " + checkResid1);
						if (checkResid1.equals("empty")) {
							System.out.println("Saving resid (random) not done.");
						} else {
							String func1SaveResidualsCsv = "saveResid <- try(write.table(resid_r$residuals, file = residFileNameRandom ,sep=\",\",row.names=FALSE), silent=TRUE)";
							rConnection.eval(residFileNameRandom);
							rConnection.eval(func1SaveResidualsCsv);

							String runSuccessSaveResid = rConnection.eval("class(saveResid)").asString();
							if (runSuccessSaveResid != null && runSuccessSaveResid.equals("try-error")) {	
								System.out.println("save residuals: error");
								String checkError = "msg <- trimStrings(strsplit(saveResid, \":\")[[1]])";
								String checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
								String checkError3 ="msg <- gsub(\"\\\"\", \"\", msg)";
								String checkError4 = "capture.output(cat(\"*** \nERROR in saving residuals to a file:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
								rConnection.eval(checkError);
								rConnection.eval(checkError2);
								rConnection.eval(checkError3);
								rConnection.eval(checkError4);
							}

							//generate heatmap
							if (heatmapResiduals) {
								String funcHeat=null;
								if (environment == "NULL") {
									funcHeat = "heat2 <- try(Heatmap(resid_r$residuals, genAs=\"random\", \"" + heatmapRow + "\", \"" + heatmapColumn + "\", " + respvarVector + ", \"" + designUsed + "\", " + environment + "), silent=TRUE)";
								} else {
									funcHeat = "heat2 <- try(Heatmap(resid_r$residuals, genAs=\"random\", \"" + heatmapRow + "\", \"" + heatmapColumn + "\", " + respvarVector + ", \"" + designUsed + "\", \"" + environment + "\"), silent=TRUE)";
								}
								System.out.println(funcHeat);
								rConnection.eval(funcHeat);

								String runSuccessHeat = rConnection.eval("class(heat2)").asString();
								System.out.println("runSuccessHeat:"+runSuccessHeat);
								if (runSuccessHeat != null && runSuccessHeat.equals("try-error")) {	
									System.out.println("heatmap (random): error");
									String checkError = "msg <- trimStrings(strsplit(heat2, \":\")[[1]])";
									String checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
									String checkError3 ="msg <- gsub(\"\\\"\", \"\", msg)";
									String checkError4 = "capture.output(cat(\"*** \nERROR in Heatmap function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
									rConnection.eval(checkError);
									rConnection.eval(checkError2);
									rConnection.eval(checkError3);
									rConnection.eval(checkError4);
								} else {
									for (int k = 0; k < respvars.length; k++) {
										int i = k + 1; // 1-relative index;

										String envLevelsCommand = "length(heat2[[" + i + "]]$site)";
										int envLevels = rConnection.eval(envLevelsCommand).asInteger();
										for (int m = 0; m < envLevels; m++) { 
											int j = m + 1; // 1-relative index;

											String warningListCommand = "heat2[[" + i + "]]$site[["+ j + "]]";
											String warningList = rConnection.eval(warningListCommand).asString();

											if (warningList.equals("empty")) {

											} else if (warningList.equals("unique")) {

											} else {
												String trialObsUsed = "capture.output(cat(\"\nERROR:\", heat2[[" + i + "]]$site[["+ j + "]],\"\n\"),file=\""	+ outFileName + "\",append = TRUE)";
												rConnection.eval(trialObsUsed);
											}
										}
									}
								}
							}
						}
					}
				}
				else if ((genotypeFixed) & (genotypeRandom)) {
					String runSsaResid1 = null;
					String runSsaResid2 = null;
					if (environment == "NULL") {
						runSsaResid1 = "resid_f <- try(ssa.resid(data, ssa1, " + respvarVector + ", env = " + environment + ", is.genoRandom = FALSE), silent=TRUE)";
						runSsaResid2 = "resid_r <- try(ssa.resid(data, ssa2, " + respvarVector + ", env = " + environment + ", is.genoRandom = TRUE), silent=TRUE)";
					} else {
						runSsaResid1 = "resid_f <- try(ssa.resid(data, ssa1, " + respvarVector + ", env = \"" + environment + "\", is.genoRandom = FALSE), silent=TRUE)";
						runSsaResid2 = "resid_r <- try(ssa.resid(data, ssa2, " + respvarVector + ", env = \"" + environment + "\", is.genoRandom = TRUE), silent=TRUE)";
					}
					System.out.println(runSsaResid1);
					System.out.println(runSsaResid2);
					rConnection.eval(runSsaResid1);
					rConnection.eval(runSsaResid2);

					String runSuccessResidFixed = rConnection.eval("class(resid_f)").asString();
					System.out.println("runSuccessResidFixed:"+runSuccessResidFixed);
					if (runSuccessResidFixed != null && runSuccessResidFixed.equals("try-error")) {	
						System.out.println("ssa.resid (genotype fixed): error");
						String checkError = "msg <- trimStrings(strsplit(resid_f, \":\")[[1]])";
						String checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
						String checkError3 ="msg <- gsub(\"\\\"\", \"\", msg)";
						String checkError4 = "capture.output(cat(\"*** \nERROR in ssa.resid function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
						rConnection.eval(checkError);
						rConnection.eval(checkError2);
						rConnection.eval(checkError3);
						rConnection.eval(checkError4);
					} else {
						String checkResid1 = rConnection.eval("resid_f$residWarning").asString();
						System.out.println("checkResid1: " + checkResid1);
						if (checkResid1.equals("empty")) {
							System.out.println("Saving resid (fixed) not done.");
						} else {
							String func1SaveResidualsCsv = "saveResid <- try(write.table(resid_f$residuals, file = residFileNameFixed ,sep=\",\",row.names=FALSE), silent=TRUE)";
							rConnection.eval(residFileNameFixed);
							rConnection.eval(func1SaveResidualsCsv);

							String runSuccessSaveResid = rConnection.eval("class(saveResid)").asString();
							System.out.println("runSuccessSaveResid:"+runSuccessSaveResid);
							if (runSuccessSaveResid != null && runSuccessSaveResid.equals("try-error")) {	
								System.out.println("save residuals: error");
								String checkError = "msg <- trimStrings(strsplit(saveResid, \":\")[[1]])";
								String checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
								String checkError3 ="msg <- gsub(\"\\\"\", \"\", msg)";
								String checkError4 = "capture.output(cat(\"*** \nERROR in saving residuals to a file:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
								rConnection.eval(checkError);
								rConnection.eval(checkError2);
								rConnection.eval(checkError3);
								rConnection.eval(checkError4);
							}

							//generate heatmap
							if (heatmapResiduals) {
								String funcHeat=null;
								if (environment == "NULL") {
									funcHeat = "heat1 <- try(Heatmap(resid_f$residuals, genAs=\"fixed\", \"" + heatmapRow + "\", \"" + heatmapColumn + "\", " + respvarVector + ", \"" + designUsed + "\", " + environment + "), silent=TRUE)";
								} else {
									funcHeat = "heat1 <- try(Heatmap(resid_f$residuals, genAs=\"fixed\", \"" + heatmapRow + "\", \"" + heatmapColumn + "\", " + respvarVector + ", \"" + designUsed + "\", \"" + environment + "\"), silent=TRUE)";
								}
								System.out.println(funcHeat);
								rConnection.eval(funcHeat);

								String runSuccessHeat = rConnection.eval("class(heat1)").asString();
								System.out.println("runSuccessHeat:"+runSuccessHeat);
								if (runSuccessHeat != null && runSuccessHeat.equals("try-error")) {	
									System.out.println("heatmap (fixed): error");
									String checkError = "msg <- trimStrings(strsplit(heat1, \":\")[[1]])";
									String checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
									String checkError3 ="msg <- gsub(\"\\\"\", \"\", msg)";
									String checkError4 = "capture.output(cat(\"*** \nERROR in Heatmap function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
									rConnection.eval(checkError);
									rConnection.eval(checkError2);
									rConnection.eval(checkError3);
									rConnection.eval(checkError4);
								} else {
									for (int k = 0; k < respvars.length; k++) {
										int i = k + 1; // 1-relative index;

										String envLevelsCommand = "length(heat1[[" + i + "]]$site)";
										int envLevels = rConnection.eval(envLevelsCommand).asInteger();
										for (int m = 0; m < envLevels; m++) { 
											int j = m + 1; // 1-relative index;

											String warningListCommand = "heat1[[" + i + "]]$site[["+ j + "]]";
											String warningList = rConnection.eval(warningListCommand).asString();

											if (warningList.equals("empty")) {

											} else if (warningList.equals("unique")) {

											} else {
												String trialObsUsed = "capture.output(cat(\"\nERROR:\", heat1[[" + i + "]]$site[["+ j + "]],\"\n\"),file=\""	+ outFileName + "\",append = TRUE)";
												rConnection.eval(trialObsUsed);
											}
										}
									}
								}
							}
						}
					}

					String runSuccessResidRandom = rConnection.eval("class(resid_r)").asString();
					System.out.println("runSuccessResidRandom:"+runSuccessResidRandom);
					if (runSuccessResidRandom != null && runSuccessResidRandom.equals("try-error")) {	
						System.out.println("ssa.resid (genotype random): error");
						String checkError = "msg <- trimStrings(strsplit(resid_r, \":\")[[1]])";
						String checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
						String checkError3 ="msg <- gsub(\"\\\"\", \"\", msg)";
						String checkError4 = "capture.output(cat(\"*** \nERROR in ssa.resid function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
						rConnection.eval(checkError);
						rConnection.eval(checkError2);
						rConnection.eval(checkError3);
						rConnection.eval(checkError4);
					} else {
						String checkResid1 = rConnection.eval("resid_r$residWarning").asString();
						System.out.println("checkResid1: " + checkResid1);
						if (checkResid1.equals("empty")) {
							System.out.println("Saving resid (random) not done.");
						} else {
							String func1SaveResidualsCsv = "saveResid2 <- try(write.table(resid_r$residuals, file = residFileNameRandom ,sep=\",\",row.names=FALSE), silent=TRUE)";
							rConnection.eval(residFileNameRandom);
							rConnection.eval(func1SaveResidualsCsv);

							String runSuccessSaveResid = rConnection.eval("class(saveResid2)").asString();
							System.out.println("runSuccessSaveResid:"+runSuccessSaveResid);
							if (runSuccessSaveResid != null && runSuccessSaveResid.equals("try-error")) {	
								System.out.println("save residuals: error");
								String checkError = "msg <- trimStrings(strsplit(saveResid2, \":\")[[1]])";
								String checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
								String checkError3 ="msg <- gsub(\"\\\"\", \"\", msg)";
								String checkError4 = "capture.output(cat(\"*** \nERROR in saving residuals to a file:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
								rConnection.eval(checkError);
								rConnection.eval(checkError2);
								rConnection.eval(checkError3);
								rConnection.eval(checkError4);
							}

							if (heatmapResiduals) {
								String funcHeat=null;
								if (environment == "NULL") {
									funcHeat = "heat2 <- try(Heatmap(resid_r$residuals, genAs=\"random\", \"" + heatmapRow + "\", \"" + heatmapColumn + "\", " + respvarVector + ", \"" + designUsed + "\", " + environment + "), silent=TRUE)";
								} else {
									funcHeat = "heat2 <- try(Heatmap(resid_r$residuals, genAs=\"random\", \"" + heatmapRow + "\", \"" + heatmapColumn + "\", " + respvarVector + ", \"" + designUsed + "\", \"" + environment + "\"), silent=TRUE)";
								}
								System.out.println(funcHeat);
								rConnection.eval(funcHeat);

								String runSuccessHeat = rConnection.eval("class(heat2)").asString();
								System.out.println("runSuccessHeat:"+runSuccessHeat);
								if (runSuccessHeat != null && runSuccessHeat.equals("try-error")) {	
									System.out.println("heatmap (random): error");
									String checkError = "msg <- trimStrings(strsplit(heat2, \":\")[[1]])";
									String checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
									String checkError3 ="msg <- gsub(\"\\\"\", \"\", msg)";
									String checkError4 = "capture.output(cat(\"*** \nERROR in Heatmap function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
									rConnection.eval(checkError);
									rConnection.eval(checkError2);
									rConnection.eval(checkError3);
									rConnection.eval(checkError4);
								} else {
									for (int k = 0; k < respvars.length; k++) {
										int i = k + 1; // 1-relative index;

										String envLevelsCommand = "length(heat2[[" + i + "]]$site)";
										int envLevels = rConnection.eval(envLevelsCommand).asInteger();
										for (int m = 0; m < envLevels; m++) { 
											int j = m + 1; // 1-relative index;

											String warningListCommand = "heat2[[" + i + "]]$site[["+ j + "]]";
											String warningList = rConnection.eval(warningListCommand).asString();

											if (warningList.equals("empty")) {

											} else if (warningList.equals("unique")) {

											} else {
												String trialObsUsed = "capture.output(cat(\"\nERROR:\", heat2[[" + i + "]]$site[["+ j + "]],\"\n\"),file=\""	+ outFileName + "\",append = TRUE)";
												rConnection.eval(trialObsUsed);
											}
										}
									}
								}
							}
						}
					}
				}
			}

			//optional output: boxplot and histogram
			String withBox = "FALSE";
			if (boxplotRawData) withBox = "TRUE";
			String withHist = "FALSE";
			if (histogramRawData) withHist = "TRUE";
			String ssaOut = "ssa1";
			if (genotypeFixed) ssaOut = "ssa1";
			else if (genotypeRandom) ssaOut = "ssa2";

			String boxHistFunc = null;
			if (environment =="NULL") {
				boxHistFunc = "boxHist <- try(graph.sea.boxhist(data, " + respvarVector + ", env = " + environment + ", " + ssaOut + ", box = \"" + withBox + "\", hist = \"" + withHist + "\"), silent=TRUE)";
			} else {
				boxHistFunc = "boxHist <- try(graph.sea.boxhist(data, " + respvarVector + ", env = \"" + environment + "\", " + ssaOut + ", box = \"" + withBox + "\", hist = \"" + withHist + "\"), silent=TRUE)";
			}
			System.out.println(boxHistFunc);
			rConnection.eval(boxHistFunc);

			String runSuccessBoxHist = rConnection.eval("class(boxHist)").asString();
			System.out.println("runSuccessBoxHist:"+runSuccessBoxHist);
			if (runSuccessBoxHist != null && runSuccessBoxHist.equals("try-error")) {	
				System.out.println("boxplot/histogram: error");
				String checkError = "msg <- trimStrings(strsplit(boxHist, \":\")[[1]])";
				String checkError2 = "msg <- trimStrings(paste(strsplit(msg, \"\\n\")[[length(msg)]], collapse = \" \"))";
				String checkError3 ="msg <- gsub(\"\\\"\", \"\", msg)";
				String checkError4 = "capture.output(cat(\"*** \nERROR in graph.sea.boxhist function:\\n  \",msg, \"\n***\n\n\", sep = \"\"), file=\"" + outFileName + "\",append = TRUE)";
				rConnection.eval(checkError);
				rConnection.eval(checkError2);
				rConnection.eval(checkError3);
				rConnection.eval(checkError4);
			}
			rConnection.eval(outspace);
			rConnection.eval(sep2);

		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			//			rConnection.close();
			end();
		}
	}

	public void test() {
		// TODO Auto-generated method stub
		try {
			rConnection.eval("cars <- c(1, 3, 6, 4, 9)");
			rConnection.eval("pdf(\"E:/cars3.pdf\")");
			rConnection.eval("plot(cars)");
			//			rConnection.eval("dev.off()");

			//			rConnection.close();
			end();
		} catch (RserveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String[] getLevels(List<String> columnList, List<String[]> dataList,
			String columnName) {
		// TODO Auto-generated method stub
		int envtColumn = 0;
		for (int i = 0; i < columnList.size(); i++) {
			if (columnList.get(i).equals(columnName)) {
				envtColumn = i;
			}
		}

		ArrayList<String> envts = new ArrayList<String>();
		for (int j = 0; j <dataList.size(); j++) {
			String level = dataList.get(j)[envtColumn];
			if (!envts.contains(level)&& !level.isEmpty()) {
				envts.add(level);
			}
		}

		String[] envtLevels = new String[envts.size()];
		for (int k = 0; k < envts.size(); k++) {
			envtLevels[k] = (String) envts.get(k);
		}

		return envtLevels;
	}

	public String isColumnNumeric (String dataFileName, String columnName){
		String isNumericOut = null;
		try{
			readData(dataFileName);
			String isNumeric = "is.numeric(dataRead$" + columnName +" )";
			System.out.println(isNumeric);
			isNumericOut=rConnection.eval(isNumeric).asString();
			System.out.println("reached end. "+isNumericOut.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isNumericOut;
	}

	public void end() {
		// TODO Auto-generated method stub
		try{
			rConnection.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}


}
