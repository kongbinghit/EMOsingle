import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FeatureExtractionForWeibo {

	public static void main(String[] args) {
//		String pathinput = "C:/Users/lin/Desktop/myemotion/testData10000-weight.fenci.xml";
//		String pathoutputsvm = "C:/Users/lin/Desktop/myemotion/weibo/testData.feature.libsvm";
//		String pathoutput = "C:/Users/lin/Desktop/myemotion/weibo/testData10000-weight.feature.arff";
//		String pathoutputmulan = "C:/Users/lin/Desktop/myemotion/weibo/testData10000mulan.feature.arff";
//		String pathoutputweibo = "C:/Users/lin/Desktop/myemotion/weibo/testweibo.txt";

		String pathinput = "C:/Users/lin/Desktop/myemotion/trainData4000-weight.fenci.xml";
		String pathoutputsvm = "C:/Users/lin/Desktop/myemotion/weibo/trainData.feature.libsvm";
		String pathoutput = "C:/Users/lin/Desktop/myemotion/weibo/trainData4000-weight.feature.arff";
		String pathoutputmulan = "C:/Users/lin/Desktop/myemotion/weibo/trainData4000mulan.feature.arff";
		
		String pathunigram = "C:/Users/lin/Desktop/myemotion/classCHI/allfeature.txt";

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

			
//			OutputStreamWriter writerweibo = new OutputStreamWriter(
//					new FileOutputStream(pathoutputweibo), "UTF-8");
			
			OutputStreamWriter writersvm = new OutputStreamWriter(
					new FileOutputStream(pathoutputsvm), "UTF-8");
			
			OutputStreamWriter writerarff = new OutputStreamWriter(
					new FileOutputStream(pathoutput), "UTF-8");
			
			OutputStreamWriter writermulan = new OutputStreamWriter(
					new FileOutputStream(pathoutputmulan), "UTF-8");
			
			writerarff.write("@relation emotion\r\n\r\n");
			writerarff.write("@attribute emotion {happiness,like,anger,sadness,fear,disgust,surprise,none}\r\n");
			
			writermulan.write("@relation emotion\r\n\r\n");
			writermulan.write("@attribute happiness {0,1}\r\n");
			writermulan.write("@attribute like {0,1}\r\n");
			writermulan.write("@attribute anger {0,1}\r\n");
			writermulan.write("@attribute sadness {0,1}\r\n");
			writermulan.write("@attribute fear {0,1}\r\n");
			writermulan.write("@attribute disgust {0,1}\r\n");
			writermulan.write("@attribute surprise {0,1}\r\n");

			for(int i=0;i<unigramlist.size();i++){
				writerarff.write("@attribute f"+(i+1)+" {0,1}\r\n");
				writermulan.write("@attribute f"+(i+1)+" {0,1}\r\n");
			}
		
			writerarff.write("\r\n@data\r\n");
			writermulan.write("\r\n@data\r\n");
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
					String featureresultmulan = "";
					String featureresultsvm = "";
					int unigramnum[] = new int[unigramlist.size()];
					String thetext = "";
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

							
							 thetext += name;
							// System.out.println(thetext);

						}
						
					}
//					writerweibo.write(emotiontype1+"\t"+thetext+"\r\n");
					for (int n = 0; n < unigramnum.length; n++) {
						if (unigramnum[n] == 1) {
							featureresultsvm += (n + 1) + ":1 ";
							featureresult += (n+1 ) + " 1, ";
							featureresultmulan += (n+7 ) + " 1, ";
						}
					}
					Map<String,String> map= new HashMap();
					map.put("happiness", "0");
					map.put("like", "0");
					map.put("anger", "0");
					map.put("sadness", "0");
					map.put("fear", "0");
					map.put("disgust", "0");
					map.put("surprise", "0");
					
					if(!emotiontype1.equals("none")){
						map.put(emotiontype1,"1");
					}
					if(!emotiontype2.equals("none")){
						map.put(emotiontype2,"1");
					}
					if (featureresult.trim().equals("")) {
						// writerarff.write(weiboid + "\t" + emotiontype1 + "\t"
						// + emotiontype2 + "\t1:0 \r\n");
						writersvm.write(emotiontype1.replace("happiness", "1")
								.replace("like", "2").replace("anger", "3")
								.replace("sadness", "4").replace("fear", "5")
								.replace("disgust", "6")
								.replace("surprise", "7").replace("none", "8") + "\t1:0 \r\n");
						writerarff.write("{0 "+emotiontype1 + "}\r\n");
						writermulan.write("{0 "+map.get("happiness")+", 1 "+map.get("like")+", 2 "+map.get("anger")+", 3 "+map.get("sadness")+", 4 "+map.get("fear")+", 5 "+map.get("disgust")+", 6 "+map.get("surprise") + "}\r\n");
					} else {
						writersvm.write(emotiontype1.replace("happiness", "1")
								.replace("like", "2").replace("anger", "3")
								.replace("sadness", "4").replace("fear", "5")
								.replace("disgust", "6")
								.replace("surprise", "7").replace("none", "8")+ "\t" + featureresultsvm + "\r\n");
						writerarff.write("{0 "+emotiontype1+", "+featureresult.substring(0, featureresult.length()-2) +"}\r\n");
						writermulan.write("{0 "+map.get("happiness")+", 1 "+map.get("like")+", 2 "+map.get("anger")+", 3 "+map.get("sadness")+", 4 "+map.get("fear")+", 5 "+map.get("disgust")+", 6 "+map.get("surprise") +", "+featureresultmulan.substring(0, featureresultmulan.length()-2) +"}\r\n");
					}
				}
			}
			System.out.println(num);
			reader.close();
//			writerweibo.flush();
//			writerweibo.close();
			writermulan.flush();
			writermulan.close();
			writersvm.flush();
			writersvm.close();
			writerarff.flush();
			writerarff.close();
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
