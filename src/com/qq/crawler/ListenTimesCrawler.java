package com.qq.crawler;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Timer;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import dao.ListenInfoDao;

public class ListenTimesCrawler {
	ListenInfoDao lDao;
	ListenInfo qlInfo;
	public ListenTimesCrawler(){
		// TODO Auto-generated constructor stub
		
	}
	public void crawlListentimes() throws ClassNotFoundException{
		lDao = new ListenInfoDao();
		ArrayList<Map<String, String>> search = new ArrayList<Map<String, String>>();
		search = lDao.getResult("id, mid", "qxsonginfoc");
		for(int i = 0; i < search.size(); i++){
			String id = search.get(i).get("id");
			String mid = search.get(i).get("mid");
			String url = "http://s.plcloud.music.qq.com/fcgi-bin/fcg_getsonglistenstatistic.fcg?utf8=1&songmidlist=" + mid + "&loginUin=0&hostUin=0&format=jsonp&inCharset=GB2312&outCharset=utf-8&notice=0&platform=yqq&jsonpCallback=JsonCallBack&needNewCode=0";
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
			this.getAndSaveListenTimes(doc, id, mid);
		}
	}
	public void xiamicrawlListentimes() throws ClassNotFoundException{
		
	}
	public void getAndSaveListenTimes(Document doc, String id, String mid) throws ClassNotFoundException{
		if (doc!=null) {
			String res = doc.select("body").text();
			res = res.substring(13, res.length()-1);
			JSONObject resObject = JSONObject.fromObject(res);
			String songlist = resObject.getString("songlist");
			JSONArray listArray = JSONArray.fromObject(songlist);
			JSONObject listenObject = listArray.getJSONObject(0);
			String listentimes = listenObject.getString("count");
			qlInfo = new ListenInfo();
			qlInfo.id = id;
			qlInfo.mid = mid;
			qlInfo.listentimes = listentimes;
			lDao.insertListenInfo(qlInfo, "qxlistentimes");
			System.out.println("listen:" + id + " " + mid + " " + listentimes);
		}else {
			System.err.println("The html is not fetched!");
		}
	}
	public Document getHtmlContent(String url){
		Document doc = null;
		while(true){
			
			try {
				doc = Jsoup.connect(url)
						.userAgent("Mozilla")
						.timeout(10*1000)
						.get();
				break;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return doc;
	}
	public static void main(String[] args) throws IOException, ClassNotFoundException, URISyntaxException {
		// TODO Auto-generated method stub
		Timer timer = new Timer();
		  timer.schedule(new Task(), new Date(), 1000 * 60 * 60 );
		  try {
		   Thread.sleep(1000);
		  } catch (Exception ex) {
		   timer.cancel();
		  }		
	}
}
