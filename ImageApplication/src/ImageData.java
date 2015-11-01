import java.awt.*;
import java.io.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.*;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import sun.awt.image.ToolkitImage;
import sun.util.logging.resources.logging;


/**

Class ImageData is a base class which
respresents image data and the methods for
producing the corresponding wavelet image,
as well as methods to access both of these
datas. </p>

@author L. Grewe
@version 0.0a Feb.

*/

//Note: extends Component to inherit its createImage() method
class ImageData extends Component
{    boolean verbose = false;

     //File where data stored and format
     String filename = "";
     String format   = "";

 
     // Num Rows, columns
     public int rows=0, cols=0;

     //image data
     public int data[];
     public float minDataRange = Float.MAX_VALUE;
     public float maxDataRange = Float.MIN_VALUE;
     
     // dataimg is the 2X2 Matrix of the Data array 
     // used in ImageData. The Matrix is required for 
     // Sobel Edge Detection
     public int dataimg[][];




    //**METHODS: for image data*/
     int getData(int row, int col)
      { if (row < rows && col <cols )
            return data[(row*cols)+col];
        else
            return 0;
      }


      int getDataForDisplay(int row, int col)
      {   if (row < rows && col <cols )
            return data[(row*cols)+col];
        else
            return 0;
      }


      void setData(int row, int col, int value)
      {  data[(row*cols)+col] = (int) value;

      }



     

  /**
   * Constructs a ImageData object using the
   * specified by an instance of java.awt.Image,
   * format, and size indicated by numberRows and
   * numberColumns.
   * @param img an Image object containing the data.
   * @param DataFormat the format of the data
   * @param numberRows the number of rows of data
   * @param numberColumns the number of columns of data
   * @exception IOException if there is an error during
   *  reading of the rangeDataFile.
   */
   public ImageData(Image img, String DataFormat,
                    int numberRows, int numberColumns) throws IOException
     {
      int pixel, red, green, blue, r,c;
      format = DataFormat;
      rows = numberRows;
      cols = numberColumns;
      PixelGrabber pg;

      //From the image passed retrieve the pixels by
      //creating a pixelgrabber and dump pixels
      //into the data[] array.
      data = new int[rows*cols];
      pg = new PixelGrabber(img, 0, 0, cols, rows, data, 0, cols);
      try {
          pg.grabPixels();
      } catch (InterruptedException e) {
          System.err.println("interrupted waiting for pixels!");
          return;
      }


      //Convert the PixelGrabber pixels to greyscale
      // from the {Alpha, Red, Green, Blue} format 
      // PixelGrabber uses.
      for(r=0; r<rows; r++)
      for(c=0; c<cols; c++)
        {   pixel = data[r*cols + c];
	        red   = (pixel >> 16) & 0xff;
            green = (pixel >>  8) & 0xff;
            blue  = (pixel      ) & 0xff;
            if(verbose)
                System.out.println("RGB: " + red + "," + green +"," +blue);
            data[r*cols+c] = (int)((red+green+blue)/3);
            
            if(verbose)
                System.out.println("Pixel: " + (int)((red+green+blue)/3));
            minDataRange = Math.min(minDataRange, data[r*cols+c]);
            maxDataRange = Math.max(maxDataRange, data[r*cols+c]);
        }                
	        
            
            
     
		//{{INIT_CONTROLS
		setBackground(java.awt.Color.white);
		setSize(0,0);
		//}}
	}

