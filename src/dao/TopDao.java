package dao;

import java.sql.Date;
import java.sql.SQLException;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

public class TopDao extends Dao{
	
	public TopDao() throws ClassNotFoundException {
		super();
		// TODO Auto-generated constructor stub
	}

	public int insertTopDaily(String mid, String toptype){
		int status = -1;
		String insertData = 
				"insert into QQTopDaily(mid, toptype)values(?,?)";
		try {
			PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(insertData);
			conn.setAutoCommit(false);
			pstmt.setString(1, mid);
			pstmt.setString(2, toptype);
			status = pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Cannot insert music into table QQTopDaily. ");
		}

		return status;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public boolean isExis(String mid) throws SQLException {
		// TODO Auto-generated method stub
		boolean exist = true;
		String queryString = "select id from QQTopSongInfo where mid=\'"+mid+"\' limit 1";
		Statement stmt=(Statement) conn.createStatement();
		rs = stmt.executeQuery(queryString);
		if(rs.next()){
			stmt.close();
			rs.close();
			return exist;
		}
		stmt.close();
		rs.close();
		return !exist;
	}

	public int insertTopSongInfo(String mid, String sname, String singer,
			String album) {
		// TODO Auto-generated method stub
		int status = -1;
		String insertData = 
				"insert into QQTopSongInfo(mid, sname, singer, album)values(?,?,?,?)";
		try {
			PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(insertData);
			conn.setAutoCommit(false);
			pstmt.setString(1, mid);
			pstmt.setString(2, sname);
			pstmt.setString(3, singer);
			pstmt.setString(4, album);
			status = pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Cannot insert music into table QQTopDaily. ");
		}

		return status;
		
	}

}
