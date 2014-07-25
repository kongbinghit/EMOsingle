import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
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

public class Biaoqing {

	public static void main(String[] args) {
		String pathinput = "C:/Users/lin/Desktop/myemotion/trainData4000-weight.xml";
		String pathinput2 = "C:/Users/lin/Desktop/myemotion/testData10000-weight.xml";
		String pathoutput = "C:/Users/lin/Desktop/myemotion/biaoqing.txt";

		File file = new File(pathinput);

		BufferedReader readerstop = null;

		try {
			Pattern pat = Pattern.compile("\\[[^\\]^\\[]+\\]");
			String tempString = null;
			Map<String, Integer> biaoqingwords = new HashMap<String, Integer>();

			readerstop = new BufferedReader(new FileReader(file));
			BufferedReader readerstop2 = new BufferedReader(new FileReader(new File(pathinput2)));
			while ((tempString = readerstop.readLine()) != null) {
//				System.out.println(tempString);
				Matcher matcher=pat.matcher(tempString);
				while(matcher.find()){
					String biaoqing=matcher.group(0);
					if(biaoqingwords.containsKey(biaoqing)){
						biaoqingwords.put(biaoqing,biaoqingwords.get(biaoqing)+1);
					}else{
						biaoqingwords.put(biaoqing, 1);
//						System.out.println(biaoqing);
					}
					
				}
			}
			while ((tempString = readerstop2.readLine()) != null) {
//				System.out.println(tempString);
				Matcher matcher=pat.matcher(tempString);
				while(matcher.find()){
					String biaoqing=matcher.group(0);
					if(biaoqingwords.containsKey(biaoqing)){
						biaoqingwords.put(biaoqing,biaoqingwords.get(biaoqing)+1);
					}else{
						biaoqingwords.put(biaoqing, 1);
//						System.out.println(biaoqing);
					}
					
				}
			}
			biaoqingwords=sortByValue(biaoqingwords,true);
			OutputStreamWriter writer = new OutputStreamWriter(
					new FileOutputStream(pathoutput), "UTF-8");
			Set<Map.Entry<String, Integer>> set = biaoqingwords.entrySet();
			for (Iterator<Map.Entry<String, Integer>> it = set.iterator(); it
					.hasNext();) {
				Map.Entry<String, Integer> entry = it
						.next();

				String key = entry.getKey();
				int value=entry.getValue();
				System.out.println(key+"\t"+value);
//				if(value>1)
				writer.write(key+"\n");
			}
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
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
