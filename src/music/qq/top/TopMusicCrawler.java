package music.qq.top;

import java.io.IOException;
import java.sql.SQLException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import dao.TopDao;

public class TopMusicCrawler {
	private static TopDao tDao = null;
	
	public void getTopSong(String toptype, String url) throws Exception{
		Document doc = Jsoup.connect(url)
				.userAgent("Mozilla")
				.timeout(5*1000)
				.ignoreContentType(true)
				.get();
//		String cookie = Jsoup.connect(url).response().cookie("lskey");
		String textData = doc.body().text();
		String[] songs = textData.split("s:");
		for (String song : songs) {
			String[] sdata = song.split("\\|");
			if (sdata.length<20) {
				continue;
			}
			String sname= sdata[1];
			String singer = sdata[3];
			String album = sdata[5];
			String mid = sdata[20];
			if (!tDao.isExis(mid)) {
				tDao.insertTopSongInfo(mid, sname, singer, album);
			}
			
			tDao.insertTopDaily(mid, toptype);
		}
	}
	
	public void taskList() throws Exception{
		tDao = new TopDao();
		String fixed = "loginUin=0&hostUin=0&format=jsonp&" +
				"inCharset=GB2312&outCharset=utf-8&notice=0&platform=yqq&jsonpCallback=MusicJsonCallback" +
				"&needNewCode=0";
		String url1 ="http://y.qq.com/y/static/toplist/json/top/7/1.js?"+fixed;
		String url2="http://y.qq.com/y/static/toplist/json/top/2/1.js?"+fixed;
		String url3="http://y.qq.com/y/static/toplist/json/top/1/1.js?"+fixed;
		String url4="http://y.qq.com/y/static/toplist/json/top/6/1.js?"+fixed;
		String url5="http://y.qq.com/y/static/toplist/json/top/9/1.js?"+fixed;
		String url6="http://y.qq.com/y/static/toplist/json/top/10/1.js?"+fixed;
		String url7="http://y.qq.com/y/static/toplist/json/mv/1_1.js?"+fixed;
		String url8="http://y.qq.com/y/static/toplist/json/top/11/1.js?"+fixed;
		String url9="http://y.qq.com/y/static/toplist/json/top/12/1.js?"+fixed;
		
		System.out.println("爬取流行指数");
		this.getTopSong("流行指数", url1);
		System.out.println("爬取内地");
		this.getTopSong("内地", url2);
		System.out.println("爬取港台");
		this.getTopSong("港台", url3);
		System.out.println("爬取欧美");
		this.getTopSong("欧美", url4);
		System.out.println("爬取韩国");
		this.getTopSong("韩国", url5);
		System.out.println("爬取日本");
		this.getTopSong("日本", url6);
		System.out.println("爬取MV");
		this.getTopSong("MV", url7);
		System.out.println("爬取民谣");
		this.getTopSong("民谣", url8);
		System.out.println("爬取摇滚");
		this.getTopSong("摇滚", url9);
		
		tDao.disConnect();
	}
	
	
	public void getPopularIndex() throws IOException, SQLException{
		String url = "http://y.qq.com/y/static/toplist/json/top/2/1.js?loginUin=0&hostUin=0&format=jsonp&" +
				"inCharset=GB2312&outCharset=utf-8&notice=0&platform=yqq&jsonpCallback=MusicJsonCallback" +
				"&needNewCode=0";
		Document doc = Jsoup.connect(url)
				.userAgent("Mozilla")
				.timeout(5*1000)
				.ignoreContentType(true)
				.get();
//		System.out.println(doc);
		String textData = doc.body().text();
		String[] songs = textData.split("s:");
		for (String song : songs) {
			String[] sdata = song.split("\\|");
			if (sdata.length<20) {
				continue;
			}
			System.out.println(sdata[1]);
			System.out.println(sdata[3]);
			System.out.println(sdata[5]);
			System.out.println(sdata[20]);
			System.out.println(sdata[6]);
		}
		
	}

	/**
	 * @param args
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		TopMusicCrawler tMusicCrawler = new TopMusicCrawler();
//		tMusicCrawler.getPopularIndex();
		tMusicCrawler.taskList();

	}

}
