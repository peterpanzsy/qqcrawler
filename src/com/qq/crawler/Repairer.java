package com.qq.crawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;



import dao.RepairDao;

public class Repairer {
	RepairDao repairDao;
	RepairInfo rInfo;
	public void export() throws ClassNotFoundException {
		repairDao = new RepairDao();
		rInfo = new RepairInfo();
		String [][] listen = new String [30][24];
		String [] listen2 = new String [720];
		String [] datetime = new String [720];
		ArrayList<Map<String, String>> idList = new ArrayList<Map<String, String>>();
		idList = repairDao.getResult("id", "xiamisonginfoc");
		ArrayList<Map<String, String>> search = new ArrayList<Map<String, String>>();
		for (int a = 0; a < idList.size(); a++) {
			String id = idList.get(a).get("id");
			search = repairDao.getResult("id, time, listentimes", "xiamilistentimes", id);
			int length = search.size();
			int x = 0;
			for (int i = 0; i < listen2.length; i++) {
				if (x == length-1) {
					listen2[i] = search.get(x).get("listentimes");
					datetime[i] = search.get(x).get("time").substring(0, 19);
					x++;
				}
				else if(x > length-1) {
					listen2[i] = "0"; 
					datetime[i] = "0";
				}
				else {
					String time = search.get(x).get("time").substring(0, 19);
					//System.out.println(time.substring(11, 13));
					String listentimes = search.get(x).get("listentimes");
					String time2 = search.get(x+1).get("time");
					//System.out.println(time2.substring(11, 13));
					int h = Integer.parseInt(time.substring(11, 13));
					int d = Integer.parseInt(time.substring(8, 10));
					int h2 = Integer.parseInt(time2.substring(11, 13));
					int d2 = Integer.parseInt(time2.substring(8, 10));
					if (h2-h != 1 && h2-h != -23) {
						listen2[i] = listentimes;
						datetime[i] = time;
						int t = 24*(d2-d)+h2-h-1;
						for (int j = 1; j <= t; j++) {
							listen2[i+j] = "0";
							if (h == 23) {
								time = time.substring(0, 8)+(d+1)+" 00:30:00";
								datetime[i+j] = time;
								h = 0;
							}
							else {
								time = time.substring(0, 11)+(++h)+":30:00";
								datetime[i+j] = time;
							}
						}
						i = i + t;
						x++;
					}
					else {
						listen2[i] = listentimes;
						datetime[i] = time;
						x++;
					}
				}
				//System.out.println(listen2[i]);
			}
			System.out.println(listen2.length);
			System.out.println(length);
			for (int j = 0; j < listen.length; j++) {
				for (int k = 0; k < listen[j].length; k++) {
					listen[j][k] = listen2[j*24+k];
					System.out.println(listen[j][k]);
				}
			}
			repair(listen, listen2, length, datetime, id);
		}
	}
	//目前出现的缺失不超过48小时，且二维数组第2、3行完整
	private void repair(String[][] listen, String[] listen2, int length, String[] datetime, String id) {
		// TODO Auto-generated method stub
		rInfo = new RepairInfo();
		int mark = 0;
		int label = 0;
		int[] location1 = new int[2];
		int[] location2 = new int[2];
		for (int i = 0; i < listen.length; i++) {
			for (int j = 0; j < listen[i].length; j++) {
				if (listen[i][j].equals("0")) {
					label++;
					if (label == 1) {
						location1[0] = i;
						location1[1] = j;
					}
					else {
						location2[0] = i;
						location2[1] = j;
					}
					continue;
				}
				if (label == 1) {
					int x = location1[0];
					int y = location1[1];
					listen2[x*24+y] = String.valueOf((Integer.parseInt(listen2[x*24+y-1]) + Integer.parseInt(listen2[x*24+y+1]))/2);
				} else if(label > 1) {
					int x1 = location1[0];
					int y1 = location1[1];
					int x2 = location2[0];
					int y2 = location2[1];
					int total = Integer.parseInt(listen2[x2*24+y2+1]) - Integer.parseInt(listen2[x1*24+y1-1]);
					System.out.println("total:"+total);
					int total2;
					if (x1 == x2) {
						total2 = Integer.parseInt(listen2[1*24+y2+1]) - Integer.parseInt(listen2[1*24+y1-1]);
						System.out.println("if total2:"+total2);
					}
					else {
						total2 = Integer.parseInt(listen2[2*24+y2+1]) - Integer.parseInt(listen2[1*24+y1-1]);
						System.out.println("else total2:"+total2);
					}
					for (int k = 0; k < label; k++) {
						double rk = (Integer.parseInt(listen2[1*24+y1-1+k+1])-Integer.parseInt(listen2[1*24+y1-1+k]))*1.0/total2;
						System.out.println("rk:"+rk);
						int s = (int) (total*rk);
						listen2[x1*24+y1+k] = String.valueOf(Integer.parseInt(listen2[x1*24+y1-1+k])+s);
						if (Integer.parseInt(listen2[x1*24+y1+k]) > Integer.parseInt(listen2[x2*24+y2+1])) {
							listen2[x1*24+y1+k] = listen2[x2*24+y2+1];
						}
					}
					length = length + label;
				}
				label = 0;
				if (i*24+j >= length-2 ) {
					mark = 1;
					System.out.println("break1");
					break;
				}
			}
			if (mark == 1 ) {
				System.out.println("break2");
				mark = 0;
				break;
			}
		}
		String [] dlistentimes =new String [720];
		for (int j = 0; j < listen.length; j++) {
			for (int k = 0; k < listen[j].length; k++) {
				if (j == 0 && k == 0) {
					dlistentimes[j*24+k] = "0";
				}
				else if (j*24+k > length-2) {
					dlistentimes[j*24+k] = "0";
				}
				else {
					dlistentimes[j*24+k] = String.valueOf(Integer.parseInt(listen2[j*24+k])-Integer.parseInt(listen2[j*24+k-1]));
				}
			}
		}
		for (int j = 0; j < listen.length; j++) {
			for (int k = 0; k < listen[j].length; k++) {
				listen[j][k] = listen2[j*24+k];
				System.out.println(listen[j][k]+" "+datetime[j*24+k]+" "+dlistentimes[j*24+k]);
				rInfo.id = id;
				rInfo.time = datetime[j*24+k];
				rInfo.listentimes = listen[j][k];
				rInfo.dlistentimes = dlistentimes[j*24+k];
				repairDao.insertRepair(rInfo, "xiamirepair");
				if (j*24+k >= length-2 ) {
					mark = 1;
					System.out.println("break3");
					break;
				}
			}
			if (mark == 1 ) {
				System.out.println("break4");
				mark = 0;
				break;
			}
		}
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		Repairer test2 = new Repairer();
		test2.export();
	}
}
