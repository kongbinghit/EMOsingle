package kbmycrawler;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.ClientProtocolException;

public class Crawler {
	/**
	 *是否存在制定页数的搜索结果页面
	 * 
	 * @param html
	 * @return
	 */
	public boolean isExistResult(String html) {
		boolean isExist = true;
		Pattern pExist = Pattern.compile("抱歉，没有找到.+?span>相关的结果");// ����Գ��Ը�ؼ�ʣ��ٴ�����������ʾָ��ҳû�н��
		Matcher mExist = pExist.matcher(html);
		if (mExist.find()) {
			isExist = false;
		}
		return isExist;
	}

	public static String getMyTime() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHH");
		String today = dateFormat.format(date);
		// System.out.println(today);

		return today;
	}

	public void excute(String[] args,String dir)
			throws ClientProtocolException, URISyntaxException, IOException,
			InterruptedException {
		/*
		 * args: searchwords, saveHTMLPath, saveTXTPath, SaveXMLPath,
		 * plainIPsPath
		 */

		long t1 = System.currentTimeMillis();
	
		String words = args[0];
		words = words.replaceAll("\n", " ");
		String[] searchwords = words.split(" ");
		for (int i = 0; i < searchwords.length; i++) {
			System.out.println(searchwords[i]);
		}
		String saveHTMLPath = args[1];
		String saveTXTPath = args[2];
		String saveXMLPath = args[3];
		String plainIPsPath = args[4];
		String pageNum = args[5];

		// String[] searchwords = {};
		
		System.out.println("Today is " + dir);
		
		File dirGetweiboSub = new File(saveHTMLPath + "/" + dir);
		dirGetweiboSub.mkdirs();
		File dirWeibostxtSub = new File(saveTXTPath + "/" + dir);
		dirWeibostxtSub.mkdirs();
		File dirWeibosxmlSub = new File(saveXMLPath + "/" + dir);
		dirWeibosxmlSub.mkdirs();
		Vector<String> ip = new Vector<String>();
		ip = FileOperation.getLines(plainIPsPath);
		if (ip == null) {
			System.out.println("再给定路径下找不到plainIP.txt文件");
			
		}
		int ipNum = ip.size();
		int iIP = 0;

		for (int n = 0; n < searchwords.length; n++) {

			String searchword = searchwords[n];
			String dirPath = saveHTMLPath + "/" + dir + "/" + searchword;
			File f = new File(dirPath);
			f.mkdirs();// 创建文件夹，另一方法mkdirs创建多层未创建的文件夹
			int totalPage = Integer.parseInt(pageNum);// 设置相要搜索的页数，搜索范围为该搜索词下的第一页到最终

			System.out.println("****Start getting weibos of the keyword \""
					+ searchword + "\"****");
			
			//将指定的搜索页面html爬取下来并保存
			String html;

			for (int i = totalPage; i > 0; i--) {// 开始爬取，先把一个话题下的html都爬取下来，再利用这些html文件。
				String hostName = ip.get(iIP).split(":")[0];
				int port = Integer.parseInt(ip.get(iIP).split(":")[1]);
				html = new HTML().getHTML("http://s.weibo.com/weibo/"
						+ searchword + "&nodup=1&page=" + String.valueOf(i),
						hostName, port);
				int iReconn = 0;
				while (html.equals("null")) {
					html = new HTML().getHTML(
							"http://s.weibo.com/weibo/" + searchword
									+ "&nodup=1&page=" + String.valueOf(i),
							hostName, port);
					iReconn++;
					System.out.println("****" + ip.get(iIP) + " reconnected "
							+ iReconn + " time(s)****");
					
					if (iReconn == 4) {// 4
						break;
					}
				}
				if (html.equals("null")) {
					System.out
							.println("****5 consecutive connections were failed, now using next IP****");
					
					if (iIP == ipNum - 1) {
						System.out
								.println("****All valid proxy IPs have been tried, still can not get all the data. Now trying the valid proxy IP list again.****");
						
						iIP = 0;
						System.out.println("****Turn to" + ip.get(iIP)
								+ ", start connecting****");
						
					} else {
						iIP++;
						System.out.println("****Turn to" + ip.get(iIP)
								+ ", start connecting****");
					
					}
					i++;
				}
				if (html.contains("version=2012")) {//�˴��Ǻ����еĺ���
					if (!html.contains("可用空格将多个关键词分开")) {
						FileOperation.writeString(html, saveHTMLPath + "/"
								+ dir + "/" + searchword + "/" + searchword
								+ String.valueOf(i) + ".html");
						System.out.println("\"" + searchword + "\"" + " No."
								+ i
								+ " page's html have been saved successfully!");
						
					} else {
						System.out.println("****\"" + searchword + "\"" + "No."
								+ i + " page does not exist****");
						
					}
				}
			}
			System.out.println("****\"" + searchword
					+ "\" crawling has been done!!****");
			
			System.out
					.println("****Now writing the weibos to local files (txt & xml)****");
			HTMLParser htmlParser = new HTMLParser();
		
			String saveEachTXTPath = saveTXTPath + "/" + dir + "/"
					+ searchword + ".txt";
			
			Vector<String> weibos = htmlParser.write2txt(searchword, dirPath,
					saveEachTXTPath);
			String saveEachXMLPath = saveXMLPath + "/" + dir + "/"
					+ searchword + ".xml";
			htmlParser.writeVector2xml(weibos, saveEachXMLPath);
			System.out.println("****Writing has been done!****");
		
			long t2 = System.currentTimeMillis();
			System.out.println((double) (t2 - t1) / 60000 + " mins");
		
		}
	}

}
