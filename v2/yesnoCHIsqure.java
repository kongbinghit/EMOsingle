package v2;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class yesnoCHIsqure {

	public static List<String> setWords;
	public static List<String> setSentences;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
			setWords = new ArrayList<String>();
			setSentences = new ArrayList<String>();
			BufferedReader reader = new BufferedReader(new FileReader(new File(
					"C:/Users/lin/Desktop/myemotion/v2/unigram.data")));
			String tempString;
			while ((tempString = reader.readLine()) != null) {
				setWords.add(tempString);
			}
			Pattern patn = Pattern
					.compile("(<sentence id=\"[0-9]*\" opinionated=\"N\">)(.*)(</sentence>)");
			Pattern paty = Pattern
					.compile("(<sentence id=\"[0-9]*\" opinionated=\"Y\" emotion-1-type=\")(.*)(\" emotion-1-weight=\"[1-3]\" emotion-2-type=\")(none|happiness|like|sadness|anger|fear|disgust|surprise)(\".*>)(.*)(</sentence>)");

			BufferedReader reader2 = new BufferedReader(
					new FileReader(
							new File(
									"C:/Users/lin/Desktop/myemotion/v2/TrainData.txt")));
			int num = 0;
			while ((tempString = reader2.readLine()) != null) {
				Matcher matchern = patn.matcher(tempString);
				if (matchern.find()) {
					setSentences.add("no\t" + matchern.group(2));
					num++;
				}

				// find sentence that opinionated="Y"
				Matcher matchery = paty.matcher(tempString);
				if (matchery.find()) {
					setSentences.add("yes\t" + matchery.group(6));
					num++;
				}

			}
			System.out.println(num);
			System.out.println(setWords.size());
			System.out.println(setSentences.size());
			reader.close();
			reader2.close();
			calc();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	private static void calc() {
		String[] classes = { "no", "yes" };
		Iterator<String> itWords = setWords.iterator();
		try {
			OutputStreamWriter w1 = new OutputStreamWriter(
					new FileOutputStream("C:/Users/lin/Desktop/myemotion/v2/trainbigramno.csv"), "UTF-8");
			OutputStreamWriter w2 = new OutputStreamWriter(
					new FileOutputStream("C:/Users/lin/Desktop/myemotion/v2/trainbigramyes.csv"), "UTF-8");
			

			Map<String, String> map = new HashMap<String, String>();
			map.put("no", "");
			map.put("yes", "");
			while (itWords.hasNext()) {
				String word = itWords.next();
//				System.out.println(++count + ":" + word + "of"
//						+ setWords.size());
				String classs = "";
				for (int i = 0; i < classes.length; i++) {
					classs = classes[i];
					int a = 0, b = 0, c = 0, d = 0;
					Iterator<String> it = setSentences.iterator();
					while (it.hasNext()) {
						String[] its = it.next().split("\t");
						if (its[0].contains(classs)) {// 属于某个类
							if (its[1].contains(word)) {
								a++;
							} else {
								c++;
							}
						} else {// 不属于某个类
							if (its[1].contains(word)) {
								b++;
							} else {
								d++;
							}
						}
					}
					double chi = 1.0 * (a * d - b * c) * (a * d - b * c)
							/ ((a + b) * (c + d));
					map.put(classs, map.get(classs)+word+","+chi+"\r\n");
//					System.out.println(a + " " + b + " " + c + " " + d
//							+ "      " + word + "和" + classs + "的卡方为" + chi);
				}
			}
			w1.write(map.get("no").toString().replaceAll("NaN", "0"));
			w2.write(map.get("yes").toString().replaceAll("NaN", "0"));
			
			System.out.println(setSentences.size());
			w1.close();
			w2.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
