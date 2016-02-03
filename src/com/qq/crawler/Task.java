package com.qq.crawler;

import java.util.TimerTask;

public class Task extends TimerTask{

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		ListenTimesCrawler lCrawler = new ListenTimesCrawler();
		try {
			lCrawler.crawlListentimes();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
