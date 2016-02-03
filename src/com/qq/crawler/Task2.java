package com.qq.crawler;

import java.util.TimerTask;

public class Task2 extends TimerTask{

	@Override
	public void run() {
		// TODO Auto-generated method stub
		XiamiListenCrawler xCrawler = new XiamiListenCrawler();
		try {
			xCrawler.crawlListentimes();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}