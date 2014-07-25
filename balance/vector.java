package balance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class vector {

	public static void main(String[] args) {

		String pathinput = "C:/Users/lin/Desktop/train_weibo_balanced.arff";
		String pathoutput = "C:/Users/lin/Desktop/train_weibo_balanced(chongxinbianUniqueID).arff";

		File file = new File(pathinput);
		BufferedReader reader = null;

		try {

			reader = new BufferedReader(new FileReader(file));
			String tempString = null;

			OutputStreamWriter writer = new OutputStreamWriter(
					new FileOutputStream(pathoutput), "UTF-8");

			int num=0;
			while ((tempString = reader.readLine()) != null) {
				// find weiboid
				if (tempString.contains(" {0 ")) {
//					num++;
					String t = tempString.substring(4,5);
//					System.out.println(t);
					for (int i = 1; i < 7; i++) {
						String begin=", "+i+" ";
						String end=", "+(i+1)+" ";
//						System.out.println(begin+end);
						t+=","+tempString.substring(tempString.indexOf(begin)+begin.length(),tempString.indexOf(end));
					}
					t+=","+num++;
					for (int i = 8; i < 207; i++) {
						String begin=", "+i+" ";
						String end=", "+(i+1)+" ";
//						System.out.println(begin+end);
						t+=","+tempString.substring(tempString.indexOf(begin)+begin.length(),tempString.indexOf(end));
					}
					t+=","+tempString.substring(tempString.indexOf(", 207 ")+6,tempString.indexOf(" }"));
//					System.out.println(t);
					writer.write(t+"\r\n");
				} else {
//					System.out.println(tempString);
					writer.write(tempString+"\r\n");
				}
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
