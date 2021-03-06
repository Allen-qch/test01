package cn.sxt.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
/**
 * 飞机游戏的主窗口
 * @author Administrator
 *
 */
public class MyGameFrame extends Frame{
	Image planeImg = GameUtil.getImage("images/plane.png");
	Image bj = GameUtil.getImage("images/bj.jpg");
	Plane plane = new Plane(planeImg,250,250);
	int planeX = 250,planeY = 250;
	Shell[] shells = new Shell[50];
	
	Explode bao;
	
	
	@Override
	public void paint(Graphics g) {	//自动被调用，g相当于一只画笔
		g.drawImage(bj, 0, 0, null);
		plane.drawSelf(g);//画飞机
		
		//画出所有炮弹
		for(int i=0;i<shells.length;i++){
			shells[i].draw(g); 
			
			boolean peng = shells[i].getRect().intersects(plane.getRect());
			
			if(peng) {
				plane.live = false;
				if(bao==null) {
					bao = new Explode(plane.x, plane.y);
				}
				bao.draw(g);
			}
			
		}
		
	}
	//帮助我们重画窗口
	class PaintThread extends Thread{
		@Override
		public void run() {
			while(true) {
				//System.out.println("窗口重画一次");
				repaint();//重画
				
				try {
					Thread.sleep(40);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	class KeyMonitor extends KeyAdapter{

		@Override
		public void keyPressed(KeyEvent e) {
			plane.addDirection(e);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			plane.minusDirection(e);
		}
		
	}
	
	/**
	 * 初始化窗口
	 */
	public void launchFrame() {
		this.setTitle("练习作品");
		this.setVisible(true);
		this.setSize(Constant.GAME_WIDTH,Constant.GAME_HEIGHT);
		this.setLocation(300, 300);
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		new PaintThread().start();	//启动重画窗口的线程
		addKeyListener(new KeyMonitor());	//给窗口增加键盘的监听
		
		//初始化50个炮弹
		for(int i=0;i<shells.length;i++) {
			shells[i]=new Shell();
		}
	}
	public static void main(String[] args) {
		MyGameFrame f = new MyGameFrame();
		f.launchFrame();
	}
	
	private Image offScreenImage = null;
	
	public void update(Graphics g) {
		if(offScreenImage==null) 
			offScreenImage=this.createImage(Constant.GAME_WIDTH,Constant.GAME_HEIGHT);//这是游戏窗口普的高度和宽度
			
			Graphics gOff = offScreenImage.getGraphics();
			paint(gOff);
			g.drawImage(offScreenImage, 0, 0, null);
		
	}
	
	
}
