

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import de.bwaldvogel.liblinear.Feature;
import de.bwaldvogel.liblinear.FeatureNode;
import de.bwaldvogel.liblinear.Linear;
import de.bwaldvogel.liblinear.Model;
import de.bwaldvogel.liblinear.Parameter;
import de.bwaldvogel.liblinear.Problem;
import de.bwaldvogel.liblinear.SolverType;

public class BigramSVMLibLinear {
	private static Scanner reader;
	private static Scanner reader2;
	private static Scanner reader3;
	private static Scanner reader4;

	public static void main(String[] args) throws IOException {
		double sumfreq=0;
        double sumpresence=0;
		for (int fold = 0; fold <= 800; fold += 200) {
			Integer testDocStartNo = fold;
			Integer testDocEndNo = fold + 200;
			File folder = new File("neg");
			File[] neglistOfFiles = folder.listFiles();
			Feature[][] feature2DArray = new Feature[1600][];
			Feature[][] featurePresence2DArray = new Feature[1600][];
			Feature[][] testFeaturePresence2DArray = new Feature[1600][];
			int j = 0;
			
			TreeMap<String, Integer> bigramTotalWordCount = new TreeMap<String, Integer>();
	
			List<TreeMap<String, Integer>> bigramArrayHashMaps = new ArrayList<TreeMap<String, Integer>>();

			for (File file : neglistOfFiles) {
				TreeMap<String, Integer> documentWordCount = new TreeMap<String, Integer>();
				if (file.isFile()) {
					Integer reviewNo = Integer.parseInt(file.getName().split(
							"_")[0].substring(2));

					if (!(reviewNo >= testDocStartNo && reviewNo < testDocEndNo)) {
						reader4 = new Scanner(file);
						reader4.useDelimiter("[^A-Za-z]+");
						String previousWord = null;
						while (reader4.hasNext()) {
							String word = reader4.next();
							
							if (previousWord != null) {
								String bigram = previousWord + " " + word;

								if (bigramTotalWordCount.containsKey(bigram))

									bigramTotalWordCount
											.put(bigram, bigramTotalWordCount
													.get(bigram) + 1);
								else {
									bigramTotalWordCount.put(bigram, 1);
								}
							
								if (documentWordCount.containsKey(bigram))

									documentWordCount.put(bigram,
											documentWordCount.get(bigram) + 1);
								else {
									documentWordCount.put(bigram, 1);
								}

							}
							previousWord = word;
						}
						bigramArrayHashMaps.add(documentWordCount);
						
					}
				}
				
			}
			File posfolder = new File("pos");
			File[] poslistOfFiles = posfolder.listFiles();
		
			for (File file : poslistOfFiles) {
				TreeMap<String, Integer> documentWordCount = new TreeMap<String, Integer>();
				if (file.isFile()) {
					Integer reviewNo = Integer.parseInt(file.getName().split(
							"_")[0].substring(2));
					if (!(reviewNo >= testDocStartNo && reviewNo < testDocEndNo)) {
						reader4 = new Scanner(file);
						reader4.useDelimiter("[^A-Za-z]+");
						String previousWord = null;
						while (reader4.hasNext()) {
							String word = reader4.next();
							
							if (previousWord != null) {
								String bigram = previousWord + " " + word;

								if (bigramTotalWordCount.containsKey(bigram))
									bigramTotalWordCount
											.put(bigram, bigramTotalWordCount
													.get(bigram) + 1);
								else {
									bigramTotalWordCount.put(bigram, 1);
								}
							
								if (documentWordCount.containsKey(bigram))

									documentWordCount.put(bigram,
											documentWordCount.get(bigram) + 1);
								else {
									documentWordCount.put(bigram, 1);
								}

							}
							previousWord = word;

						}
						bigramArrayHashMaps.add(documentWordCount);
					

					}
				}
			}
			//System.out.println(bigramTotalWordCount.size());
				Feature[][] testFeature2DArray = new Feature[400][];
			j = 0;
			List<TreeMap<String, Integer>> bigramTestArrayHashMaps = new ArrayList<TreeMap<String, Integer>>();
			for (File negfile : neglistOfFiles) {
				TreeMap<String, Integer> testDocumentWordCount = new TreeMap<String, Integer>();
				if (negfile.isFile()) {
					Integer reviewNo = Integer.parseInt(negfile.getName()
							.split("_")[0].substring(2));
					if ((reviewNo >= testDocStartNo && reviewNo < testDocEndNo)) {
						reader2 = new Scanner(negfile);
						reader2.useDelimiter("[^A-Za-z]+");
						String previousWord = null;
						while (reader2.hasNext()) {
							String word = reader2.next();
							if (previousWord != null) {
								String bigram = previousWord + " " + word;
								if (bigramTotalWordCount.containsKey(bigram))
									bigramTotalWordCount.put(bigram,
											bigramTotalWordCount.get(bigram) + 1);
								else {
									bigramTotalWordCount.put(bigram, 1);
								}
							

								if (testDocumentWordCount.containsKey(bigram))

									testDocumentWordCount
											.put(bigram, testDocumentWordCount
													.get(bigram) + 1);
								else {
									testDocumentWordCount.put(bigram, 1);
								}
							}
							previousWord = word;
						}
						bigramTestArrayHashMaps.add(testDocumentWordCount);
						
					}

				}
			}
			
			for (File posfile : poslistOfFiles) {
				TreeMap<String, Integer> documentWordCount = new TreeMap<String, Integer>();
				if (posfile.isFile()) {
					Integer reviewNo = Integer.parseInt(posfile.getName()
							.split("_")[0].substring(2));
					if ((reviewNo >= testDocStartNo && reviewNo < testDocEndNo)) {
						reader3 = new Scanner(posfile);
						reader3.useDelimiter("[^A-Za-z]+");
						String previousWord = null;
						while (reader3.hasNext()) {
							String word = reader3.next();

							if (previousWord != null) {
								String bigram = previousWord + " " + word;

								
								if (bigramTotalWordCount.containsKey(bigram))
									bigramTotalWordCount.put(bigram,
											bigramTotalWordCount.get(bigram) + 1);
								else {
									bigramTotalWordCount.put(bigram, 1);
								}
								if (documentWordCount.containsKey(bigram))

									documentWordCount.put(bigram,
											documentWordCount.get(bigram) + 1);
								else {
									documentWordCount.put(bigram, 1);
								}

							}
							previousWord = word;
						}
						
						bigramTestArrayHashMaps.add(documentWordCount);
					}
				}
			}
			
			Integer uniqueWordCount = bigramTotalWordCount.size();
			TreeMap<String, Integer> indexMap = new TreeMap<String, Integer>();
			int position = 1;
			for (String word : bigramTotalWordCount.keySet()) {
				indexMap.put(word, position);
				position++;
			}
			for (TreeMap<String, Integer> documentWordCount : bigramArrayHashMaps) {
				Feature[] featureArray = new FeatureNode[documentWordCount
						.size()];
				Feature[] featurePresenceArray = new FeatureNode[documentWordCount
				                 						.size()];
				int i = 0;
				for (Map.Entry<String, Integer> wordCountEntry : documentWordCount
						.entrySet()) {
					int index = indexMap.get(wordCountEntry.getKey());
					FeatureNode featureNode = new FeatureNode(index,
							wordCountEntry.getValue().doubleValue());
					FeatureNode featurePresenceNode = new FeatureNode(index,
							1);
					featureArray[i] = featureNode;
					featurePresenceArray[i]=featurePresenceNode;
					i++;
				}

				feature2DArray[j] = featureArray;
				featurePresence2DArray[j]=featurePresenceArray;
				j++;
			}
		//	System.out.println(feature2DArray.toString());
			
			double[] targetValues = new double[1600];
			for (int k = 0; k < 800; k++) {
				targetValues[k] = 0;
			}
			for (int k = 800; k < 1600; k++) {
				targetValues[k] = 1;
			}

			j = 0;
			for (TreeMap<String, Integer> documentWordCount : bigramTestArrayHashMaps) {
				Feature[] featureArray = new FeatureNode[documentWordCount
						.size()];
				Feature[] featurePresenceArray = new FeatureNode[documentWordCount
							                 						.size()];
				int i = 0;
				for (Map.Entry<String, Integer> wordCountEntry : documentWordCount
						.entrySet()) {
					int index = indexMap.get(wordCountEntry.getKey());
					FeatureNode featureNode = new FeatureNode(index,
							wordCountEntry.getValue().doubleValue());
					FeatureNode featurePresenceNode = new FeatureNode(index,
							1);
					featureArray[i] = featureNode;
					featurePresenceArray[i]=featurePresenceNode;
					i++;
				}
				testFeature2DArray[j] = featureArray;
				testFeaturePresence2DArray[j]=featurePresenceArray;
				j++;
			}
			
		//	System.out.println(testFeature2DArray.toString());
		//	System.out.println("In training");
			Problem problem = new Problem();
			problem.l = 1600;
			problem.n = uniqueWordCount; // number of features
			problem.x = feature2DArray;
			problem.y = targetValues;// target values
			problem.bias=1;

			SolverType solver = SolverType.L2R_LR; // -s 0
			double C = 1.0; // cost of constraints violation
			double eps = 0.01; // stopping criteria

			Parameter parameter = new Parameter(solver, C, eps);
			Model model = Linear.train(problem, parameter);
			File modelFile = new File("model");
			model.save(modelFile);
			// load model or use it directly
			model = Model.load(modelFile);
		//	System.out.println("In prediction");
			int testErrorCount = 0;
			for (int k = 0; k < 200; k++) {
				Feature[] instance = testFeature2DArray[k];
				double prediction = Linear.predict(model, instance);
				if (prediction == 1){
		//			System.out.println("freq err no in neg"+ k);
			
					testErrorCount++;
				}
			}
			for (int k = 200; k < 400; k++) {
				Feature[] instance = testFeature2DArray[k];
				double prediction = Linear.predict(model, instance);
				if (prediction == 0)
					testErrorCount++;
			}
			//System.out.println("Test Error Count " + testErrorCount);
			//System.out.println("Accuracy " + (double) (400 - testErrorCount)
			//		/ 4 + "%");
			//sumfreq+=(double) (400 - testErrorCount)/4;
			
		
			
		
			Problem presenceproblem = new Problem();
			presenceproblem.l = 1600;
			presenceproblem.n = uniqueWordCount; // number of features
			presenceproblem.x = featurePresence2DArray;
			presenceproblem.y = targetValues;// target values

		 // stopping criteria

			Parameter presenceparameter = new Parameter(solver, C, eps);
			Model presencemodel = Linear.train(presenceproblem, presenceparameter);
			File presencemodelFile = new File("model");
			presencemodel.save(presencemodelFile);
			// load model or use it directly
			presencemodel = Model.load(presencemodelFile);
		//	System.out.println("In prediction");
			int testpresenceErrorCount = 0;
			for (int k = 0; k < 200; k++) {
				Feature[] instance = testFeaturePresence2DArray[k];
				double prediction = Linear.predict(presencemodel, instance);
				if (prediction == 1){
			//		System.out.println("pres err no in neg"+ k);
				
					testpresenceErrorCount++;
				}
			}
			for (int k = 200; k < 400; k++) {
				Feature[] instance = testFeaturePresence2DArray[k];
				double prediction = Linear.predict(presencemodel, instance);
				if (prediction == 0)
					testpresenceErrorCount++;
			}
			System.out.println("----Trial "+((fold/200)+1)+ " results----");
			//System.out.println("Test Frequency Errors: " + testErrorCount);
			System.out.println("Test Frequency Accuracy: " + (double) (400 - testErrorCount)
					/ 4 + "%");
			sumfreq+=(double) (400 - testErrorCount)/4;
			
		//	System.out.println("TEST PRESENCE ERRORS " + testPresenceErrorCount);
			System.out.println("Test Presence Accuracy: "+(double) (400 - testpresenceErrorCount)
					/ 4 + "%");
			sumpresence+=(double) (400 - testpresenceErrorCount)/4;
			System.out.println("-----------------------");
			System.out.println("");
		}
		System.out.println("Average Frequency Accuracy: "+ sumfreq/5+"%");
		System.out.println("Average Presence Accuracy: "+ sumpresence/5+"%");

	}
	
}
