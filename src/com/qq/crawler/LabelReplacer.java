package com.qq.crawler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.dom4j.io.XMLWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Node;


public class LabelReplacer {
	
	public void replaceLabel() throws IOException, TransformerException{
		File input = new File("F:\\刘宇坤\\java\\新建文件夹\\relatedsong.gexf");
		Document doc = Jsoup.parse(input, "UTF-8");
		Elements labelInfo = doc.select("node");
		//System.out.println("size:"+songInfo.size());
		String i = "0";
		for (Element element : labelInfo) {
			//System.out.println(i);
			//System.out.println(element.attr("label"));
			element.attr("label", i);
			i = (Integer.parseInt(i) + 1) + "";
		}
		//XMLWriter writer = new XMLWriter(new FileWriter("F:\\刘宇坤\\java\\新建文件夹\\relatedsong1.gexf"));
		//writer.write(doc);
		//writer.close();
		String docu = doc.toString();
		File file = new File("F:\\刘宇坤\\java\\新建文件夹\\relatedsong2.gexf");
		FileOutputStream outstream = new FileOutputStream(file);
		outstream.write(docu.getBytes("utf-8"));
		outstream.close();
	}
	public static void main(String[] args) throws IOException, TransformerException {
		LabelReplacer lr = new LabelReplacer();
		lr.replaceLabel();
	}
}