   // The ImageData Constructor is used for Sobel Edge Detection
   // The Char ch parameter is for Identification of the constructor
   // and is used for the ease of calling it exclusively.
   public ImageData(Image img, String DataFormat,
           int numberRows, int numberColumns,char ch) throws IOException
{
	   int pixel, red, green, blue, r,c;
	   format = DataFormat;
	   rows = numberRows;
	   cols = numberColumns;
	   PixelGrabber pg;

	   //From the image passed retrieve the pixels by
	   //creating a pixelgrabber and dump pixels
	   //into the data[] array.
	   data = new int[rows*cols];
	   dataimg=new int[rows][cols];
	 
	   pg = new PixelGrabber(img, 0, 0, cols, rows, data, 0, cols);
	   try {
		   pg.grabPixels();
	   } catch (InterruptedException e) {
		   System.err.println("interrupted waiting for pixels!");
		   return;
	   }


	   //Convert the PixelGrabber pixels to greyscale
	   // from the {Alpha, Red, Green, Blue} format 
	   // PixelGrabber uses.
	   for(r=0; r<rows; r++)
		   for(c=0; c<cols; c++)
		   {   pixel = data[r*cols + c];
		   Color cc=new Color(pixel);
		   red=cc.getRed();
		   green=cc.getGreen();
		   blue=cc.getBlue();
		   data[r*cols+c] = (int)((red+green+blue)/3);
		   
		    if(verbose)
		    	System.out.println("Pixel: " + (int)((red+green+blue)/3));
		    minDataRange = Math.min(minDataRange, data[r*cols+c]);
		    maxDataRange = Math.max(maxDataRange, data[r*cols+c]);
}                
   
   
	  // This is to convert a 1D Array of data[] to a 2D array
	   // of dataimg[rows][cols] for Sobel Edge detection
	   
	   if(data.length!=(rows*cols))
	   {
		   throw new IllegalArgumentException("Invalid array Length!");
	   }
	   dataimg =new int[rows][cols];
	   for(int i=0;i<rows;i++)
	   {
		   System.arraycopy(data, (i*cols), dataimg[i], 0, cols);
	   }

//{{INIT_CONTROLS
setBackground(java.awt.Color.white);
setSize(0,0);
//}}
}   
   
 
   
//	The ImageData constructor is similar to the given constructor used in this project with a flag
 // 	that is by default 0 indicating that it is a user defined function and
//		custom made. The flg makes it easy to identify and call without any confusion
   public ImageData(Image img, String DataFormat,
           int numberRows, int numberColumns,int flg) throws IOException
{	
	   int pixel, red, green, blue, r,c,alpha;
	   format = DataFormat;
	   rows = numberRows;
	   cols = numberColumns;
	   PixelGrabber pg;

	   //From the image passed retrieve the pixels by
	   //creating a pixelgrabber and dump pixels
	   //into the data[] array.
	   data = new int[rows*cols];
	   pg = new PixelGrabber(img, 0, 0, cols, rows, data, 0, cols);
	   try {
		   	//pg.grabPixels();
		   if(pg.grabPixels())
		   {
			   int width=pg.getWidth();
			   int height=pg.getHeight();
			   
			   data=(int[])pg.getPixels();
		   }
	   } catch (InterruptedException e) {
		   System.err.println("interrupted waiting for pixels!");
		   return;
	   }
   
   

//{{INIT_CONTROLS
setBackground(java.awt.Color.white);
setSize(0,0);
//}}
}

  /**
   * Constructs a ImageData object using the
   * specified  size indicated by
   * numberRows and numberColumns that is EMPTY.
   * @param numberRows the number of rows of data
   * @param numberColumns the number of columns of data
   */
   public ImageData(int numberRows, int numberColumns){

      rows = numberRows;
      cols = numberColumns;
      
     

   }
   
   
   
   /**
   * Constructs a ImageData object using the
   * specified  size indicated by
   * numberRows and numberColumns.  Fill the data[]
   * array with the information stored in
   * the ImageData instance ID, from the 2D
   * neighborhood starting at the upper-left coordinate
   * (rStart,cStart) 
   * @param numberRows the number of rows of data
   * @param numberColumns the number of columns of data
   * @param ID image data to copy data from
   * @param rStart,cStart  Start of Neighborhood copy
   */
   public ImageData(int numberRows, int numberColumns, ImageData ID,
                    int rStart,int cStart){


      //saftey check: Retrieval in ID outside of boundaries
      if(ID.rows<(rStart+numberRows) || ID.cols<(cStart+numberColumns))
      {  rows = 0;
         cols = 0;
         return;
      }   
      
      
      rows = numberRows;
      cols = numberColumns;
      
      //create data[] array.
      data = new int[rows*cols];
      
      //Copy data from ID.
      for(int i=0; i<rows; i++)
      for(int j=0; j<cols; j++)
        {   data[i*cols+j] = ID.data[(rStart+i)*ID.cols + j + cStart];
            minDataRange = Math.min(minDataRange, data[i*cols+j]);
            maxDataRange = Math.max(maxDataRange, data[i*cols+j]);
        }    
      
      
   }   



//METHODS
 

 
   


