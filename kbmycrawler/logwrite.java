package kbmycrawler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class logwrite {
 public static void write(String path,String info){
	 File file=new File(path);
		FileWriter fileWriter;
		Date date=new Date();
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time=df.format(date);
		try {
			fileWriter = new FileWriter(file,true);
			BufferedWriter bufferedWriter=new BufferedWriter(fileWriter);
			bufferedWriter.write(time+":      "+info+"\r\n"); 
			bufferedWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 }

}
