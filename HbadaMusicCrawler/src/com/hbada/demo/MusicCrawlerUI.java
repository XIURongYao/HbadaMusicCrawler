package com.hbada.demo;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class MusicCrawlerUI extends JFrame implements ActionListener,Runnable{
	
	private static final long serialVersionUID = 4284316464552703969L;
	private static int count = 1;
	private static String path = "e:\\music\\";
	
	private JPanel p1,p2,p3,p4;
	private JLabel jl1,jl2,jl3;
	private JTextField jtf1,jtf2;
	private JButton jb,jb2;
	
	public MusicCrawlerUI(){
		getTop();
		
		Container jp = this.getContentPane();
		jp.setLayout(new GridLayout(4,1));
		
		
		p1=new JPanel();//p1.setBackground(Color.red);
		p2=new JPanel();//p2.setBackground(Color.yellow);
		p3=new JPanel();//p3.setBackground(Color.blue);
		p4=new JPanel();//p4.setBackground(Color.GREEN);
         
		p2.setLayout(new BorderLayout());
        
		
        jl1=new JLabel("歌曲名或者歌手：");
        JPanel jp21 = new JPanel();
        jl2=new JLabel("下载位置：");
        jp21.add(jl2);
        
        jtf1=new JTextField();  
        jtf1.setColumns(20);
        jtf1.setText("gala");
       
        JPanel jp22 = new JPanel();
        jtf2=new JTextField();
        jtf2.setColumns(10);
        jtf2.setText("e:\\music\\");
        jtf2.setEditable(false);
        jp22.add(jtf2);
        
        
        JPanel jp23 = new JPanel();
        jb2 = new JButton("选择");
        Dimension dim2 = new Dimension(80,18);
        jb2.setPreferredSize(dim2);
        jb2.addActionListener(this);
        jp23.add(jb2);
        
        jb = new JButton("开始下载");
        Dimension  dim = new Dimension(100,20);
        jb.setPreferredSize(dim);
        jb.addActionListener(this);
        
        p1.add(jl1);
        p1.add(jtf1);
        p2.add(jp21,BorderLayout.WEST);
        p2.add(jp22,BorderLayout.CENTER);
        p2.add(jp23,BorderLayout.EAST);
       
        
        
        p3.add(jb);
        
        
        jl3 = new JLabel();
        jl3.setText("已下载首");
		p4.add(jl3);
        
        jp.add(p1);
        jp.add(p2);
        jp.add(p3);
        jp.add(p4);
        getBottom();
	}
	
	
	public static void main(String[] args) {
		/*MusicCrawlerUI musicCrawlerUI = new MusicCrawlerUI();
		Thread thread = new Thread(musicCrawlerUI);
		thread.start();*/
		
		new MusicCrawlerUI();
	}



	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==jb2){
			
			
			try{
			JFileChooser jfc=new JFileChooser();
	        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );
	        jfc.showDialog(new JLabel(), "选择存放位置");
	        File file=jfc.getSelectedFile();
	       
	        System.out.println("文件夹:"+file.getAbsolutePath());
	        
	        path = file.getAbsolutePath();
	        String w = jtf1.getText();//你搜索的歌手或者歌曲名字
	        jtf2.setText(path+"\\"+w+"\\");
	        //System.out.println(jfc.getSelectedFile().getName());
			}catch(Exception err){
				System.out.println("空指针");
			}
		}
		if(e.getSource()==jb){
			String w = jtf1.getText();//你搜索的歌手或者歌曲名字     
			//String f = jtf2.getText()==null ? "e:/music/"+w+"/" : jtf2.getText()+"/"+w+"/"; //下载到此位置
			//MusicCrawler.downTaskList(path+"\\"+w+"\\", w, 2);
			int p = 2;
			String f = path+"\\"+w+"\\";
			long d1 = new Date().getTime();
			for(int j=1;j<p;j++){
				Map<String,List<String>> songMap =MusicCrawler.url1(w,j);
				List<String> song_names = (List<String>) songMap.get("song_names");
				List<String> song_singers = (List<String>) songMap.get("song_singers");
				List<String> urls = (List<String>) songMap.get("song_urls");
				for(int i=0;i<urls.size();i++){
					if(MusicCrawler.downMusic(f,song_names.get(i)+"-"+song_singers.get(i),urls.get(i))){
						
						System.out.println("已下载"+(count)+"首");
						jl3.setText("已下载"+(count)+"首");
						count++;
					}else{
						
						System.out.println("第"+(count)+"首下载失败");
						jl3.setText("第"+(count)+"首下载失败");
						count++;
					}
				}
				if(urls.size()<1){
					break;
				}
			}
			System.out.print("QQ音乐下载完成.");
			long d2 = new Date().getTime();
			System.out.println("用时："+(d2-d1)/1000+"秒");
			jl3.setText("QQ音乐下载完成.用时："+(d2-d1)/1000+"秒");
		}
		
	}
	
	public void getTop(){
		this.setTitle("QQ音乐下载器");
		this.setSize(300, 250);
		this.setLocationRelativeTo(null);
	}
	public void getBottom(){
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	


	@Override
	public void run() {
		
	}
	
	public void downTaskList(String f,String w,int p){
		long d1 = new Date().getTime();
		for(int j=1;j<p;j++){
			Map<String,List<String>> songMap =MusicCrawler.url1(w,j);
			List<String> song_names = (List<String>) songMap.get("song_names");
			List<String> song_singers = (List<String>) songMap.get("song_singers");
			List<String> urls = (List<String>) songMap.get("song_urls");
			for(int i=0;i<urls.size();i++){
				if(MusicCrawler.downMusic(f,song_names.get(i)+"-"+song_singers.get(i),urls.get(i))){
					System.out.println("已下载"+(count++)+"首");
					//jl3.setText("已下载"+(count++)+"首");
				}else{
					System.out.println("第"+(count++)+"首下载失败");
					//jl3.setText("第"+(count++)+"首下载失败");
				}
			}
			if(urls.size()<1){
				break;
			}
		}
		System.out.print("QQ音乐下载完成.");
		long d2 = new Date().getTime();
		System.out.println("用时："+(d2-d1)/1000+"秒");
		//jl3.setText("QQ音乐下载完成.用时："+(d2-d1)/1000+"秒");
	}

}
