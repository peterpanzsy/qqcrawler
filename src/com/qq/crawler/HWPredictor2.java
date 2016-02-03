package com.qq.crawler;

import java.util.ArrayList;
import java.util.Map;

import dao.DHDao;

public class HWPredictor2 {
	
	DHDao dhDao;
	public void fit() throws ClassNotFoundException {
		dhDao = new DHDao();
		ArrayList<Map<String, String>> xi = new ArrayList<Map<String,String>>();
		ArrayList<Integer> si = new ArrayList<Integer>();
		double pi[] = new double[24];
		int s1, s2, ti, m = 0;
		double p, a, b, c;
		a = 0.4;
		b = 0.05;
		c = 0.95;
		String queryString = "select dlistentimes from qqrepair where id=918";
		xi = dhDao.getResult(queryString);
		s1 = s_Initiate(xi);
		ti = t_Initiate(xi);
		for (int i = 0; i < pi.length; i++) {
			pi[i] = 0;
		}
		for (int j = 0; j < xi.size(); j++) {
			s2 = (int) (a * (Integer.parseInt(xi.get(j).get("dlistentimes")) - pi[j%24]) + 
					(1 - a) * (s1 + ti));
			si.add(s2);
			ti = (int) (b * (s2 - s1) + (1 - b) * ti);
			p = c * (Integer.parseInt(xi.get(j).get("dlistentimes")) - s2) + (1 - c) * pi[j%24];
			pi[j%24] = p;
			s1 = s2;
			m = j%24;
		}
		//System.out.println(m);
		predict(si, ti, pi, m);
	}
	public void predict(ArrayList<Integer> si, int ti, double[] pi, int m) {
		int s = si.get(429);
		int x;
		for (int i = 1; i < 73; i++) {
			x = (int) (s + ti * i + pi[(m+i)%24]);
			si.add(x);
		}
		for (int j = 0; j < si.size(); j++) {
			System.out.println(si.get(j));
		}
	}
	public int s_Initiate(ArrayList<Map<String, String>> xi) {
		int s = 0;
		for (int i = 0; i < 24; i++) {
			s = s + Integer.parseInt(xi.get(i).get("dlistentimes"));
		}
		s = s/24;
		return s;
	}
	public int t_Initiate(ArrayList<Map<String, String>> xi) {
		int t = 0;
		for (int i = 1; i < 24; i++) {
			t = t + Integer.parseInt(xi.get(i).get("dlistentimes"));
			t = t - Integer.parseInt(xi.get(i-1).get("dlistentimes"));
		}
		t = t/23;
		return t;
	}
	public static void main(String[] args) throws ClassNotFoundException {
		HWPredictor2 predictor2 = new HWPredictor2();
		predictor2.fit();
	}	
}