  /**
   * creates a java.awt.Image from the pixels stored 
   * in the array data using 
   * java.awt.image.MemoryImageSource
   */
  public Image createImage()
   {
        int pixels[], t;
        pixels = new int[rows*cols];
    
        //translate the data in data[] to format needed
        for(int r=0;r<rows; r++)
        for(int c=0;c<cols; c++)
        {  t = data[r*cols + c];
           if(t == 999) //due to reg. transformation boundaries produced
            { t = 0; }  // see Transform.ApplyToImage() method
           if(t<0) //due to processing
            { t = -t; }
           else if(t>255) //due to processing
            { t = 255; }
           
           pixels[r*cols+c] = (255 << 24) | (t << 16) | (t << 8) | t;
           //note data is greyscale so red=green=blue above (alpha first)
        }
    
        //Now create Image using new MemoryImageSource
        return ( super.createImage(new MemoryImageSource(cols, rows, pixels, 0, cols)));
	
   } 
   
  //	The createImage Function is similar to the fn used in this project with a flag
  // 	that is by default 0 indicating that it is a user defined function and
 //		custom made.
  public Image createImage(int flg)
  {
       int pixels[], t;
       pixels = new int[rows*cols];
   
       //translate the data in data[] to format needed
       for(int r=0;r<rows; r++)
       for(int c=0;c<cols; c++)
       {  t = data[r*cols + c];
        
          
          pixels[r*cols+c] = t;
          
       }
   
       //Now create Image using new MemoryImageSource
       return ( super.createImage(new MemoryImageSource(cols, rows, pixels, 0, cols)));
	
  }
   
  //Function to create a Negative of an image.
  //Here we go through each pixel and subtract is from 255
  // which will yield a Negative of any image.
   
  public Image createNegative()
  {
       int pixels[], t,alpha,red,green,blue;
       pixels = new int[rows*cols];
       int mid=120;
       System.out.println("Value of mid " + mid);
  
       
       for(int r=0; r<rows; r++)
    	      for(int c=0; c<cols; c++)
    	        {   
    	    	  	t = data[r*cols + c];
    
    		    	  	int r1,g1,b1;
      	              
    	                Color cc=new Color(t);
    	                r1=cc.getRed();
    	                g1=cc.getGreen();
    	                b1=cc.getBlue();

    	                // Subtracting each value from 255 to get its opposite value.
    	                r1=255-r1;
    	                g1=255-g1;
    	                b1=255-b1;
    	                
    	                
    	                pixels[r*cols+c] = (255 << 24) | (r1 << 16) | (g1 << 8) | b1;
    	        }      
   
       //Now create Image using new MemoryImageSource
       return ( super.createImage(new MemoryImageSource(cols, rows, pixels, 0, cols)));
	
  } 
  
   //Function for Thresholding where I accpet a value from the user. 
  // The value is stored in val.
 
