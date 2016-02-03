package dao;

import java.sql.SQLException;

import com.mysql.jdbc.PreparedStatement;
import com.qq.crawler.ListenInfo;

import java.sql.Timestamp;

public class ListenInfoDao extends Dao{

	public ListenInfoDao() throws ClassNotFoundException {
		super();
		// TODO Auto-generated constructor stub
	}
	public int insertListenInfo(ListenInfo lInfo, String tab){
		int status = -1;
		String insertData;
		if(tab.equals("qxlistentimes"))
			insertData = "insert into " + tab + "(id, mid, time, listentimes)values(?,?,?,?)";
		else insertData = "insert into " + tab + "(id, sid, time, listentimes)values(?,?,?,?)";
		try {
			PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(insertData);
			conn.setAutoCommit(false);
			pstmt.setString(1, lInfo.id);
			pstmt.setString(2, lInfo.mid);
			pstmt.setTimestamp(3, new Timestamp(System.currentTimeMillis())); 
			pstmt.setString(4, lInfo.listentimes);
			status = pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Cannot insert info into table \"qxlistentimes\". ");
		}
		return status;
	}
}
