//package org.sbu.nlp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

public class GRETrigramClassifier {
	private static Scanner reader;
	private static Scanner reader2;
	private static Scanner reader3;
	private static Scanner reader4;

	public static void main(String[] args) throws FileNotFoundException {

		// for (int fold = 0; fold <= 800; fold += 200) {
		// Integer testDocStartNo = fold;
		// Integer testDocEndNo = fold + 200;
		File folder = new File("Holmes_Training_Data");
		File[] listOfFiles = folder.listFiles();
		HashMap<String, Integer> unigramWordCount = new HashMap<String, Integer>();
		HashMap<String, Integer> bigramTotalWordCount = new HashMap<String, Integer>();
		HashMap<String, Integer> trigramTotalWordCount = new HashMap<String, Integer>();
		for (File file : listOfFiles) {
			HashMap<String, Integer> documentWordCount = new HashMap<String, Integer>();
			if (file.isFile()) {
				reader4 = new Scanner(file);

				while (reader4.hasNextLine()) {

					String line = reader4.nextLine();
					// reader4.useDelimiter("[^A-Za-z]+");
					line = line.replaceAll("[^A-Za-z\\[\\] ] ", "");
					//line= line.replaceAll("\\p{Punct}+\\[\\]", "");
					line = line.replaceAll("\\s+", " ");
					//int targetWordIndex = 0;
					line = line.trim();
					String[] words = line.split(" ");
					for (int i = 0; i < words.length; i++ ){
						if (unigramWordCount.containsKey(words[i]))

							unigramWordCount.put(words[i],
									unigramWordCount.get(words[i]) + 1);
						else {
							unigramWordCount.put(words[i], 1);
						}
						
						if (i < words.length - 1){
						String bigram = words[i] + " " + words[i+1];

						if (bigramTotalWordCount.containsKey(bigram))

							bigramTotalWordCount.put(bigram,
									bigramTotalWordCount.get(bigram) + 1);
						else {
							bigramTotalWordCount.put(bigram, 1);
						}
						}
						if (i < words.length - 2){
							String trigram = words[i] + " " + words[i+1]+" "+words[i+2];

							if (trigramTotalWordCount.containsKey(trigram))

								trigramTotalWordCount.put(trigram,
										trigramTotalWordCount.get(trigram) + 1);
							else {
								trigramTotalWordCount.put(trigram, 1);
							}
							}
						
					}
					
				
					// bigramArrayHashMaps.add(documentWordCount);

				}
			}
		}
		int totalWordCount = 0;
//		Iterator<Map.Entry<String, Integer>> iter1 = unigramWordCount
//				.entrySet().iterator();
//		while (iter1.hasNext()) {
//			Map.Entry<String, Integer> entry = iter1.next();
//			if (entry.getValue() < 1) {
//				iter1.remove();
//			}
//		}
//		Iterator<Map.Entry<String, Integer>> iter = bigramTotalWordCount
//				.entrySet().iterator();
//		while (iter.hasNext()) {
//			Map.Entry<String, Integer> entry = iter.next();
//			if (entry.getValue() < 2) {
//				iter.remove();
//			}
//		}

		for (Integer bigramCount : bigramTotalWordCount.values()) {
			totalWordCount += bigramCount;
		}

		Integer uniqueWordCount = unigramWordCount.size();
		System.out.println("unigramcount"+uniqueWordCount);
		System.out.println("bigramcount"+bigramTotalWordCount.size());
		System.out.println("triigramcount"+trigramTotalWordCount.size());
		System.out.println("in testing");
		ArrayList<String> testAnswerArray = new ArrayList<String>();
		HashMap<Integer, String> answerInttoStringMap = new HashMap<Integer, String>();
		answerInttoStringMap.put(1, "a");
		answerInttoStringMap.put(2, "b");
		answerInttoStringMap.put(3, "c");
		answerInttoStringMap.put(4, "d");
		answerInttoStringMap.put(5, "e");
		folder = new File("testing_data/Holmes.machine_format.questions.txt");

		if (folder.isFile()) {

			reader2 = new Scanner(folder);
			int answerNumber = 1;
			int maxAnswerNumber = 0;
			double maxSentenceProbability = 0;
			int lineno=0;
			while (reader2.hasNextLine()) {

				if (answerNumber == 6) {
					testAnswerArray.add(answerInttoStringMap
							.get(maxAnswerNumber));
					if (testAnswerArray.get(testAnswerArray.size()-1)==null){
						System.out.println(maxSentenceProbability);
					}
					answerNumber = 1;
					maxSentenceProbability = 0;
					maxAnswerNumber = 0;
					//System.out.println(answerInttoStringMap.get(maxAnswerNumber));
					
				}
				String line = reader2.nextLine();
				lineno++;
				
				line = line.substring(line.indexOf(")") + 1, line.length());
				line = line.replaceAll("[^A-Za-z\\[\\] ] ", "");
				//line= line.replaceAll("\\p{Punct}+\\[\\]", "");
				line = line.replaceAll("\\s+", " ");
				line = line.trim();
				int targetWordIndex = 0;
				String[] words = line.split(" ");
				for (int i = 0; i < words.length; i++ ){
					if(words[i].startsWith("[")){
						targetWordIndex=i;
					}
				}
				line = line.replaceAll("[^A-Za-z ]", "");
				//line = line.replaceAll("\\s+", " ");
				// reader4.useDelimiter("[^A-Za-z]+");
				double sentenceProbability = 1;
				words = line.split(" ");
				//String previousWord = null;

			 
					String backwardbigram = null;
					if (targetWordIndex > 0) {
						backwardbigram = words[targetWordIndex-1] + " " + words[targetWordIndex];
					}
					String forwardbigram = null;
					if (targetWordIndex < words.length - 1) {
						forwardbigram = words[targetWordIndex] + " " + words[targetWordIndex +1];
					}
					String forwardtrigram1 = null;
					String forwardtrigram2 = null;
					String backwardtrigram1 = null;
					String backwardtrigram2 = null;
					String fwdbwdtrigram1 = null;
					String fwdbwdtrigram2 = null;
					double backwardbigramProbability = 0;
					double forwardbigramProbability = 0;
					double backwardtrigram1Probability = 0;
					double backwardtrigram2Probability = 0;
					double forwardtrigram1Probability = 0;
					double forwardtrigram2Probability = 0;
					double fwdbwdtrigram1Probability = 0;
					double fwdbwdtrigram2Probability = 0;
//					backwardtrigram2Probability = (double) 1
//							/ uniqueWordCount;
//					backwardtrigram1Probability = (double) 1
//							/ uniqueWordCount;
//					forwardtrigram2Probability = (double) 1
//							/ uniqueWordCount;
//					forwardtrigram1Probability = (double) 1
//							/ uniqueWordCount;


					if ((targetWordIndex-2) >0&& words[targetWordIndex-2] != null) {
						backwardtrigram2= words[targetWordIndex-2]+" " + words[targetWordIndex-1]+ " "+ words[targetWordIndex];
						if (unigramWordCount.containsKey(words[targetWordIndex-2])) {
							if (trigramTotalWordCount
									.containsKey(backwardtrigram2)) {
								backwardtrigram2Probability = (double) (trigramTotalWordCount
										.get(backwardtrigram2) + 1)
										/ (unigramWordCount.get(words[targetWordIndex-2])
										+ uniqueWordCount);
							} else {
								backwardtrigram2Probability = (double) 1
										/ (unigramWordCount.get(words[targetWordIndex-2])
										+ uniqueWordCount);

							}
						} else {
							if (trigramTotalWordCount
									.containsKey(backwardtrigram2)) {
								backwardtrigram2Probability = (double) (trigramTotalWordCount
										.get(backwardtrigram2) + 1)
										/ uniqueWordCount;
							} else {
								backwardtrigram2Probability = (double) 1
										/ uniqueWordCount;

							}
						}
					}
					if ((targetWordIndex-2) >0&& words[targetWordIndex-2] != null) {
						backwardtrigram1= words[targetWordIndex-2]+" " + words[targetWordIndex-1]+ " "+ words[targetWordIndex];
						if (bigramTotalWordCount.containsKey(words[targetWordIndex-2]+" " + words[targetWordIndex-1])) {
							if (trigramTotalWordCount
									.containsKey(backwardtrigram1)) {
								backwardtrigram1Probability = (double) (trigramTotalWordCount
										.get(backwardtrigram1) + 1)
										/ (bigramTotalWordCount.get(words[targetWordIndex-2]+" " + words[targetWordIndex-1])
										+ uniqueWordCount);
							} else {
								backwardtrigram1Probability = (double) 1
										/ (bigramTotalWordCount.get(words[targetWordIndex-2]+" " + words[targetWordIndex-1])
										+ uniqueWordCount);

							}
						} else {
							if (trigramTotalWordCount
									.containsKey(backwardtrigram1)) {
								backwardtrigram1Probability = (double) (trigramTotalWordCount
										.get(backwardtrigram1) + 1)
										/ uniqueWordCount;
							} else {
								backwardtrigram1Probability = (double) 1
										/ uniqueWordCount;

							}
						}
					}

					if ((targetWordIndex+2) <words.length&& words[targetWordIndex+2] != null) {
						forwardtrigram2= words[targetWordIndex]+" "+ words[targetWordIndex+1]+" " + words[targetWordIndex+2];
						if (unigramWordCount.containsKey(words[targetWordIndex+2])) {
							if (trigramTotalWordCount
									.containsKey(forwardtrigram2)) {
								forwardtrigram2Probability = (double) (trigramTotalWordCount
										.get(forwardtrigram2) + 1)
										/ (unigramWordCount.get(words[targetWordIndex+2])
										+ uniqueWordCount);
							} else {
								forwardtrigram2Probability = (double) 1
										/ (unigramWordCount.get(words[targetWordIndex+2])
										+ uniqueWordCount);

							}
						} else {
							if (trigramTotalWordCount
									.containsKey(forwardtrigram2)) {
								forwardtrigram2Probability = (double) (trigramTotalWordCount
										.get(forwardtrigram2) + 1)
										/ uniqueWordCount;
							} else {
								forwardtrigram2Probability = (double) 1
										/ uniqueWordCount;

							}
						}
					}
					if ((targetWordIndex+2) <words.length&& words[targetWordIndex+2] != null) {
						forwardtrigram1 = words[targetWordIndex]+" "+ words[targetWordIndex+1]+" " + words[targetWordIndex+2];
						if (bigramTotalWordCount.containsKey(words[targetWordIndex+1]+" " + words[targetWordIndex+2])) {
							if (trigramTotalWordCount
									.containsKey(forwardtrigram1)) {
								forwardtrigram1Probability = (double) (trigramTotalWordCount
										.get(forwardtrigram1) + 1)
										/ (bigramTotalWordCount.get(words[targetWordIndex+1]+" " + words[targetWordIndex+2])
										+ uniqueWordCount);
							} else {
								forwardtrigram1Probability = (double) 1
										/ (bigramTotalWordCount.get(words[targetWordIndex+1]+" " + words[targetWordIndex+2])
										+ uniqueWordCount);

							}
						} else {
							if (trigramTotalWordCount
									.containsKey(forwardtrigram1)) {
								forwardtrigram1Probability = (double) (trigramTotalWordCount
										.get(forwardtrigram1) + 1)
										/ uniqueWordCount;
							} else {
								forwardtrigram1Probability = (double) 1
										/ uniqueWordCount;

							}
						}
					}
					
					if ((targetWordIndex-1) >0&& words[targetWordIndex-1] != null&&(targetWordIndex+1) <words.length&& words[targetWordIndex+1] != null) {
						fwdbwdtrigram2= words[targetWordIndex-1]+" " + words[targetWordIndex]+ " "+ words[targetWordIndex+1];
						if (unigramWordCount.containsKey(words[targetWordIndex-1])) {
							if (trigramTotalWordCount
									.containsKey(fwdbwdtrigram2)) {
								fwdbwdtrigram2Probability = (double) (trigramTotalWordCount
										.get(fwdbwdtrigram2) + 1)
										/ (unigramWordCount.get(words[targetWordIndex-1])
										+ uniqueWordCount);
							} else {
								fwdbwdtrigram2Probability = (double) 1
										/ (unigramWordCount.get(words[targetWordIndex-1])
										+ uniqueWordCount);

							}
						} else {
							if (trigramTotalWordCount
									.containsKey(fwdbwdtrigram2)) {
								fwdbwdtrigram2Probability = (double) (trigramTotalWordCount
										.get(fwdbwdtrigram2) + 1)
										/ uniqueWordCount;
							} else {
								fwdbwdtrigram2Probability = (double) 1
										/ uniqueWordCount;

							}
						}
					}
					if ((targetWordIndex-1) >0&& words[targetWordIndex-1] != null&&(targetWordIndex+1) <words.length&& words[targetWordIndex+1] != null) {
						fwdbwdtrigram1= words[targetWordIndex-1]+" " + words[targetWordIndex]+ " "+ words[targetWordIndex+1];
						if (unigramWordCount.containsKey(words[targetWordIndex+1])) {
							if (trigramTotalWordCount
									.containsKey(fwdbwdtrigram1)) {
								fwdbwdtrigram1Probability = (double) (trigramTotalWordCount
										.get(fwdbwdtrigram1) + 1)
										/ (unigramWordCount.get(words[targetWordIndex+1])
										+ uniqueWordCount);
							} else {
								fwdbwdtrigram1Probability = (double) 1
										/ (unigramWordCount.get(words[targetWordIndex+1])
										+ uniqueWordCount);

							}
						} else {
							if (trigramTotalWordCount
									.containsKey(fwdbwdtrigram1)) {
								fwdbwdtrigram1Probability = (double) (trigramTotalWordCount
										.get(fwdbwdtrigram1) + 1)
										/ uniqueWordCount;
							} else {
								fwdbwdtrigram1Probability = (double) 1
										/ uniqueWordCount;

							}
						}
					}
//					if (targetWordIndex < words.length - 1 && unigramWordCount.containsKey(words[targetWordIndex+1])
//							&& forwardbigram != null) {
//						if (bigramTotalWordCount.containsKey(forwardbigram)) {
//							forwardbigramProbability = (double) (bigramTotalWordCount
//									.get(forwardbigram) + 1)
//									/ (unigramWordCount.get(words[targetWordIndex+1])
//									+ uniqueWordCount);
//						} else {
//							forwardbigramProbability = (double) 1
//									/ (unigramWordCount.get(words[targetWordIndex+1])
//									+ uniqueWordCount);
//
//						}
//					} else {
//						if (bigramTotalWordCount.containsKey(forwardbigram)) {
//							forwardbigramProbability = (double) (bigramTotalWordCount
//									.get(forwardbigram) + 1) / uniqueWordCount;
//						} else {
//							forwardbigramProbability = (double) 1
//									/ uniqueWordCount;
//
//						}
//					}
//					if (targetWordIndex >0&& words[targetWordIndex-1] != null) {
//						if (unigramWordCount.containsKey(words[targetWordIndex-1])
//								&& backwardbigram != null) {
//							if (bigramTotalWordCount
//									.containsKey(backwardbigram)) {
//								backwardbigramProbability = (double) (bigramTotalWordCount
//										.get(backwardbigram) + 1)
//										/ (unigramWordCount.get(words[targetWordIndex-1])
//										+ uniqueWordCount);
//							} else {
//								backwardbigramProbability = (double) 1
//										/ (unigramWordCount.get(words[targetWordIndex-1])
//										+ uniqueWordCount);
//
//							}
//						} else {
//							if (bigramTotalWordCount
//									.containsKey(backwardbigram)) {
//								backwardbigramProbability = (double) (bigramTotalWordCount
//										.get(backwardbigram) + 1)
//										/ uniqueWordCount;
//							} else {
//								backwardbigramProbability = (double) 1
//										/ uniqueWordCount;
//
//							}
//						}
//					}

//					if (targetWordIndex < words.length - 1 && unigramWordCount.containsKey(words[targetWordIndex+1])
//							&& forwardbigram != null) {
//						if (bigramTotalWordCount.containsKey(forwardbigram)) {
//							forwardbigramProbability = (double) (bigramTotalWordCount
//									.get(forwardbigram) + 1)
//									/ (unigramWordCount.get(words[targetWordIndex+1])
//									+ uniqueWordCount);
//						} else {
//							forwardbigramProbability = (double) 1
//									/ (unigramWordCount.get(words[targetWordIndex+1])
//									+ uniqueWordCount);
//
//						}
//					} else {
//						if (bigramTotalWordCount.containsKey(forwardbigram)) {
//							forwardbigramProbability = (double) (bigramTotalWordCount
//									.get(forwardbigram) + 1) / uniqueWordCount;
//						} else {
//							forwardbigramProbability = (double) 1
//									/ uniqueWordCount;
//
//						}
//					}
				//	sentenceProbability = (backwardbigramProbability + forwardbigramProbability);
					sentenceProbability = (backwardtrigram1Probability+backwardtrigram2Probability+fwdbwdtrigram1Probability+fwdbwdtrigram2Probability+forwardtrigram1Probability+forwardtrigram2Probability );
					

				
				if (maxSentenceProbability < sentenceProbability) {
					maxSentenceProbability = sentenceProbability;

					maxAnswerNumber = answerNumber;
					// System.out.println(maxAnswerNumber);
				}
			//	System.out.println(sentenceProbability);

				answerNumber++;
			}
			testAnswerArray.add(answerInttoStringMap.get(maxAnswerNumber));
			
		}

		ArrayList<String> testActualAnswerArray = new ArrayList<String>();
		// answer in testing
		folder = new File("testing_data/Holmes.machine_format.answers.txt");

		// HashMap<String, Integer> testDocumentWordCount = new HashMap<String,
		// Integer>();
		if (folder.isFile()) {

			reader3 = new Scanner(folder);
			int answerNumber = 1;
			while (reader3.hasNextLine()) {
				double maxSentenceProbability = 0;

				String line = reader3.nextLine();
				line = line.substring(line.indexOf(")") - 1, line.indexOf(")"));
				testActualAnswerArray.add(line);

			}
		}

		// code for counting errors in classification
		System.out.println("testanswersd " + testAnswerArray.toString());
		System.out.println("testanswerssize " + testAnswerArray.size());
		System.out.println("testActualanswers "
				+ testActualAnswerArray.toString());
		System.out.println("testActualanswerssize "
				+ testActualAnswerArray.size());
		int testErrorCount = 0;
		int testPresenceErrorCount = 0;
		for (int i = 0; i < testActualAnswerArray.size(); i++) {
			if (!testAnswerArray.get(i).equalsIgnoreCase(
					testActualAnswerArray.get(i))) {
				// System.out.println("freq err no in neg"+ i);
				testErrorCount += 1;
			}
		}

		// System.out.println("----Trial " + ((fold / 200) + 1)
		// + " results----");
		// System.out.println("Test Frequency Errors: " + testErrorCount);
		System.out.println("Test Answer Prediction Accuracy: "
				+ (double) (testActualAnswerArray.size() - testErrorCount)
				* 100 / testActualAnswerArray.size() + "%");

		System.out.println("-----------------------");
		System.out.println("");
		// }
		// System.out.println("Average Frequency Accuracy: " + sumfreq / 5 +
		// "%");
		// System.out.println("Average Presence Accuracy: " + sumpresence / 5
		// + "%");

	}
}
