package kbmycrawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class GetTopic {

	public static List<String[]> search(String sort, int number)
			throws ClientProtocolException, IOException {
		HttpClient httpclient = new DefaultHttpClient();
		List<String[]> list = new ArrayList<String[]>();
		for (int page = 0; page < number; page++) {
			//creat get
			String url = "http://huati.weibo.com/aj_topiclist/big?ctg1=99&ctg2=0&prov=0&sort="
					+ sort
					+ "&p="
					+ Integer.toString(page)
					+ "&t=1&_t=0&__rnd=1405513612404";
			HttpGet httpgets = new HttpGet(url);
			httpgets.setHeader("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			httpgets.setHeader("Accept-Encoding", "UTF-8");
			httpgets.setHeader("Accept-Language",
					"zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
			httpgets.setHeader("Content-Type",
					"application/x-www-form-urlencoded");
			httpgets.setHeader(
					"Cookie",
					"UOR=,,login.sina.com.cn; SUBP=002A2c-gVlwEm1dAWxfgXELuuu1xVxBxAuKN01wSRQDjIgjtotAMswHuHYq17IYcaEic7uMcIAYETiabR-%3D; SINAGLOBAL=7261884966436.946.1395492318712; ULV=1405511261283:8:1:1:4279240755523.39.1405511261210:1402223609228; BR=usrmd15178; SUB=AbL5YffpTDf3R9iU5oC2b9lRpIPhxt6166tmkPw7bjWHAFK5uKNfQth09AVYO9dyDSjD6BBtGUh0zAKGFXTh0kxDC3JG32ENzBVuk206ozfmLK04A1yA2JHfXzOUuZkKgjil3P3Q6HwD2s2zF8ABMya1zqwzmzmyBmIf%2BJiigSI6; _s_tentry=login.sina.com.cn; Apache=4279240755523.39.1405511261210; WBStore=0227cb35607f4724|undefined; __utma=3067689.1877266229.1405511263.1405511263.1405511263.1; __utmb=3067689.13.10.1405511263; __utmc=3067689; __utmz=3067689.1405511263.1.1.utmcsr=login.sina.com.cn|utmccn=(referral)|utmcmd=referral|utmcct=/sso/login.php; myuid=1767953817");
			httpgets.setHeader("Host", "huati.weibo.com");
			httpgets.setHeader("Referer",
					"http://huati.weibo.com/?ctg1=99&ctg2=0&prov=0&sort=time&p=2&t=1");
			httpgets.setHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.3; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");
			httpgets.setHeader("X-Requested-With", "XMLHttpRequest");
			HttpResponse response = httpclient.execute(httpgets);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instreams = entity.getContent();
				String unicodehtml = convertStreamToString(instreams);
				String html = convert(unicodehtml);
				String regextopic = "url=([^\"]*topic=([^\"]*))";
				Matcher m = Pattern.compile(regextopic).matcher(html);
				// find  url  and the topic 
				while (m.find()) {
					String changeurl = m.group(1).toString()
							.substring(1, m.group(1).toString().length() - 1);
					String topic = m.group(2).toString()
							.substring(0, m.group(2).toString().length() - 1);
					System.out.println(changeurl + "     " + topic);

					if (changeurl.contains("tid=")&&!(changeurl.contains("k\\/"))) {

						// get the jump page
						String[] changehtml = new HTML()
								.getHTML("http://huati.weibo.com" + changeurl);
						// System.out.println(changehtml[1]);
						String regexnewurl = "http://weibo.com/p/([^\"]*)";
						Matcher findnewurl = Pattern.compile(regexnewurl)
								.matcher(changehtml[1]);
						// find  the new url
						String discribe = "";
						if (findnewurl.find()) {
							String newurl = "http://weibo.com/p/"
									+ findnewurl.group(1).toString();
							// get the new page which contains the description of the topic 
							discribe = search(newurl);
						}
						String[] TopicAndDiscribe = new String[2];
						TopicAndDiscribe[0] = topic;
						TopicAndDiscribe[1] = discribe;
						list.add(TopicAndDiscribe);
					}
				}
			}
			// Do not need the rest
			httpgets.abort();

		}
		return list;
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws NumberFormatException
	 */

	public static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public static String convert(String utfString) {
		if (!utfString.contains("\\u")) {
			return utfString;
		}
		StringBuilder sb = new StringBuilder();
		int i = -1;
		int pos = 0;

		while ((i = utfString.indexOf("\\u", pos)) != -1) {
			sb.append(utfString.substring(pos, i));
			if (i + 5 < utfString.length()) {
				pos = i + 6;
				sb.append((char) Integer.parseInt(
						utfString.substring(i + 2, i + 6), 16));
			}
		}
		return sb.toString();
	}

	public static String search(String url) throws ClientProtocolException,
			IOException {
		HttpClient httpclient = new DefaultHttpClient();
		// 
		HttpGet httpgets = new HttpGet(url);
		httpgets.setHeader("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		httpgets.setHeader("Accept-Language",
				"zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
		httpgets.setHeader("Connection", "	keep-alive");
		httpgets.setHeader(
				"Cookie","	SINAGLOBAL=1731838870327.9197.1394873459527; __gads=ID=0aa602c8e05f2558:T=1399896124:S=ALNI_MYzlQfA5LKLnpg7XijHLd54bjMe3g; ULV=1405310025674:79:14:4:1468672095797.956.1405310025656:1405252752753; myuid=2436893315; SUBP=002A2c-gVlwEm1uAWxfgXELuuu1xVxBxAACpGXCUYZTDG7SDejrIcJBuHY-u_1%3D; ALF=1437048697; un=1178756935@qq.com; wvr=5; UOR=book.51cto.com,widget.weibo.com,www.doc88.com; UOR=book.51cto.com,widget.weibo.com,spr_web_sq_firefox_weibo_t001; SUBP=002A2c-gVlwEm1uAWxfgXELuuu1xVxBxAACpGXCUYZTDG7SDejrIcJBuHY-u_E%3D; ULV=1405843386952:80:15:1:5852206890928.662.1405843386578:1405310025674; SINAGLOBAL=162808340861.80972.1405596971438; myuid=2436893315; ALF=1437379387; UUG=usr1024; SUS=SID-2436893315-1405843388-GZ-r8o2f-4c692efb595a505650b3d21f4ff21fe2; SUE=es%3D2217b9e7cf91d8a607a81f156610559b%26ev%3Dv1%26es2%3D9b9b45bb65dedda757838406e1ddb561%26rs0%3DM7MNzvHWiVeZ%252BBzANlM8o1FVNBc%252FDU%252BAx2YVZhAz9OrupOk4x8BQ9THYOrXKs5P9nMoszwcH8A%252FIPL2ueljWOKbIxNNGARv%252BNtMVpNKPbzddE4Xi1tabr0dqiyPbOWlIX9MK8WbIHBl5KWcYjE50qCYfRLYX1vYHLNMUxnN8jVc%253D%26rv%3D0; SUP=cv%3D1%26bt%3D1405843388%26et%3D1405929788%26d%3Dc909%26i%3D1fe2%26us%3D1%26vf%3D0%26vt%3D0%26ac%3D0%26st%3D0%26uid%3D2436893315%26name%3D1178756935%2540qq.com%26nick%3Dbingbing%25E6%259E%259C%25E6%259E%259C%26fmp%3D%26lcp%3D; SUB=AZFlbo22LoQ3hHiRmW4hhFR7kL6mk35GrVI74ry%2B8jv3x%2Bk6j3t6nVOV8bY%2BTLf%2FhXTKGl4f7g1%2FyL4G4AEh%2BLwhD%2FggETwl%2BvKyJxdsc3CzkW7jrlkdyfY2IVS5o6qi97RlN9PCpAQQE4LawnBCjDM%3D; SSOLoginState=1405843388; UV5=usrmdins312_133; UV5PAGE=usr512_239; _s_tentry=login.sina.com.cn; Apache=5852206890928.662.1405843386578");
		httpgets.setHeader("Host", "weibo.com");
		httpgets.setHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.3; WOW64; rv:30.0) Gecko/20100101 Firefox/30.0");

		HttpResponse response = httpclient.execute(httpgets);
		HttpEntity entity = response.getEntity();

		if (entity != null) {
			StringBuilder b = new StringBuilder();
			InputStream instreams = entity.getContent();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					instreams, "UTF-8"));
			String t = "";
			while ((t = br.readLine()) != null) {
				b.append(t);
				// System.out.println(t+"\r\n");
			}
			br.close();
			httpgets.abort();

			String regextopic = "meta content=([^name=]*)";
			Matcher m = Pattern.compile(regextopic).matcher(b.toString());
			if (m.find()) {
				return m.group(1).toString();
			}
		}
		return "";
	}
}
