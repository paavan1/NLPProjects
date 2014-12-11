
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class BigramPerceptron {

	private static Scanner reader;
	private static Scanner reader2;
	private static Scanner reader3;
	private static Scanner reader4;

	public static void main(String[] args) throws FileNotFoundException {

		double sumfreq = 0;
		double sumpresence = 0;
		for (int fold = 0; fold <= 800; fold += 200) {
			Integer testDocStartNo = fold;
			Integer testDocEndNo = fold + 200;
			File folder = new File("neg");
			File[] neglistOfFiles = folder.listFiles();
			HashMap<String, Double> weights = new HashMap<String, Double>();
			HashMap<String, Double> presenceweights = new HashMap<String, Double>();
			HashMap<String, Integer> bigramTotalWordCount = new HashMap<String, Integer>();
			HashMap<String, Integer> bigramNegWordCount = new HashMap<String, Integer>();
			HashMap<String, Integer> bigramPosWordCount = new HashMap<String, Integer>();
			List<HashMap<String, Integer>> bigramArrayHashMaps = new ArrayList<HashMap<String, Integer>>();

			for (File file : neglistOfFiles) {
				HashMap<String, Integer> documentWordCount = new HashMap<String, Integer>();
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
				HashMap<String, Integer> documentWordCount = new HashMap<String, Integer>();
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

								if (bigramTotalWordCount.containsKey(word))
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

			for (String uniqueword : bigramTotalWordCount.keySet()) {
				weights.put(uniqueword, (double) 0);
			}

			for (String uniqueword : bigramTotalWordCount.keySet()) {
				presenceweights.put(uniqueword, (double) 0);
			}

			// learning

			// for ( int loop=0 ; loop <10;loop++){
			int loop = 1;
			int presenceloop = 1;
			int count = 0;
			while (loop != 0 & presenceloop != 0) {
				loop = 0;
				int i = 0;
				for (int k = 0; k < 800; k = k + 200) {
					for (int j = k; j < 200 + k; j++) {
						HashMap<String, Integer> documentHashMap = bigramArrayHashMaps
								.get(j);
						Double sumActivation = (double) 0;
						Double presencesumActivation = (double) 0;

						for (Map.Entry<String, Integer> entry : documentHashMap
								.entrySet()) {

							sumActivation += entry.getValue()
									* weights.get(entry.getKey());
							presencesumActivation += 1 * presenceweights
									.get(entry.getKey());

						}
						if (i >= 0 && i < 800) {
							if (sumActivation >= 0) {
								loop++;

								for (Map.Entry<String, Integer> entry : documentHashMap
										.entrySet()) {

									weights.put(
											entry.getKey(),
											weights.get(entry.getKey())
													- entry.getValue());

								}
							}
							if (presencesumActivation >= 0) {
								presenceloop++;

								for (Map.Entry<String, Integer> entry : documentHashMap
										.entrySet()) {

									presenceweights
											.put(entry.getKey(),
													presenceweights.get(entry
															.getKey()) - 1);

								}
							}
						}
					}
					for (int j = bigramArrayHashMaps.size() / 2 + k; j < bigramArrayHashMaps
							.size() / 2 + 200 + k; j++) {
						HashMap<String, Integer> documentHashMap = bigramArrayHashMaps
								.get(j);
						Double sumActivation = (double) 0;
						Double presencesumActivation = (double) 0;

						for (Map.Entry<String, Integer> entry : documentHashMap
								.entrySet()) {

							sumActivation += entry.getValue()
									* weights.get(entry.getKey());
							presencesumActivation += 1 * presenceweights
									.get(entry.getKey());

						}

						if (sumActivation < 0) {
							loop++;

							for (Map.Entry<String, Integer> entry : documentHashMap
									.entrySet()) {

								weights.put(
										entry.getKey(),
										weights.get(entry.getKey())
												+ entry.getValue());

							}
						}
						if (presencesumActivation < 0) {
							presenceloop++;

							for (Map.Entry<String, Integer> entry : documentHashMap
									.entrySet()) {

								presenceweights
										.put(entry.getKey(), presenceweights
												.get(entry.getKey()) + 1);

							}
						}
					}
				}

				count++;

			}

			// System.out.println("count"+count);

			HashMap<String, Integer> testTotalWordCount = new HashMap<String, Integer>();
			List<HashMap<String, Integer>> bigramTestArrayHashMaps = new ArrayList<HashMap<String, Integer>>();
			for (File negfile : neglistOfFiles) {
				HashMap<String, Integer> testDocumentWordCount = new HashMap<String, Integer>();
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
				HashMap<String, Integer> documentWordCount = new HashMap<String, Integer>();
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

			List<Double> testActivationForDoc = new ArrayList<Double>();
			List<Double> presencetestActivationForDoc = new ArrayList<Double>();

			for (HashMap<String, Integer> documentHashMap : bigramTestArrayHashMaps) {
				Double testActivation = (double) 0;
				Double presencetestActivation = (double) 0;
				for (Map.Entry<String, Integer> entry : documentHashMap
						.entrySet()) {
					if (weights.containsKey(entry.getKey())) {
						testActivation += entry.getValue()
								* weights.get(entry.getKey());
					}
					if (presenceweights.containsKey(entry.getKey())) {
						presencetestActivation += 1 * presenceweights.get(entry
								.getKey());
					}
				}
				testActivationForDoc.add(testActivation);
				presencetestActivationForDoc.add(presencetestActivation);
			}
			int testerrorcount = 0;
			int presencetesterrorcount = 0;

			for (int i = 0; i < 200; i++) {

				if (testActivationForDoc.get(i) >= 0) {
					// System.out.println("freq err no in neg"+ i);
					testerrorcount += 1;
				}
				if (presencetestActivationForDoc.get(i) >= 0) {
					// System.out.println("pres err no in neg"+ i);
					presencetesterrorcount += 1;
				}

			}

			for (int i = 200; i < 400; i++) {

				if (testActivationForDoc.get(i) < 0) {
					testerrorcount += 1;
				}

				if (presencetestActivationForDoc.get(i) < 0) {
					presencetesterrorcount += 1;
				}

			}
			System.out.println("----Trial "+((fold/200)+1)+ " results----");
			//System.out.println("Test Frequency Errors: " + testErrorCount);
			System.out.println("Test Frequency Accuracy: " + (double) (400 - testerrorcount)
					/ 4 + "%");
			sumfreq+=(double) (400 - testerrorcount)/4;
			
		//	System.out.println("TEST PRESENCE ERRORS " + testPresenceErrorCount);
			System.out.println("Test Presence Accuracy: "+(double) (400 - presencetesterrorcount)
					/ 4 + "%");
			sumpresence+=(double) (400 - presencetesterrorcount)/4;
			System.out.println("-----------------------");
			System.out.println("");
		}
		System.out.println("Average Frequency Accuracy: "+ sumfreq/5+"%");
		System.out.println("Average Presence Accuracy: "+ sumpresence/5+"%");


	}
}
