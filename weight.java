import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class weight {//extends temp{

	private double sigma;	//σ
	private double lamda = 0.01;	//λ
	private double B = 100;		//B的倍數
	private int w, h;
	int[][] original = new int[w][h];
	private ArrayList<Point> pointL = new ArrayList();
	private ArrayList<Point> pointR = new ArrayList();

	private List<List<List<Double>>> weights = new ArrayList<List<List<Double>>>();		//原本表格的型態
//	private int[][] table = new int[w * h + 2][w * h + 2];	
	private List<List<Integer>> table = new ArrayList<List<Integer>>();	//為了輸入準備的

//	void setL(int x, int y) {
//		Point p = new Point(x, y);
	void setW(int width) {
		w = width;
		System.out.println(w);
	}

	void setH(int high) {
		h = high;
		System.out.println(h);
	}

	void setL(Point p) {
		pointL.add(p);
		System.out.println("L " + p.x + ", " + p.y);
	}

	void setR(Point p) {
//		Point p = new Point(x, y);
//		System.out.println("R " + p.x + ", " + p.y);
		pointR.add(p);
	}

	void setOriginal(int[][] o) {
		original = o;
		System.out.println(original);
	}

	void initWeight() {
		System.out.println("Init weight Start" + w + ", " + h);
		
		for (int i = 0; i < w; i++) {
			List<List<Double>> t = new ArrayList<List<Double>>();
			weights.add(t);
//			System.out.println("Test 1 ");
		}
//		System.out.println("Test 1 Finish);
		
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				List<Double> tem = new ArrayList<Double>();
				List<Double> temp = new ArrayList<Double>();
				weights.get(i).add(tem);
				for (int k = 0; k < 6; k++) {
					temp.add((double) 0);
				}
				weights.get(i).get(j).addAll(temp);
//				System.out.println("Test 2 " + i );
			}
		}
