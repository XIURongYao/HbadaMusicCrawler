package com.hbada.demo;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
/**
 * 
* Copyright: Copyright (c) 2017 OSFF
* 
* @ClassName: SimpleMusicCrawler.java
* @Description: 简单的下载QQ音乐的例子《永恒的印记》单次下载，稍微麻烦的是url的地址的获取
*
* @version: v1.0.0
* @author: 水煮鱼
* @date: 2017年11月16日 下午8:02:30 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2017年11月16日      水煮鱼                                     v1.0.0               修改原因
 */
public class SimpleMusicCrawler {
	
	public static void main(String[] args) {
		URL url = null;  
        try {  
            url = new URL("http://dl.stream.qqmusic.qq.com/C400002gjiqa0nbYLR.m4a?vkey=129D91D943D2C242A27D0707F0A289F6473059503A591A3723C13EAF23C2A006411B6B678AA655E95A46C0134B2C3E9A1CF1A60BDF3ACE58&guid=3917925390&uin=0&fromtag=66");  
            DataInputStream dataInputStream = new DataInputStream(url.openStream());  
            String imageName = "qqmusic.m4a";  
            File file=new File("e:/music/");    //设置下载路径  
            if(!file.isDirectory()){  
                file.mkdirs();  
            }  
            FileOutputStream fileOutputStream = new FileOutputStream(new File("e:/music/"+ imageName.trim()));  
            byte[] buffer = new byte[1024];  
            int length;  
            while ((length = dataInputStream.read(buffer)) > 0) {  
                fileOutputStream.write(buffer, 0, length);  
            }  
            dataInputStream.close();  
            fileOutputStream.close();  
        } catch (MalformedURLException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
	}

}
