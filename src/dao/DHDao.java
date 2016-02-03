package dao;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.mysql.jdbc.PreparedStatement;
import com.qq.crawler.RepairInfo;

public class DHDao extends Dao {

	public DHDao() throws ClassNotFoundException {
		super();
		// TODO Auto-generated constructor stub
	}
	public ArrayList<Map<String,String>> getResult(String queryString){
		//String queryString = "select " + cols+ " from " + tab + " where id = " + id;
		ArrayList<Map<String,String>> list = new ArrayList<Map<String,String>>();
		PreparedStatement pstmt;
		try {
			pstmt = (PreparedStatement) conn.prepareStatement(queryString);
			rs = pstmt.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			int colsLen = metaData.getColumnCount();
			while(rs.next()){
				Map<String,String> map = new HashMap<String,String>();
				for(int i = 0; i < colsLen; i++){
					String colName = metaData.getColumnName(i+1);
					String colVal = rs.getString(colName);
					map.put(colName, colVal);
				}
				list.add(map);
			}
			pstmt.close();
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
	}
	public int insertSDegree(Map<String, String> map, String tab){
		int status = -1;
		String insertString;
		insertString = "insert into " + tab + "(id, time, soaringDegree)values(?,?,?)";
		try {
			PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(insertString);
			conn.setAutoCommit(false);
			pstmt.setString(1, map.get("id"));
			pstmt.setString(2, map.get("time"));
			pstmt.setString(3, map.get("soaringDegree")); 
			status = pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Cannot insert info into table \"listentimes\". ");
		}
		return status;
	}
	public int insert(Map<String, String> map, String tab){
		int status = -1;
		String insertString;
		insertString = "insert into " + tab + "(id, listentimes)values(?,?)";
		try {
			PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(insertString);
			conn.setAutoCommit(false);
			pstmt.setString(1, map.get("id"));
			pstmt.setString(2, map.get("listentimes")); 
			status = pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Cannot insert info into table \"listentimes\". ");
		}
		return status;
	}

}
