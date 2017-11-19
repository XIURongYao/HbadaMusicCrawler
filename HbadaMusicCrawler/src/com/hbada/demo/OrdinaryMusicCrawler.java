package com.hbada.demo;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 
* Copyright: Copyright (c) 2017 OSFF
* 
* @ClassName: OrdinaryMusicCrawler.java
* @Description: 普通的单线程通过歌曲名字下载QQ音乐的方法类,只能下载第一页的20首.
* 	参考了https://www.cnblogs.com/dearvee/p/6602677.html 的python下载的实现方法
* 	dearvee 兄弟把url找到了，算是把问题解决了一大半..
*   java对json的解析貌似有更简便的方法，有时间再重写一下吧..
*
* @version: v1.0.0
* @author: 水煮鱼
* @date: 2017年11月16日 下午8:13:18 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2017年11月16日      水煮鱼                                     v1.0.0               修改原因
 */
public class OrdinaryMusicCrawler{
	
	private static List <Object> song_name = new ArrayList<Object>(); 
	private static List <Object> people_name =new ArrayList<Object>();
	
	public static void main(String[] args) {
		String f = "e:/music/"; //下载到此位置
		String w = "冯提莫";       //你搜索的歌手或者歌曲名字
		downTaskList(f,w);
	}
	
	/**
	 * url1的获取
	 * @param word
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static List<String> url1(String word) {
		
		String url1 = "https://c.y.qq.com/soso/fcgi-bin/client_search_cp?&lossless=0&flag_qc=0&p=1&n=20&w="+word;
		try {
			//1.请求网页
			URL pageUrl = new URL(url1);
			//2.下载网页
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					pageUrl.openStream()));
			String line;
			//3.读取网页
			StringBuffer pageBuffer = new StringBuffer();
			while ((line = reader.readLine()) != null) {
				pageBuffer.append(line);
			}
		
			line = pageBuffer.toString().substring(9, pageBuffer.toString().length()-1);
			//System.out.println(line);
			JSONObject jo = new JSONObject();
			JSONObject joline = jo.parseObject(line);
			
			JSONObject jldata = (JSONObject) joline.get("data");
			JSONObject jldata_song = (JSONObject) jldata.get("song");
			JSONArray jldata_song_list = (JSONArray) jldata_song.get("list");
		
			List<Object> mids = null,songmids = null,songnames = null,singers = null;
			mids = new ArrayList<Object>();
			songmids = new ArrayList<Object>();
			songnames = new ArrayList<Object>();
			singers = new ArrayList<Object>();
			
			for(Object j : jldata_song_list){
				mids.add(((JSONObject) j).get("media_mid"));
				songmids.add(((JSONObject) j).get("songmid"));
				songnames.add(((JSONObject) j).get("songname"));
				singers.add(((JSONObject)((JSONArray)((JSONObject) j).get("singer")).get(0)).get("name"));
			}
			song_name = songnames;
			people_name = singers;
			
			return url2(mids,songmids,songnames,singers);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * url2的获取
	 * @param mids
	 * @param songmids
	 * @param songnames
	 * @param singers
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static List<String> url2(List<Object> mids,List<Object> songmids,List<Object> songnames,List<Object> singers ){
		
		List<String> vkeys = new ArrayList<String>();
		
		for(int i=0;i<mids.size();i++){
			
			String url2 = "https://c.y.qq.com/base/fcgi-bin/fcg_music_express_mobile3.fcg?&jsonpCallback=MusicJsonCallback&cid=205361747"
					+ "&songmid="+songmids.get(i)+"&filename=C400"+mids.get(i)+".m4a&guid=6612300644";
			try {
				URL pageUrl = new URL(url2);
				BufferedReader reader = new BufferedReader(new InputStreamReader(
						pageUrl.openStream()));
				String line;
				StringBuffer pageBuffer = new StringBuffer();
				while ((line = reader.readLine()) != null) {
					pageBuffer.append(line);
				}
				
				JSONObject jo = new JSONObject();
				JSONObject joline = jo.parseObject(pageBuffer.toString());
				
				JSONObject jldata = (JSONObject) joline.get("data");
				
				if(((JSONArray) jldata.get("items")).size()>0){
					JSONObject jldata_items = (JSONObject)((JSONArray) jldata.get("items")).get(0);
					String vkey = (String) jldata_items.get("vkey");
					vkeys.add(vkey);
				}else{
					vkeys.add("");
				}
			
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return url3(mids,vkeys);
	}
	
	/**
	 * url3的获取
	 * @param mids
	 * @param vkeys
	 * @return
	 */
	public static List<String> url3(List<Object> mids,List<String> vkeys){
		List<String> url3s = new ArrayList<String>();
		for(int i=0;i<mids.size();i++){
			String url3 = "http://dl.stream.qqmusic.qq.com/C400"+mids.get(i)+".m4a?vkey="+vkeys.get(i)+"&guid=6612300644&uin=0&fromtag=66";
			url3s.add(url3);
		}
		return url3s;
	}
	

	/**
	 * 下载单个music
	 * @param f
	 * @param n
	 * @param u
	 * @return
	 */
	public static boolean downMusic(String f,String n,String u){
		//下载音乐数据的方法
        try {  
        	URL url = new URL(u);  
            DataInputStream dataInputStream = new DataInputStream(url.openStream());  
            String imageName = n+".m4a";
            File file=new File(f); 
            if(!file.isDirectory()){  
                file.mkdirs();  
            }  
            FileOutputStream fileOutputStream = new FileOutputStream(new File(f + imageName.trim()));  
            byte[] buffer = new byte[1024];  
            int length;  
            while ((length = dataInputStream.read(buffer)) > 0) {  
                fileOutputStream.write(buffer, 0, length);  
            }  
            dataInputStream.close();  
            fileOutputStream.close();
            return true;
        } catch (Exception e) {
        	//e.printStackTrace();//有时下载会出错返回false
        }
        
		return false;  
	}

	/**
	 * 开始下载任务
	 * @param f
	 * @param w
	 */
	public static void downTaskList(String f,String w){
		long d1 = new Date().getTime();
		List<String> url1 = url1(w);
		for(int i=0;i<url1.size();i++){
			if(downMusic(f,song_name.get(i)+"-"+people_name.get(i),(String)url1.get(i))){
				System.out.println("已下载"+(i+1)+"首");
			}else{
				System.out.println("第"+(i+1)+"首下载失败");
			}
		}
		System.out.print("QQ音乐下载完成.");
		long d2 = new Date().getTime();
		System.out.println("用时："+(d2-d1)/1000+"秒");
	}

}
