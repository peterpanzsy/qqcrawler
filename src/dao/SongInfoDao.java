package dao;

import java.sql.SQLException;

import com.mysql.jdbc.PreparedStatement;
import com.qq.crawler.SongInfo;

public class SongInfoDao extends Dao{
	
	public SongInfoDao() throws ClassNotFoundException {
		super();
		// TODO Auto-generated constructor stub
	}

	public int insertSongInfo(SongInfo sInfo){
		int status = -1;
		String insertData = "insert into qxsonginfoc(id, mid, sname, singer, language, album, date)values(?,?,?,?,?,?,?)";
		try {
			PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(insertData);
			conn.setAutoCommit(false);
			pstmt.setString(1, sInfo.id);
			pstmt.setString(2, sInfo.mid);
			pstmt.setString(3, sInfo.sname);
			pstmt.setString(4, sInfo.singer);
			pstmt.setString(5, sInfo.language);
			pstmt.setString(6, sInfo.album);
			pstmt.setString(7, sInfo.date);
			status = pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Cannot insert info into table \"qxsonginfoc\". ");
		}
		return status;
	}
	
	public int updateSongInfo(String mid, String id){
		int status = -1;
		String updateData = "update qxsonginfos set mid = \'" + mid + "\' where id = " + id;
		//String updateData = "update 300qqtopsonginfo set language = \'" + language + "\',date = \'" + date + "\' where id = " + id;
		try {
			PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(updateData);
			conn.setAutoCommit(false);
			status = pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Cannot update info in table \"qxsonginfos\". ");
		}
		return status;
	}

}
