//package org.sbu.nlp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class GREBigramClassifier {
	private static Scanner reader;
	private static Scanner reader2;
	private static Scanner reader3;
	private static Scanner reader4;

	public static void main(String[] args) throws FileNotFoundException {
		double averagefreqaccuracy = 0;
		double averagepresaccuracy = 0;
		double sumfreq = 0;
		double sumpresence = 0;
		for (int fold = 0; fold <= 800; fold += 200) {
			Integer testDocStartNo = fold;
			Integer testDocEndNo = fold + 200;
			File folder = new File("neg");
			File[] neglistOfFiles = folder.listFiles();

			HashMap<String, Integer> bigramTotalWordCount = new HashMap<String, Integer>();
			HashMap<String, Integer> negWordCount = new HashMap<String, Integer>();
			HashMap<String, Integer> bigramNegWordCount = new HashMap<String, Integer>();
			HashMap<String, Integer> posWordCount = new HashMap<String, Integer>();
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
							if (negWordCount.containsKey(word))

								negWordCount.put(word,
										negWordCount.get(word) + 1);
							else {
								negWordCount.put(word, 1);
							}
							if (previousWord != null) {
								String bigram = previousWord + " " + word;

								if (bigramTotalWordCount.containsKey(bigram))

									bigramTotalWordCount
											.put(bigram, bigramTotalWordCount
													.get(bigram) + 1);
								else {
									bigramTotalWordCount.put(bigram, 1);
								}
								if (bigramNegWordCount.containsKey(bigram))

									bigramNegWordCount.put(bigram,
											bigramNegWordCount.get(bigram) + 1);
								else {
									bigramNegWordCount.put(bigram, 1);
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
							if (posWordCount.containsKey(word))

								posWordCount.put(word,
										posWordCount.get(word) + 1);
							else {
								posWordCount.put(word, 1);
							}
							if (previousWord != null) {
								String bigram = previousWord + " " + word;

								if (bigramTotalWordCount.containsKey(word))
									bigramTotalWordCount
											.put(bigram, bigramTotalWordCount
													.get(bigram) + 1);
								else {
									bigramTotalWordCount.put(bigram, 1);
								}
								if (bigramPosWordCount.containsKey(bigram))

									bigramPosWordCount.put(bigram,
											bigramPosWordCount.get(bigram) + 1);
								else {
									bigramPosWordCount.put(bigram, 1);
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

			HashMap<String, Double> negProbability = new HashMap<String, Double>();
			Integer uniqueWordCount = bigramTotalWordCount.size();
			int totalWordCountInNegClass = 0;
			for (Integer wordCount : negWordCount.values()) {
				totalWordCountInNegClass = wordCount + totalWordCountInNegClass;
			}

			for (Map.Entry<String, Integer> negWordCountEntry : bigramNegWordCount
					.entrySet()) {

				Integer negWordCountPerClass = negWordCountEntry.getValue();
				String unigram = negWordCountEntry.getKey().split(" ")[0];

				Double probability = ((double) (negWordCountPerClass + 1) / (negWordCount
						.get(unigram) + uniqueWordCount));

				negProbability.put(negWordCountEntry.getKey(), probability);

			}

			HashMap<String, Double> posProbability = new HashMap<String, Double>();

			for (Map.Entry<String, Integer> posWordCountEntry : bigramPosWordCount
					.entrySet()) {
				String unigram = posWordCountEntry.getKey().split(" ")[0];
				Integer posWordCountPerClass = posWordCountEntry.getValue();

				Double probability = ((double) (posWordCountPerClass + 1) / (posWordCount
						.get(unigram) + uniqueWordCount));

				posProbability.put(posWordCountEntry.getKey(), probability);

			}

			ArrayList<Double> posDocProbability = new ArrayList<Double>();
			ArrayList<Double> negDocProbability = new ArrayList<Double>();
			ArrayList<Double> PresencePosDocProbability = new ArrayList<Double>();
			ArrayList<Double> PresenceNegDocProbability = new ArrayList<Double>();

			HashMap<String, Integer> bigramTestTotalWordCount = new HashMap<String, Integer>();

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

								if (bigramTestTotalWordCount
										.containsKey(bigram))

									bigramTestTotalWordCount.put(bigram,
											bigramTestTotalWordCount
													.get(bigram) + 1);
								else {
									bigramTestTotalWordCount.put(bigram, 1);
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

								// previousWord=word;
								if (bigramTestTotalWordCount
										.containsKey(bigram))
									bigramTestTotalWordCount.put(bigram,
											bigramTestTotalWordCount
													.get(bigram) + 1);
								else {
									bigramTestTotalWordCount.put(bigram, 1);
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
						// System.out.println(file.getName());
						bigramTestArrayHashMaps.add(documentWordCount);
					}
				}
			}
			ArrayList<Double> testPosDocProbability = new ArrayList<Double>();
			ArrayList<Double> testNegDocProbability = new ArrayList<Double>();
			ArrayList<Double> testPresencePosDocProbability = new ArrayList<Double>();
			ArrayList<Double> testPresenceNegDocProbability = new ArrayList<Double>();
			for (HashMap<String, Integer> documentHashMap : bigramTestArrayHashMaps) {
				Double testposSum = (double) 0;
				Double testposSumPresence = (double) 0;
				Double testnegSum = (double) 0;
				Double testnegSumPresence = (double) 0;

				int TestTotalWordCount = 0;
				for (Integer wordCount : documentHashMap.values()) {
					TestTotalWordCount = wordCount + TestTotalWordCount;
				}

				for (Map.Entry<String, Integer> wordCountPerDocumentEntry : documentHashMap
						.entrySet()) {
					if (posProbability.containsKey(wordCountPerDocumentEntry
							.getKey())) {
						testposSum += -((double) 1 / TestTotalWordCount)
								* (wordCountPerDocumentEntry.getValue())
								* (Math.log(posProbability
										.get(wordCountPerDocumentEntry.getKey())));
						testposSumPresence += -((double) 1 / documentHashMap
								.size())
								* Math.log(posProbability
										.get(wordCountPerDocumentEntry.getKey()));
					}

					else {
						String unigram = wordCountPerDocumentEntry.getKey()
								.split(" ")[0];
						if (posWordCount.containsKey(unigram)) {
							testposSum += -((double) 1 / TestTotalWordCount)
									* (wordCountPerDocumentEntry.getValue())
									* (Math.log((double) 1
											/ (posWordCount.get(unigram) + uniqueWordCount)));
							testposSumPresence += -((double) 1 / documentHashMap
									.size())
									* (Math.log((double) 1
											/ (posWordCount.get(unigram) + uniqueWordCount)));
						} else {
							testposSum += -((double) 1 / TestTotalWordCount)
									* (wordCountPerDocumentEntry.getValue())
									* (Math.log((double) 1 / uniqueWordCount));
							testposSumPresence += -((double) 1 / documentHashMap
									.size())
									* (Math.log((double) 1 / uniqueWordCount));

						}
					}

					if (negProbability.containsKey(wordCountPerDocumentEntry
							.getKey())) {
						testnegSum += -((double) 1 / TestTotalWordCount)
								* (wordCountPerDocumentEntry.getValue())
								* (Math.log(negProbability
										.get(wordCountPerDocumentEntry.getKey())));
						testnegSumPresence += -((double) 1 / documentHashMap
								.size())
								* (Math.log(negProbability
										.get(wordCountPerDocumentEntry.getKey())));
					}

					else {
						String unigram = wordCountPerDocumentEntry.getKey()
								.split(" ")[0];
						if (negWordCount.containsKey(unigram)) {
							testnegSum += -((double) 1 / TestTotalWordCount)
									* (wordCountPerDocumentEntry.getValue())
									* (Math.log((double) 1
											/ (negWordCount.get(unigram) + uniqueWordCount)));
							testnegSumPresence += -((double) 1 / documentHashMap
									.size())
									* (Math.log((double) 1
											/ (negWordCount.get(unigram) + uniqueWordCount)));
						} else {
							testnegSum += -((double) 1 / TestTotalWordCount)
									* (wordCountPerDocumentEntry.getValue())
									* (Math.log((double) 1 / (uniqueWordCount)));
							testnegSumPresence += -((double) 1 / documentHashMap
									.size())
									* (Math.log((double) 1 / (uniqueWordCount)));
						}
					}
				}
				// System.out.println("Psum" + testposSum);
				// System.out.println("nsum" + testnegSum);
				testPosDocProbability.add(testposSum);
				testNegDocProbability.add(testnegSum);
				testPresencePosDocProbability.add(testposSumPresence);
				testPresenceNegDocProbability.add(testnegSumPresence);
				// System.out.println("Psum" + testposSumPresence);
				// System.out.println("Nsum" + testnegSumPresence);

			}
			// System.out.println("PCount"+testPresencePosDocProbability.size());
			// System.out.println("NCount"+testPresenceNegDocProbability.size());
			// System.out.println("arraylist"+testArrayHashMaps.size());
			// System.out.println("PCount"+testPosDocProbability.toString());
			// System.out.println("NCount"+testNegDocProbability.toString());

			// code for counting errors in classification
			int testErrorCount = 0;
			int testPresenceErrorCount = 0;
			for (int i = 0; i < 200; i++) {
				if (testPosDocProbability.get(i) < testNegDocProbability.get(i)) {
					// System.out.println("freq err no in neg"+ i);
					testErrorCount += 1;
				}
			}

			for (int i = 200; i < 400; i++) {
				if (testPosDocProbability.get(i) > testNegDocProbability.get(i)) {
					testErrorCount += 1;
				}
			}

			for (int i = 0; i < 200; i++) {

				if (testPresencePosDocProbability.get(i) < testPresenceNegDocProbability
						.get(i)) {
					testPresenceErrorCount += 1;
					// System.out.println("pres err no in neg"+ i);
				}
			}

			for (int i = 200; i < 400; i++) {
				if (testPresencePosDocProbability.get(i) > testPresenceNegDocProbability
						.get(i)) {
					testPresenceErrorCount += 1;
				}
			}
			System.out.println("----Trial "+((fold/200)+1)+ " results----");
			//System.out.println("Test Frequency Errors: " + testErrorCount);
			System.out.println("Test Frequency Accuracy: " + (double) (400 - testErrorCount)
					/ 4 + "%");
			sumfreq+=(double) (400 - testErrorCount)/4;
			
		//	System.out.println("TEST PRESENCE ERRORS " + testPresenceErrorCount);
			System.out.println("Test Presence Accuracy: "+(double) (400 - testPresenceErrorCount)
					/ 4 + "%");
			sumpresence+=(double) (400 - testPresenceErrorCount)/4;
			System.out.println("-----------------------");
			System.out.println("");
		}
		System.out.println("Average Frequency Accuracy: "+ sumfreq/5+"%");
		System.out.println("Average Presence Accuracy: "+ sumpresence/5+"%");

	}
}
