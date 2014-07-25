package v2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeiboSentenceIDUnlabel {

	public static Map<Integer,List<Integer>> getID() {

		Map<Integer,List<Integer>> map=new HashMap<Integer,List<Integer>>();
		String pathinput = "C:/Users/lin/Desktop/myemotion/weibo/unlabel/weibounlabel.fenci2.xml";
		String pathoutputtrain = "weibosentenceidunlabel.txt";
		BufferedReader reader = null;

		try {
			// Pattern patweibo = Pattern.compile("(<weibo id=\")([0-9]*)");
			Pattern patweibo = Pattern
					.compile("(<weibo id=\")([0-9]*)(\">)");

			Pattern patn = Pattern
					.compile("<sentence id=\"([0-9]*)\">(.*)(</sentence>)");
			reader = new BufferedReader(new FileReader(new File(pathinput)));
			String tempString = null;

			OutputStreamWriter writertrain = new OutputStreamWriter(
					new FileOutputStream(pathoutputtrain), "UTF-8");

			
			int num = 0;

			int weiboid = 0;
			boolean iszhuanfa = false;
			List<Integer> list = null;
			while ((tempString = reader.readLine()) != null) {

				// find weiboid
				Matcher matcherweibo = patweibo.matcher(tempString);
				if (matcherweibo.find()) {
					iszhuanfa = false;
					weiboid ++;
					list= new ArrayList<Integer>();
				}

				String text = "";
				// find sentence that opinionated="N"
				Matcher matchern = patn.matcher(tempString);
				if (matchern.find()) {
					text = matchern.group(2);
					if(text.startsWith("///session @"))
						iszhuanfa=true;
					
					if(!iszhuanfa){
						list.add(num++);
					}
				}
				
				if(tempString.contains("</weibo>")){
					map.put(weiboid-1, list);
					writertrain.write(weiboid-1+"\t"+Arrays.toString(list.toArray())+"\r\n");
				}
				
			}
			
			System.out.println(num);
			reader.close();
			writertrain.flush();
			writertrain.close();
			return map;
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
		return map;

	}
	
	
	public static void main(String[] args) {
		Map<Integer,List<Integer>> map=getID();
		
	}
}