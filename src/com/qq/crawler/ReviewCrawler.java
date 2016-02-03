package com.qq.crawler;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.sun.org.apache.xml.internal.serializer.ElemDesc;

import dao.ReviewInfoDao;

public class ReviewCrawler {
	ReviewInfo rInfo;
	ReviewInfoDao rDao;
	public ReviewCrawler() {
		// TODO Auto-generated constructor stub
	}
	public void crawlReview() throws ClassNotFoundException, URISyntaxException{
		rDao = new ReviewInfoDao();
		ArrayList<Map<String, String>> search = new ArrayList<Map<String, String>>();
		search = rDao.getResult("id, mid, sname", "qxsonginfoc");
		for(int i = 0; i < search.size(); i++){
			String id = search.get(i).get("id");
			String mid = search.get(i).get("mid");
			String sname = search.get(i).get("sname").trim().replace(" ", "%20");
			try {
				URI uri = new URI(sname);
				sname = uri.toASCIIString();
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("uriException:"+id);
				continue;
			}
			String url = "http://qzone-music.qq.com/fcg-bin/get_tweetlist.fcg?utf8=1&pageflag=1&reqnum=10&oututf8=1&g_tk=6c4aec72ac3b398a1dd3e564ac66efed&pageinfolen=0&pageinfobuf=&topic=" + sname + "&loginUin=0&hostUin=0&format=jsonp&inCharset=GB2312&outCharset=utf8&notice=0&platform=yqq&jsonpCallback=jsonCallback&needNewCode=0";
			Document doc = null;
			doc = this.getHtmlContent(url);
			String res = doc.select("body").text();
			String s[] = res.split("\",endflag")[0].split("infobuf:\"");
			if (s.length < 2) {
				try {
					Thread.sleep(5*1000);
					doc = this.getHtmlContent(url);
					res = doc.select("body").text();
					s = res.split("\",endflag")[0].split("infobuf:\"");
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					System.out.println("Error:"+e1.getMessage());
				}
				if (s.length < 2){
					continue;
				}
			}
			this.getAndSaveReview(doc, id, mid, sname);
			try {
				Thread.sleep(5*1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	public void getAndSaveReview(Document doc, String id, String mid, String sname) throws ClassNotFoundException{
		if (doc!=null) {
			String res = doc.select("body").text();
			int totalnum;
			int curnum;
			int cnt;
			int tr = 0;
			JSONObject resObject;
			String pageinfobuf = "";
			try {
				res = res.substring(13, res.length()-1);//.replace("n't", "nt");
				resObject = JSONObject.fromObject(res);
				totalnum = Integer.parseInt(resObject.getString("totalnum"));
				//curnum = Integer.parseInt(resObject.getString("curnum"));
				curnum = 10;
				pageinfobuf = resObject.getString("pageinfobuf");
				this.extractReviewInfo1(resObject, id, mid);
				System.out.println("try");
			} catch (Exception e) {
				// TODO: handle exception
				totalnum = Integer.parseInt(res.split(",curnum")[0].split("num:")[1]);
				//curnum = Integer.parseInt(res.split(",pageinfolen")[0].split("curnum:")[1]);
				curnum = 10;
				pageinfobuf = res.split("\",endflag")[0].split("infobuf:\"")[1];
				this.extractReviewInfo2(res, id, mid);
				System.out.println("catch");
			}
			if (totalnum > 3000) {
				totalnum = 3000;
			}
			cnt = curnum;
			while(cnt < totalnum){
				String url = "http://qzone-music.qq.com/fcg-bin/get_tweetlist.fcg?utf8=1&pageflag=1&reqnum=10&oututf8=1&g_tk=6c4aec72ac3b398a1dd3e564ac66efed&pageinfolen=24&pageinfobuf=" + pageinfobuf + "&topic=" + sname + "&loginUin=0&hostUin=0&format=jsonp&inCharset=GB2312&outCharset=utf8&notice=0&platform=yqq&jsonpCallback=jsonCallback&needNewCode=0";
				try {
					doc = this.getHtmlContent(url);
					res = doc.select("body").text();
				} catch (Exception e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
					System.out.println("Error3:"+e3.getMessage());
				}
				int ex1;
				try {
					res = res.substring(13, res.length()-1);//.replace("n't", "nt");
					resObject = JSONObject.fromObject(res);
					//curnum = Integer.parseInt(resObject.getString("curnum"));
					curnum = 10;
					int times = 0;
					while(!resObject.getString("ret").equals("0")){
						try {
							Thread.sleep(5*1000);
							doc = this.getHtmlContent(url);
							res = doc.select("body").text();
							res = res.substring(13, res.length()-1);
							resObject = JSONObject.fromObject(res);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							System.out.println("Error:"+e1.getMessage());
						}
						times++;
						if (times == 10) {
							break;
						}
					}
					pageinfobuf = resObject.getString("pageinfobuf");
					//System.out.println(pageinfobuf);
					
					ex1 = this.extractReviewInfo1(resObject, id, mid);
					if(ex1 == 0)
						tr++;
					else tr = 0;
					if(tr == 100){
						System.out.println("break");
						break;
					}
					System.out.println("try");
				} catch (Exception e) {
					// TODO: handle exception
					//curnum = Integer.parseInt(res.split(",pageinfolen")[0].split("curnum:")[1]);
					
					curnum = 10;
					try {
						String s[] = res.split("\",endflag")[0].split("infobuf:\"");
						//System.out.println(s[s.length-1]);
						int times = 0;
						while(s.length < 2){
							try {
								Thread.sleep(5*1000);
								doc = this.getHtmlContent(url);
								res = doc.select("body").text();
								s = res.split("\",endflag")[0].split("infobuf:\"");
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
								System.out.println("Error:"+e1.getMessage());
							}
							times++;
							if (times == 10) {
								break;
							}
						}
						pageinfobuf = s[1];
						this.extractReviewInfo2(res, id, mid);
						System.out.println("catch");
					} catch (Exception e2) {
						// TODO: handle exception
						System.out.println("Error2:"+e2.getMessage());
					}
				}
				cnt += curnum;
				try {
					Thread.sleep(2*1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					System.out.println("Error:"+e1.getMessage());
				}
			}
			System.out.println(totalnum+" "+cnt);
						
		}else {
			System.err.println("The html is not fetched!");
		}
	}
	private String getLocation(String weiboName) {
		// TODO Auto-generated method stub
		String url = "http://t.qq.com/" + weiboName;
		try {
			Document doc = this.getHtmlContent(url);
			String location = doc.select("a[boss = btnApolloCity]").text();
			return location;
		} catch (Exception e) {
			// TODO: handle exception
			String location = "";
			return location;
		}
	}
	
	private int extractReviewInfo1(JSONObject resObject, String id, String mid){
		
		String list = resObject.getString("list");
		JSONArray listArray = JSONArray.fromObject(list);
		for(int j = 0; j < listArray.size(); j++){
			JSONObject rInfoObject = listArray.getJSONObject(j);
			String reviewer = rInfoObject.getString("nick");
			String weiboName = rInfoObject.getString("name");
			String location = this.getLocation(weiboName);
			String content = rInfoObject.getString("content");
			System.out.println("id,content:"+id+" "+content);
			String time = rInfoObject.getString("showtime");
			rInfo = new ReviewInfo();
			rInfo.id = id;
			rInfo.mid = mid;
			rInfo.reviewer = reviewer;
			rInfo.location = location;
			rInfo.content = content;
			rInfo.time = time;
			rDao.insertListenInfo(rInfo);
		}
		return listArray.size();
	}
	
	private void extractReviewInfo2(String res, String id, String mid) {
		// TODO Auto-generated method stub
		String[] list = res.split("tweetid:");
		for(int j = 1; j < list.length; j++){
			String reviewer = list[j].split(",nick:'")[1].split("',name:")[0];
			String weiboName = list[j].split(",name:'")[1].split("',content:")[0];
			String location = this.getLocation(weiboName);
			String content = list[j].split(",content:'")[1].split("'}")[0];
			System.out.println("id,content:"+id+" "+content);
			String time = list[j].split(",showtime:'")[1].split("',face:")[0];
			rInfo = new ReviewInfo();
			rInfo.id = id;
			rInfo.mid = mid;
			rInfo.reviewer = reviewer;
			rInfo.location = location;
			rInfo.content = content;
			rInfo.time = time;
			rDao.insertListenInfo(rInfo);
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
	public static void main(String[] args) throws IOException, ClassNotFoundException, URISyntaxException {
		// TODO Auto-generated method stub
		ReviewCrawler rCrawler = new ReviewCrawler();
		rCrawler.crawlReview();			
	}
}
