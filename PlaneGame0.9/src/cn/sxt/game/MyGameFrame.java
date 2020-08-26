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
 * �ɻ���Ϸ��������
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
	public void paint(Graphics g) {	//�Զ������ã�g�൱��һֻ����
		g.drawImage(bj, 0, 0, null);
		plane.drawSelf(g);//���ɻ�
		
		//���������ڵ�
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
	//���������ػ�����
	class PaintThread extends Thread{
		@Override
		public void run() {
			while(true) {
				//System.out.println("�����ػ�һ��");
				repaint();//�ػ�
				
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
	 * ��ʼ������
	 */
	public void launchFrame() {
		this.setTitle("��ϰ��Ʒ");
		this.setVisible(true);
		this.setSize(Constant.GAME_WIDTH,Constant.GAME_HEIGHT);
		this.setLocation(300, 300);
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		new PaintThread().start();	//�����ػ����ڵ��߳�
		addKeyListener(new KeyMonitor());	//���������Ӽ��̵ļ���
		
		//��ʼ��50���ڵ�
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
			offScreenImage=this.createImage(Constant.GAME_WIDTH,Constant.GAME_HEIGHT);//������Ϸ�����յĸ߶ȺͿ��
			
			Graphics gOff = offScreenImage.getGraphics();
			paint(gOff);
			g.drawImage(offScreenImage, 0, 0, null);
		
	}
	
	
}
