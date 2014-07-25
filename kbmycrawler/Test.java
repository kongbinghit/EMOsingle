package kbmycrawler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Test {

	public Test() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(System.getProperty("java.library.path"));
		
	}
	public static boolean  savetomysql(int topicid,List<String[]> list){
		System.out.println("");
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
			addparams.add(list.get(i)[0]);
			addparams.add(list.get(i)[1]);
			addparams.add(list.get(i)[2]);
			try {
				flag=db.updateBypreparedPreparedStatement(sql, addparams);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return flag;
	}
	public static int gettopicid(String topic){
		System.out.println("get the topic id from table topic...");
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
