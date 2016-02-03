package com.qq.crawler;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Timer;

import music.qq.top.TopMusicCrawler;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import dao.ListenInfoDao;

public class XiamiListenCrawler {
	ListenInfoDao xlDao;
	ListenInfo xlInfo;
	public XiamiListenCrawler(){
		// TODO Auto-generated constructor stub
		
	}
	public void crawlListentimes() throws ClassNotFoundException{
		xlDao = new ListenInfoDao();
		ArrayList<Map<String, String>> search = new ArrayList<Map<String, String>>();
		search = xlDao.getResult("id, sid", "xxsonginfoc");
		for(int i = 0; i < search.size(); i++){
			String id = search.get(i).get("id");
			String sid = search.get(i).get("sid");
			String url = "http://www.xiami.com/count/getplaycount?id=" + sid + "&type=song";
			System.out.println(url);
			Document doc = null;
			if((i+1)%10 == 0){
				try {
					Thread.sleep(5*1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			doc = this.getHtmlContent(url);
			this.getAndSaveListenTimes(doc, id, sid);
		}
	}
	public void getAndSaveListenTimes(Document doc, String id, String sid) throws ClassNotFoundException{
		if (doc!=null) {
			String res = doc.select("body").text();
			JSONObject resObject = JSONObject.fromObject(res);
			String listentimes = resObject.getString("plays");
			xlInfo = new ListenInfo();
			xlInfo.id = id;
			xlInfo.mid = sid;
			xlInfo.listentimes = listentimes;
			xlDao.insertListenInfo(xlInfo, "xxlistentimes");
			System.out.println("listen:" + id + " " + sid + " " + listentimes);
		}else {
			System.err.println("The html is not fetched!");
		}
	}
	public Document getHtmlContent(String url){
		Document doc = null;
		while(true){
			
			try {
				doc = Jsoup.connect(url)
				.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36")
				.timeout(10*1000)
				.ignoreContentType(true)
				.get();
				break;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return doc;
	}
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		MyThread t = new MyThread();
		MyThread2 t2 = new MyThread2();
		t.start();
		t2.start();
		/*Timer timer = new Timer();
		timer.schedule(new Task3(), new Date(), 1000 * 60 * 60 * 24 );
		try {
		   Thread.sleep(1000);
		} catch (Exception ex) {
		   timer.cancel();
		}*/
	}
}
