package hownet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Replace {

	public static void main(String[] args) {
//		String pathinput = "C:/Users/lin/Desktop/myemotion/testData10000-weight.fenci.xml";
//		String pathoutput = "C:/Users/lin/Desktop/myemotion/testData10000-weight.feature.arff";

		String pathinput = "C:/Users/lin/Desktop/myemotion/trainData4000-weight.fenci.xml";
		String pathoutput = "C:/Users/lin/Desktop/myemotion/trainData4000-weight.feature.arff";
		
		String pathunigram = "C:/Users/lin/Desktop/myemotion/unigram.data";

		File file = new File(pathinput);
		File unigramfile = new File(pathunigram);
		BufferedReader reader = null;
		BufferedReader readerunigram = null;

		List<String> unigramlist = new ArrayList<String>();
		try {
			Pattern patweibo = Pattern
					.compile("(<weibo id=\")([0-9]*)(\" emotion-type1=\")(.*)(\" emotion-type2=\")(.*)(\">)");
			// Pattern patweibo = Pattern
			// .compile("(<weibo id=\")([0-9]*)(\" emotion-type1=\")(.*)(\" emotion-type2=\")(.*)(\">)");

			Pattern patsentence = Pattern
					.compile("(<sentence id=\"[0-9]*\" .*>)(.*)(</sentence>)");
			reader = new BufferedReader(new FileReader(file));
			readerunigram = new BufferedReader(new FileReader(unigramfile));

			String tempString = null;

			// unigram添加到List
			while ((tempString = readerunigram.readLine()) != null) {
				unigramlist.add(tempString);
			}

			String polarity = "";// String polarity

			OutputStreamWriter writer = new OutputStreamWriter(
					new FileOutputStream(pathoutput), "UTF-8");
			writer.write("@relation emotion\r\n\r\n");
			for(int i=0;i<unigramlist.size();i++){
				writer.write("@attribute f"+(i+1)+" {0,1}\r\n");
			}
			writer.write("@attribute emotion {happiness,like,anger,sadness,fear,disgust,surprise,none}\r\n");

			writer.write("\r\n@data\r\n");
			int num = 0;

			String weiboid = "";
			String emotiontype1 = "none";
			String emotiontype2 = "none";
			String text = "";
			while ((tempString = reader.readLine()) != null) {

				// find weiboid
				Matcher matcherweibo = patweibo.matcher(tempString);
				if (matcherweibo.find()) {
					text = "";
					weiboid = matcherweibo.group(2);
					emotiontype1 = matcherweibo.group(4);
					emotiontype2 = matcherweibo.group(6);
				}

				// find sentence
				Matcher matchersentence = patsentence.matcher(tempString);
				if (matchersentence.find()) {
					text += matchersentence.group(2);
					num++;
				}

				if (tempString.contains("</weibo>")) {
					String featureresult = "";
					int unigramnum[] = new int[unigramlist.size()];
					// String thetext = "";
					String wordlist[] = text.split(" ");
					for (int i = 0; i < wordlist.length; i++) {
						String temp[] = wordlist[i].split("/");
						if (temp.length == 2) {
							String name = temp[0];
							String pos = temp[1];

							// System.out.println(wordlist[i]);
							// unigram-feature extraction
							for (int u = 0; u < unigramlist.size(); u++) {
								if (name.equals(unigramlist.get(u))) {
									unigramnum[u] = 1;
									break;
								}
							}

							// thetext += name;
							// System.out.println(thetext);

						}

					}

					for (int n = 0; n < unigramnum.length; n++) {
						if (unigramnum[n] == 1) {
							featureresult += (n ) + " 1, ";
						}
					}
					
					if (featureresult.trim().equals("")) {
						// writer.write(weiboid + "\t" + emotiontype1 + "\t"
						// + emotiontype2 + "\t1:0 \r\n");
//						writer.write(emotiontype1.replace("happiness", "1")
//								.replace("like", "2").replace("anger", "3")
//								.replace("sadness", "4").replace("fear", "5")
//								.replace("disgust", "6")
//								.replace("surprise", "7").replace("none", "8")
//								+ "\t1:0 \r\n");
						writer.write("{8756 "+emotiontype1 + "}\r\n");
					} else {
//						writer.write(emotiontype1.replace("happiness", "1")
//								.replace("like", "2").replace("anger", "3")
//								.replace("sadness", "4").replace("fear", "5")
//								.replace("disgust", "6")
//								.replace("surprise", "7").replace("none", "8")
//								+ "\t" + featureresult + "\r\n");
						writer.write("{"+featureresult +"8756 "+emotiontype1 + "}\r\n");
					}
				}
			}
			System.out.println(num);
			reader.close();
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}

	}

}
