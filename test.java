import java.util.Scanner;
import java.awt.*;
import java.awt.event.*;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class test extends JPanel implements MouseMotionListener, MouseListener, ActionListener
{

	private int step = 1,click = 1;
	private static int ww = 300;
	private static int wh = 0;
	public ArrayList<Point> pointL = new ArrayList();
	public ArrayList<Point> pointR = new ArrayList();
	private ArrayList<Integer> gsinkVertex;
	static BufferedImage image = null;
	static BufferedImage image2 = null;
	static BufferedImage image3 = null;
	static int[][] pixels = null;
	static int[][] pixels2 = null;
 	private	final Point A_BREAK = new Point(-1,-1);
 	private Point p;
 	Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
 	static JFrame A = new JFrame();
	static Scanner input;
	static graph g1;
	static graph g2;
	public static void main(String args[]){
		
		System.out.println("Pls input the file name:");

		input = new Scanner(System.in);
		g1 = new graph(input.nextLine());
		input = new Scanner(System.in);
		g2 = new graph(input.nextLine());
//		g1 = new graph("test1");
		image = g1.getP();
		image2 = new BufferedImage(g1.getW(), g1.getH(), g1.gett());
		image2.setData(image.getData());
		image3 = g2.getP();
		g1.showImg();
		pixels = g1.getImageGRB();
		pixels2 = g2.getImageGRB();
		
		ww = g1.getW();
		wh = g1.getH();
		
		A.setTitle("Draw");
		
		JLabel lblimage = new JLabel(new ImageIcon(image));
		A.add(lblimage, BorderLayout.CENTER);

		A.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		A.setSize(g1.getW()+50, g1.getH()+50);
		A.setLocation(g1.getW() + 100 , 0);
		A.setLocationRelativeTo(null);
		test B = new test();
		A.add(B);
		A.setVisible(true);
	}
	
	public test(){
		super.addMouseListener(this);
		super.addMouseMotionListener(this);

		Button btn = new Button("Pixels!");
		btn.setActionCommand("b1");
        btn.addActionListener(this);
        this.add(btn, BorderLayout.SOUTH);
	}


	public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd == "b1") {
           
    		//Set width, high
    		int width = g1.getW(), high = g1.getH();
//        	int width = 3, high = 3;
        	//------
        	
        	//Set Original
    		int[][] o = new int[width][high];
//	    		= {{240,	227,	74},
//				{204,	240,	51},
//				{227,	36,	74}};
//	    		= {{240, 240, 227, 227, 224, 224, 240, 240, 227, 227, 224, 224},
//	    			{240, 240, 227, 227, 224, 224, 240, 240, 227, 227, 224, 224},
//    				{204, 204, 51, 51, 240,	240, 204, 204, 51, 51, 240,	240},
//    				{204, 204, 51, 51, 240,	240, 204, 204, 51, 51, 240,	240},
//    				{227, 227, 36, 36, 74, 74, 227, 227, 36, 36, 74, 74},
//    				{227, 227, 226, 226, 224, 224, 227, 227, 226, 226, 224, 224},
//		    		{240, 240, 227, 227, 224, 224, 240, 240, 227, 227, 224, 224},
//					{240, 240, 227, 227, 224, 224, 240, 240, 227, 227, 224, 224},
//					{204, 204, 51, 51, 240,	240, 204, 204, 51, 51, 240,	240},
//					{204, 204, 51, 51, 240,	240, 204, 204, 51, 51, 240,	240},
//					{227, 227, 36, 36, 74, 74, 227, 227, 36, 36, 74, 74},
//					{227, 227, 226, 226, 224, 224, 227, 227, 226, 226, 224, 224}};
    		for (int i = 0; i < width; i++) {
    			for (int j = 0; j < high; j++) {
    				o[i][j] = (pixels[i][j] >> 16) & 0xff;
    			}
    		}
    		//------
    		
//    		System.out.println("Start Cutting");
    		weight w = new weight();
    		
    		w.setW(width);
    		w.setH(high);
    		
    		//Set line point
//    		p = new Point(2,0);	//
//    		pointL.add(p);	//
    		w.setL(pointL);
//    		p = new Point(0,2);	//
//    		pointR.add(p);	//
    		w.setR(pointR);
    		//------
    		
    		w.setOriginal(o);
    		

    		int nodeNum;
    		int flow;
    		
    		List<List<List<Double>>> graph;

    		System.out.println("Start findSigma");
    		w.findSigma();
    		System.out.println("Start initWeight");
    		w.initWeightDouble();
    		
    		double b = 1;
    		double Lamda = 1;
    		w.setB(b);
    		w.setLamda(Lamda);
    		graph = w.findWeightDouble();
    		maxflow maxflowA = new maxflow(width*high, width, high);
    		flow = maxflowA.maxflow(graph);
    		System.out.println("Max flow = " + flow);
    		
    		image2.setData(image.getData());
    		gsinkVertex=maxflowA.resourceVertex();
    		int sWight = g1.getW(), sHight = g1.getH();
            for (int x, y, ii=0; ii < gsinkVertex.size() ; ii++) {
            		x=gsinkVertex.get(ii)%sWight;
            		y=gsinkVertex.get(ii)/sWight;
                	int rgb=image3.getRGB(x,y);
                	int red=(rgb&0x00ff0000)>>16;
                	int green=(rgb&0x0000ff00)>>8;
                	int blue=rgb&0x000000ff;
                	rgb=(0xff000000|(red<<16)|(green<<8)|blue);                               
                	/*pixels[x][y]=rgb;
                	System.out.printf(rgb + " ,");*/
                	image2.setRGB(x,y,rgb);
//                	System.out.printf(gsinkVertex.size() + " ,");
            }
    		JFrame frame = new JFrame();
       		frame.setTitle("after");
    		JLabel lblimage = new JLabel(new ImageIcon(image2));
    		frame.getContentPane().add(lblimage, BorderLayout.CENTER);
    		frame.setSize(width+50, high+50);
    		//frame.setLocationRelativeTo(null);
    		wh+=50+sHight;
    		if(wh+300>screenSize.getHeight())
    		{
    			ww=ww+sWight+80;
    			wh=100+sHight;
    		}
    		frame.setLocation(ww,wh);
    		frame.setVisible(true);
        }
    }

	public int choosePixels(int x, int y)
   	{
   		return pixels[x][y];
   	}



	public void mousePressed(MouseEvent e){
		if (e.getButton() == MouseEvent.BUTTON1)
			step = 1;
		else if(e.getButton() == MouseEvent.BUTTON3)
			step = 2;
		Point p = new Point(e.getX(), e.getY());
		if(e.getButton() == MouseEvent.BUTTON1){
			pointL.add(p);
			pointL.add(p);
		}
		else{
			pointR.add(p);
			pointR.add(p);
		}
		repaint();
	}

	public void mouseDragged(MouseEvent e){
		Graphics g = getGraphics();
		Point p = new Point(e.getX(), e.getY());
		if(step == 1)
			pointL.add(p);
		else
			pointR.add(p);
		repaint();
	}

	public void mouseReleased(MouseEvent e){
		Point p = new Point(-1, -1);

		if(e.getButton() == MouseEvent.BUTTON1)
			pointL.add(p);
		else
			pointR.add(p);
	}

	public void mouseMoved(MouseEvent e){}
 	public void mouseEntered(MouseEvent e){}
 	public void mouseExited(MouseEvent e){}
 	public void mouseClicked(MouseEvent e){}

 	public void paint(Graphics g){
 		int x1 = -1, x2 = -1, y1 = -1, y2 =-1;
 		Graphics2D g2 = (Graphics2D)g;
 		super.paint(g2);
 		g2.drawImage(image, null, null);
 		g2.setStroke( new BasicStroke( 5,  BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER) );
 		for(int i=0; i<pointL.size()-1; i++){
 			g2.setColor(new Color(255, 0, 0, 255));
 			Point p=(Point)pointL.get(i);
 			Point p2=(Point)pointL.get(i+1);
 	        if(p.equals(A_BREAK)  || p2.equals(A_BREAK)) {
 	            continue;
 	        } else {
 	           g.drawLine(p.x, p.y, p2.x, p2.y);
 	        }
 		}

 		for(int i=0; i<pointR.size()-1; i++){
 			g2.setColor(new Color(0, 0, 255, 255));
 			Point p=(Point)pointR.get(i);
 			Point p2=(Point)pointR.get(i+1);
 	        if(p.equals(A_BREAK)  || p2.equals(A_BREAK)) {
 	            continue;
 	        } else {
 	           g.drawLine(p.x, p.y, p2.x, p2.y);
 	        }
 		}
 	}

// 	private void printPixelARGB(int x, int y, int pixel)
//    {
//	    int alpha = (pixel >> 24) & 0xff;
//	    int red = (pixel >> 16) & 0xff;
//	    int green = (pixel >> 8) & 0xff;
//	    int blue = (pixel) & 0xff;
//	    System.out.println("argb: (" + x +" " + y + "): " + alpha + ", " + red + ", " + green + ", " + blue + pixel);
//    }

}
