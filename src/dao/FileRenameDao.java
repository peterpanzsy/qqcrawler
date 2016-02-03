package dao;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.mysql.jdbc.PreparedStatement;

public class FileRenameDao extends Dao{

	public FileRenameDao() throws ClassNotFoundException {
		super();
		// TODO Auto-generated constructor stub
	}
	public ArrayList<Map<String,String>> getResult(String cols,String tab,String name){
		String queryString = "select " + cols+ " from " + tab + " where mid like \"" + name + "\"" ;
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
}
