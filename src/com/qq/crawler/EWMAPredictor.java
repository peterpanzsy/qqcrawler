package com.qq.crawler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dao.DHDao;

public class EWMAPredictor {
	DHDao dhDao;
	public void getEXPMA() throws ClassNotFoundException {
		dhDao = new DHDao();
		ArrayList<Map<String, String>> idList = new ArrayList<Map<String,String>>();
		ArrayList<Map<String, String>> xi = new ArrayList<Map<String,String>>();
		Map<String, String> map = new HashMap<String, String>();
		idList = dhDao.getResult("select id from qqsonginfoc where id<1003");
		for (int i = 0; i < idList.size(); i++) {
			String id = idList.get(i).get("id");
			String queryString = "select time,soaringDegree from qqsdegree where id=" + id;
			xi = dhDao.getResult(queryString);
			int n = xi.size();
			Double a = 0.7;
		    Double si = Double.parseDouble(xi.get(0).get("soaringDegree")); 
		    String time = xi.get(1).get("time");
		    map.put("id", id);
		    map.put("time", time);
		    map.put("soaringDegree", String.valueOf(si));
		    dhDao.insertSDegree(map, "qqsdegree_ewma");
		    for (int j = 1; j < xi.size(); j++) {   
		        si = Double.parseDouble(xi.get(j).get("soaringDegree")) * a + si * (1 - a);
			    map.put("id", id);
			    if (j == xi.size()-1) {
					time = "2014-11-28 00:30:00";
					map.put("time", time);
				} else {
					time = xi.get(j+1).get("time");
					map.put("time", time);
				}
			    map.put("soaringDegree", String.valueOf(si));
			    dhDao.insertSDegree(map, "qqsdegree_ewma");
		    }
		}    
	}
	public static void main(String[] args) throws ClassNotFoundException {
		EWMAPredictor ewmaPredictor = new EWMAPredictor();
		ewmaPredictor.getEXPMA();
	}
}
