//package org.sbu.nlp;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class GREBackwardBigramModelUsingMSWebService {
	private static Scanner reader;
	private static Scanner reader2;
	private static Scanner reader3;
	private static Scanner reader4;

	public static void main(String[] args) throws MalformedURLException,
			ProtocolException {

		File folder = new File("Holmes_Training_Data");
		File[] listOfFiles = folder.listFiles();
		HashMap<String, Integer> unigramWordCount = new HashMap<String, Integer>();
		HashMap<String, Integer> bigramTotalWordCount = new HashMap<String, Integer>();

		System.out.println("in testing");

		ArrayList<String> testAnswerArray = new ArrayList<String>();
		ArrayList<String> bigramArray = new ArrayList<String>();
		HashMap<Integer, String> answerInttoStringMap = new HashMap<Integer, String>();
		answerInttoStringMap.put(1, "a");
		answerInttoStringMap.put(2, "b");
		answerInttoStringMap.put(3, "c");
		answerInttoStringMap.put(4, "d");
		answerInttoStringMap.put(5, "e");
		folder = new File("testing_data/Holmes.machine_format.questions.txt");

		int optionCount = 1;
		// int answerNumber = 1;
		int maxAnswerNumber = 0;

		double maxSentenceProbability = 0;

		if (folder.isFile()) {

			try {
				reader2 = new Scanner(folder);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String s = "http://weblm.research.microsoft.com/weblm/rest.svc/bing-body/apr10/4/cp?u=d7c5b729-bf71-4a13-94d3-f2d6e8238ca6";
			URL url = new URL(s);
			HttpURLConnection conn = null;
			try {
				conn = (HttpURLConnection) url.openConnection();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");

			conn.setDoOutput(true);
			int lineno = 0;
			while (reader2.hasNextLine()) {
				if (optionCount == 6) {
					if (maxAnswerNumber == 0)
						maxAnswerNumber = 1;
					testAnswerArray.add(answerInttoStringMap
							.get(maxAnswerNumber));
					optionCount = 1;
					maxSentenceProbability = 0;
//					System.out.println(answerInttoStringMap
//							.get(maxAnswerNumber));
					maxAnswerNumber = 0;

				}
				//
				lineno++;
//				System.out.println(lineno);
				String line = reader2.nextLine();
				// String line2=line;
				line = line.substring(line.indexOf(")") + 1, line.length());
				line = line.replaceAll("[^A-Za-z\\[\\] ] ", "");
				// line= line.replaceAll("\\p{Punct}+\\[\\]", "");
				line = line.replaceAll("\\s+", " ");
				line = line.trim();
				int targetWordIndex = 0;
				String[] words = line.split(" ");
				for (int i = 0; i < words.length; i++) {
					if (words[i].startsWith("[")) {
						// System.out.println(words[i]);
						targetWordIndex = i;
					}
				}
				line = line.replaceAll("[^A-Za-z ]", "");
				words = line.split(" ");

				String backwardbigram = null;
				String backwardtrigram = null;
				String backwardfourgram = null;
				if ((targetWordIndex - 1) > 0) {
					backwardbigram = words[targetWordIndex - 1] + " "
							+ words[targetWordIndex];
				}
				if ((targetWordIndex - 2) > 0) {
					backwardtrigram = words[targetWordIndex - 2] + " "
							+ words[targetWordIndex - 1] + " "
							+ words[targetWordIndex];
				}
				if ((targetWordIndex - 3) > 0) {
					backwardfourgram = words[targetWordIndex - 3] + " "
							+ words[targetWordIndex - 2] + " "
							+ words[targetWordIndex - 1] + " "
							+ words[targetWordIndex];
				}

				String perSentenceArray = null;
				perSentenceArray = backwardbigram;
				perSentenceArray = perSentenceArray + "\n" + backwardtrigram;
				perSentenceArray = perSentenceArray + "\n" + backwardfourgram;

				try {

					Map<String, Object> params = new LinkedHashMap<>();
					params.put("p", perSentenceArray);
					StringBuilder postData = new StringBuilder();
					for (Map.Entry<String, Object> param : params.entrySet()) {
						if (postData.length() != 0)
							postData.append('&');
						postData.append(param.getKey());
						postData.append('=');
						postData.append(param.getValue());
					}

					byte[] postDataBytes = postData.toString()
							.getBytes("UTF-8");
					conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("POST");
					conn.setRequestProperty("Content-Type",
							"application/x-www-form-urlencoded");
					conn.setRequestProperty("Content-Length",
							String.valueOf(postDataBytes.length));
					conn.setDoOutput(true);
					conn.getOutputStream().write(postDataBytes);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					conn.disconnect();
				}

				DataInputStream input = null;
				String str = null;
				List<Double> probabilities = new ArrayList<Double>();
				try {
					input = new DataInputStream(conn.getInputStream());
					while (null != ((str = input.readLine()))) {
						probabilities.add(Double.parseDouble(str));
						// System.out.println(str);
					}
					input.close();
				} catch (IOException ex) {
					
					ex.printStackTrace();
				}

				// System.out.println(probabilities);
				double sentenceProbability = 0;

				sentenceProbability = Math.pow(10, probabilities.get(0));// +
																			// 3*trigramprobabiltiessum+bigramprobabiltiessum;

				if (maxSentenceProbability < sentenceProbability) {
					maxSentenceProbability = sentenceProbability;

					maxAnswerNumber = optionCount;

				}

				// System.out.println(sentenceProbability);

				optionCount++;
			}
			testAnswerArray.add(answerInttoStringMap.get(maxAnswerNumber));

		}

		ArrayList<String> testActualAnswerArray = new ArrayList<String>();
		// answer in testing
		folder = new File("testing_data/Holmes.machine_format.answers.txt");

		if (folder.isFile()) {

			try {
				reader3 = new Scanner(folder);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			while (reader3.hasNextLine()) {

				String line = reader3.nextLine();
				line = line.substring(line.indexOf(")") - 1, line.indexOf(")"));
				testActualAnswerArray.add(line);

			}
		}

		// code for counting errors in classification
//		System.out.println("testanswers " + testAnswerArray.toString());
//		System.out.println("testanswerssize " + testAnswerArray.size());
//		System.out.println("testActualanswers "
//				+ testActualAnswerArray.toString());
//		System.out.println("testActualanswerssize "
//				+ testActualAnswerArray.size());
		int testErrorCount = 0;
		int testPresenceErrorCount = 0;
		for (int i = 0; i < (testActualAnswerArray.size()); i++) {
			if (!testAnswerArray.get(i).equalsIgnoreCase(
					testActualAnswerArray.get(i))) {
				
				testErrorCount += 1;
			}
		}

		System.out.println("Test Answer Prediction Accuracy: "
				+ (double) (testActualAnswerArray.size() - testErrorCount)
				* 100 / testActualAnswerArray.size() + "%");

		System.out.println("-----------------------");
		System.out.println("");

	}

}
