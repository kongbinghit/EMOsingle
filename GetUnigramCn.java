import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetUnigramCn {

	public static void main(String[] args) {
		String pathinput = "C:/Users/lin/Desktop/myemotion/trainData4000-weight.fenci.xml";
		String pathstopword = "C:/Users/lin/Desktop/emotionExp/stopword1208.txt";
		String pathoutput = "C:/Users/lin/Desktop/myemotion/unigram.data";

		File file = new File(pathinput);
		File filestopwords = new File(pathstopword);

		BufferedReader reader = null;
		BufferedReader readerstop = null;

		try {
			Pattern pat = Pattern.compile("(<sentence .*>)(.*)(</sentence>)");
			String tempString = null;
			Map<String, String> stopwords = new HashMap<String, String>();

			readerstop = new BufferedReader(new FileReader(filestopwords));
			while ((tempString = readerstop.readLine()) != null) {
				System.out.println(tempString);
				stopwords.put(tempString, "");
			}
			OutputStreamWriter writer = new OutputStreamWriter(
					new FileOutputStream(pathoutput), "UTF-8");

			Map<String, Integer> unigramwords = new HashMap<String, Integer>();
			reader = new BufferedReader(new FileReader(file));
			while ((tempString = reader.readLine()) != null) {
				Matcher matcher = pat.matcher(tempString);
				String text = "";
				if (matcher.find()) {
					text = matcher.group(2);
					String wordlist[] = text.split(" ");
					for (int i = 0; i < wordlist.length; i++) {
						String temp[] = wordlist[i].split("/");
						if (temp.length == 2) {
							String name = temp[0];
							String pos = temp[1];

							if ("v".equals(pos) || "n".equals(pos)
									|| "nl".equals(pos) || "ng".equals(pos)
									|| "vd".equals(pos) || "vl".equals(pos)
									|| "vx".equals(pos) || "vg".equals(pos)
									|| "vi".equals(pos) || "vn".equals(pos)
									|| "a".equals(pos)|| "ad".equals(pos)
									|| "al".equals(pos)
									|| "an".equals(pos)
									|| "ag".equals(pos)
									|| "z".equals(pos)|| "ry".equals(pos)
									|| "d".equals(pos)
									|| "rys".equals(pos)
									|| "e".equals(pos)) {
								if (!unigramwords.containsKey(name)) {
									unigramwords.put(name, 1);
									// writer.write(name + "\r\n");
									// System.out.println(name);
								} else {
									unigramwords.put(name,
											unigramwords.get(name) + 1);
								}

							}

						}
					}
				}

			}
			unigramwords = sortByValue(unigramwords, true);
			Set<Map.Entry<String, Integer>> set = unigramwords.entrySet();
			for (Iterator<Map.Entry<String, Integer>> it = set.iterator(); it
					.hasNext();) {
				Map.Entry<String, Integer> entry = it
						.next();

				String key = entry.getKey();
				if (key.contains("。") || key.contains("。") || key.contains("，")
						|| key.contains("？") || key.contains("‘")
						|| key.contains("！") || key.contains("“")
						|| key.contains("）") || key.contains("（")
						|| key.contains("）") || key.contains("…")
						|| key.contains("；") || key.contains(";")
						|| key.contains("&") || key.contains("”")
						|| key.contains("：") || key.contains("——")
						|| key.contains("、") || key.contains("《")
						|| key.contains("`") || key.contains("-")
						|| key.contains("`") || key.contains("*")
						|| key.contains(".")) {

				} else {
					if (entry.getValue() > 1
							&& !stopwords.containsKey(entry.getKey())) {
//						 writer.write(entry.getKey() + "\t" + entry.getValue()
//						 + "\r\n");
						System.out.println(entry.getKey() + "--->"
								+ entry.getValue());
						writer.write(entry.getKey() + "\r\n");
					}

				}
			}
			System.out.println(unigramwords.size());
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

	public static Map sortByValue(Map<String, Integer> map,
			final boolean reverse) {
		List list = new LinkedList(map.entrySet());
		Collections.sort(list, new Comparator() {
			@Override
			public int compare(Object o1, Object o2) {
				if (reverse) {
					return -((Comparable) ((Map.Entry) o1).getValue())
							.compareTo(((Map.Entry) o2).getValue());
				}
				return ((Comparable) ((Map.Entry) o1).getValue())
						.compareTo(((Map.Entry) o2).getValue());
			}
		});

		Map result = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}
}
