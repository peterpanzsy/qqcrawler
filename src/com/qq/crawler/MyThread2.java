package com.qq.crawler;

import java.util.Date;
import java.util.Timer;

public class MyThread2 extends Thread{
	public void run(){
		Timer timer = new Timer();
		  timer.schedule(new Task2(), new Date(), 1000 * 60 * 60 );
		  try {
		   Thread.sleep(1000);
		  } catch (Exception ex) {
		   timer.cancel();
		  }
	}
}

