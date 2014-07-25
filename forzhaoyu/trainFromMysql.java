package forzhaoyu;

import forzhaoyu._baseCon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



public class trainFromMysql {

	static _baseCon basecon;
	static Connection con;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		List<String> weiboList = new ArrayList<>();
		List<String> emotionList = new ArrayList<>();
		getListFromMysql(weiboList,emotionList);
		Iterator i1 = weiboList.iterator();
		Iterator i2 = emotionList.iterator();
		int t=0;
		while(i1.hasNext())
		{
			System.out.println(t+"-----"+i2.next().toString()+i1.next().toString());
			t++;
		}
		
		//String pathinput = "date/weibofenci.txt";//输入分好词的文件
		String pathoutput = "newtrain.arff";//输出向量文件

		String pathunigram = "resource/feature.txt";
		String pathdic = "resource/情感词汇本体.csv";

		//File file = new File(pathinput);
		File unigramfile = new File(pathunigram);
		File dicfile = new File(pathdic);
		//BufferedReader reader = null;
		BufferedReader readerunigram = null;
		BufferedReader readerdic = null;
		List<String>[] diclist = new ArrayList[7];
		for (int i = 0; i < 7; i++) {
			diclist[i] = new ArrayList<String>();
		}
		List<String> unigramlist = new ArrayList<String>();
		List<String> emotionwordslist = new ArrayList<String>();
		try {
	
			//reader = new BufferedReader(new FileReader(file));
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

			// OutputStreamWriter writerweibo = new OutputStreamWriter(
			// new FileOutputStream(pathoutputweibo), "UTF-8");

			OutputStreamWriter writer = new OutputStreamWriter(
					new FileOutputStream(pathoutput), "UTF-8");


			writer.write("@relation emotion\r\n\r\n");
			writer.write("@attribute emotion {happiness,like,anger,sadness,fear,disgust,surprise,none}\r\n");
//			writertrain.write("@attribute emotion {yes,no}\r\n");


			writer.write("@attribute s1 {0,1}\r\n");
			writer.write("@attribute s2 {0,1}\r\n");
			for (int i = 3; i <= 19; i++) {
				writer.write("@attribute s" + i + " real\r\n");
			}
			writer.write("@attribute s20 {0,1}\r\n");
			writer.write("@attribute s21 {0,1}\r\n");
			
			writer.write("@attribute happiness {0,1}\r\n");
			writer.write("@attribute like {0,1}\r\n");
			writer.write("@attribute anger {0,1}\r\n");
			writer.write("@attribute sadness {0,1}\r\n");
			writer.write("@attribute fear {0,1}\r\n");
			writer.write("@attribute disgust {0,1}\r\n");
			writer.write("@attribute surprise {0,1}\r\n");
			
			
			
			for (int i = 0; i < unigramlist.size(); i++) {
				writer.write("@attribute f" + (i + 1) + " {0,1}\r\n");
			}
			writer.write("@attribute id String\r\n");
			writer.write("\r\n@data\r\n");
			int num = 0;

			int weiboid = 0;
			//String emotiontype1 = "like";
			//String emotiontype2 = "fear";
			String text = "";
			String firsttext = "";
			Iterator e1 = emotionList.iterator();
			Iterator e2 = weiboList.iterator();
			while (e1.hasNext()&&e2.hasNext()) {
//				tempString=tempString.replaceAll("$", "");
				// find weiboid 
					text = e2.next().toString();
					firsttext = "";
					weiboid ++;
//					System.out.println(weiboid);

					num++;
				
					String featureresult = "";
					int unigramnum[] = new int[unigramlist.size()];
					String thetext = "";
//					if (text.contains("///session @")) {
//						text = text.split("///session  @")[0];
//					}
					String wordlist[] = text.split(" ");
					int dic[] = new int[7];
					for (int i = 0; i < wordlist.length; i++) {
						String temp[] = wordlist[i].split("/");
						String temp2[] = { "" };
						if (i < wordlist.length - 1) {
							temp2 = wordlist[i + 1].split("/");
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
								if (diclist[u].contains(name)||diclist[u].contains(name+name2))
									dic[u] = 1;
							}
							thetext += name;
							// System.out.println(thetext);

						}
					}

					// 1：是否为排比
					if (isPaibi(thetext)) {
						featureresult += "1 1,";
					} else {
						featureresult += "1 0,";
					}
					// 2：是否星座
					if (isXingzuo(thetext)) {
						featureresult += "2 1,";
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
					featureresult += "15 " + maxSame(firsttext) + ",";
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
					} else {
						featureresult += "20 0,";
					}
					// 21:是否含有温度符号℃
					if (thetext.contains("℃") || thetext.contains("天气预报:")) {
						featureresult += "21 1,";
					} else {
						featureresult += "21 0,";
					}
					int n;
					for(n=0;n<7;n++){
						featureresult += (n + 22) + " "+dic[n]+",";
					}
					// writerweibo.write(emotiontype1+"\t"+thetext+"\r\n");
					
					for (n = 0; n < unigramnum.length; n++) {
						if (unigramnum[n] == 1) {
							featureresult += (n + 29) + " 1,";
						}
					}
					featureresult += (n + 29) + " " + weiboid + ",";

						
					
					if (featureresult.trim().equals("")) {
						writer.write("{0 " + e1.next().toString()+ "}\r\n");

					} else {
						writer.write("{0 "
									+ e1.next().toString()
									+ ","
									+ featureresult.substring(0,
											featureresult.length() - 1)
									+ "}\r\n");
						
					}
				}
			System.out.println(num);
			//reader.close();
			writer.flush();
			writer.close();
		} catch (Exception e) {
		}
	}

	private static void getListFromMysql(List<String> weiboList,List<String> emotionList) {
		basecon = new _baseCon();
		con = basecon.connSQL();
		
		try {
			String getWeiboFenciString = "SELECT * FROM `sentence` WHERE `class` in ('无','无 无')  order by`id` limit 0,2000";
			PreparedStatement statement = con.prepareStatement(getWeiboFenciString);
			statement.executeQuery();

			ResultSet rs = statement.getResultSet();
			while (rs.next()) {
				//System.out.println(rs.getString("segment"));
				weiboList.add(rs.getString("segment"));
				emotionList.add("none");
				//System.out.println(weiboList.size());
			}
			
			getWeiboFenciString = "SELECT * FROM `sentence` WHERE `class` like '幸福类%'  order by`id` limit 0,2000";
			statement = con.prepareStatement(getWeiboFenciString);
			statement.executeQuery();

			rs = statement.getResultSet();
			while (rs.next()) {
				//System.out.println(rs.getString("segment"));
				weiboList.add(rs.getString("segment"));
				emotionList.add("happiness");
				//System.out.println(weiboList.size());
			}
			
			getWeiboFenciString = "SELECT * FROM `sentence` WHERE `class` like '喜爱类%'  order by`id` limit 0,2000";
			statement = con.prepareStatement(getWeiboFenciString);
			statement.executeQuery();

			rs = statement.getResultSet();
			while (rs.next()) {
				//System.out.println(rs.getString("segment"));
				weiboList.add(rs.getString("segment"));
				emotionList.add("like");
				//System.out.println(weiboList.size());
			}
			
			getWeiboFenciString = "SELECT * FROM `sentence` WHERE `class` like '愤怒类%'  order by`id` limit 0,2000";
			statement = con.prepareStatement(getWeiboFenciString);
			statement.executeQuery();

			rs = statement.getResultSet();
			while (rs.next()) {
				//System.out.println(rs.getString("segment"));
				weiboList.add(rs.getString("segment"));
				emotionList.add("anger");
				//System.out.println(weiboList.size());
			}
			
			getWeiboFenciString = "SELECT * FROM `sentence` WHERE `class` like '悲伤类%'  order by`id` limit 0,2000";
			statement = con.prepareStatement(getWeiboFenciString);
			statement.executeQuery();

			rs = statement.getResultSet();
			while (rs.next()) {
				//System.out.println(rs.getString("segment"));
				weiboList.add(rs.getString("segment"));
				emotionList.add("sadness");
				//System.out.println(weiboList.size());
			}
			
			getWeiboFenciString = "SELECT * FROM `sentence` WHERE `class` like '害怕类%'  order by`id` limit 0,2000";
			statement = con.prepareStatement(getWeiboFenciString);
			statement.executeQuery();

			rs = statement.getResultSet();
			while (rs.next()) {
				//System.out.println(rs.getString("segment"));
				weiboList.add(rs.getString("segment"));
				emotionList.add("fear");
				//System.out.println(weiboList.size());
			}
			
			getWeiboFenciString = "SELECT * FROM `sentence` WHERE `class` like '讨厌类%'  order by`id` limit 0,2000";
			statement = con.prepareStatement(getWeiboFenciString);
			statement.executeQuery();

			rs = statement.getResultSet();
			while (rs.next()) {
				//System.out.println(rs.getString("segment"));
				weiboList.add(rs.getString("segment"));
				emotionList.add("disgust");
				//System.out.println(weiboList.size());
			}
			
			getWeiboFenciString = "SELECT * FROM `sentence` WHERE `class` like '惊讶类%'  order by`id` limit 0,2000";
			statement = con.prepareStatement(getWeiboFenciString);
			statement.executeQuery();

			rs = statement.getResultSet();
			while (rs.next()) {
				//System.out.println(rs.getString("segment"));
				weiboList.add(rs.getString("segment"));
				emotionList.add("surprise");
				//System.out.println(weiboList.size());
			}
//			String getWeiboQingxu = "SELECT `class` FROM `sentence` WHERE `source`=1";
//			PreparedStatement statement2 = con.prepareStatement(getWeiboQingxu);
//			statement2.executeQuery();
//
//			ResultSet rs2 = statement2.getResultSet();
//	
//			while (rs2.next()) {
//				//System.out.println(rs2.getString("class"));
//				//emotionList.add(rs2.getString("class"));
//
//				if(rs2.getString("class").startsWith("幸福类"))
//					emotionList.add("happiness");
//				else if(rs2.getString("class").startsWith("喜爱类"))
//					emotionList.add("like");
//				else if(rs2.getString("class").startsWith("愤怒类"))
//					emotionList.add("anger");
//				else if(rs2.getString("class").startsWith("悲伤类"))
//					emotionList.add("sadness");
//				else if(rs2.getString("class").startsWith("害怕类"))
//					emotionList.add("fear");
//				else if(rs2.getString("class").startsWith("讨厌类"))
//					emotionList.add("disgust");
//				else if(rs2.getString("class").startsWith("惊讶类"))
//					emotionList.add("surprise");
//				else
//					emotionList.add("none");
//			}
			//System.out.println(emotionList);
			con.close();

			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
