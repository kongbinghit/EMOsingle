import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class FromSentenceToWeibo {

	public static void main(String[] args) {
		String pathinput = "C:/Users/lin/Desktop/myemotion/sentence/out_neighbor100.prediction";
		String pathinput2 = "C:/Users/lin/Desktop/myemotion/sentence/EMO2_withID.map";

		String pathoutputmulan = "C:/Users/lin/Desktop/myemotion/sentence/res.txt";

		

		File file = new File(pathinput);
		File unigramfile = new File(pathinput2);
		BufferedReader reader = null;
		BufferedReader reader2 = null;

		
		try {
			reader = new BufferedReader(new FileReader(file));
			reader2 = new BufferedReader(new FileReader(unigramfile));

			String tempString = null;

			OutputStreamWriter writer = new OutputStreamWriter(
					new FileOutputStream(pathoutputmulan), "UTF-8");
			int len0=0,len1=0,len2=0,len3=0;
			String []weibo=new String[10000];
			for(int i=0;i<10000;i++){
				weibo[i]="";
			}
			while ((tempString = reader.readLine()) != null) {
				int weiboid=Integer.parseInt(reader2.readLine());
				System.out.println(weiboid);
				if(!"".equals(tempString)){
					weibo[weiboid-1] =tempString+weibo[weiboid-1];
				}
				
				String t[]=tempString.split(" ");
//				System.out.println(t.length);
				if(t.length==0){
					len0++;
				}else if(t.length==1){
					len1++;
				}else if(t.length==2){
					len2++;
				}else{
					System.out.println(tempString);
					len3++;
				}
			
			}
			for(int w=0;w<10000;w++){
				System.out.println(weibo[w]);
//				writer.write(weibo[w]+"\r\n");
				String[] words = weibo[w].split(" ") ;
				int len = words.length ;
				//考虑到二维数组不方便所以这里使用两个数组，二者之间是一一对应的
				int[] count = new int[len] ;
				String[] ss = new String[len] ;
				for (int i=0;i<words.length;i++) {
					String word = words[i].trim();
					int f = onList(word,words) ;
					if(f == -1){
						ss[i] = word ;
						count[i] = 1 ;				
					}else{
						ss[f] = word ;
						count[f] += 1;
					}
				}
				int index = mostFrequent(count) ;
				System.out.println("出现次数最多的单词是：" + ss[index] + "出现的次数为：" + count[index]);
				writer.write(ss[index]+"\r\n");
			}
			System.out.println(len0);
			System.out.println(len1);
			System.out.println(len2);
			System.out.println(len3);
			reader.close();
			reader2.close();
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
	static public int mostFrequent(int[] list) {
		int max = 0 ;
		int index = 0 ;
		for(int i=0;i<list.length;i++){
			if(max < list[i]){
				max = list[i] ;
				index = i ;
			}
		}
		return index ;
	}
	static public int onList(String word, String[] list) {
		for(int i=0;i<list.length;i++){
			if(word.equals(list[i])){
				return i ;
			}			
		}
		return -1 ;
	}
}
