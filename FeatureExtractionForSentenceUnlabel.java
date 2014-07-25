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

		String pathinput = "C:/Users/lin/Desktop/myemotion/weibo/unlabel/weibounlabel.xml";
		String pathoutput = "C:/Users/lin/Desktop/myemotion/weibo/unlabel/sentenceunlabel.feature.arff";

		String pathunigram = "C:/Users/lin/Desktop/myemotion/classCHI/allfeature.txt";

		File file = new File(pathinput);
		File unigramfile = new File(pathunigram);
		BufferedReader reader = null;
		BufferedReader readerunigram = null;

		List<String> unigramlist = new ArrayList<String>();
		try {

			Pattern patn = Pattern
					.compile("<sentence id=\"([0-9]*)\">(.*)(</sentence>)");
			reader = new BufferedReader(new FileReader(file));
			readerunigram = new BufferedReader(new FileReader(unigramfile));

			String tempString = null;

			// unigram添加到List
			while ((tempString = readerunigram.readLine()) != null) {
				unigramlist.add(tempString);
			}

			OutputStreamWriter writerarff = new OutputStreamWriter(
					new FileOutputStream(pathoutput), "UTF-8");

			OutputStreamWriter writersentence = new OutputStreamWriter(
					new FileOutputStream("C:/Users/lin/Desktop/myemotion/weibo/unlabel/sentence.txt"), "UTF-8");
			writerarff.write("@relation emotion\r\n\r\n");

			writerarff
					.write("@attribute emotion {happiness,like,anger,sadness,fear,disgust,surprise,none}\r\n");
			// writerarff.write("@attribute emotion {1,2,3,4,5,6,7,8}\r\n");

			for (int i = 0; i < unigramlist.size(); i++) {
				writerarff.write("@attribute f" + (i + 1) + " {0,1}\r\n");
			}

			writerarff.write("\r\n@data\r\n");
			int num = 0;

			while ((tempString = reader.readLine()) != null) {

				String text = "";

				Matcher matchern = patn.matcher(tempString);
				if (matchern.find()) {
					text = matchern.group(2);
					
					
				}
				if (!"".equals(text)) {
					num++;
					writersentence.write(num+":"+text+"\r\n");
					String featureresultarff = "";
					int unigramnum[] = new int[unigramlist.size()];
					for (int u = 0; u < unigramlist.size(); u++) {
						if (text.contains(unigramlist.get(u))) {
							unigramnum[u] = 1;
						}
					}

					for (int n = 0; n < unigramnum.length; n++) {
						if (unigramnum[n] == 1) {
							featureresultarff += (n + 1) + " 1,";
						}
					}

					if (featureresultarff.trim().equals("")) {

						writerarff.write("{0 none}\r\n");

					} else {

						writerarff.write("{0 none,"
								+ featureresultarff.substring(0,
										featureresultarff.length() - 1)
								+ "}\r\n");
					}
				}

			}
			System.out.println(num);

			reader.close();
			writerarff.flush();
			writerarff.close();
			writersentence.flush();
			writersentence.close();
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
