package hownet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;

public class Hownet {

	public static void main(String[] args) {
		String pathinput = "C:/Users/lin/Desktop/myemotion/hownet/HowNet.txt";
		String pathoutput = "C:/Users/lin/Desktop/myemotion/hownet/AHowNet.txt";

		File file = new File(pathinput);

		BufferedReader readerstop = null;

		try {
			OutputStreamWriter writer = new OutputStreamWriter(
					new FileOutputStream(pathoutput), "UTF-8");
			String tempString = null;
//			Map<String, Integer> biaoqingwords = new HashMap<String, Integer>();

			readerstop = new BufferedReader(new FileReader(file));
			while ((tempString = readerstop.readLine()) != null) {
				
				if(tempString.contains("W_C=")){
					writer.write(tempString.replaceAll("W_C=", "")+"\t");
				}else if(tempString.contains("G_C=")){
					String s[]=tempString.split(" ");
					writer.write(s[0].replaceAll("G_C=", "")+"\t");
				}else if(tempString.contains("DEF=")){
					writer.write(tempString.replaceAll("DEF=", "")+"\r\n");
				}
			}
			
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}

	}
}
