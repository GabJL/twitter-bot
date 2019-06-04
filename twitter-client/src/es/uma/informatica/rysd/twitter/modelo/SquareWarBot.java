package es.uma.informatica.rysd.twitter.modelo;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

public class SquareWarBot {
	final static private int size = 3;
	final static private int sq = 50;
	private int [][] square;
	private int [] possessions;
	
	public SquareWarBot(){
		square = new int[size][size];
		possessions = new int[size*size];
		
		for(int i = 0; i < size; i++){
			for(int j = 0; j < size; j++){
				square[i][j] = i*size + j + 1;
				possessions[i*size + j] = 1;
			}
		}
	}
	
	public int getActiveSquares(){
		int count = 0;
		for(int i: possessions){
			if(i > 0) count++;
		}
		return count;
	}
	
	public int getPossessions(int s){
		return possessions[s-1];
	}

	public void printMap(String filename, int [] mov){
	    try {
	        int width = size*sq, height = size*sq;

	        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

	        Graphics2D ig2 = bi.createGraphics();
	        Font font = new Font("Verdana", Font.BOLD, 20);
	        ig2.setFont(font);
	        for(int i = 0; i < size; i++){
	        	for(int j = 0; j < size; j++){
	    	        String message = Integer.toString(square[i][j]);
	    	        int x = (square[i][j]-1)/size;
	    	        int y = (square[i][j]-1)%size;
	    	        
	    	        FontMetrics fontMetrics = ig2.getFontMetrics();
	    	        int stringWidth = fontMetrics.stringWidth(message);
	    	        int stringHeight = fontMetrics.getAscent();
	    	        ig2.setColor(new Color(((float)x)/3, ((float)x+y)/6, ((float)y)/3));
	    	        ig2.fillRect(i*sq, j*sq, sq, sq);
	    	        ig2.setPaint(Color.GREEN);
	    	        ig2.drawString(message, i*sq + (sq - stringWidth) / 2, j*sq + (sq / 2 + stringHeight / 4));
	        	}
	        }
	        ig2.setColor(Color.red);
	        ig2.drawRect(mov[2]*sq, mov[3]*sq, sq, sq);
	        ig2.drawRect(mov[4]*sq, mov[5]*sq, sq, sq);

	        ImageIO.write(bi, "PNG", new File(filename));
	        
	      } catch (IOException ie) {
	        ie.printStackTrace();
	      }		
	}
	
	public int [] getMovement(){
		int [] values = new int [6];
		boolean correct = false;
		Random r = new Random();
		
		while(!correct){
			int x = r.nextInt(size);
			int y = r.nextInt(size);
			
			ArrayList<Integer> candidate = new ArrayList<Integer>();
			for(int i = -1; i <= 1; i++){
				int x1 = x+i;
				if(x1 >= 0 && x1 < size){
					for(int j = -1; j <= 1; j++){
						int y1 = y+j;
						if(y1 >= 0 && y1 <size){
							if(square[x][y] != square[x1][y1]){
								candidate.add(x1*size+y1);
							}
						}
					}
				}
			}
			if(candidate.size() > 0){
				correct = true;
				int p = r.nextInt(candidate.size());
				values[0] = square[x][y];
				possessions[square[x][y]-1]++;
				int x1 = candidate.get(p)/size;
				int y1 = candidate.get(p)%size;
				values[1] = square[x1][y1];
				possessions[square[x1][y1]-1]--;
				square[x1][y1] = square[x][y];
				values[2] = x; values[3] = y; 
				values[4] = x1; values[5] = y1; 
			}
		}
		
		return values;
	}

}
