import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class calcu {

	public static void main(String[] args) {
		String pathinput = "C:/Users/lin/Desktop/myemotion/sentence/res.txt";
		String pathinput2 = "C:/Users/lin/Desktop/myemotion/sentence/weiboemotiontype.txt";

//		String pathoutputmulan = "C:/Users/lin/Desktop/myemotion/sentence/res.txt";

		

		File file = new File(pathinput);
		File unigramfile = new File(pathinput2);
		BufferedReader reader = null;
		BufferedReader reader2 = null;

		
		try {
			reader = new BufferedReader(new FileReader(file));
			reader2 = new BufferedReader(new FileReader(unigramfile));

			String tempString = null;
			int num=0,num2=0;
			while ((tempString = reader.readLine()) != null) {
				String tem2= reader2.readLine();
				if(tempString.equals(tem2)){
					num++;
				}else{
					num2++;
				}
			}
			System.out.println(num);
			System.out.println(num2);
			reader.close();
			reader2.close();

		
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
