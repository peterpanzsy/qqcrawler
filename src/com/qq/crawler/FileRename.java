package com.qq.crawler;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import dao.FileRenameDao;

public class FileRename {
	public static void main(String[] args) throws ClassNotFoundException {
		FileRenameDao fDao;
		fDao = new FileRenameDao();
		File dir = new File("F:\\¡ı”Ó¿§\\java\\data\\wordcloud\\2");
		String dirPath = dir.getAbsolutePath();
		ArrayList<Map<String,String>> list = new ArrayList<Map<String,String>>();
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			for (File file : files) {
				String name = file.getName();
				name = name.split(".png")[0];
				System.out.println(name);
				list = fDao.getResult("id,sname", "qqsonginfoc", name);
				String id = list.get(0).get("id");
				String sname = list.get(0).get("sname");
				System.out.println(id + sname);
				String name2 = id + "-" + sname + ".png";
				File toFile = new File("F:\\¡ı”Ó¿§\\java\\data\\wordcloud\\2\\" + name2);
				if (file.exists() && !toFile.exists()) {
					file.renameTo(toFile);
				}
			}
		}
	}
}
