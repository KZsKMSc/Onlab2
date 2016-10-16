package com.sample;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.drools.core.WorkingMemory;
import org.drools.core.time.SessionPseudoClock;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSessionConfiguration;
import org.kie.api.runtime.conf.ClockTypeOption;
import org.kie.internal.KnowledgeBase;
import org.kie.internal.KnowledgeBaseFactory;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.StatefulKnowledgeSession;

/**
 * This is a sample class to launch a rule.
 */
@SuppressWarnings({ "restriction", "deprecation" })
public class IrisCep {

	public static final void main(String[] args) {
		try {

			////// **************************************
			// Tudasbazis feltoltese
			// KnowledgeBuilder
			KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
			kbuilder.add(ResourceFactory.newClassPathResource("rules/Clusters2.drl"), ResourceType.DRL);
			if (kbuilder.hasErrors()) {
				System.err.println(kbuilder.getErrors().toString());
			}
			// KnowledgeBase
			KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
			kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());

			// Session kerese
			// StatefulKnowlegeSession
			StatefulKnowledgeSession kSession = kbase.newStatefulKnowledgeSession();

			// Stream mode
			KieBaseConfiguration bconf = KnowledgeBaseFactory.newKnowledgeBaseConfiguration();
			bconf.setOption(EventProcessingOption.STREAM);

			// Pseudo clock
			KieSessionConfiguration sconf = KnowledgeBaseFactory.newKnowledgeSessionConfiguration();
			sconf.setOption(ClockTypeOption.get("pseudo"));
			StatefulKnowledgeSession session = kbase.newStatefulKnowledgeSession(sconf, null);
			SessionPseudoClock clock = session.getSessionClock();    

			///// ******************************

			/*
			 * // load up the knowledge base KieServices ks =
			 * KieServices.Factory.get(); KieContainer kContainer =
			 * ks.getKieClasspathContainer(); KieSession kSession =
			 * kContainer.newKieSession("ksession-rules");
			 */

			// Integer x = 0;
			// kSession.setGlobal( "x", x );

			// go !
			/*
			 * // 5.1 3.5 1.4 0.2 setosa Flower setosa1 = new Flower();
			 * setosa1.setSepalLength(5.1); setosa1.setSepalWidth(3.5);
			 * setosa1.setPetalLength(1.4); setosa1.setPetalWidth(0.2);
			 * setosa1.setSpecie("setosa");
			 * 
			 * // 4.9 3.0 1.4 0.2 setosa Flower setosa2 = new Flower();
			 * setosa2.setSepalLength(4.9); setosa2.setSepalWidth(3.0);
			 * setosa2.setPetalLength(1.4); setosa2.setPetalWidth(0.2);
			 * setosa2.setSpecie("setosa");
			 * 
			 * // 5.1 2.5 3.0 1.1 I. versicolor Flower versicolor1 = new
			 * Flower(); versicolor1.setSepalLength(5.1);
			 * versicolor1.setSepalWidth(2.5); versicolor1.setPetalLength(3.0);
			 * versicolor1.setPetalWidth(1.8);
			 * versicolor1.setSpecie("versicolor");
			 * 
			 * clock.advanceTime(1, TimeUnit.SECONDS); kSession.insert(setosa1);
			 * kSession.fireAllRules(); clock.advanceTime(1, TimeUnit.SECONDS);
			 * kSession.insert(setosa2); kSession.fireAllRules();
			 * clock.advanceTime(1, TimeUnit.SECONDS);
			 * kSession.insert(versicolor1);
			 */
			////////////////////////////////////////////
			// String csvFile = "iris.csv";
			
			//String csvFile = "base.csv";
			//String csvFile = "part2.csv";
			//String csvFile = "data3.csv";
			
			//String csvFile = "SLbase.csv";
			//String csvFile = "SLpart2.csv";
			String csvFile = "SLdata3.csv";
			
			String line = "";
			String cvsSplitBy = ",";

			try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

				line = br.readLine(); // header

				while ((line = br.readLine()) != null) {

					// use comma as separator
					String[] sF = line.split(cvsSplitBy);

					Flower flower = new Flower();
					flower.setSepalLength(Double.parseDouble(sF[0]));
					flower.setSepalWidth(Double.parseDouble(sF[1]));
					flower.setPetalLength(Double.parseDouble(sF[2]));
					flower.setPetalWidth(Double.parseDouble(sF[3]));
					String[] spec = sF[4].split("\"");
					flower.setOriginalSpecies (spec[1]);
					flower.setClusterType(csvFile);
/*
					if (csvFile == "base.csv") {
						// base
						if (Double.parseDouble(sF[3]) <= 0.6)
							flower.setSpecie("setosa");
						else if ((Double.parseDouble(sF[3]) > 0.6) && (Double.parseDouble(sF[3]) <= 1.7))
							flower.setSpecie("versicolor");
						else if ((Double.parseDouble(sF[3]) > 1.7))
							flower.setSpecie("virginica");
					}
					
					if (csvFile == "part2.csv") {
						// part2
						if (Double.parseDouble(sF[3]) <= 1.0)
							flower.setSpecie("setosa");
						else
							flower.setSpecie("virginica");
						
					}
					
					else if (csvFile == "data3.csv") {
						// data3
						if (Double.parseDouble(sF[3]) <= 1.0)
							flower.setSpecie("setosa");
						else if ((Double.parseDouble(sF[3]) > 1.0) && (Double.parseDouble(sF[3]) <= 1.7))
							flower.setSpecie("versicolor");
						else if ((Double.parseDouble(sF[3]) > 1.7))
							flower.setSpecie("virginica");
					}
					*/
					clock.advanceTime(1, TimeUnit.SECONDS);
					kSession.insert(flower);
					kSession.fireAllRules();
					}

			} catch (IOException e) {
				e.printStackTrace();
			}
			////////////////////////////////////////////////

			kSession.fireAllRules();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public static class Flower {

		private Double sepalLength;
		private Double sepalWidth;
		private Double petalLength;
		private Double petalWidth;
		private String originalSpecies = "";
		private String clusteredSpecies = "";
		private String clusterType = "";

		public Double getSepalLength() {
			return this.sepalLength;
		}

		public void setSepalLength(Double sepalLength) {
			this.sepalLength = sepalLength;
		}

		public Double getSepalWidth() {
			return this.sepalWidth;
		}

		public void setSepalWidth(Double sepalWidth) {
			this.sepalWidth = sepalWidth;
		}

		public Double getPetalLength() {
			return this.petalLength;
		}

		public void setPetalLength(Double petalLength) {
			this.petalLength = petalLength;
		}

		public Double getPetalWidth() {
			return this.petalWidth;
		}

		public void setPetalWidth(Double petalWidth) {
			this.petalWidth = petalWidth;
		}

		public String getOriginalSpecies() {
			return this.originalSpecies;
		}

		public void setOriginalSpecies(String originalSpecie) {
			this.originalSpecies = originalSpecie;
		}
		
		public String getClusteredSpecies() {
			return this.clusteredSpecies;
		}

		public void setClusteredSpecies(String clusteredSpecie) {
			this.clusteredSpecies = clusteredSpecie;
		}
		
		public String getClusterType() {
			return this.clusterType;
		}

		public void setClusterType(String clusterType) {
			this.clusterType = clusterType;
		}
	}
}
