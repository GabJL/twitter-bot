package es.uma.informatica.rysd.twitter.modelo;


public class Main {
	public static void main(String[] args) throws Exception {
		SquareWarBot wb = new SquareWarBot();
		Twitter twitter = new Twitter();
		int [] values = null;
		while(wb.getActiveSquares() != 1){ // Mientras haya más de 1 contendiente
			values = wb.getMovement(); // Hacemos un movimiento
			// Generamos el mensaje a mostrar
			String msg = Integer.toString(values[0]) + " ha conquistado una zona de " + values[1] +".\n";
			if(wb.getPossessions(values[1]) == 0)
				msg += values[1] + " ha sido derrotado. Quedan " + wb.getActiveSquares() + " cuadrados.\n";
			msg += "#Cuad"+values[0] + " vs #Cuad"+values[1]+ " #SquareWarBot.";
			// Generamos la imagen
			wb.printMap("img.png", values);
			System.out.println(msg);
			// Subimos la imagen a Twitter
			String id = twitter.UploadFile("img.png");
			// Enviamos el tuit con los datos generados previamente
			twitter.sendTweet(msg, id);
			// Esperamos antes de hacer otro combate
			Thread.sleep(10000);
		}
		// Motramos el ganador
		twitter.sendTweet(""+values[0]+" ha ganado la batalla.\n  #Cuad"+values[0] + " wins! #SquareWarBot.", null);
	}
}
