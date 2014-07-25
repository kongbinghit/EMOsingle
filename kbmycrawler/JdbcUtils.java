package kbmycrawler;



import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JdbcUtils {

	//�����û���
	private final static String USERNAME="root";
	//��������
	private final static String PASSWORD="123456";
	//��ݿ������Ϣ
	private final static String DRIVER="com.mysql.jdbc.Driver";
	//��ݿ�ĵ�ַ
	private final static String URL="jdbc:mysql://219.223.251.44/publicopinion";
	//����һ������
	private Connection connection;
	//����sql����ִ�ж���
	private PreparedStatement pstmt;
	//�����ѯ���صĽ��
	private ResultSet resultSet;
	public JdbcUtils() {
		// TODO Auto-generated constructor stub
		try {
			Class.forName(DRIVER);
			System.out.println("数据库连接成功！");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	//��������ݿ������
	public Connection getConnection(){
	try {
		connection=DriverManager.getConnection(URL, USERNAME, PASSWORD);
		
	} catch (Exception e) {
		// TODO: handle exception
	}
		return connection;
	}
	public boolean updateBypreparedPreparedStatement(String sql, List<Object>params) throws SQLException{
		boolean flag=false;
		int result=-1;//��ʾ���û�ִ����Ӻ�ɾ�������ʱ����Ӱ�������

		pstmt=connection.prepareStatement(sql);
		int index=1;
		if(params!=null && !params.isEmpty()){
			for(int i=0;i<params.size();i++){
				pstmt.setObject(index++, params.get(i));
				
			}
		}
		result=pstmt.executeUpdate();
		flag=result>0? true:false;
		return flag;
	}
	/**
	 * ��������������ص�����ѯ��¼��
	 * @param sql�����sql��ѯ���
	 * @param params�����ռλ��
	 * @return
	 */
	public Map<String ,Object> getsimpleresult(String sql,List<Object> params)throws SQLException{
		//�������淵�ؽ��
		Map<String,Object>map=new HashMap<String,Object>();
		//
		pstmt=connection.prepareStatement(sql);
		
		int index=1;
		if(params!=null && !params.isEmpty()){
			for(int i=0;i<params.size();i++){
				pstmt.setObject(index++, params.get(i));
				
			}
		}
		
		resultSet=pstmt.executeQuery();
		ResultSetMetaData metaData=resultSet.getMetaData();
		int col_len=metaData.getColumnCount();//����е�����
		while(resultSet.next()){
			for(int i=0;i<col_len;i++){
				String col_name=metaData.getColumnName(i+1);
				Object value=resultSet.getObject(col_name);	
				if(value==null){
					value="";
				}
				map.put(col_name, value);
			}
			
		}
		
		return map;
	}
	public List<Map<String ,Object>> getcomplexresult(String sql,List<Object> params)throws SQLException{
		//�������淵�ؽ��
		List<Map<String,Object>>list=new ArrayList<Map<String,Object>>();
		
		//
		pstmt=connection.prepareStatement(sql);
		int index=1;
		if(params!=null && !params.isEmpty()){
			for(int i=0;i<params.size();i++){
				pstmt.setObject(index++, params.get(i));
				
			}
		}
		resultSet=pstmt.executeQuery();
		ResultSetMetaData metaData=resultSet.getMetaData();
		int col_len=metaData.getColumnCount();//����е�����
		while(resultSet.next()){
			//���浥����¼
			Map<String,Object>map=new HashMap<String,Object>();
			//�������е���
			for(int i=0;i<col_len;i++){
				String col_name=metaData.getColumnName(i+1);
				Object value=resultSet.getObject(col_name);	
				if(value==null){
					value="";
				}
				map.put(col_name, value);
			}
			list.add(map);
		}
		
		return list;
	}
	/**
	 * jdbc������Ʒ�װ
	 * @param sql
	 * @param params
	 * @param cls
	 * @return
	 */
	public <T> T findSimpleRefResult(String sql,List<Object>params,Class<T>cls) throws Exception{
		T resultObject=null;
		pstmt=connection.prepareStatement(sql);
		int index=1;
		if(params!=null && !params.isEmpty()){
			for(int i=0;i<params.size();i++){
				pstmt.setObject(index++, params.get(i));
				
			}
		}
		resultSet=pstmt.executeQuery();
		ResultSetMetaData metaData=resultSet.getMetaData();
		int col_len=metaData.getColumnCount();//����е�����
		while(resultSet.next()){
			//���浥����¼
			resultObject=cls.newInstance();
			
			//�������е���
			for(int i=0;i<col_len;i++){
				String col_name=metaData.getColumnName(i+1);
				Object value=resultSet.getObject(col_name);	
				if(value==null){
					value="";
				}
				Field field=cls.getDeclaredField(col_name);	
				field.setAccessible(true);
				field.set(resultObject, value);
			}
			
		}	
		return resultObject;	
	}
	public <T> List<T> findMoreRefResult(String sql,List<Object>params,Class<T>cls) throws Exception{
		List<T> list=new ArrayList<T>();
		pstmt=connection.prepareStatement(sql);
		int index=1;
		if(params!=null && !params.isEmpty()){
			for(int i=0;i<params.size();i++){
				pstmt.setObject(index++, params.get(i));
				
			}
		}
		resultSet=pstmt.executeQuery();
		ResultSetMetaData metaData=resultSet.getMetaData();
		int col_len=metaData.getColumnCount();//����е�����
		
		while(resultSet.next()){
			//���浥����¼
			T resultObject=cls.newInstance();
			
			//�������е���
			for(int i=0;i<col_len;i++){
				String col_name=metaData.getColumnName(i+1);
				Object value=resultSet.getObject(col_name);	
				if(value==null){
					value="";
				}
				Field field=cls.getDeclaredField(col_name);	
				field.setAccessible(true);
				field.set(resultObject, value);
			}
			list.add(resultObject);
		}	
		return list;	
	}
	/**
	 * �ͷ���Դ
	 */
	public void releasecon(){
		if(resultSet!=null){
			try {
				resultSet.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(pstmt!=null){
			try {
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(connection!=null){
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
