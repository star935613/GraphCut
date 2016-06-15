import java.awt.Point;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
//import java.util.Scanner;

class weight {//extends temp{

	int test = 0;
	private double sigma;	//σ
	private double lamda;	//λ
	private double B;		//B的倍數
	private int w, h;
	int[][] original = new int[w][h];
	private ArrayList<Point> pointL;
	private ArrayList<Point> pointR;
	private	final Point A_BREAK = new Point(-1,-1);

	private List<List<List<Double>>> weightsDouble = new ArrayList<List<List<Double>>>();		//原本表格的型態，怕小數點誤差過大所以先保留
//	private List<List<List<Integer>>> weightsInteger = new ArrayList<List<List<Integer>>>();		//原本表格的型態
	void setW(int width) {
		w = width;
		System.out.println(w);
	}

	void setH(int high) {
		h = high;
		System.out.println(h);
	}

	void setL(ArrayList<Point> point) {
		pointL = point;
//		System.out.println("L " + pointL.get(x) + ", " + pointL.y);
	}

	void setR(ArrayList<Point> point) {
		pointR = point;
	}
	
	void setLamda(double Lamda) {
		lamda = Lamda;
	}
	
	void setB(double b) {
		B = b;
	}

	void setOriginal(int[][] o) {
		original = o;
		System.out.println(original);
	}

	void initWeightDouble() {
//		System.out.println("Init weight Start" + w + ", " + h);
		
		for (int i = 0; i <= w; i++) {
			List<List<Double>> t = new ArrayList<List<Double>>();
			weightsDouble.add(t);
		}
		
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				List<Double> tem = new ArrayList<Double>();
				List<Double> temp = new ArrayList<Double>();
				weightsDouble.get(i).add(tem);
				for (int k = 0; k < 6; k++) {
					temp.add((double) 0);
				}
				weightsDouble.get(i).get(j).addAll(temp);
			}
		}
