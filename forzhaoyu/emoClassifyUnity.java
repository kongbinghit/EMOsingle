package forzhaoyu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fenci.yu.fenci;

public class emoClassifyUnity {

	public static void main(String[] args) {
//		List<String[]> list = emoClassifyUnity.emoClassify("date/5.xml");
//		for(int i=0;i<list.size();i++){
//			System.out.println(list.get(i)[0]+list.get(i)[1]+list.get(i)[2]);
//		}
	/*	// TODO Auto-generated method stub
		List<String[]> list= new ArrayList<>();
		List<String[]> finalList= new ArrayList<>();
		String pathinput = "date/2.xml";// input file
		String pathfenci = fenci.weiboFenci(pathinput);
		String pathfeature = FeatureExtractionForWeiboUnlabel.featureExtraction(pathfenci);
		list = BaseWeka.emotionClassify(pathfeature);
		//System.out.println(list.get(0)[1]);
		
		File file = new File(pathinput);
		String tempString = null;
		int k = 0;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			Pattern pat = Pattern.compile("<weibo .*>(.*)</weibo>");
			while ((tempString = reader.readLine()) != null) {
				Matcher m= pat.matcher(tempString);
				//String text = tempString;
				if(m.find()){
					finalList.add(new String[]{list.get(k)[0],list.get(k)[1],m.group(1)});
					k++;
				}
			}
			for(int i=0;i<k;i++)
				System.out.println(finalList.get(i)[0]+finalList.get(i)[1]+finalList.get(i)[2]);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
	public  List<String[]> emoClassify(String path){
		List<String[]> list= new ArrayList<>();
		List<String[]> finalList= new ArrayList<>();
		//String pathinput = "date/1.xml";// input file
		String pathinput = path;
		
		String pathfenci = fenci.weiboFenci(pathinput);
	
		String pathfeature = FeatureExtractionForWeiboUnlabel.featureExtraction(pathfenci);
	
		list = BaseWeka.emotionClassify(pathfeature);
		
		File file = new File(pathinput);
		String tempString = null;
		int k = 0;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			Pattern pat = Pattern.compile("<weibo .*>(.*)</weibo>");
			while ((tempString = reader.readLine()) != null) {
				Matcher m= pat.matcher(tempString);
				//String text = tempString;
				if(m.find()){			
					finalList.add(new String[]{list.get(k)[0],list.get(k)[1],m.group(1)});
					k++;
				}
			}
			
			return finalList;
//			for(int i=0;i<k;i++)
//				System.out.println(finalList.get(i)[0]+finalList.get(i)[1]+finalList.get(i)[2]);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
