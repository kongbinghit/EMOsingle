

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class FileOperation {

	public static Vector<String> getLines(String path) throws IOException {
		// TODO Auto-generated method stub
		Vector<String> lines = new Vector<String>();
		File f = new File(path);// "d:/data/weibo/validIPs.txt"
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		String s;
		while ((s = br.readLine()) != null) {
			lines.add(s);
		}
		br.close();

		return lines;
	}

	public static void write2txt(Vector<String> vector, String savePath)
			throws IOException {
		// TODO Auto-generated method stub
		File f = new File(savePath);//
		FileWriter fw = new FileWriter(f);
		BufferedWriter bw = new BufferedWriter(fw);
		for (int i = 0; i < vector.size(); i++) {
			bw.write(vector.get(i) + "\r\n");
			// System.out.println(vector.get(i));
		}
		bw.close();
	}

	/**
	 * ��Stringд�������ļ�
	 * 
	 * @param s
	 * @param savePath
	 * @throws IOException
	 */
	public static void writeString(String s, String savePath)
			throws IOException {
		// TODO Auto-generated method stub
		File f = new File(savePath);
		FileWriter fw = new FileWriter(f);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(s);
		bw.close();
	}

	/**
	 * ��html�ļ��õ�΢��
	 * 
	 * @param html
	 * @return
	 * @throws IOException
	 */
	public static String html2String(String htmlPath) throws IOException {
		String html = "";
		File f = new File(htmlPath);
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		String s;
		while ((s = br.readLine()) != null) {
			html += s;
		}
		br.close();

		return html;
	}

	/**
	 * ��ĳ�ؼ�����������΢��ȫд���ļ���ȥ �����д����txt��ʽ
	 * 
	 * @param vector
	 * @param savePath
	 * @throws IOException
	 */
	public void writeVector(Vector<String> vector, String savePath)
			throws IOException {
		// TODO Auto-generated method stub
		File f = new File(savePath);
		FileWriter fw = new FileWriter(f);
		BufferedWriter bw = new BufferedWriter(fw);
		for (int i = 0; i < vector.size(); i++) {
			bw.write(vector.get(i) + "\r\n");
		}
		bw.close();
	}

}
