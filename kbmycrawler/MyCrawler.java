package kbmycrawler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.net.URISyntaxException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.client.ClientProtocolException;

import forzhaoyu.emoClassifyUnity;

public class MyCrawler {
	/*
	private String ippath = "";
	private String searchwords = "";
	private String txtpath = "";
	private String xmlpath = "";
	private String htmlpath = "";
	private String pagecount = "";

	public MyCrawler(String ippath, String searchwords, String txtpath,
			String xmlpath, String htmlpath, String pagecount) {
		super();
		this.ippath = ippath;
		this.searchwords = searchwords;
		this.txtpath = txtpath;
		this.xmlpath = xmlpath;
		this.htmlpath = htmlpath;
		this.pagecount = pagecount;
	}

	public void getips() {

		if (ippath.equals("")) {
			System.out.println("path error\r\n\r\n");

		} else {
			final String[] args = new String[3];
			args[0] = ippath + "/allIPs.txt";
			args[1] = ippath + "/validIPs.txt";
			args[2] = ippath + "/plainIPs.txt";
			final ProxyIP p = new ProxyIP();

			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						p.excute(args);
					} catch (IOException | URISyntaxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
		}
	}
*/
	public   void   begin(String[] args) {

		List<String[]>topiclist=new ArrayList<>();
		try {
			topiclist=GetTopic.search("time", Integer.parseInt("2"));			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//
		//

//		
		String today=getMyTime();
		for(int i=0;i<topiclist.size();i++){
	
			System.out.println(topiclist.get(i)[0]+"---->>>"+topiclist.get(i)[1]);
			Mythread mythread=new Mythread("./mycraw",topiclist.get(i)[0],"./mycraw","./mycraw","./mycraw",topiclist.get(i),"50",today);
			mythread.run();;
			
		}
		
	}
	public static void main(String []args){
		MyTask myTask=new MyTask();
		Timer timer=new Timer();
		timer.schedule(myTask,0,7200000);
		
	}
	public static String getMyTime() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHH");
		String today = dateFormat.format(date);
		return today;
	}
}


class Mythread  {
	String ippath = null;
	String searchwords = null;
	String txtpath = null;
	String xmlpath = null;
	String htmlpath = null;
	String pagecount = null;
    String[] topiclist=null;
    String dir=null;
	public Mythread(String ippath, String searchwords, String txtpath,
			String xmlpath, String htmlpath,String[] topiclist,String pagecount,String dir) {
		super();
		this.ippath = ippath;
		this.searchwords = searchwords;
		this.txtpath = txtpath;
		this.xmlpath = xmlpath;
		this.htmlpath = htmlpath;
		this.pagecount = pagecount;
		this.topiclist=topiclist;
		this.dir=dir;
	}


	public void run() {
		// TODO Auto-generated method stub
		
		final String[] crawlerArgs = { searchwords, htmlpath, txtpath, xmlpath,
				ippath + "/plainIPs.txt", pagecount };

		int ifCrawlFlag = 0;

		if (ifCrawlFlag == 0) {
			try {
				Crawler c = new Crawler();
				c.excute(crawlerArgs,dir);
			} catch (IOException | URISyntaxException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// 
		System.out.println("now begin to classify the file：");
		emoClassifyUnity emo=new emoClassifyUnity();
		List<String[]>list2=emo.emoClassify("./mycraw/"+dir+"/"+searchwords+".xml");
		//save the  topic
		
		JdbcUtils db=new JdbcUtils();
		db.getConnection();
		String sql="insert into topic(topic ,topicdescription,pdate) values (?,?,?)";
		String deltopicid="delete from topic where topic=?";
		
			//
			List<Object>paramsdel=new ArrayList<Object>();
			paramsdel.add(topiclist[0]);
			try {
				db.updateBypreparedPreparedStatement(deltopicid, paramsdel);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//
			System.out.println(topiclist[0]+"---->>>"+topiclist[1]);
			List<Object>params=new ArrayList<Object>();
			params.add(topiclist[0]);
			params.add(topiclist[1]);
			java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());
			params.add(currentDate);
			try {
				db.updateBypreparedPreparedStatement(sql, params);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		db.releasecon();
		//
		savetomysql(gettopicid(searchwords ),list2);
		
	}
	
	public boolean  savetomysql(int topicid,List<String[]> list){
		
		boolean flag=false;
		JdbcUtils db=new JdbcUtils();
		db.getConnection();
		String del="delete from emotion where topicid=?";
		List<Object>delparams=new ArrayList<Object>();
		delparams.add(topicid);
		try {
			db.updateBypreparedPreparedStatement(del, delparams);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String sql="insert into emotion(topicid ,content,emotionalcategory,confidence) values (?,?,?,?)";
		for(int i=0;i<list.size();i++){
			List<Object>addparams=new ArrayList<Object>();
			addparams.add(topicid);
			addparams.add(list.get(i)[2]);
			addparams.add(list.get(i)[0]);
			addparams.add(list.get(i)[1]);
			try {
				flag=db.updateBypreparedPreparedStatement(sql, addparams);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return flag;
	}
	public int gettopicid(String topic){
	
		String sql="select topicid from topic where topic=?";
		JdbcUtils db=new JdbcUtils();
		db.getConnection();
		List<Object>addparams=new ArrayList<Object>();
		addparams.add(topic);
		try {
			return (int) db.getsimpleresult(sql,addparams ).get("topicid");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
}
class MyTask extends TimerTask{

	@Override
	public void run() {
		// TODO Auto-generated method stub
		//、
	
		String info="开始一轮下载！";
		logwrite.write("./timerlog.txt", info);
		//
		MyCrawler myCrawler=new MyCrawler();//开始爬取
		myCrawler.begin(null);
		//
		//
		 info="结束下载，结果保存在数据库内，一轮查询结果完成！";
		logwrite.write("./timerlog.txt", info);
	}
}