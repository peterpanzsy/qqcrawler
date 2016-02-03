package com.qq.crawler;

import java.util.ArrayList;
import java.util.Map;

import dao.ReviewCountDao;

public class ReviewCounter {
	
	ReviewCountDao rcDao;
	public void reviewCount() throws ClassNotFoundException{
		rcDao = new ReviewCountDao();
		String[] province = {"陕西","北京","天津","上海","重庆","河北","河南","云南","辽宁","黑龙江","湖南","安徽","山东","新疆","江苏","浙江","江西","湖北","广西",
				"甘肃","山西","内蒙古","吉林","福建","贵州","广东","青海","西藏","四川","宁夏","海南","台湾","香港","澳门"};
		ArrayList<Map<String, String>> result = new ArrayList<Map<String, String>>();
		for (int i = 0; i < province.length; i++) {
			result = rcDao.getResult("id,count(*)", "review", province[i]);
			String s1,s2,s3;
			try {
				s1 = result.get(0).get("count(*)");
			} catch (Exception e) {
				// TODO: handle exception
				s1 = "1";
			}
			try {
				s2 = result.get(1).get("count(*)");
			} catch (Exception e) {
				// TODO: handle exception
				s2 = "1";
			}
			try {
				s3 = result.get(2).get("count(*)");
			} catch (Exception e) {
				// TODO: handle exception
				s3 = "1";
			}
			System.out.println(s1);
			System.out.println(s2);
			System.out.println(s3);
			System.out.println("\n");
		}
	}
	
	/**
	 * @param args
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws ClassNotFoundException {
		// TODO Auto-generated method stub
		ReviewCounter reviewCounter = new ReviewCounter();
		reviewCounter.reviewCount();
	}

}
