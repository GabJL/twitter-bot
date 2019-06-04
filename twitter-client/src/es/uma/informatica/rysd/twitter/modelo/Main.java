package es.uma.informatica.rysd.twitter.modelo;


public class Main {
	public static void main(String[] args) throws Exception {
		SquareWarBot wb = new SquareWarBot();
		Twitter twitter = new Twitter();
		int [] values = null;
		while(wb.getActiveSquares() != 1){
			values = wb.getMovement();
			String msg = Integer.toString(values[0]) + " ha conquistado una zona de " + values[1] +".\n";
			if(wb.getPossessions(values[1]) == 0)
				msg += values[1] + " ha sido derrotado. Quedan " + wb.getActiveSquares() + " cuadrados.\n";
			msg += "#Cuad"+values[0] + " vs #Cuad"+values[1]+ " #SquareWarBot.";
			wb.printMap("img.png", values);
			System.out.println(msg);
			String id = twitter.UploadFile("img.png");
			twitter.sendTweet(msg, id);
			Thread.sleep(10000);
		}
		twitter.sendTweet(""+values[0]+" ha ganado la batalla.\n  #Cuad"+values[0] + " wins! #SquareWarBot.", null);
	}
}
