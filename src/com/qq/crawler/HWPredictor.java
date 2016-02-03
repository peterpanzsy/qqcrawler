package com.qq.crawler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dao.DHDao;

public class HWPredictor {
	
	DHDao dhDao;
	public void fit() throws ClassNotFoundException {
		dhDao = new DHDao();
		ArrayList<Map<String, String>> idList = new ArrayList<Map<String,String>>();
		ArrayList<Map<String, String>> xi = new ArrayList<Map<String,String>>();
		idList = dhDao.getResult("select id from qqsonginfoc where id<1003");
		double pi[] = new double[24];
		long s1, s2, ti, m = 0;
		double p, a, b, c;
		a = 0.1;
		b = 0.05;
		c = 0.9;
		for (int i = 0; i < idList.size(); i++) {
			ArrayList<Long> si = new ArrayList<Long>();
			String id = idList.get(i).get("id");
			String queryString = "select listentimes from xiamirepair where id=" + id;
			xi = dhDao.getResult(queryString);
			s1 = s_Initiate(xi);
			ti = t_Initiate(xi);
			for (int j = 0; j < pi.length; j++) {
				pi[j] = Double.parseDouble(xi.get(j).get("listentimes"))/s1;
			}
			for (int j = 0; j < xi.size(); j++) {
				s2 = (int) (a * Integer.parseInt(xi.get(j).get("listentimes")) / pi[j%24] + 
						(1 - a) * (s1 + ti));
				si.add(s2);
				ti = (int) (b * (s2 - s1) + (1 - b) * ti);
				p = c * Integer.parseInt(xi.get(j).get("listentimes")) / s2 + (1 - c) * pi[j%24];
				pi[j%24] = p;
				s1 = s2;
				m = j%24;
			}
			predict(si, ti, pi, m, id);
		}
	}
	public void predict(ArrayList<Long> si, long ti, double[] pi, long m, String id) {
		Map<String, String> map = new HashMap<String, String>();
		long s = si.get(429);
		long x;
		for (int i = 1; i < 73; i++) {
			x = (long)((s + ti * i) * pi[(int) ((m+i)%24)]);
			si.add(x);
		}
		for (int j = 0; j < si.size(); j++) {
			map.put("id", id);
			map.put("listentimes", String.valueOf(si.get(j)));
			dhDao.insert(map, "qqhot");
			//System.out.println(si.get(j));
		}
	}
	public long s_Initiate(ArrayList<Map<String, String>> xi) {
		long s = 0;
		for (int i = 0; i < 24; i++) {
			s = s + Integer.parseInt(xi.get(i).get("listentimes"));
		}
		s = s/24;
		return s;
	}
	public long t_Initiate(ArrayList<Map<String, String>> xi) {
		long t = 0;
		for (int i = 1; i < 24; i++) {
			t = t + Integer.parseInt(xi.get(i).get("listentimes"));
			t = t - Integer.parseInt(xi.get(i-1).get("listentimes"));
		}
		t = t/23;
		return t;
	}
	public static void main(String[] args) throws ClassNotFoundException {
		HWPredictor predictor = new HWPredictor();
		predictor.fit();
	}	
}
