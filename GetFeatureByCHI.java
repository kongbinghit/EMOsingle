import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;


public class GetFeatureByCHI {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
			try {
				OutputStreamWriter w = new OutputStreamWriter(
						new FileOutputStream("C:\\Users\\lin\\Desktop\\myemotion\\classCHI\\allfeature.txt"), "UTF-8");
				
				//unigramfeature
				File[] unigramfileList= new File("C:\\Users\\lin\\Desktop\\myemotion\\classCHI\\unigram").listFiles();
				OutputStreamWriter w1 = new OutputStreamWriter(
						new FileOutputStream("C:\\Users\\lin\\Desktop\\myemotion\\classCHI\\unigramfeature.txt"), "UTF-8");
				for(int i=0;i<unigramfileList.length;i++){
					BufferedReader reader = new BufferedReader(new FileReader(unigramfileList[i]));
					String tempString="";
					while((tempString=reader.readLine())!=null){
						String[] s=tempString.split(",");
						if(s.length==2){
							double chivalue=Double.parseDouble(s[1]);
							//select chi>5000
							if(chivalue>4000){
								w.write(s[0]+"\r\n");
								w1.write(s[0]+"\r\n");
							}
						}
					}
				}
				w1.flush();
				w1.close();
				
				//biaoqingfeature
				File[] biaoqingfileList= new File("C:\\Users\\lin\\Desktop\\myemotion\\classCHI\\biaoqing").listFiles();
				OutputStreamWriter w2 = new OutputStreamWriter(
						new FileOutputStream("C:\\Users\\lin\\Desktop\\myemotion\\classCHI\\biaoqingfeature.txt"), "UTF-8");
				for(int i=0;i<biaoqingfileList.length;i++){
					BufferedReader reader = new BufferedReader(new FileReader(biaoqingfileList[i]));
					String tempString="";
					while((tempString=reader.readLine())!=null){
						String[] s=tempString.split(",");
						if(s.length==2){
							double chivalue=Double.parseDouble(s[1]);
							//select chi>100
							if(chivalue>100){
								w.write(s[0]+"\r\n");
								w2.write(s[0]+"\r\n");
							}
						}
					}
				}
				w2.flush();
				w2.close();
				
				//dicfeature
				File[] dicfileList= new File("C:\\Users\\lin\\Desktop\\myemotion\\classCHI\\dic").listFiles();
				OutputStreamWriter w3 = new OutputStreamWriter(
						new FileOutputStream("C:\\Users\\lin\\Desktop\\myemotion\\classCHI\\dicfeature.txt"), "UTF-8");
				for(int i=0;i<dicfileList.length;i++){
					BufferedReader reader = new BufferedReader(new FileReader(dicfileList[i]));
					String tempString="";
					while((tempString=reader.readLine())!=null){
						String[] s=tempString.split(",");
						if(s.length==2){
							double chivalue=Double.parseDouble(s[1]);
							//select chi>100
							if(chivalue>100){
								w.write(s[0]+"\r\n");
								w3.write(s[0]+"\r\n");
							}
						}
					}
				}
				w3.flush();
				w3.close();
				
				
				//wangluocifeature
				File[] wlcfileList= new File("C:\\Users\\lin\\Desktop\\myemotion\\classCHI\\wlc").listFiles();
				OutputStreamWriter w4 = new OutputStreamWriter(
						new FileOutputStream("C:\\Users\\lin\\Desktop\\myemotion\\classCHI\\wlcfeature.txt"), "UTF-8");
				for(int i=0;i<wlcfileList.length;i++){
					BufferedReader reader = new BufferedReader(new FileReader(wlcfileList[i]));
					String tempString="";
					while((tempString=reader.readLine())!=null){
						String[] s=tempString.split(",");
						if(s.length==2){
							double chivalue=Double.parseDouble(s[1]);
							//select chi>2000
							if(chivalue>3000){
								w.write(s[0]+"\r\n");
								w4.write(s[0]+"\r\n");
							}
						}
					}
				}
				w4.flush();
				w4.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		
	}

}
