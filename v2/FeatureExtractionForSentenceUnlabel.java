package v2;

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

public class FeatureExtractionForSentenceUnlabel {

	public static void main(String[] args) {

		String pathinput = "C:/Users/lin/Desktop/myemotion/weibo/unlabel/weibounlabel.fenci2.xml";
		String pathoutputtrain = "C:/Users/lin/Desktop/myemotion/v2/unlabelfeatureSentence.arff";

		String pathunigram = "C:/Users/lin/Desktop/myemotion/v2/classCHI/feature.txt";
		String pathdic = "C:/Users/lin/Desktop/myemotion/dictionary/情感词汇本体.csv";

		File file = new File(pathinput);
		File unigramfile = new File(pathunigram);
		File dicfile = new File(pathdic);
		BufferedReader reader = null;
		BufferedReader readerunigram = null;
		BufferedReader readerdic = null;

		List<String> unigramlist = new ArrayList<String>();
		List<String>[] diclist = new ArrayList[7];
		for (int i = 0; i < 7; i++) {
			diclist[i] = new ArrayList<String>();
		}
		List<String> emotionwordslist = new ArrayList<String>();
		try {
			// Pattern patweibo = Pattern.compile("(<weibo id=\")([0-9]*)");
			Pattern patweibo = Pattern
					.compile("(<weibo id=\")([0-9]*)(\">)");

			Pattern patn = Pattern
					.compile("<sentence id=\"([0-9]*)\">(.*)(</sentence>)");
			reader = new BufferedReader(new FileReader(file));
			readerunigram = new BufferedReader(new FileReader(unigramfile));
			readerdic = new BufferedReader(new FileReader(dicfile));
			String tempString = null;

			// unigram添加到List
			while ((tempString = readerunigram.readLine()) != null) {
				unigramlist.add(tempString);
			}
			// dic添加到List
			while ((tempString = readerdic.readLine()) != null) {
				String word = tempString.split(",")[0];
				// System.out.println(word);
				if (tempString.contains("PA") || tempString.contains("PE")) {
					diclist[0].add(word);
				}
				if (tempString.contains("PD") || tempString.contains("PH")
						|| tempString.contains("PG")
						|| tempString.contains("PB")
						|| tempString.contains("PK")) {
					diclist[1].add(word);
				}
				if (tempString.contains("NA")) {
					diclist[2].add(word);
				}
				if (tempString.contains("NB") || tempString.contains("NJ")
						|| tempString.contains("NH")
						|| tempString.contains("NF")) {
					diclist[3].add(word);
				}
				if (tempString.contains("NI") || tempString.contains("NC")
						|| tempString.contains("NG")) {
					diclist[4].add(word);
				}
				if (tempString.contains("NE") || tempString.contains("ND")
						|| tempString.contains("NN")
						|| tempString.contains("NK")
						|| tempString.contains("NL")) {
					diclist[5].add(word);
				}
				if (tempString.contains("PC")) {
					diclist[6].add(word);
				}
			}
			for (int i = 0; i < 7; i++) {
				System.out.println(diclist[i].size());
			}
			String polarity = "";// String polarity
			OutputStreamWriter writeruniqueID = new OutputStreamWriter(
					new FileOutputStream(
							"C:/Users/lin/Desktop/myemotion/v2/uniqueID.map"),
					"UTF-8");

			OutputStreamWriter writertrain = new OutputStreamWriter(
					new FileOutputStream(pathoutputtrain), "UTF-8");

			writertrain.write("@relation emotion\r\n\r\n");
			writertrain
					.write("@attribute emotion {happiness,like,anger,sadness,fear,disgust,surprise}\r\n");
			// writertrain.write("@attribute emotion {yes,no}\r\n");

			writertrain.write("@attribute s1 {0,1}\r\n");
			writertrain.write("@attribute s2 {0,1}\r\n");
			for (int i = 3; i <= 19; i++) {
				writertrain.write("@attribute s" + i + " real\r\n");
			}
			writertrain.write("@attribute s20 {0,1}\r\n");
			writertrain.write("@attribute s21 {0,1}\r\n");

			writertrain.write("@attribute happiness {0,1}\r\n");
			writertrain.write("@attribute like {0,1}\r\n");
			writertrain.write("@attribute anger {0,1}\r\n");
			writertrain.write("@attribute sadness {0,1}\r\n");
			writertrain.write("@attribute fear {0,1}\r\n");
			writertrain.write("@attribute disgust {0,1}\r\n");
			writertrain.write("@attribute surprise {0,1}\r\n");

			for (int i = 0; i < unigramlist.size(); i++) {
				writertrain.write("@attribute f" + (i + 1) + " {0,1}\r\n");
			}
			writertrain.write("@attribute id String\r\n");
			writertrain.write("\r\n@data\r\n");
			int num = 0;

			int weiboid = 0;
			String sentenceid = "";
			String weiboemo1 = "";
			String weiboemo2 = "";
			int  eid = 0;
			boolean iszhuanfa = false;
			String uniqueID="";
			while ((tempString = reader.readLine()) != null) {

				// find weiboid
				Matcher matcherweibo = patweibo.matcher(tempString);
				if (matcherweibo.find()) {
//					iszhuanfa = false;
					weiboid ++;
					System.out.println(weiboid);
				}

				String emotiontype1 = "like";
				String emotiontype2 = "happiness";
				String text = "";

				// find sentence that opinionated="N"
				Matcher matchern = patn.matcher(tempString);
				if (matchern.find()) {

					text = matchern.group(2);
					num++;
					sentenceid = matchern.group(1);
					uniqueID=weiboid+"_"+sentenceid;
					num++;
					eid++;
				}

				if (!"".equals(text)) {
					String featureresult = "";
					int unigramnum[] = new int[unigramlist.size()];
					String thetext = "";
					String wordlist[] = text.split(" ");
					int dic[] = new int[7];
					for (int i = 0; i < wordlist.length; i++) {
						String temp[] = wordlist[i].split("/");
						String temp2[] = { "" };
						if (i < wordlist.length - 1) {
							temp2 = wordlist[i + 1].split("/");
						} else {

						}
						if (temp.length == 2) {
							String name = temp[0];
							String pos = temp[1];
							String name2 = "";
							if (temp2.length == 2) {
								name2 = temp2[0];
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
								if ((name + name2).equals(unigramlist.get(u))) {
									unigramnum[u] = 1;
									break;
								}
							}
							for (int u = 0; u < 7; u++) {
								if (diclist[u].contains(name)
										|| diclist[u].contains(name + name2))
									dic[u] = 1;
							}
							thetext += name;
							// System.out.println(thetext);

						}

					}

					// 1：是否为排比
					if (isPaibi(thetext)) {
						featureresult += "1 1,";
//						System.out.println("排比:" + thetext);
					} else {
						featureresult += "1 0,";
					}
					// 2：是否星座
					if (isXingzuo(thetext)) {
						featureresult += "2 1,";
//						System.out.println("星座:" + thetext);
					} else {
						featureresult += "2 0,";
					}
					// 3：数字个数
					featureresult += "3 " + count123(thetext) + ",";
					// 4：字母个数
					featureresult += "4 " + countABC(thetext) + ",";
					// 5：分号个数
					featureresult += "5 "
							+ (countString(thetext, "；") + countString(thetext,
									";")) + ",";
					// 6：冒号个数
					featureresult += "6 "
							+ (countString(thetext, "：") + countString(thetext,
									":")) + ",";
					// 7：顿号个数
					featureresult += "7 " + countString(thetext, "、") + ",";
					// 8：引号个数
					featureresult += "8 "
							+ (countString(thetext, "“") + countString(thetext,
									"\"")) + ",";
					// 9：逗号个数
					featureresult += "9 "
							+ (countString(thetext, "，") + countString(thetext,
									",")) + ",";
					// 10：感叹号个数
					featureresult += "10 "
							+ (countString(thetext, "！") + countString(thetext,
									"!")) + ",";
					// 11：书名号个数
					featureresult += "11 " + countString(thetext, "《") + ",";
					// 12：破折号个数
					featureresult += "12 " + countString(thetext, "——") + ",";
					// 13：人名个数
					featureresult += "13 " + countString(text, "/nr ") + ",";
					// 14：地名个数
					featureresult += "14 " + countString(text, "/ns ") + ",";
					// 15：句首相同句子个数
					featureresult += "15 " + maxSame("") + ",";
					// 16：省略号个数
					featureresult += "16 " + countString(thetext, "…") + ",";
					// 17：弯弯号个数
					featureresult += "17 "
							+ (countString(thetext, "～") + countString(thetext,
									"~")) + ",";
					// 18：百分号个数
					featureresult += "18 " + countString(thetext, "%") + ",";
					// 19：含有情绪词个数
					int hasemotionwords = 0;
					for (int i = 0; i < emotionwordslist.size(); i++) {
						if (thetext.contains(emotionwordslist.get(i))) {
							hasemotionwords++;
						}
					}
					featureresult += "19 " + hasemotionwords + ",";
					// 20：是否含有日期
					if ((thetext.contains("月") && thetext.contains("日"))
							|| (thetext.contains("年") && thetext.contains("月"))) {
						featureresult += "20 1,";
//						System.out.println("年月:" + thetext);
					} else {
						featureresult += "20 0,";
					}
					// 21:是否含有温度符号℃
					if (thetext.contains("℃") || thetext.contains("天气预报:")) {
//						System.out.println("天气:" + thetext);
						featureresult += "21 1,";
					} else {
						featureresult += "21 0,";
					}
					int n;
					for (n = 0; n < 7; n++) {
						featureresult += (n + 22) + " " + dic[n] + ",";
					}

					for (n = 0; n < unigramnum.length; n++) {
						if (unigramnum[n] == 1) {
							featureresult += (n + 29) + ":1 ";
						}
					}
					featureresult += (n + 29) + " " + uniqueID + ",";

					if (!weiboemo1.equals("none")) {
						if (featureresult.trim().equals("")) {

							writertrain.write("{0 " + emotiontype1 + "}\r\n");

//							writeruniqueID.write(uniqueID + " cet_" + eid
//									+ "_P1_S" + sentenceid + "\r\n");
						} else {

							writertrain.write("{0 "
									+ emotiontype1
									+ ","
									+ featureresult.substring(0,
											featureresult.length() - 1)
									+ "}\r\n");
//							writeruniqueID.write(uniqueID + " cet_" + eid
//									+ "_P1_S" + sentenceid + "\r\n");

						}
					}
				} else {
					if (tempString.contains("sentence"))
						System.out.println(weiboid);
				}
			}
			System.out.println(num);
			reader.close();
			writertrain.flush();
			writertrain.close();
			writeruniqueID.flush();
			writeruniqueID.close();
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

	public static int countString(String src, String find) {
		int o = 0;
		int index = -1;
		while ((index = src.indexOf(find, index)) > -1) {
			++index;
			++o;
		}
//		if (o != 0) {
//			System.out.println("个数" + find + o + "-------" + src);
//		}
		return o;
	}

	public static int countABC(String str) {

		int zmCount = 0;

		for (int i = 0; i < str.length(); i++) {

			char c = str.charAt(i);
			if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))
				zmCount++;

		}
//		System.out.println("字母：" + zmCount);
		return zmCount;
	}