  public Image createThreshold(int val)
  {
       int pixels[], t,alpha,red,green,blue;
       pixels = new int[rows*cols];
       int mid=127;
       System.out.println("Value of mid " + mid);
  
       
       for(int r=0; r<rows; r++)
    	      for(int c=0; c<cols; c++)
    	        {   
    	    	  	t = data[r*cols + c];
  
    	               
    	    	  	int r1,g1,b1;
    	              
    	                Color cc=new Color(t);
    	                r1=cc.getRed();
    	                g1=cc.getGreen();
    	                b1=cc.getBlue();    	                
    	                
    	        
    	                /*Thresholding for all three values*/
//    	                if((r1+g1+b1)/3 < val)
//    	                {
//    	                	r1 = g1 = b1 = 0;
//    	                }
//    	                else 
//    	                {
//    	                	r1 = g1 = b1 = 255;
//    	                }
    	                
    	                // Individual Thresholding using the val.
    	                // Here I compare each red green & blue value with the
    	                // Threshold value and assign either 0 or 255.
    	                
    	                if(r1<val)
    	                {
    	                	r1=0;
    	                }
    	                else
    	                {
    	                	r1=255;
    	                }
    	                if(g1<val)
    	                {
    	                	g1=0;
    	                	
    	                }
    	                else
    	                {
    	                	g1=255;
    	                }
    	                if(b1<val)
    	                {
    	                	b1=0;
    	                }
    	                else
    	                {
    	                	b1=255;
    	                }
    	                
    	                pixels[r*cols+c] = (255 << 24) | (r1 << 16) | (g1 << 8) | b1;
    	           
    	        }      
   
       //Now create Image using new MemoryImageSource
       return ( super.createImage(new MemoryImageSource(cols, rows, pixels, 0, cols)));
	
  }   
  
