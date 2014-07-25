package balance;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class MAP {

	public static void main(String[] args) {

		String pathinput = "C:/Users/lin/Desktop/test.txt";
		String pathoutput = "C:/Users/lin/Desktop/retest.map";

		File file = new File(pathinput);
		BufferedReader reader = null;

		try {

	
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;

			OutputStreamWriter writer = new OutputStreamWriter(
					new FileOutputStream(pathoutput), "UTF-8");
		
			int sno=0;
			int pno=0;
			int no=-1;
			int repno=0;
			while ((tempString = reader.readLine()) != null) {
				// find weiboid
				String map;
				String t[]=tempString.split(",");
				no++;
				if(pno!=Integer.parseInt(t[1].substring(2))){
					sno=0;
					pno=Integer.parseInt(t[1].substring(2));
					
					repno++;
				}
				map=no+" cet_"+repno+"_P1_S"+(++sno);
				writer.write(map+"\r\n");
				System.out.println(map);
				
				
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