//		System.out.println("Init weight Finish");
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
//				System.out.println(sum);
				sum = sum + original[i][j];
				squareSum = squareSum + Math.pow(original[i][j], 2);
			}
		}
		squareSum = squareSum / (w * h - 1);
		sigma = squareSum - Math.pow(sum / (w * h - 1), 2);
		System.out.println("Sigma: " + sigma + "    sum: " + sum + "    squareSum: " + squareSum);
	}
		
	int check(int x, int y) {
		for (int i = 0; i < pointL.size(); i++) {
			
			if (pointL.get(i).getX() != -1 && pointL.get(i).getY() != -1) {
				if (pointL.get(i).getX() == y && pointL.get(i).getY() == x) {
//					System.out.println("L	" + pointL.get(i).getX() + " " + pointL.get(i).getY());
					return 2;
				}
			}
		}
		
		for (int i = 0; i < pointR.size(); i++) {
			
			if (pointR.get(i).getX() != -1 && pointR.get(i).getY() != -1) {
				if (pointR.get(i).getX() == y && pointR.get(i).getY() == x) {
//					System.out.println("R	" + pointR.get(i).getX() + " " + pointR.get(i).getY());
					return 1;
				}
			}
		}
		return 0;
	}

	List<List<List<Double>>> findWeightDouble() {
		double checkL = 0, checkR = 0;
		double sumUp = 0, sumDown = 0, sumL = 0, sumR = 0;
		int check, den;

		

		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				check = check(i, j);
				if (i - 1 >= 0) {	//上
					weightsDouble.get(i).get(j).set(1, Math.exp( (-1) * Math.pow(choosePixels(i, j) - choosePixels(i - 1, j), 2) / (2 * sigma) ) * B);
				}
				if (i + 1 < h) {	//下
					weightsDouble.get(i).get(j).set(2, Math.exp( (-1) * Math.pow(choosePixels(i, j) - choosePixels(i + 1, j), 2) / (2 * sigma) ) * B);
				}
				if (j - 1 >= 0) {	//左
					weightsDouble.get(i).get(j).set(3, Math.exp( (-1) * Math.pow(choosePixels(i, j) - choosePixels(i, j - 1), 2) / (2 * sigma) ) * B);
				}
				if (j + 1 < w) {	//右
					weightsDouble.get(i).get(j).set(4, Math.exp( (-1) * Math.pow(choosePixels(i, j) - choosePixels(i, j + 1), 2) / (2 * sigma) ) * B);
				}
			}
		}
		
		//can more sample by pointL & pointR
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				check = check(i, j);
				if (check == 2) {	//R
					if (i - 1 >= 0) {	//上
						sumUp = weightsDouble.get(i-1).get(j).get(4) + weightsDouble.get(i-1).get(j).get(1) + weightsDouble.get(i-1).get(j).get(2) + weightsDouble.get(i-1).get(j).get(3);
					}	
					if (j - 1 >= 0) {	//左
						sumL = weightsDouble.get(i).get(j-1).get(4) + weightsDouble.get(i).get(j-1).get(1) + weightsDouble.get(i).get(j-1).get(2) + weightsDouble.get(i).get(j-1).get(3);
					}
					if (i + 1 < h) {	//下
						sumDown = weightsDouble.get(i+1).get(j).get(4) + weightsDouble.get(i+1).get(j).get(1) + weightsDouble.get(i+1).get(j).get(2) + weightsDouble.get(i+1).get(j).get(3);
					}
					if (j + 1 < w) {	//右
						 sumR = weightsDouble.get(i).get(j+1).get(4) + weightsDouble.get(i).get(j+1).get(1) + weightsDouble.get(i).get(j+1).get(2) + weightsDouble.get(i).get(j+1).get(3);
					}
					weightsDouble.get(i).get(j).set(0, 1 + Math.max( Math.max(sumUp, sumL), Math.max(sumDown, sumR) ));
					weightsDouble.get(i).get(j).set(5 ,(double) 0);

					checkR = checkR + weightsDouble.get(i).get(j).get(0);
					for (int x = 0; x < w; x++) {
						for (int y = 0; y < h; y++) {
							if (check(x, y) == 0) {
//								if (choosePixels(x, y) != choosePixels(i, j)) {
									weightsDouble.get(x).get(y).set(0, Math.max(weightsDouble.get(x).get(y).get(0), (255 - Math.abs(choosePixels(x, y )- choosePixels(i, j))) / Math.sqrt((Math.pow(x-i,2)+Math.pow(y-j,2))) / 255  * lamda));
//									System.out.println("0	(" + i + ", " + j + ")  (" + x + ", " + y + ")" +((255 - Math.abs(choosePixels(x, y)- choosePixels(i, j))) / Math.sqrt((Math.pow(x-i,2)+Math.pow(y-j,2)))) / 255  * lamda);
//								}
//								else {
//									weightsDouble.get(x).get(y).set(0, Math.max(weightsDouble.get(x).get(y).get(0), 255 / Math.sqrt((Math.pow(x-i,2)+Math.pow(y-j,2))) * lamda));
//								}
							}
						}
					}
				}
			
				else if (check == 1) {	//L
					if (i - 1 >= 0) {	//上
						sumUp = weightsDouble.get(i-1).get(j).get(4) + weightsDouble.get(i-1).get(j).get(1) + weightsDouble.get(i-1).get(j).get(2) + weightsDouble.get(i-1).get(j).get(3);
					}
					if (j - 1 >= 0) {	//左
						sumL = weightsDouble.get(i).get(j-1).get(4) + weightsDouble.get(i).get(j-1).get(1) + weightsDouble.get(i).get(j-1).get(2) + weightsDouble.get(i).get(j-1).get(3);
					}
					if (i + 1 < h) {	//下
						sumDown = weightsDouble.get(i+1).get(j).get(4) + weightsDouble.get(i+1).get(j).get(1) + weightsDouble.get(i+1).get(j).get(2) + weightsDouble.get(i+1).get(j).get(3);
					}
					if (j + 1 < w) {	//右
						 sumR = weightsDouble.get(i).get(j+1).get(4) + weightsDouble.get(i).get(j+1).get(1) + weightsDouble.get(i).get(j+1).get(2) + weightsDouble.get(i).get(j+1).get(3);
					}
					weightsDouble.get(i).get(j).set(0, (double) 0);
					weightsDouble.get(i).get(j).set(5, 1 + Math.max( Math.max(sumUp, sumL), Math.max(sumDown, sumR) ));
					checkL = checkL + weightsDouble.get(i).get(j).get(5);
					for (int x = 0; x < w; x++) {
						for (int y = 0; y < h; y++) {
							if (check(x, y) == 0) {
//								if (choosePixels(x, y) != choosePixels(i, j)) {
//									weightsDouble.get(x).get(y).set(0, Math.max(weightsDouble.get(x).get(y).get(0), 255 / Math.abs(choosePixels(x, y)- choosePixels(i, j)) / Math.sqrt((Math.pow(x-i,2)+Math.pow(y-j,2))) * lamda));
									weightsDouble.get(x).get(y).set(5, Math.max(weightsDouble.get(x).get(y).get(5), (255 - Math.abs(choosePixels(x, y)- choosePixels(i, j))) / Math.sqrt((Math.pow(x-i,2)+Math.pow(y-j,2))) / 255 * lamda));
//									System.out.println("5	(" + i + ", " + j + ")  (" + x + ", " + y + ")" + (255 - Math.abs(choosePixels(x, y)- choosePixels(i, j))) / Math.sqrt((Math.pow(x-i,2)+Math.pow(y-j,2))) * lamda);
//								}
//								else {
//									weightsDouble.get(x).get(y).set(5, Math.max(weightsDouble.get(x).get(y).get(5), 255 / Math.sqrt((Math.pow(x-i,2)+Math.pow(y-j,2))) * lamda));
//								}
							}
						}
					}
				}
			}	
		}
		