	public static int count123(String str) {
		int szCount = 0;
		for (int i = 0; i < str.length(); i++) {

			char c = str.charAt(i);
			if (c >= '0' && c <= '9') {
				szCount++;
			}
		}
//		System.out.println("数字：" + szCount);
		return szCount;
	}

	public static boolean isPaibi(String str) {
		if (str.contains("1.") && str.contains("2.") && str.contains("3."))
			return true;
		if (str.contains("1、") && str.contains("2、") && str.contains("3、"))
			return true;
		if (str.contains("1）") && str.contains("2）") && str.contains("3）"))
			return true;
		if (str.contains("1)") && str.contains("2)") && str.contains("3)"))
			return true;
		if (str.contains("⑴") && str.contains("⑵") && str.contains("⑶"))
			return true;
		if (str.contains("（1）") && str.contains("（2）") && str.contains("（3）"))
			return true;
		if (str.contains("(1)") && str.contains("(2)") && str.contains("(3)"))
			return true;
		if (str.contains("①") && str.contains("②") && str.contains("③"))
			return true;
		if (str.contains("一") && str.contains("二") && str.contains("三"))
			return true;
		return false;
	}

	public static boolean isXingzuo(String str) {
		if (str.contains("星座") || str.contains("双鱼") || str.contains("白羊")
				|| str.contains("金牛") || str.contains("双子")
				|| str.contains("巨蟹") || str.contains("狮子")
				|| str.contains("处女") || str.contains("天秤")
				|| str.contains("天蝎") || str.contains("射手")
				|| str.contains("摩羯") || str.contains("水瓶"))
			return true;
		return false;
	}

	public static int maxSame(String str) {
//		System.out.println(str);
		str = str.replaceAll("\\[", "").replaceAll("\\(", "")
				.replaceAll("\\+", "").replaceAll("\\)", "")
				.replaceAll("\\{", "").replaceAll("\\*", "")
				.replaceAll("\\?", "").replaceAll("\\|", "")
				.replaceAll("\\\\", "").replaceAll("\\^", "");
		int max_length = 0;
		String max_str = "";
		while (str.length() > 0) {
			int length = str.length();
			String first = str.substring(0, 1);
			str = str.replaceAll(first, "");
			if (max_length < length - str.length()) {
				max_length = length - str.length();
				max_str = first;
			}
		}
//		System.out.println(max_length);
//		System.out.println(max_str);
		if (max_length != 0)
			return max_length - 1;
		return 0;
	}
}
