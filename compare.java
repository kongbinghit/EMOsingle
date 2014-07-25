import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class compare {

	public static void main(String[] args) {
		String pathinput1 = "C:/Users/lin/Desktop/myemotion/weibo/testData.feature.ouput";
		String pathinput2 = "C:/Users/lin/Desktop/myemotion/weibo/testweibo.txt";
		String pathoutputarff = "C:/Users/lin/Desktop/myemotion/weibo/compare.txt";
	
		File file1 = new File(pathinput1);
		File file2 = new File(pathinput2);
		BufferedReader reader1 = null;
		BufferedReader reader2 = null;

		try {
			reader1 = new BufferedReader(new FileReader(file1));
			reader2 = new BufferedReader(new FileReader(file2));

			String tempString = null;

			OutputStreamWriter writerarff = new OutputStreamWriter(
					new FileOutputStream(pathoutputarff), "UTF-8");

			
			while ((tempString = reader1.readLine()) != null) {
				tempString=tempString.replace("1.0", "happiness")
						.replace("2.0", "like").replace("3.0", "anger")
						.replace("4.0", "sadness").replace("5.0", "fear")
						.replace("6.0", "disgust")
						.replace("7.0", "surprise").replace("8.0", "none");
				String tem2=reader2.readLine();
				writerarff.write(tempString+"\t"+tem2+"\r\n");
				
			}
			reader1.close();
			reader2.close();
			writerarff.flush();
			writerarff.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader1 != null) {
				try {
					reader1.close();
				} catch (IOException e1) {
				}
			}
		}

	}

}
