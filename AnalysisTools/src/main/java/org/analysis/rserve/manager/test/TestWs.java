package org.analysis.rserve.manager.test;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.pbtools.analysis.model.SingleSiteAnalysisModel;
import org.pbtools.analysis.utilities.InputTransform;
import org.zkoss.zk.ui.Sessions;

import com.google.gson.Gson;
public class TestWs {

	private InputTransform inputTransform;
	
	private static String BSLASH = "\\";
	private static String FSLASH = "/";
//	private static String OUTPUTFOLDER_PATH =  Sessions.getCurrent().getWebApp().getRealPath("outputfolder")+ System.getProperty("file.separator");
//	public static String DATA_PATH = System.getProperty("user.dir")+ System.getProperty("file.separator") + "sample_datasets" + System.getProperty("file.separator");

	public static void main(String[] args){

		try {
			SingleSiteAnalysisModel params = new SingleSiteAnalysisModel();
			
			String[] dataHeader={"Site","Blk","Gen","Y1","Y2"};
			
			List<String[]> data= new ArrayList<String[]>();
			data.add(new String[]{"Env1","1","1","50.2","20.5"});
			data.add(new String[]{"Env1","1","2","41.8","19.5"});
			data.add(new String[]{"Env1","1","3","41.8","18.5"});
			data.add(new String[]{"Env1","1","4","41.8","19.5"});
			data.add(new String[]{"Env1","1","5","44.8","18.5"});
			data.add(new String[]{"Env1","1","6","43.8","19.5"});
			data.add(new String[]{"Env1","1","7","48.8","20.5"});
			data.add(new String[]{"Env1","1","8","41.8","19.5"});
			
			data.add(new String[]{"Env1","2","1","50.2","20.5"});
			data.add(new String[]{"Env1","2","2","41.8","18.5"});
			data.add(new String[]{"Env1","2","3","45.8","19.5"});
			data.add(new String[]{"Env1","2","4","46.8","18.5"});
			data.add(new String[]{"Env1","2","5","41.8","19"});
			data.add(new String[]{"Env1","2","6","49.8","19.5"});
			data.add(new String[]{"Env1","2","7","37.8","17.3"});
			data.add(new String[]{"Env1","2","8","41.8","19.5"});
			
			data.add(new String[]{"Env1","3","1","50.2","20.5"});
			data.add(new String[]{"Env1","3","2","44.8","19.5"});
			data.add(new String[]{"Env1","3","3","41.8","19.5"});
			data.add(new String[]{"Env1","3","4","41.8","16.5"});
			data.add(new String[]{"Env1","3","5","42.8","19.5"});
			data.add(new String[]{"Env1","3","6","41.8","15.5"});
			data.add(new String[]{"Env1","3","7","43.8","18.5"});
			data.add(new String[]{"Env1","3","8","48.8","19.5"});
			
			data.add(new String[]{"Env1","4","1","50.2","20.5"});
			data.add(new String[]{"Env1","4","2","41.8","19.5"});
			data.add(new String[]{"Env1","4","3","46.8","19.5"});
			data.add(new String[]{"Env1","4","4","41.8","16.5"});
			data.add(new String[]{"Env1","4","5","47.8","19.5"});
			data.add(new String[]{"Env1","4","6","41.8","15.5"});
			data.add(new String[]{"Env1","4","7","44.8","18.5"});
			data.add(new String[]{"Env1","4","8","43.8","19.5"});
			
			data.add(new String[]{"Env2","1","1","50.2","20.5"});
			data.add(new String[]{"Env2","1","2","41.8","19.5"});
			data.add(new String[]{"Env2","1","3","41.8","18.5"});
			data.add(new String[]{"Env2","1","4","41.8","19.5"});
			data.add(new String[]{"Env2","1","5","41.8","18.5"});
			data.add(new String[]{"Env2","1","6","41.8","19.5"});
			data.add(new String[]{"Env2","1","7","41.8","20.5"});
			data.add(new String[]{"Env2","1","8","41.8","19.5"});
			
			data.add(new String[]{"Env2","2","1","50.2","20.5"});
			data.add(new String[]{"Env2","2","2","41.8","18.5"});
			data.add(new String[]{"Env2","2","3","45.8","19.5"});
			data.add(new String[]{"Env2","2","4","43.8","18.5"});
			data.add(new String[]{"Env2","2","5","41.8","19"});
			data.add(new String[]{"Env2","2","6","48.8","19.5"});
			data.add(new String[]{"Env2","2","7","41.8","17.3"});
			data.add(new String[]{"Env2","2","8","43.8","19.5"});
			
			data.add(new String[]{"Env2","3","1","50.2","20.5"});
			data.add(new String[]{"Env2","3","2","41.8","19.5"});
			data.add(new String[]{"Env2","3","3","46.8","19.5"});
			data.add(new String[]{"Env2","3","4","41.8","16.5"});
			data.add(new String[]{"Env2","3","5","47.8","19.5"});
			data.add(new String[]{"Env2","3","6","41.8","15.5"});
			data.add(new String[]{"Env2","3","7","47.8","18.5"});
			data.add(new String[]{"Env2","3","8","47.8","19.5"});
			
			data.add(new String[]{"Env2","4","1","50.2","20.5"});
			data.add(new String[]{"Env2","4","2","41.8","19.5"});
			data.add(new String[]{"Env2","4","3","41.8","19.5"});
			data.add(new String[]{"Env2","4","4","44.8","16.5"});
			data.add(new String[]{"Env2","4","5","43.8","19.5"});
			data.add(new String[]{"Env2","4","6","41.8","15.5"});
			data.add(new String[]{"Env2","4","7","42.8","18.5"});
			data.add(new String[]{"Env2","4","8","41.8","19.5"});

/*			int design = 0;
			String respvars[] = {"Y1"};
			String env="NULL";
			String[] environmentLevels = {};
			
			String genotype = "Gen";
			String block = "Blk";
			String rep ="NULL";
			String row = "NULL";
			String column = "NULL";*/
			
			
			String env="Site";
			String respvars[] = {"Y1"};
//			String respvars[] = {""};
			String[] environmentLevels={};
			int design = 0;
			String genotype = "Gen";
			String block = "Blk";
			String rep = "NULL";
			String row = "NULL";
			String column = "NULL";
			boolean descriptiveStat = true; 
			boolean varianceComponents = true;
			boolean boxplotRawData = true;
			boolean histogramRawData = true;
			boolean heatmapResiduals = false;
			String heatmapRow = "NULL";
			String heatmapColumn = "NULL";
			boolean diagnosticPlot = true;
			boolean genotypeFixed = true;
			boolean performPairwise = false;
			String pairwiseAlpha = "0.05";
			boolean compareControl = false;
			boolean performAllPairwise = false;
			boolean genotypeRandom = true;
			boolean excludeControls = false;
			boolean genoPhenoCorrelation = false;
			String[] genotypeLevels = {};
			String[] controlLevels = {};

		
/*			boolean genotypeFixed = true;
			boolean genotypeRandom = false;
			
			boolean boxplotRawData = true;
			boolean histogramRawData = true;
			boolean heatmapResiduals = false;
			boolean diagnosticPlot = true;
			
			String heatmapRow = "NULL";
			String heatmapColumn = "NULL";
			

			boolean performPairwise = false;
			String pairwiseAlpha = "0.05";
			boolean compareControl = false;
			String[] genotypeLevels = {};
			String[] controlLevels = {};
			boolean performAllPairwise = false;
			boolean excludeControls = false;
			boolean genoPhenoCorrelation = true;
			
			boolean descriptiveStat = true; 
			boolean varianceComponents = true;*/
			

			

			params.setUserAccount("user1");
			params.setAnalysisResultFolder("AngelSingleTrialTest");
			params.setDataHeader(dataHeader);
			params.setData(data);
			
			
			params.setDesign(design);
			params.setRespvars(respvars);
			params.setEnvironment(env);
			params.setEnvironmentLevels(environmentLevels);
			
			params.setGenotype(genotype);
			params.setBlock(block);
			params.setRep(rep);
			params.setRow(row);
			params.setColumn(column);
			
			params.setBoxplotRawData(boxplotRawData);
			params.setHistogramRawData(histogramRawData);
			params.setDiagnosticPlot(diagnosticPlot);
			params.setHeatmapResiduals(heatmapResiduals);
			params.setHeatmapRow(heatmapRow);
			params.setHeatmapColumn(heatmapColumn);
			
			params.setGenotypeFixed(genotypeFixed);
			params.setGenotypeRandom(genotypeRandom);
			
			params.setPerformPairwise(performPairwise);
			params.setPairwiseAlpha(pairwiseAlpha);
			params.setGenotypeLevels(genotypeLevels);
			params.setControlLevels(controlLevels);
			params.setCompareControl(compareControl);
			params.setPerformAllPairwise(performAllPairwise);
		
			params.setExcludeControls(excludeControls);
			params.setGenoPhenoCorrelation(genoPhenoCorrelation);
			
			params.setDescriptiveStat(descriptiveStat);
			params.setVarianceComponents(varianceComponents);

			Gson gson = new Gson();
			String json = gson.toJson(params);
			
			System.out.println(json);

			Client c = ClientBuilder.newClient();
			WebTarget target= c.target("http://172.29.4.166:8080/WS-RS/rest/SingleTrial/analyze");
//			WebTarget target= c.target("http://localhost:8080/WS-RS/rest/SingleTrial/run");
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

}
