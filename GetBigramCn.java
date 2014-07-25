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

public class GetBigramCn {

	public static void main(String[] args) {
		String pathinput = "C:/Users/lin/Desktop/myemotion/trainData4000-weight.fenci.xml";
		String pathstopword = "C:/Users/lin/Desktop/emotionExp/stopword1208.txt";
		String pathoutput = "C:/Users/lin/Desktop/myemotion/bigram.data";

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
					for (int i = 0; i < wordlist.length - 1; i++) {
						String temp[] = wordlist[i].split("/");
						String temp2[] = wordlist[i + 1].split("/");
						if (temp.length == 2 && temp2.length == 2) {
							String name = temp[0];
							String pos = temp[1];
							String name2 = temp2[0];
							String pos2 = temp2[1];

							if (true) {
								if (!unigramwords.containsKey(name+name2)) {
									unigramwords.put(name+name2, 1);
									// writer.write(name + "\r\n");
									// System.out.println(name);
								} else {
									unigramwords.put(name+name2,
											unigramwords.get(name+name2) + 1);
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
						|| key.contains("？") || key.contains("‘")|| key.contains(",")
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
						// writer.write(entry.getKey() + "\t" + entry.getValue()
						// + "\r\n");
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
