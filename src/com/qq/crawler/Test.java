package com.qq.crawler;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Timer;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.sun.xml.internal.ws.wsdl.writer.document.soap.Body;

public class Test {
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		File dir1 = new File("F:\\¡ı”Ó¿§\\java\\data\\wordcloud\\1");
		File dir2 = new File("F:\\¡ı”Ó¿§\\java\\data\\wordcloud\\2");
		File[] files1 = dir1.listFiles();
		File[] files2 = dir2.listFiles();
		int count = 0;
		for (File file1 : files1) {
			String name1 = file1.getName().split("-")[0];
			for (File file2 : files2) {
				String name2 = file2.getName().split("-")[0];
				if (name1.equals(name2)) {
					System.out.println(name1);
					count++;
					break;
				}
			}
		}
		System.out.println(count);
	}
}
