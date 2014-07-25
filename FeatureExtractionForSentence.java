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

public class FeatureExtractionForSentence {

	public static void main(String[] args) {
//		String pathinput = "C:/Users/lin/Desktop/myemotion/trainData4000-weight.fenci.xml";
//		String pathoutput = "C:/Users/lin/Desktop/myemotion/sentence/trainDatabi.feature.libsvm";
//		String pathoutputarff = "C:/Users/lin/Desktop/myemotion/sentence/trainDatabi.feature.arff";
//		String pathoutputmulan = "C:/Users/lin/Desktop/myemotion/sentence/trainData4000mulanbi.feature.arff";

		 String pathinput =
		 "C:/Users/lin/Desktop/myemotion/testData10000-weight.fenci.xml";
		 String pathoutput =
		 "C:/Users/lin/Desktop/myemotion/sentence/testDatabi.feature.libsvm";
		 String pathoutputarff =
		 "C:/Users/lin/Desktop/myemotion/sentence/testDatabi.feature.arff";
		 String pathoutputmulan =
		 "C:/Users/lin/Desktop/myemotion/sentence/testData1000mulanbi.feature.arff";

		String pathunigram = "C:/Users/lin/Desktop/myemotion/classCHI/allfeaturebigram.txt";

		File file = new File(pathinput);
		File unigramfile = new File(pathunigram);
		BufferedReader reader = null;
		BufferedReader readerunigram = null;

		List<String> unigramlist = new ArrayList<String>();
		try {
			// Pattern patweibo = Pattern.compile("(<weibo id=\")([0-9]*)");
			Pattern patweibo = Pattern
					.compile("(<weibo id=\")([0-9]*)(\" emotion-type1=\")(.*)(\" emotion-type2=\")(.*)(\">)");

			Pattern patn = Pattern
					.compile("<sentence id=\"([0-9]*)\" opinionated=\"N\">(.*)(</sentence>)");
			Pattern paty = Pattern
					.compile("<sentence id=\"([0-9]*)\" opinionated=\"Y\" emotion-1-type=\"(.*)(\" emotion-1-weight=\"[1-3]\" emotion-2-type=\")(none|happiness|like|sadness|anger|fear|disgust|surprise)(\".*>)(.*)(</sentence>)");
			reader = new BufferedReader(new FileReader(file));
			readerunigram = new BufferedReader(new FileReader(unigramfile));

			String tempString = null;

			// unigram添加到List
			while ((tempString = readerunigram.readLine()) != null) {
				unigramlist.add(tempString);
			}

			String polarity = "";// String polarity
			OutputStreamWriter writeruniqueID = new OutputStreamWriter(
					new FileOutputStream(
							"C:/Users/lin/Desktop/myemotion/sentence/uniqueID.map"),
					"UTF-8");

			OutputStreamWriter writer = new OutputStreamWriter(
					new FileOutputStream(pathoutput), "UTF-8");

			OutputStreamWriter writerarff = new OutputStreamWriter(
					new FileOutputStream(pathoutputarff), "UTF-8");

			OutputStreamWriter writermulan = new OutputStreamWriter(
					new FileOutputStream(pathoutputmulan), "UTF-8");

			OutputStreamWriter writercompre = new OutputStreamWriter(
					new FileOutputStream(
							"C:/Users/lin/Desktop/myemotion/sentence/compare.txt"),
					"UTF-8");

			writerarff.write("@relation emotion\r\n\r\n");

			writerarff
					.write("@attribute emotion {happiness,like,anger,sadness,fear,disgust,surprise,none}\r\n");
			// writerarff.write("@attribute emotion {1,2,3,4,5,6,7,8}\r\n");

			writermulan.write("@relation emotion\r\n\r\n");
			writermulan.write("@attribute happiness {0,1}\r\n");
			writermulan.write("@attribute like {0,1}\r\n");
			writermulan.write("@attribute anger {0,1}\r\n");
			writermulan.write("@attribute sadness {0,1}\r\n");
			writermulan.write("@attribute fear {0,1}\r\n");
			writermulan.write("@attribute disgust {0,1}\r\n");
			writermulan.write("@attribute surprise {0,1}\r\n");
			writermulan.write("@attribute uniqueID string\r\n");

			for (int i = 0; i < unigramlist.size(); i++) {
				writerarff.write("@attribute f" + (i + 1) + " {0,1}\r\n");
				writermulan.write("@attribute f" + (i + 1) + " {0,1}\r\n");
			}

			writerarff.write("\r\n@data\r\n");
			writermulan.write("\r\n@data\r\n");
			int num = 0, nonenum = 0;

			String weiboid = "";
			String sentenceid = "";
			String weiboemo1 = "";
			String weiboemo2 = "";
			int uniqueID = 0, eid = 0;
			while ((tempString = reader.readLine()) != null) {

				// find weiboid
				Matcher matcherweibo = patweibo.matcher(tempString);
				if (matcherweibo.find()) {
					weiboid = matcherweibo.group(2);
					weiboemo1 = matcherweibo.group(4);
					weiboemo2 = matcherweibo.group(6);
//					 if (!weiboemo1.equals("none")) {
//							 writercompre.write((weiboemo1+","+weiboemo2).replaceAll("happiness", "0").replaceAll("like", "1").replaceAll("anger", "2").replaceAll("sadness", "3").replaceAll("fear", "4").replaceAll("disgust", "5").replaceAll("surprise", "6").replaceAll("none", "7")+ "\r\n");
//											eid++;
//					 }
					 
					
				}

				String emotiontype1 = "none";
				String emotiontype2 = "none";
				String text = "";

				// find sentence that opinionated="N"
				Matcher matchern = patn.matcher(tempString);
				if (matchern.find()) {

					text = matchern.group(2);
					num++;
					sentenceid = matchern.group(1);
					// if ((nonenum++) > 1000)
					// continue;
					writercompre.write((emotiontype1+","+emotiontype2).replaceAll("happiness", "0").replaceAll("like", "1").replaceAll("anger", "2").replaceAll("sadness", "3").replaceAll("fear", "4").replaceAll("disgust", "5").replaceAll("surprise", "6").replaceAll("none", "7")+ "\r\n");
					eid++;
				}

				// find sentence that opinionated="Y"
				Matcher matchery = paty.matcher(tempString);
				if (matchery.find()) {
					sentenceid = matchery.group(1);
					emotiontype1 = matchery.group(2);
					emotiontype2 = matchery.group(4);
					text = matchery.group(6);
					num++;
					writercompre.write((emotiontype1+","+emotiontype2).replaceAll("happiness", "0").replaceAll("like", "1").replaceAll("anger", "2").replaceAll("sadness", "3").replaceAll("fear", "4").replaceAll("disgust", "5").replaceAll("surprise", "6").replaceAll("none", "7")+ "\r\n");
					eid++;
				}

				if (!"".equals(text)) {
					String featureresult = "";
					String featureresultarff = "";
					String featureresultmulan = "";
					int unigramnum[] = new int[unigramlist.size()];
					String thetext = "";
					String wordlist[] = text.split(" ");
					for (int i = 0; i < wordlist.length; i++) {
						String temp[] = wordlist[i].split("/");
						String temp2[] = {""};
						if(i<wordlist.length-1){
						temp2 = wordlist[i+1].split("/");
						}else{
							
						}
						if (temp.length == 2) {
							String name = temp[0];
							String pos = temp[1];
							String name2 = "";
							if(temp2.length==2){
							 name2= temp2[0];
							}
							// System.out.println(wordlist[i]);
							// unigram-feature extraction
							for (int u = 0; u < unigramlist.size(); u++) {
								if (name.equals(unigramlist.get(u))) {
									unigramnum[u] = 1;
									break;
								}
							}
							for (int u = 0; u < unigramlist.size(); u++) {
								if ((name+name2).equals(unigramlist.get(u))) {
									unigramnum[u] = 1;
									break;
								}
							}
							thetext += name;
							// System.out.println(thetext);

						}

					}
					for (int n = 0; n < unigramnum.length; n++) {
						if (unigramnum[n] == 1) {
							featureresult += (n + 1) + ":1 ";
							featureresultarff += (n + 1) + " 1, ";
							featureresultmulan += (n + 8) + " 1, ";
						}
					}
					Map<String, String> map = new HashMap();
					map.put("happiness", "0");
					map.put("like", "0");
					map.put("anger", "0");
					map.put("sadness", "0");
					map.put("fear", "0");
					map.put("disgust", "0");
					map.put("surprise", "0");

					if (!emotiontype1.equals("none")) {
						map.put(emotiontype1, "1");
					}
					if (!emotiontype2.equals("none")) {
						map.put(emotiontype2, "1");
					}

					 if (!weiboemo1.equals("none")) {
					if (featureresult.trim().equals("")) {
						// writer.write(weiboid + "\t" + emotiontype1 + "\t"
						// + emotiontype2 + "\t1:0 \r\n");
						writer.write(emotiontype1.replace("happiness", "1")
								.replace("like", "2").replace("anger", "3")
								.replace("sadness", "4").replace("fear", "5")
								.replace("disgust", "6")
								.replace("surprise", "7").replace("none", "8")
								+ "\t1:0 \r\n");
						writeruniqueID.write(uniqueID + " cet_" + eid + "_P1_S"
								+ sentenceid + "\r\n");
						writermulan.write("{0 " + map.get("happiness") + ", 1 "
								+ map.get("like") + ", 2 " + map.get("anger")
								+ ", 3 " + map.get("sadness") + ", 4 "
								+ map.get("fear") + ", 5 " + map.get("disgust")
								+ ", 6 " + map.get("surprise") + ", 7 "
								+ (uniqueID++) + "}\r\n");
//						writercompre.write(num + ":" + weiboid + "-"
//								+ emotiontype1 + "," + emotiontype2 + "\t"
//								+ thetext + "\r\n");

						// if(emotiontype1.equals("none")){
						// emotiontype1="0";
						// }else{
						// emotiontype1="1";
						// }
						writerarff.write("{0 " + emotiontype1 + "}\r\n");

					} else {
						// writer.write(weiboid + "\t" + emotiontype1 + "\t"
						// + emotiontype2 + "\t" + featureresult + "\r\n");
						writer.write(emotiontype1.replace("happiness", "1")
								.replace("like", "2").replace("anger", "3")
								.replace("sadness", "4").replace("fear", "5")
								.replace("disgust", "6")
								.replace("surprise", "7").replace("none", "8")
								+ "\t" + featureresult + "\r\n");
						// if(emotiontype1.equals("none")){
						// emotiontype1="0";
						// }else{
						// emotiontype1="1";
						// }
						writerarff.write("{0 "
								+ emotiontype1
								+ ", "
								+ featureresultarff.substring(0,
										featureresultarff.length() - 2)
								+ "}\r\n");
						writeruniqueID.write(uniqueID + " cet_" + eid + "_P1_S"
								+ sentenceid + "\r\n");
						writermulan.write("{0 "
								+ map.get("happiness")
								+ ", 1 "
								+ map.get("like")
								+ ", 2 "
								+ map.get("anger")
								+ ", 3 "
								+ map.get("sadness")
								+ ", 4 "
								+ map.get("fear")
								+ ", 5 "
								+ map.get("disgust")
								+ ", 6 "
								+ map.get("surprise")
								+ ", 7 "
								+ (uniqueID++)
								+ ", "
								+ featureresultmulan.substring(0,
										featureresultmulan.length() - 2)
								+ "}\r\n");
//						writercompre.write(num + ":" + weiboid + "-"
//								+ emotiontype1 + "," + emotiontype2 + "\t"
//								+ thetext + "\r\n");

					}
					 }
				} else {
					if (tempString.contains("sentence"))
						System.out.println(weiboid);
				}
			}
			System.out.println(num);
			writercompre.flush();
			writercompre.close();
			reader.close();
			writer.flush();
			writer.close();
			writermulan.flush();
			writermulan.close();
			writeruniqueID.flush();
			writeruniqueID.close();
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
