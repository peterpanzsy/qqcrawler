package com.qq.crawler;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

import javax.security.auth.callback.LanguageCallback;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import dao.Dao;
import dao.SongInfoDao;

public class SongInfoCrawler {
	
	SongInfo sInfo;
	Dao dao;
	SongInfoDao sDao;
	private HashSet<String> crawledSongs;
	
	public SongInfoCrawler() {
		// TODO Auto-generated constructor stub
		
	}
	
	public void initial() throws ClassNotFoundException{
		crawledSongs = new HashSet<String>();
		sDao = new SongInfoDao();
		ArrayList<Map<String, String>> songs = sDao.getResult("mid", "qqsonginfoc");
		for (int i = 0; i < songs.size(); i++) {
			crawledSongs.add(songs.get(i).get("mid"));
		}
	}
	
	public void crawlSongInfo() throws ClassNotFoundException, URISyntaxException{
		sDao = new SongInfoDao();
		ArrayList<Map<String, String>> search = sDao.getResult("*", "qqsonginfos");
		for(int i = 0; i < search.size(); i++){
			String id = search.get(i).get("id");
			String sname = search.get(i).get("sname").trim().replace(" ", "%20");
			URI uri = new URI(sname);
			sname = uri.toASCIIString();
			String singer = search.get(i).get("singer").trim().replace(" ", "%20");
			//System.out.println(singer.length());
			uri = new URI(singer);
			singer = uri.toASCIIString();
			String album = search.get(i).get("album").trim().replace(" ", "%20");
			System.out.println("album:"+album);
			uri = new URI(album);
			album = uri.toASCIIString();
			//System.out.println("album2:"+album);
			System.out.println(id+sname+singer+album);
			
			String url3 = "http://s.plcloud.music.qq.com/fcgi-bin/smartbox.fcg?o_utf8=1&utf8=1&key=" + sname + "%20" + singer + "&g_tk=5381&loginUin=0&hostUin=0&format=jsonp&inCharset=GB2312&outCharset=utf-8&notice=0&platform=yqq&jsonpCallback=MusicJsonCallBack&needNewCode=0";
			Document doc3 = null;
			doc3 = this.getHtmlContent(url3);
			String res = doc3.select("body").text();
			int length = res.length();
			res = res.substring(18, length-2);
			System.out.println("res:"+res);
			JSONObject resObject = JSONObject.fromObject(res);
			String tips = resObject.getString("tips");
			//String t = "{\"song\":[],\"singer\":[],\"album\":[],\"mv\":[]}";
			System.out.println("tips:"+tips);
			JSONObject dataObject = JSONObject.fromObject(tips);
			String song = dataObject.getString("song");
			System.out.println("song:"+song);
			//JSONObject songObject = JSONObject.fromObject(song);
			//String itemlist = songObject.getString("itemlist");
			if(song.length()>3){
				JSONArray songArray = JSONArray.fromObject(song);
//				JSONObject songObject = JSONObject.fromObject(song);
				JSONObject midObject = (JSONObject) songArray.get(0);
				String mid = midObject.getString("mid");
				System.out.println("mid:"+mid);
				String url4 = "http://s.plcloud.music.qq.com/fcgi-bin/fcg_yqq_song_detail_info.fcg?songmid=" + mid;
				Document doc4 = null;
				doc4 = this.getHtmlContent(url4);
				this.getAndSaveSongInfo(doc4, mid, id);
			}else{
				String url = "http://soso.music.qq.com/fcgi-bin/multiple_music_search.fcg?mid=1&p=1&catZhida=1&lossless=0&t=100&utf8=1&w=" + sname + "%20" + singer + "%20" + album;
				//String url = "http://soso.music.qq.com/fcgi-bin/multiple_music_search.fcg?mid=1&p=1&catZhida=1&lossless=0&t=100&utf8=1&w=ϲ����%20BEYOND%20���ܾ���";
				System.out.println(url);
				Document doc = null;
				doc = this.getHtmlContent(url);
				String url2 = this.getURL(doc);
				if(url2 != null){
					System.out.println("url2:"+url2);
					String mid = url2.split("=")[1].split("&")[0];
					//System.out.println(mid);
					Document doc2 = null;
					doc2 = this.getHtmlContent(url2);
					this.getAndSaveSongInfo(doc2, mid, id);
				}
				//else sDao.updateSongInfo("notfound", id);
			}
		}
		
	}
	
	public String getURL(Document doc){
		if (doc!=null) {
			Elements elements = doc.select("div.music_name").select("a");
			String url = null;
			for(Element element : elements){
				url = element.attr("href");
				if(!url.equalsIgnoreCase("javascript:;")){
					return url;
				}
			}
			return null;
		}else {
			System.err.println("The html is not fetched!");
			return null;
		}
	}
	
	public void getAndSaveSongInfo(Document doc, String mid, String id){
		ArrayList<String> SongInfo = null;
		sInfo = new SongInfo();
		sInfo.id = id;
		if(!crawledSongs.contains(mid)){
			crawledSongs.add(mid);
			sInfo.mid = mid;
			if (doc!=null) {
				String sname = doc.select("div.song_title").select("span").text();
				sInfo.sname = sname;
				SongInfo = extractSongInfo(doc);
			}else {
				System.err.println("The html is not fetched!");
			}
			if (SongInfo!=null && SongInfo.size()>0) {
				sInfo.singer = SongInfo.get(0);
				sInfo.language = SongInfo.get(1);
				sInfo.album = SongInfo.get(2);
				sInfo.date = SongInfo.get(3);
			}

			sDao.insertSongInfo(sInfo);
			sDao.updateSongInfo(sInfo.mid, id);
		}
		
		crawledSongs.add(mid);

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
	
	public ArrayList<String> extractSongInfo(Document doc){
		ArrayList<String> SongInfoList = new ArrayList<String>();
		Elements songInfo = doc.select("ul.song_info").select("li");
		//System.out.println("size:"+songInfo.size());
		for (Element element : songInfo) {
			//System.out.println("info:"+ element.text());
			String[] info = element.text().split("��");
			if(info.length>1){
				//System.out.println("info:"+info[1]);
				SongInfoList.add(info[1]);
			}
		}
		return SongInfoList;
	}
	

	/**
	 * @param args
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws URISyntaxException 
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException, URISyntaxException {
		// TODO Auto-generated method stub
		SongInfoCrawler sCrawler = new SongInfoCrawler();
		sCrawler.initial();
		sCrawler.crawlSongInfo();			
	}

}
