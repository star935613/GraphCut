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
import javax.swing.*;
import javax.swing.JComponent;

public class test extends JPanel implements MouseMotionListener, MouseListener, ActionListener
{

	private int step = 1,click = 1;
	public ArrayList<Point> pointL;
	public ArrayList<Point> pointR;
	static BufferedImage image = null;
	static int[][] pixels = null;
 	private	final Point A_BREAK = new Point(-1,-1);
 	
 	static JFrame A = new JFrame();
	static Scanner input;
	static graph g1;
	public static void main(String args[]){
		
		System.out.println("Pls input the file name:");
		input = new Scanner(System.in);
		g1 = new graph(input.nextLine());
		image = g1.getP();
		g1.showImg();
		pixels = g1.getImageGRB();
		
		A.setTitle("Draw");

		JLabel lblimage = new JLabel(new ImageIcon(image));
		A.add(lblimage, BorderLayout.CENTER);

		A.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		A.setSize(g1.getW(), g1.getH());
		A.setLocation(g1.getW() + 100 , 0);
		test B = new test();
		A.add(B);
		A.setVisible(true);
	}

	public test(){
		pointL = new ArrayList();
		pointR = new ArrayList();
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
//            System.out.println("R: ");
//           
        	int nodeNum;
    		int source;
    		int sink;
    		int flow;
    		int[][] graph;
    		
    		int width = g1.getW(), high = g1.getH();
    		int[][] o = new int[width][high];
//	    		= {{240,	227,	74},
//    				{204,	240,	51},
//    				{227,	36,	74}};
    		
    		for (int i = 0; i < width; i++) {
    			for (int j = 0; j < high; j++) {
    				o[i][j] = (pixels[i][j] >> 16) & 0xff;
    			}
    		}
    		
    		System.out.println("Start Cutting");
    		weight w = new weight();
    		w.setW(width);
    		w.setH(high);
    		 for(int i=0; i<pointR.size()-1; i++){
             	Point p=(Point)pointR.get(i);
             	if(!p.equals(A_BREAK))
             		w.setL(p);
             }
             System.out.println("L: ");
             for(int i=0; i<pointL.size()-1; i++){
             	Point p=(Point)pointL.get(i);
             	if(!p.equals(A_BREAK))
             		w.setR(p);
             }
    		w.setOriginal(o);
    		
    		System.out.println("Start findSigma");
    		w.findSigma();
    		System.out.println("Start initWeight");
    		w.initWeight();
    		System.out.println("Start findWeights");
    		w.findWeights();
    		
    		System.out.println("Start initTable");
    		w.initTable();
    		System.out.println("Start findTeble");
    		w.findTeble();
    		
    		nodeNum = width * high + 2;
    		graph = new int[nodeNum][nodeNum];
    		graph = w.findTeble();
    		source = 0;
    		sink = width * high + 1;
    		maxflow maxflowA = new maxflow(nodeNum);
    		flow = maxflowA.maxflow(graph,source,sink);
    		System.out.println("Max flow = " + flow);
        }
    }

	public int choosePixels(int x, int y)
   	{
//   		printPixelARGB(x, y, pixels[x][y]);
//   		System.out.println(pixels[x][y]);
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
 		g2.setStroke( new BasicStroke( 15,  BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER) );
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
