package com.qq.crawler;

import java.util.TimerTask;

import music.qq.top.TopMusicCrawler;

public class Task3 extends TimerTask{

	@Override
	public void run() {
		// TODO Auto-generated method stub
		TopMusicCrawler tCrawler = new TopMusicCrawler();
		try {
			tCrawler.taskList();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
