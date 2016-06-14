import java.awt.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.*;
import java.awt.image.DataBufferByte;

class graph
{
	ByteArrayOutputStream picture = new ByteArrayOutputStream();
	BufferedImage image = null;
	private int height, width;
	
	public graph(String name)
	{		
		name += ".jpg";
		OpenFile(name);
		
	}

	private void OpenFile(String fileName)
	{
		File img = new File(fileName);
		try
		{
			image = ImageIO.read(img);				

			height = image .getHeight();
			width = image .getWidth();
			ImageIO.write(image, "jpg", picture);
			picture.flush();
			byte[] imgByte = picture.toByteArray();	
		//	getImageGRB();
			picture.close();
		}catch(IOException e){
			System.out.println("error IO");

		}
	}

	public int getW()
	{
		return width;
	}

	public int getH()
	{
		return height;
	}
	
	public BufferedImage getP()
	{
		return image;
	}	
	
	public void wrightGraph(String name)
	{		
		name += ".jpg";

		try
		{
		// Get the byte array of the image.
		byte[] resizedBytes = picture.toByteArray();		
		// Construct the file.		
		File resizedImgFile = new File(name);
		// Write the image bytes to file.
		FileOutputStream fos = new FileOutputStream(resizedImgFile);
		fos.write(resizedBytes);
		fos.close();

		}catch(IOException e){
			System.out.println("error for IO!");
		}		
	}




   	void showImg()
   	{
   		JFrame frame = new JFrame();
   		frame.setTitle("Original");
		JLabel lblimage = new JLabel(new ImageIcon(image));
		frame.getContentPane().add(lblimage, BorderLayout.CENTER);
		frame.setSize(width+50, height+50);
		frame.setVisible(true);
   	}
  
  	int[][] getImageGRB()
  	{
  		int[][] pixels = new int[width][height];
  		for(int i = 0; i < width ; i++){
  			for(int j = 0; j < height; j++){
  				pixels[i][j] = image.getRGB(i,j);
//  				image.setRGB(startX, startY, w, h, rgbArray, offset, scansize);
//  				
  //			printPixelARGB(i, j, pixels[i][j]); 

  			} 				
  		}
  		return pixels;
   	}
  	
  	void setPixRGB(int x, int y, int rgb) {
  		image.setRGB(x, y, rgb);
  	}
   /*
   void printPixelARGB(int x, int y, int pixel) 
   {
	    int alpha = (pixel >> 24) & 0xff;
	    int red = (pixel >> 16) & 0xff;
	    int green = (pixel >> 8) & 0xff;
	    int blue = (pixel) & 0xff;
	    System.out.println("argb: (" + x +" " + y + "): " + alpha + ", " + red + ", " + green + ", " + blue);
  }
  */
  /*
  int[] reversal(Byte[] b)
  {
  		int[] numInt = new int[b.length];
  		for(int i = 0; i<b.length; i++ ){
  			numInt[i] = b[i]&0x000000ff;
  			System.out.println(numInt[i]);
  		}
  		return numInt;

  } 

*/
}