  // Function for Sobel Edge Detection which has two parts
  
  
public Image find_Edges()
{
	  int r1,c1,t=0;
	  r1=rows;
	  c1=cols;
	  
	  double[][] ImageNew=new double[rows][cols];
	  int[] imgpix = new int[(rows*cols)];
	 double SobelX[][] = {{-1,0,1},{-2,0,2},{-1,0,1}};    //{{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
	  double SobelY[][] = {{-1,-2,-1},{0, 0, 0},{1,2,1}};
	  double mag,magX = 0.0,magY = 0.0; // this is your magnitude 
	  
	  // Part 1
	  //The Sobel Algorithm
	  for(int r=1;r<rows-1;r++)
	  {
		  for(int c=1;c<cols-1;c++)
		  {
	  
		  magX = dataimg[r-1][c-1] * SobelX[0][0] + dataimg[r-1][c] * SobelX[0][1] + dataimg[r-1][c+1] * SobelX[0][2] + dataimg[r][c-1] * SobelX[1][0] +  dataimg[r][c] * SobelX[1][1] + dataimg[r][c+1]* SobelX[1][2] + dataimg[r+1][c-1] * SobelX[2][0] +  dataimg[r+1][c] * SobelX[2][1] + dataimg[r+1][c+1]* SobelX[2][2];
		  
		  magY = dataimg[r-1][c-1] * SobelY[0][0] + dataimg[r-1][c] * SobelY[0][1] + dataimg[r-1][c+1] * SobelY[0][2] + dataimg[r][c-1] * SobelY[1][0] +  dataimg[r][c] * SobelY[1][1] + dataimg[r][c+1]* SobelY[1][2] + dataimg[r+1][c-1] * SobelY[2][0] +  dataimg[r+1][c] * SobelY[2][1] + dataimg[r+1][c+1]* SobelY[2][2];
		  //  magY = you figure it out using the y mask
	
		  mag = Math.sqrt(magX*magX + magY*magY);
	  		ImageNew[r][c] = mag;
		  }
	  }
	  
	  // Part 2
	  // Here I am creating a 1D array from ImageNew which
	  // contains the computed pixels from the Sobel Algorithm
	  // These pixels are made absolute to get Int values for the array.
	  
	    int[] pixels = new int[(rows*cols)];
	  
	    int s = 0;
	    for(int i = 0; i < ImageNew.length; i ++) 
	          for(int j = 0; j < ImageNew[0].length; j ++){                           
	              pixels[s] = (int)Math.abs(ImageNew[i][j]);
	              s++;
	          }   
	    
	    // The For loops are then used to get Red Green Blue values from the arbitrary
	    // values from the 1D array of pixels to be put into the final imgpix array.
	    // This is necessary to send the array to the createImage function.
	       for(int r=0; r<rows; r++)
	    	      for(int c=0; c<cols; c++)
	    	        {   
	    	    	  	t = pixels[r*cols + c];
	  
	    	    	  	int r2,g2,b2,gr;
	    	              
	    	                Color cc=new Color(t);
	    	                r2=cc.getRed();
	    	                g2=cc.getGreen();
	    	                b2=cc.getBlue();
	
	    	                gr=(r2+g2+b2)/3;
	    	                
	    	        
	    
	    	                imgpix[r*cols+c] = (255 << 24) | (gr << 16) | (gr << 8) | gr;
	    	           
	    	        }      
	    return ( super.createImage(new MemoryImageSource(cols, rows, imgpix, 0, cols)));
	
	
	
}
  
// Method to perform Contrast Stretch with Image as its Return type
// Here val is the Value passed from the JSlider in ImageFrame
// The slider value will decide the amount of Contrast on the image

public Image cont_stretch(int val)
{
	int t=0,min=0,max=300;
	if(val==0)
	{
		max=400;
	}
	else
	{
		max=val;
	}
	float Y,I,Q;
	int[] pixels=new int [rows*cols];
	for(int r=0;r<rows;r++)
	{
		
		for(int c=0;c<cols;c++)
		{
			t = data[r*cols + c];
			Color cc=new Color(t);
			int R,G,B;
			R=cc.getRed();				//Get RGB values for each value of t
			G=cc.getGreen();
			B=cc.getBlue();
			
			// Converting RGB values to YIQ which makes it easy for contrast stretching 
			Y = (float) (0.299*R + 0.587*G + 0.114*B);
			I = (float) (0.596*R - 0.275*G - 0.321*B);
			Q = (float) (0.212*R - 0.523*G + 0.311*B);

			//perform contrast stretching

			if(Y <= min)
				Y = 0;
			else if (Y >= max)
				Y = 255;
			else
				Y = ((float)(Y-min)/(float)(max-min))*(float)(255);

			// convert YIQ to RGB
			R = (int) (1.0*Y + 0.956*I + 0.620*Q);
			G = (int) (1.0*Y - 0.272*I - 0.647*Q);
			B = (int) (1.0*Y - 1.108*I - 1.705*Q);

			// fix overflows
			if (R > 255) R = 255;
			if (R < 0) R = 0;
			if (G > 255) G = 255;
			if (G < 0) G = 0;
			if (B > 255) B = 255;
			if (B < 0) B = 0;

			pixels[r*cols+c] = (255 << 24) | (R << 16) | (G << 8) | B;
		}
	}
	
	  return ( super.createImage(new MemoryImageSource(cols, rows, pixels, 0, cols)));
}



   /**
	 *Stores the data image to a 
	 * a file as COLOR raw image data format
	 */
	public void storeImage(String filename)throws IOException
	{ 
	   
	    int  pixel, alpha, red, green,blue;
	    
	    
	        
        //Open up file	
        FileOutputStream file_output = new FileOutputStream(filename);
        DataOutputStream DO = new DataOutputStream(file_output);
 
 
        //Write out each pixel as integers
        
	
         
        for(int r=0; r<rows; r++)
	    for(int c=0; c<cols; c++) {
            pixel = data[r*cols + c];
	        red = pixel;
            green = pixel;
            blue = pixel;
            if(verbose)//verbose
    	        {System.out.println("value: " + (int)((red+green+blue)/3));
    	         System.out.println(" R,G,B: " + red +"," + green +"," + blue); }
	   
 	        DO.writeByte(red);
 	        DO.writeByte(green);
 	        DO.writeByte(blue);
        }	

        //flush Stream
        DO.flush();
        //close Stream
        DO.close();

    }
   
   
  
     
 
	//{{DECLARE_CONTROLS
	//}}
}//End ImageData
