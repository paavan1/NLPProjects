

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class UnigramClassifier {
	private static Scanner reader;
	private static Scanner reader2;
	private static Scanner reader3;
	private static Scanner reader4;

	public static void main(String[] args) throws FileNotFoundException {
		 double averagefreqaccuracy=0;
         double averagepresaccuracy=0;
          double sumfreq=0;
          double sumpresence=0;
		for ( int fold=0 ;fold<=800;fold+=200){
			Integer testDocStartNo= fold; 
			Integer testDocEndNo=fold+200;
		File folder = new File("neg");
		File[] neglistOfFiles = folder.listFiles();
		HashMap<String, Integer> totalWordCount = new HashMap<String, Integer>();
		HashMap<String, Integer> negWordCount = new HashMap<String, Integer>();
		HashMap<String, Integer> posWordCount = new HashMap<String, Integer>();
		List<HashMap<String, Integer>> arrayHashMaps = new ArrayList<HashMap<String, Integer>>();

		for (File file : neglistOfFiles) {
			HashMap<String, Integer> documentWordCount = new HashMap<String, Integer>();
			if (file.isFile()) {
				Integer reviewNo = Integer
						.parseInt(file.getName().split("_")[0].substring(2));
				if (!(reviewNo >= testDocStartNo && reviewNo < testDocEndNo)) {
					reader4 = new Scanner(file);
					reader4.useDelimiter("[^A-Za-z]+");

					while (reader4.hasNext()) {
						String word = reader4.next();

						if (totalWordCount.containsKey(word))

							totalWordCount.put(word,
									totalWordCount.get(word) + 1);
						else {
							totalWordCount.put(word, 1);
						}
						if (negWordCount.containsKey(word))

							negWordCount.put(word, negWordCount.get(word) + 1);
						else {
							negWordCount.put(word, 1);
						}
						if (documentWordCount.containsKey(word))

							documentWordCount.put(word,
									documentWordCount.get(word) + 1);
						else {
							documentWordCount.put(word, 1);
						}
					}
					arrayHashMaps.add(documentWordCount);
				
				}
			}
			
		}
		
		File posfolder = new File("pos");
		File[] poslistOfFiles = posfolder.listFiles();
			
		for (File file : poslistOfFiles) {
			HashMap<String, Integer> documentWordCount = new HashMap<String, Integer>();
			if (file.isFile()) {
				Integer reviewNo = Integer
						.parseInt(file.getName().split("_")[0].substring(2));
				if (!(reviewNo >= testDocStartNo && reviewNo < testDocEndNo)) {
					reader4 = new Scanner(file);
					reader4.useDelimiter("[^A-Za-z]+");
				while (reader4.hasNext()) {
					String word = reader4.next();
					if (totalWordCount.containsKey(word))
						totalWordCount.put(word, totalWordCount.get(word) + 1);
					else {
						totalWordCount.put(word, 1);
					}
					if (posWordCount.containsKey(word))

						posWordCount.put(word, posWordCount.get(word) + 1);
					else {
						posWordCount.put(word, 1);
					}
					if (documentWordCount.containsKey(word))

						documentWordCount.put(word,
								documentWordCount.get(word) + 1);
					else {
						documentWordCount.put(word, 1);
					}

				}
				arrayHashMaps.add(documentWordCount);
				
				
			}
		}
		}
	
		HashMap<String, Double> negProbability = new HashMap<String, Double>();
		Integer uniqueWordCount = totalWordCount.size();
		int totalWordCountInNegClass = 0;
		for (Integer wordCount : negWordCount.values()) {
			totalWordCountInNegClass = wordCount + totalWordCountInNegClass;
		}

		for (Map.Entry<String, Integer> negWordCountEntry : negWordCount
				.entrySet()) {
			Integer negWordCountPerClass = negWordCountEntry.getValue();

			Double probability = ((double) (negWordCountPerClass + 1) / (totalWordCountInNegClass + uniqueWordCount));

			negProbability.put(negWordCountEntry.getKey(), probability);

		}
		
		HashMap<String, Double> posProbability = new HashMap<String, Double>();

		int totalWordCountInPosClass = 0;
		for (Integer wordCount : posWordCount.values()) {
			totalWordCountInPosClass = wordCount + totalWordCountInPosClass;
		}

		for (Map.Entry<String, Integer> posWordCountEntry : posWordCount
				.entrySet()) {
			Integer posWordCountPerClass = posWordCountEntry.getValue();

			Double probability = ((double) (posWordCountPerClass + 1) / (totalWordCountInPosClass + uniqueWordCount));

			posProbability.put(posWordCountEntry.getKey(), probability);

		}
	
		ArrayList<Double> posDocProbability = new ArrayList<Double>();
		ArrayList<Double> negDocProbability = new ArrayList<Double>();
		ArrayList<Double> PresencePosDocProbability= new ArrayList<Double>();
		ArrayList<Double> PresenceNegDocProbability= new ArrayList<Double>();
		for (HashMap<String, Integer> documentHashMap : arrayHashMaps) {
					Double posSum = (double) 0;
					Double negSum = (double) 0;
					Double posSumPresence = (double) 0;
					Double negSumPresence = (double) 0;
					
					int TrainingTotalWordCount = 0;
					for (Integer wordCount : documentHashMap.values()) {
						TrainingTotalWordCount = wordCount + TrainingTotalWordCount;
					}

					for (Map.Entry<String, Integer> wordCountPerDocumentEntry : documentHashMap
							.entrySet()) {
						if (posProbability.containsKey(wordCountPerDocumentEntry
								.getKey()))
							{
							posSum += -((double)1 /TrainingTotalWordCount)*(wordCountPerDocumentEntry.getValue())*( Math.log( posProbability.get(wordCountPerDocumentEntry.getKey())));
						posSumPresence += - ((double)1 /documentHashMap.size())* Math.log( posProbability.get(wordCountPerDocumentEntry.getKey()));
							}
						else
							{
							posSum += -((double)1 /TrainingTotalWordCount)*(wordCountPerDocumentEntry.getValue())*(Math.log( (double)1 / (totalWordCountInPosClass + uniqueWordCount)));
							posSumPresence += -((double)1 /documentHashMap.size())* (Math.log( (double)1 / (totalWordCountInPosClass + uniqueWordCount)));
							}
						
						if (negProbability.containsKey(wordCountPerDocumentEntry
								.getKey()))
							{
							negSum += -((double)1 /TrainingTotalWordCount)*(wordCountPerDocumentEntry.getValue())*( Math.log( negProbability.get(wordCountPerDocumentEntry.getKey())));
						negSumPresence += - ((double)1 /documentHashMap.size())* Math.log( negProbability.get(wordCountPerDocumentEntry.getKey()));
							}
						else
							{
							negSum += -((double)1 /TrainingTotalWordCount)*(wordCountPerDocumentEntry.getValue())*(Math.log( (double)1 / (totalWordCountInNegClass + uniqueWordCount)));
							negSumPresence += -((double)1 /documentHashMap.size())* (Math.log( (double)1 / (totalWordCountInNegClass + uniqueWordCount)));
							}
						
					}
					posDocProbability.add(posSum);
					negDocProbability.add(negSum);
					PresencePosDocProbability.add(posSumPresence);
					PresenceNegDocProbability.add(negSumPresence);
					
				}

				// code for counting errors in classification
		int ErrorCount = 0;
				int PresenceErrorCount = 0;
				for (int i = 0; i < 200; i++) {
					if (posDocProbability.get(i) < negDocProbability.get(i)) {
						 ErrorCount += 1;
					}
				}

				for (int i = 200; i < 400; i++) {
					if (posDocProbability.get(i) > negDocProbability.get(i)) {
						ErrorCount += 1;
					}
				}
				
				for (int i = 0; i < 200; i++) {
			
					if (PresencePosDocProbability.get(i) < PresenceNegDocProbability.get(i)) {
						PresenceErrorCount += 1;
					}
				}

				for (int i = 200; i < 400; i++) {
					if (PresencePosDocProbability.get(i) > PresenceNegDocProbability.get(i)) {
						PresenceErrorCount += 1;
					}
				}
		
		
		// testing


		HashMap<String, Integer> testTotalWordCount = new HashMap<String, Integer>();
	
		List<HashMap<String, Integer>> testArrayHashMaps = new ArrayList<HashMap<String, Integer>>();
		for (File negfile : neglistOfFiles) {
			HashMap<String, Integer> testDocumentWordCount = new HashMap<String, Integer>();
			if (negfile.isFile()) {
				Integer reviewNo = Integer
						.parseInt(negfile.getName().split("_")[0].substring(2));
				if ((reviewNo >= testDocStartNo && reviewNo < testDocEndNo)) {
				reader2 = new Scanner(negfile);
				reader2.useDelimiter("[^A-Za-z]+");

				while (reader2.hasNext()) {
					String word = reader2.next();

					if (testTotalWordCount.containsKey(word))

						testTotalWordCount.put(word,
								testTotalWordCount.get(word) + 1);
					else {
						testTotalWordCount.put(word, 1);
					}

					if (testDocumentWordCount.containsKey(word))

						testDocumentWordCount.put(word,
								testDocumentWordCount.get(word) + 1);
					else {
						testDocumentWordCount.put(word, 1);
					}
				}
				testArrayHashMaps.add(testDocumentWordCount);
			
			}
			
		}
		}
		
		for (File posfile : poslistOfFiles) {
			HashMap<String, Integer> documentWordCount = new HashMap<String, Integer>();
			if (posfile.isFile()) {
				Integer reviewNo = Integer
						.parseInt(posfile.getName().split("_")[0].substring(2));
				if ((reviewNo >= testDocStartNo && reviewNo < testDocEndNo)) {
				reader3 = new Scanner(posfile);
				reader3.useDelimiter("[^A-Za-z]+");

				while (reader3.hasNext()) {
					String word = reader3.next();
					if (testTotalWordCount.containsKey(word))
						testTotalWordCount.put(word,
								testTotalWordCount.get(word) + 1);
					else {
						testTotalWordCount.put(word, 1);
					}

					if (documentWordCount.containsKey(word))

						documentWordCount.put(word,
								documentWordCount.get(word) + 1);
					else {
						documentWordCount.put(word, 1);
					}

				}

				testArrayHashMaps.add(documentWordCount);
			}
		}
		}
		
		ArrayList<Double> testPosDocProbability = new ArrayList<Double>();
		ArrayList<Double> testNegDocProbability = new ArrayList<Double>();
		ArrayList<Double> testPresencePosDocProbability= new ArrayList<Double>();
		ArrayList<Double> testPresenceNegDocProbability= new ArrayList<Double>();
		for (HashMap<String, Integer> documentHashMap : testArrayHashMaps) {
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
						.getKey()))
					{
					testposSum += -((double)1 /TestTotalWordCount)*(wordCountPerDocumentEntry.getValue())*( Math.log( posProbability.get(wordCountPerDocumentEntry.getKey())));
				testposSumPresence += - ((double)1 /documentHashMap.size())*Math.log( posProbability.get(wordCountPerDocumentEntry.getKey()));
					}

				else{
					testposSum += -((double)1 /TestTotalWordCount)*(wordCountPerDocumentEntry.getValue())*(Math.log( (double)1 / (totalWordCountInPosClass + uniqueWordCount)));
					testposSumPresence += -((double)1 /documentHashMap.size())* (Math.log( (double)1 / (totalWordCountInPosClass + uniqueWordCount)));
				}
				
				if (negProbability.containsKey(wordCountPerDocumentEntry
						.getKey()))
					{testnegSum += -((double)1 /TestTotalWordCount)*(wordCountPerDocumentEntry.getValue())*( Math.log( negProbability.get(wordCountPerDocumentEntry.getKey())));
					testnegSumPresence += -((double)1 /documentHashMap.size())* ( Math.log( negProbability.get(wordCountPerDocumentEntry.getKey())));
					}

				else
					{testnegSum += -((double)1 /TestTotalWordCount)*(wordCountPerDocumentEntry.getValue())*(Math.log( (double)1 / (totalWordCountInNegClass + uniqueWordCount)));	
					testnegSumPresence += -((double)1 /documentHashMap.size())* (Math.log( (double)1 / (totalWordCountInNegClass + uniqueWordCount)));
			}
			}
		
			testPosDocProbability.add(testposSum);
			testNegDocProbability.add(testnegSum);
			testPresencePosDocProbability.add(testposSumPresence);
			testPresenceNegDocProbability.add(testnegSumPresence);
	
				
		
		}
		
			

		// code for counting errors in classification
		int testErrorCount = 0;
		int testPresenceErrorCount = 0;
		for (int i = 0; i < 200; i++) {
			if (testPosDocProbability.get(i) < testNegDocProbability.get(i)) {
			//	System.out.println("freq err no in neg"+ i);
				 testErrorCount += 1;
			}
		}

		for (int i = 200; i < 400; i++) {
			if (testPosDocProbability.get(i) > testNegDocProbability.get(i)) {
				testErrorCount += 1;
			}
		}
		
		for (int i = 0; i < 200; i++) {
	
			if (testPresencePosDocProbability.get(i) < testPresenceNegDocProbability.get(i)) {
				testPresenceErrorCount += 1;
			//	System.out.println("pres err no in neg"+ i);
			}
		}

		for (int i = 200; i < 400; i++) {
			if (testPresencePosDocProbability.get(i) > testPresenceNegDocProbability.get(i)) {
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
		

