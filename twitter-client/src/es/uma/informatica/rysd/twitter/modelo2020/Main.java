package es.uma.informatica.rysd.twitter.modelo2020;

import es.uma.informatica.rysd.twitter.client.Twitter;

public class Main {
	public static void main(String[] args) throws Exception {
		StudentWarBot wb = new StudentWarBot("students.txt");
		Twitter twitter = new Twitter();
		while(wb.getActiveStudents() != 1){ // Mientras haya más de 1 contendiente
			wb.generateMovement();
			String message = wb.getMessage();
			// Generamos el mensaje a mostrar
			message += "\n\nQuedan " + wb.getActiveStudents() + " estudiantes activos #StudentWarBot.";
			System.out.println(message);
			wb.generateImage("img.png");
			// Subimos la imagen a Twitter
			String id = twitter.UploadFile("img.png");
			// Enviamos el tuit con los datos generados previamente
			twitter.sendTweet(message, id);
			// Esperamos antes de hacer otro combate
			Thread.sleep(10000);
		}
		// Motramos el ganador
		twitter.sendTweet(wb.getWinner()+ " ha ganado la batalla #StudentWarBot.", null);
	}
}
