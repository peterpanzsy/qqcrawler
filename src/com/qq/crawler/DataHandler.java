package com.qq.crawler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dao.DHDao;

public class DataHandler {
	
	DHDao dhDao;
	public void dataSum() throws ClassNotFoundException {
		dhDao = new DHDao();
		ArrayList<Map<String, String>> idList = new ArrayList<Map<String,String>>();
		ArrayList<Map<String, String>> record1 = new ArrayList<Map<String,String>>();
		ArrayList<Map<String, String>> record2 = new ArrayList<Map<String,String>>();
		String r1 = null;
		String r2 = null;
		String r3 = null;
		String r4 = null;
		idList = dhDao.getResult("select id from qqsonginfoc where id<1003 and date like \"19%\"");
		//idList = dhDao.getResult("select id from qqsonginfoc where id<1003");
		System.out.println(idList.size());
		record1 = dhDao.getResult("select listentimes,dlistentimes from xiamirepair where id=" + idList.get(0).get("id"));
		//System.out.println(record1.size());
		for (int i = 1; i < idList.size(); i++) {
			String id = idList.get(i).get("id");
			String queryString = "select listentimes,dlistentimes from xiamirepair where id=" + id;
			record2 = dhDao.getResult(queryString);
			//System.out.println(record2.size());
			//showList(record1);
			//System.out.println("-------------------------------------");
			for (int j = 0; j < record1.size(); j++) {
				r1 = record1.get(j).get("listentimes");
				r2 = record2.get(j).get("listentimes");
				r1 = String.valueOf(Long.parseLong(r1) + Long.parseLong(r2));
				r3 = record1.get(j).get("dlistentimes");
				r4 = record2.get(j).get("dlistentimes");
				r3 = String.valueOf(Integer.parseInt(r3) + Integer.parseInt(r4));
				Map<String,String> map = new HashMap<String,String>();
				//System.out.println(r1 + "  " + r3);
				map.put("listentimes", r1);
				map.put("dlistentimes", r3);
				record1.set(j, map);
			}
		}
		showList(record1);
	}
	public void soaringDegree() throws ClassNotFoundException {
		dhDao = new DHDao();
		ArrayList<Map<String, String>> idList = new ArrayList<Map<String,String>>();
		ArrayList<Map<String, String>> record1 = new ArrayList<Map<String,String>>();
		ArrayList<Map<String, String>> record2 = new ArrayList<Map<String,String>>();
		String time = null;
		String listentimes1 = null;
		String listentimes2 = null;
		idList = dhDao.getResult("select id from qqsonginfoc where id<1003");
		for (int i = 0; i < idList.size(); i++) {
			String id = idList.get(i).get("id");
			record1 = dhDao.getResult("select time,listentimes from xiamirepair where id=" + id + " and time like \"% 00%\"");
			for (int j = 0; j < record1.size()-1; j++) {
				time = record1.get(j).get("time");
				listentimes1 = record1.get(j).get("listentimes");
				listentimes2 = record1.get(j+1).get("listentimes");
				String dlistentimes = String.valueOf(Integer.parseInt(listentimes2) - Integer.parseInt(listentimes1));
				//System.out.println(id+","+time+","+dlistentimes);
				Map<String, String> map = new HashMap<String, String>();
				map.put("time", time);
				map.put("dlistentimes", dlistentimes);
				//System.out.println(map.get("dlistentimes"));
				if (id.equals("1")) {
					record2.add(map);
					//System.out.println(j+","+record2.get(j).get("dlistentimes"));
				} else {
					record2.set(j, map);
					//System.out.println(j+","+record2.get(j).get("dlistentimes"));
				}
			}
			for (int k = 0; k < record2.size()-1; k++) {
				time = record2.get(k+1).get("time");
				listentimes1 = record2.get(k).get("dlistentimes");
				listentimes2 = record2.get(k+1).get("dlistentimes");
				//System.out.println(listentimes2+","+listentimes1);
				String sDegree = String.valueOf(Integer.parseInt(listentimes2) - Integer.parseInt(listentimes1));
				System.out.println(id+","+time+","+sDegree);
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", id);
				map.put("time", time);
				map.put("soaringDegree", sDegree);
				dhDao.insertSDegree(map, "xiamisdegree");
			}
		}
	}
	public void soaringDegree2() throws ClassNotFoundException {
		dhDao = new DHDao();
		ArrayList<Map<String, String>> idList = new ArrayList<Map<String,String>>();
		ArrayList<Map<String, String>> record1 = new ArrayList<Map<String,String>>();
		ArrayList<Map<String, String>> record2 = new ArrayList<Map<String,String>>();
		String time = null;
		String dlistentimes1 = null;
		String dlistentimes2 = null;
		idList = dhDao.getResult("select id from qqsonginfoc where id<1003");
		for (int i = 0; i < idList.size(); i++) {
			String id = idList.get(i).get("id");
			record1 = dhDao.getResult("select time,dlistentimes from qqrepair where id=" + id);
			for (int j = 0; j < record1.size()-1; j++) {
				time = record1.get(j+1).get("time");
				dlistentimes1 = record1.get(j).get("dlistentimes");
				dlistentimes2 = record1.get(j+1).get("dlistentimes");
				String dlistentimes = String.valueOf(Integer.parseInt(dlistentimes2) - Integer.parseInt(dlistentimes1));
				//System.out.println(id+","+time+","+dlistentimes);
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", id);
				map.put("time", time);
				map.put("soaringDegree", dlistentimes);
				//System.out.println(map.get("dlistentimes"));
				dhDao.insertSDegree(map, "qqsdegree2");
			}
		}
	}
	public void showList(ArrayList<Map<String, String>> record1) {
		for (int i = 0; i < record1.size(); i++) {
			String listentimes = record1.get(i).get("listentimes");
			String dlistentimes = record1.get(i).get("dlistentimes");
			System.out.println(listentimes + "	" + dlistentimes);
		}
	}
	public static void main(String[] args) throws ClassNotFoundException {
		DataHandler dataHandler = new DataHandler();
		//dataHandler.dataSum();
		dataHandler.soaringDegree();
	}

}
