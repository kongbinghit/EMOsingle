package fenci.yu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fenci.yu.NlpirTest.CLibrary;

public class fenci {

	public static void main(String[] args) throws UnsupportedEncodingException {
		
		String argu = "";
		// String system_charset = "GBK";//GBK----0
		String system_charset = "UTF-8";
		int charset_type = 1;
		// int charset_type = 0;
		// 调用printf打印信息
		int init_flag = CLibrary.Instance.NLPIR_Init(argu
				.getBytes(system_charset), charset_type, "0"
				.getBytes(system_charset));

		if (0 == init_flag) {
			System.err.println("初始化失败！");
			return;
		}

		// TODO Auto-generated method stub
		String pathinput = "date/3.xml";// input file
		//String pathoutput = "date/weibofenci.xml";// output file
		String pathoutput = pathinput.substring(0, pathinput.indexOf("."))+"fenci.txt";// output file
		File file = new File(pathinput); 

		BufferedReader reader = null;

		int n = 0;
		try {

			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			Pattern pat = Pattern.compile("(<weibo .*>)(.*)(</weibo>)");

			// FileWriter writer = new FileWriter(pathoutput);
			// BufferedWriter writer = new BufferedWriter(new FileWriter(new
			// File(pathoutput)));
			OutputStreamWriter writer = new OutputStreamWriter(
					new FileOutputStream(pathoutput), "UTF-8");
			// osw.write(toStr);

			while ((tempString = reader.readLine()) != null) {
				Matcher matcher= pat.matcher(tempString);
				String text = tempString;
				if(matcher.find()){
//					System.out.println(matcher.group(2));
					String fenci=NlpirMethod.NLPIR_ParagraphProcess(matcher.group(2), 2);
					System.out.println(fenci);
					writer.write(fenci+"\r\n");
					text=matcher.group(1)+fenci+matcher.group(3);
				}
				//writer.write(text+"\r\n");
				
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
	
	public static String weiboFenci(String path){
		String argu = "";
		//String system_charset = "GBK";//GBK----0
		String system_charset = "UTF-8";
		int charset_type = 1;
		//int charset_type = 0;
		// 调用printf打印信息
		
		try {
			int init_flag;
			init_flag = CLibrary.Instance.NLPIR_Init(argu
					.getBytes(system_charset), charset_type, "0"
					.getBytes(system_charset));
			if (0 == init_flag) {
				System.err.println("初始化失败！");
				return null;
			}
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		System.out.println(path);

		// TODO Auto-generated method stub
		//String pathinput = "date/3.xml";// input file
		String pathinput =new String( path);// input file
		//String pathoutput = "date/weibofenci.txt";// output file
		String pathoutput =new String("."+ path.substring(1,path.length()-4)+"fenci.txt");// output file
System.out.println(pathoutput);
		File file = new File(pathinput); 

		BufferedReader reader = null;

		int n = 0;
		try {

			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			Pattern pat = Pattern.compile("(<weibo .*>)(.*)(</weibo>)");

			// FileWriter writer = new FileWriter(pathoutput);
			// BufferedWriter writer = new BufferedWriter(new FileWriter(new
			// File(pathoutput)));
			OutputStreamWriter writer = new OutputStreamWriter(
					new FileOutputStream(pathoutput), "UTF-8");
			// osw.write(toStr);

			while ((tempString = reader.readLine()) != null) {
				Matcher matcher= pat.matcher(tempString);
				String text = tempString;
				if(matcher.find()){
//					System.out.println(matcher.group(2));
					String fenci=NlpirMethod.NLPIR_ParagraphProcess(matcher.group(2), 2);
					System.out.println(fenci);
					writer.write(fenci+"\r\n");
					text=matcher.group(1)+fenci+matcher.group(3);
				}
				//writer.write(text+"\r\n");
				
			}
			reader.close();
			writer.flush();
			writer.close();
		
			return pathoutput;
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
		return null;
	}

}
