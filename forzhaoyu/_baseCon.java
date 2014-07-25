package forzhaoyu;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class _baseCon {
	// connect to MySQL
	Connection connSQL() {
		PreparedStatement statement = null;
		Connection conn = null;
		String url = "jdbc:mysql://219.223.251.44:3306/dict?characterEncoding=UTF-8";
		String username = "root";
		String password = "123456"; // 加载驱动程序以连接数据库
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url, username, password);
            System.out.println("succeed!");
		}
		// 捕获加载驱动程序异常
		catch (ClassNotFoundException cnfex) {
			System.err.println("装载 JDBC/ODBC 驱动程序失败。");
			cnfex.printStackTrace();
		}
		// 捕获连接数据库异常
		catch (SQLException sqlex) {
			System.err.println("无法连接数据库");
			sqlex.printStackTrace();
		}
		return conn;
	}
}
