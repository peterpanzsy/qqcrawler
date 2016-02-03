package com.qq.crawler;

import java.util.ArrayList;
import java.util.Map;

import dao.ReviewCountDao;

public class ReviewCounter {
	
	ReviewCountDao rcDao;
	public void reviewCount() throws ClassNotFoundException{
		rcDao = new ReviewCountDao();
		String[] province = {"����","����","���","�Ϻ�","����","�ӱ�","����","����","����","������","����","����","ɽ��","�½�","����","�㽭","����","����","����",
				"����","ɽ��","���ɹ�","����","����","����","�㶫","�ຣ","����","�Ĵ�","����","����","̨��","���","����"};
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
