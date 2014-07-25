package balance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class TEst {

	public static void main(String[] args) {

		String pathinput = "C:/Users/lin/Desktop/train_weibo_balanced.arff";
		String pathoutput = "C:/Users/lin/Desktop/train_weibo_balanced.remap";

		File file = new File(pathinput);
		BufferedReader reader = null;

		try {

			reader = new BufferedReader(new FileReader(file));
			String tempString = null;

			OutputStreamWriter writer = new OutputStreamWriter(
					new FileOutputStream(pathoutput), "UTF-8");

			int num=4001;
			boolean tag=false;
			int num1=13161;
			while ((tempString = reader.readLine()) != null) {
				// find weiboid
				if (tempString.contains(", 7 800001, ")) {
					tag=true;
				}
				if(tag){
//					for(int i=0;i<7;i++){
//						if(tempString.contains(i+" 1, ")){
//							System.out.println(i);
//							writer.write(i+"\r\n");
//						}
//					}
					writer.write((num1++)+" cet_"+(num++)+"_P1_S1\r\n");
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