//		for (int i = 0; i < w; i++) {
//			for (int j = 0; j < h; j++) {
////				double result = (-Math.log(1-L))/L;
//				weightsDouble.get(i).get(j).set(0, (-1 * Math.log(1 - weightsDouble.get(i).get(j).get(0))) / weightsDouble.get(i).get(j).get(0));
//				weightsDouble.get(i).get(j).set(0, (-1 * Math.log(1 - weightsDouble.get(i).get(j).get(5))) / weightsDouble.get(i).get(j).get(5));
//			}
//		}
		
		System.out.println("find Weights Finish" + ", B=" + B + ", Lamda=" + lamda + ", " + checkL + ", " + checkR);
//		if (checkL  > checkR ) {
////			lamda = lamda * 0.1;
////			B = B * 10;
////			System.out.println("find Weights Finish" + ", B=" + B + ", Lamda=" + lamda + ", " + checkL + ", " + checkR);
//			test = 1;
////			checkL = 0;
////			checkR = 0;
////			findWeightDouble();
//		}
//		else {
//			test = 1;
//		}

		return weightsDouble;
	}

	void printWeightDouble() {
		
		DecimalFormat df = new DecimalFormat("0.00");
		
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				System.out.print("	(" + i + " "+ j + ") " + i*3+j +"	S" + " " + df.format(weightsDouble.get(i).get(j).get(0)));
				System.out.print("	上" + " " + df.format(weightsDouble.get(i).get(j).get(1)));
				System.out.print("	下" + " " + df.format(weightsDouble.get(i).get(j).get(2)));
				System.out.print("	左" + " " + df.format(weightsDouble.get(i).get(j).get(3)));
				System.out.print("	右" + " " + df.format(weightsDouble.get(i).get(j).get(4)));
				System.out.print("	T" + " " + df.format(weightsDouble.get(i).get(j).get(5)));
				System.out.println();
			}
		}
		
	System.out.println();
}
	
void printPointR() {
		
		DecimalFormat df = new DecimalFormat("0");
		
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				if (check(i, j) == 2) {
					System.out.print("R ");
				}
				else {
					System.out.print("0 ");
				}
			}
			System.out.println();
		}
		
	System.out.println();
}

void printPointL() {
	
	DecimalFormat df = new DecimalFormat("0");
	
	for (int i = 0; i < h; i++) {
		for (int j = 0; j < w; j++) {
			if (check(i, j) == 1) {
				System.out.print("S ");
			}
			else {
				System.out.print("0 ");
			}
		}
		System.out.println();
	}
	
System.out.println();
}
	
void printS() {
		
		DecimalFormat df = new DecimalFormat("0");
		
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				System.out.print(" " + df.format(weightsDouble.get(i).get(j).get(0)));
			}
			System.out.println();
		}
		
	System.out.println();
}

void printT() {
	
	DecimalFormat df = new DecimalFormat("0");
	
	for (int i = 0; i < w; i++) {
		for (int j = 0; j < h; j++) {
			System.out.print( " " + df.format(weightsDouble.get(i).get(j).get(5)));
		}
		System.out.println();
	}
	
System.out.println();
}

	int choosePixels(int x, int y) {
		return original[x][y];
	}
}