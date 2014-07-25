import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class AVPpingce2 {

	public static void main(String[] args) {

		String pathinput = "C:/Users/lin/Desktop/OurResult10000Sentence.txt";
		String pathinput2 = "C:/Users/lin/Desktop/北信科大微博情感挖掘系统_中文微博情绪识别_2_CLOSE_2.txt";
		String pathoutput = "C:/Users/lin/Desktop/res.txt";

		File file = new File(pathinput);
		File file2 = new File(pathinput2);
		BufferedReader reader = null;
		BufferedReader reader2 = null;

		try {

			reader = new BufferedReader(new FileReader(file));
			reader2 = new BufferedReader(new FileReader(file2));
			String tempString = null;

			OutputStreamWriter writer = new OutputStreamWriter(
					new FileOutputStream(pathoutput), "UTF-8");
			int num = 0, num2 = 0;
			String tempString2 = null;
			float score = 0;
			while ((tempString = reader.readLine()) != null) {

				tempString2 = reader2.readLine();
				int y1 = 0, y2 = 0, x1 = 0, x2 = 0;

				String t[] = tempString.replaceAll("happiness", "0")
						.replaceAll("likeness", "1").replaceAll("anger", "2")
						.replaceAll("sadness", "3").replaceAll("fear", "4")
						.replaceAll("disgust", "5").replaceAll("surprise", "6")
						.replaceAll("none", "7").split(" ");
				String t2[] = tempString2.replaceAll("happiness", "0")
						.replaceAll("like", "1").replaceAll("anger", "2")
						.replaceAll("sadness", "3").replaceAll("fear", "4")
						.replaceAll("disgust", "5").replaceAll("surprise", "6")
						.replaceAll("none", "7").split(" ");
				if (!t[2].equals("7")) {
					y1 = Integer.parseInt(t[2]) + 1;
				}
				if (!t[3].equals("7")) {
					y2 = Integer.parseInt(t[3]) + 1;
				}
				if (!t2[6].equals("7")) {
					x1 = Integer.parseInt(t2[6]) + 1;
				}
				if (!t2[7].equals("7")) {
					x2 = Integer.parseInt(t2[7]) + 1;
				}
				if (y1 != 0) {
					num++;
					if (x1 == y1) {
						score++;
						if (x2 == y2) {
							score += 0.5;
						}
					} else if (x1 != 0) {
						if (x1 == y2) {
							score += 0.5;
						} else if (x2 == y2) {
							score += 0.5;
						}

					}
				}
				// System.out.println(y1 + " " + y2 + " " + " " + x1 + " " +
				// x2);
			}
			System.out.println(num);
			System.out.println(score / (num * 2));
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
