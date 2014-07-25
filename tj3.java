import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class tj3 {

	public static void main(String[] args) {

		String pathinput = "C:/Users/lin/Desktop/myemotion/trainData4000-weight.xml";
		String pathinput2 = "C:/Users/lin/Desktop/myemotion/testData10000-weight.xml";
		String pathoutput = "C:/Users/lin/Desktop/tj3.csv";

		File file = new File(pathinput);
		File file2 = new File(pathinput2);
		BufferedReader reader = null;
		BufferedReader reader2 = null;

		try {
			Map<String, Integer> weibomap = new HashMap<String, Integer>();
			weibomap.put("happiness", 0);
			weibomap.put("like", 1);
			weibomap.put("anger", 2);
			weibomap.put("sadness", 3);
			weibomap.put("fear", 4);
			weibomap.put("disgust", 5);
			weibomap.put("surprise", 6);
			weibomap.put("none", 7);
			Map<String, Integer> sentencemap = new HashMap<String, Integer>();
			sentencemap.put("happiness", 0);
			sentencemap.put("like", 1);
			sentencemap.put("anger", 2);
			sentencemap.put("sadness", 3);
			sentencemap.put("fear", 4);
			sentencemap.put("disgust", 5);
			sentencemap.put("surprise", 6);
			sentencemap.put("none", 7);

			double[] emo1 = new double[7];
			double[] emo2 = new double[7];
			int[] emo1num = new int[7];
			int[] emo2num = new int[7];
			Pattern pat1 = Pattern
					.compile("<sentence id=\"[0-9]*\" opinionated=\"Y\" emotion-1-type=\"(.*)\" emotion-1-weight=\"([1-3])\" emotion-2-type=\"none\">.*</sentence>");
			Pattern pat2 = Pattern
					.compile("<sentence id=\"[0-9]*\" opinionated=\"Y\" emotion-1-type=\"(.*)\" emotion-1-weight=\"([1-3])\" emotion-2-type=\"(happiness|like|sadness|anger|fear|disgust|surprise)\" emotion-2-weight=\"([1-3])\">.*</sentence>");
			reader = new BufferedReader(new FileReader(file));
			reader2 = new BufferedReader(new FileReader(file2));
			String tempString = null;

			OutputStreamWriter writer = new OutputStreamWriter(
					new FileOutputStream(pathoutput), "UTF-8");
			writer.write(",happiness,like,anger,sadness,fear,disgust,surprise,none\r\n");
			int num = 0, num2 = 0;

			String emotiontype1 = "none";
			String emotiontype2 = "none";
			String firstemo="";
			String secondemo="";
			int weight1=0,weight2=0;
			boolean begin=false;
			while ((tempString = reader.readLine()) != null) {
				// find sentence that opinionated="N"
				Matcher matchern = pat1.matcher(tempString);
				if (matchern.find()) {
					emotiontype1=matchern.group(1);
					weight1=Integer.parseInt(matchern.group(2));
					emo1[sentencemap.get(emotiontype1)]+=weight1;
					emo1num[sentencemap.get(emotiontype1)]++;
				}

				// find sentence that opinionated="Y"
				Matcher matchery = pat2.matcher(tempString);
				if (matchery.find()) {
					emotiontype1=matchery.group(1);
					weight1=Integer.parseInt(matchery.group(2));
					emo1[sentencemap.get(emotiontype1)]+=weight1;
					System.out.println(emotiontype1+weight1);
					emotiontype2=matchery.group(3);
					weight2=Integer.parseInt(matchery.group(4));
					emo2[sentencemap.get(emotiontype2)]+=weight2;
					System.out.println(emotiontype2+weight2);
					emo1num[sentencemap.get(emotiontype1)]++;
					emo2num[sentencemap.get(emotiontype2)]++;
				}

			}

			while ((tempString = reader2.readLine()) != null) {
				// find sentence that opinionated="N"
				Matcher matchern = pat1.matcher(tempString);
				if (matchern.find()) {
					emotiontype1=matchern.group(1);
					weight1=Integer.parseInt(matchern.group(2));
					emo1[sentencemap.get(emotiontype1)]+=weight1;
					emo1num[sentencemap.get(emotiontype1)]++;
				}

				// find sentence that opinionated="Y"
				Matcher matchery = pat2.matcher(tempString);
				if (matchery.find()) {
					emotiontype1=matchery.group(1);
					weight1=Integer.parseInt(matchery.group(2));
					emo1[sentencemap.get(emotiontype1)]+=weight1;
					
					emotiontype2=matchery.group(3);
					weight2=Integer.parseInt(matchery.group(4));
					emo2[sentencemap.get(emotiontype2)]+=weight2;
					
					emo1num[sentencemap.get(emotiontype1)]++;
					emo2num[sentencemap.get(emotiontype2)]++;
				}

			}
			for (int i = 0; i < 7; i++) {

				System.out.println(emo1[i]/emo1num[i]+","+emo2[i]/emo2num[i]);
				writer.write(emo1[i]/emo1num[i]+","+emo2[i]/emo2num[i]+"\r\n");
			}
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