//		System.out.println("Test 2 Finish");
		
		
//		System.out.println(weights);
		System.out.println("Init weight Finish");
	}

	void initTable() {
		
		for (int i = 0; i < w*h*2; i++) {
			table.add( new ArrayList<Integer>() );
			List<Integer> t = new ArrayList<Integer>();
			for (int j = 0; j < w*h*2; j++) {
				t.add(0);
			}
			table.get(i).addAll(t);
		}
		
//		for (int i = 0; i < w*h+2; i++) {
//			for (int j = 0; j < w*h+2; j++) {
//				table[i][j] = 0;
//			}
//		}
		System.out.println("Init Table Finish");
	}

	void findSigma() {
		double sum = 0;
		double squareSum = 0;
//		 int alpha, red, green, blue;

		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				// 之後要另外加function
//				 alpha = (choosePixels(i, j) >> 24) & 0xff;
//				 red = (choosePixels(i, j) >> 16) & 0xff;
//				 green = (choosePixels(i, j) >> 8) & 0xff;
//				 blue = (choosePixels(i, j)) & 0xff;

				sum = sum + original[i][j];
				squareSum = squareSum + Math.pow(original[i][j], 2);
			}
		}
		squareSum = squareSum / (w * h);
		sum = sum / (w * h);
		sigma = squareSum - Math.pow(sum, 2);
		System.out.println("Sigma: " + sigma + "    sum: " + sum + "    squareSum: " + squareSum);
	}

	void findWeights() {
		final Point A_BREAK = new Point(-1,-1);	//重複定義，在 test 定義過
		double avgL = 0, avgR = 0;
		double checkL = 0, checkR = 0;
		double sumUp = 0, sumDown = 0, sumL = 0, sumR = 0;

		//actionPerformed重複，暫時假設只有各點一點
//		 int alpha, red, green, blue;
//		 for(int i=0; i<pointR.size()-1; i++){
			 Point pR=(Point)pointR.get(0);
//         	if(!pR.equals(A_BREAK)) {
//	   			 alpha = (choosePixels(pR.x, pR.y) >> 24) & 0xff;
//	   			 red = (choosePixels(pR.x, pR.y) >> 16) & 0xff;
//	   			 green = (choosePixels(pR.x, pR.y) >> 8) & 0xff;
//	   			 blue = (choosePixels(pR.x, pR.y)) & 0xff;

	   			 avgR = avgR+ choosePixels(pR.x, pR.y);
//   			 }
//         }
		 avgR = avgR / 1;//pointR.size();

//         for(int i=0; i<pointL.size()-1; i++){
        	 Point pL=(Point)pointL.get(0);
//          	if(!pL.equals(A_BREAK)) {
// 	   			 alpha = (choosePixels(pL.x, pL.y) >> 24) & 0xff;
// 	   			 red = (choosePixels(pL.x, pL.y) >> 16) & 0xff;
// 	   			 green = (choosePixels(pL.x, pL.y) >> 8) & 0xff;
// 	   			 blue = (choosePixels(pL.x, pL.y)) & 0xff;

 	   			 avgL = avgL + choosePixels(pL.x, pL.y);
// 	   		}
//         }
 		 avgL = avgL / 1;//pointL.size();

		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				if (i - 1 >= 0) {
					weights.get(i).get(j).set(0, Math.exp( (-1) * Math.pow(choosePixels(i, j) - choosePixels(i - 1, j), 2) / (2 * sigma) ) * B);
				}
				if (j - 1 >= 0) {
					weights.get(i).get(j).set(1, Math.exp( (-1) * Math.pow(choosePixels(i, j) - choosePixels(i, j - 1), 2) / (2 * sigma) ) * B);
				}
				if (i + 1 < h) {
					weights.get(i).get(j).set(2, Math.exp( (-1) * Math.pow(choosePixels(i, j) - choosePixels(i + 1, j), 2) / (2 * sigma) ) * B);
				}
				if (j + 1 < w) {
					weights.get(i).get(j).set(3, Math.exp( (-1) * Math.pow(choosePixels(i, j) - choosePixels(i, j + 1), 2) / (2 * sigma) ) * B);
				}
				if (i == pL.x && j == pL.y) {
//					System.out.println("L (" + i + ", " + j + ")");
					if (i - 1 >= 0) {
						sumUp = weights.get(i-1).get(j).get(0) + weights.get(i-1).get(j).get(1) + weights.get(i-1).get(j).get(2) + weights.get(i-1).get(j).get(3);
					}
					if (j - 1 >= 0) {
						sumL = weights.get(i).get(j-1).get(0) + weights.get(i).get(j-1).get(1) + weights.get(i).get(j-1).get(2) + weights.get(i).get(j-1).get(3);
					}
					if (i + 1 < h) {
						sumDown = weights.get(i+1).get(j).get(0) + weights.get(i+1).get(j).get(1) + weights.get(i+1).get(j).get(2) + weights.get(i+1).get(j).get(3);
					}
					if (j + 1 < w) {
						 sumR = weights.get(i).get(j+1).get(0) + weights.get(i).get(j+1).get(1) + weights.get(i).get(j+1).get(2) + weights.get(i).get(j+1).get(3);
					}

					weights.get(i).get(j).set(4, 1 + Math.max( Math.max(sumUp, sumL), Math.max(sumDown, sumR) ));
					weights.get(i).get(j).set(5 ,(double) 0);
				}
				else if (i == pR.x && j == pR.y) {
//					System.out.println("R (" + i + ", " + j + ")");
					if (i - 1 >= 0) {
						sumUp = weights.get(i-1).get(j).get(0) + weights.get(i-1).get(j).get(1) + weights.get(i-1).get(j).get(2) + weights.get(i-1).get(j).get(3);
					}
					if (j - 1 >= 0) {
						sumL = weights.get(i).get(j-1).get(0) + weights.get(i).get(j-1).get(1) + weights.get(i).get(j-1).get(2) + weights.get(i).get(j-1).get(3);
					}
					if (i + 1 < h) {
						sumDown = weights.get(i+1).get(j).get(0) + weights.get(i+1).get(j).get(1) + weights.get(i+1).get(j).get(2) + weights.get(i+1).get(j).get(3);
					}
					if (j + 1 < w) {
						 sumR = weights.get(i).get(j+1).get(0) + weights.get(i).get(j+1).get(1) + weights.get(i).get(j+1).get(2) + weights.get(i).get(j+1).get(3);
					}
					weights.get(i).get(j).set(4, (double) 0);
					weights.get(i).get(j).set(5, 1 + Math.max( Math.max(sumUp, sumL), Math.max(sumDown, sumR) ));
				}
				else {
//					System.out.println("(" + i + ", " + j + ")");
					weights.get(i).get(j).set(4, Math.abs(choosePixels(i, j)- choosePixels(pL.x, pL.y)) * Math.sqrt((Math.pow(i-pL.x,2)+Math.pow(j-pL.y,2)))/(Math.sqrt((Math.pow(i-pL.x,2)+Math.pow(j-pL.y,2)))+Math.sqrt((Math.pow(i-pR.x,2)+Math.pow(j-pR.y,2)))) * lamda);
					weights.get(i).get(j).set(5, Math.abs(choosePixels(i, j)- choosePixels(pR.x, pR.y)) * Math.sqrt((Math.pow(i-pR.x,2)+Math.pow(j-pR.y,2)))/(Math.sqrt((Math.pow(i-pL.x,2)+Math.pow(j-pL.y,2)))+Math.sqrt((Math.pow(i-pR.x,2)+Math.pow(j-pR.y,2)))) * lamda);
				}
				checkL = checkL + weights.get(i).get(j).get(4);
				checkR = checkR + weights.get(i).get(j).get(5);
//				System.out.println(weights[i][j][0] + "\n" + weights[i][j][1] + "\n" + weights[i][j][2] + "\n" + weights[i][j][3] + "\n" + weights[i][j][4] + "\n" + weights[i][j][5]);
//				System.out.println();
			}
		}

		if (checkL < checkR) {
			lamda = lamda * 0.1;
			B = B * 10;
			System.out.println("find Weights Finish" + ", " + B + ", " + lamda + ", " + checkL + ", " + checkR);
			checkL = 0;
			checkR = 0;
			findWeights();
		}


	}

	List<List<Integer>> findTeble() {

		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				//nlink
				if (weights.get(i).get(j).get(0) != 0) {
					table.get(i * 3 + j + 1).set((i - 1) * 3 + j + 1, (int) Math.floor(weights.get(i).get(j).get(0)));
				}
				if (weights.get(i).get(j).get(1) != 0) {
					table.get(i * 3 + j + 1).set(i * 3 + (j - 1) + 1, (int) Math.floor(weights.get(i).get(j).get(1)));
				}
				if (weights.get(i).get(j).get(2) != 0) {
					table.get(i * 3 + j + 1).set((i + 1) * 3 + j + 1, (int) Math.floor(weights.get(i).get(j).get(2)));
				}
				if (weights.get(i).get(j).get(3) != 0) {
					table.get(i * 3 + j + 1).set(i * 3 + (j + 1) + 1, (int) Math.floor(weights.get(i).get(j).get(3)));
				}
//				System.out.println("find Tablie nlink Finish");

				//S
				table.get(i*3 + j + 1).set(0, (int) Math.floor(weights.get(i).get(j).get(4)));
				table.get(0).set(i*3 + j + 1, (int) Math.floor(weights.get(i).get(j).get(4)));
//				System.out.println("find Tablie S Finish");

				//T
				table.get(i*3 + j + 1).set(w*h + 1, (int) Math.floor(weights.get(i).get(j).get(5)));
				table.get(w*h + 1).set(i*3 + j + 1, (int) Math.floor(weights.get(i).get(j).get(5)));
//				System.out.println("find Tablie T Finish");
			}
		}
//		System.out.println();
	System.out.println("find Tablie Finish");

		System.out.printf("	S");
		for (int i = 1; i <= w*h+2; i++) {
			System.out.printf("	" + i);
		}
		System.out.println("END");

		for (int i = 0; i < h * w + 2; i++) {
			if (i == h*w+1) {
				System.out.printf("  " + "T");
			}
			else {
				System.out.printf("  " + i);
			}
			for (int j = 0; j < h * w + 2; j++) {
					System.out.printf("	" + table.get(i).get(j));
			}
			System.out.println();
		}
		System.out.println();
		System.out.println();
		return table;
	}

	int choosePixels(int x, int y) {
		return original[x][y];
	}
}
