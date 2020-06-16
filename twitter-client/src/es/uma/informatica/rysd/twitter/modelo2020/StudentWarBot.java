package es.uma.informatica.rysd.twitter.modelo2020;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import gui.ava.html.image.generator.HtmlImageGenerator;

enum ESTADO {VIVO, MUERTO};

public class StudentWarBot {
	class Student{
		public String name;
		public ESTADO status;
		public Student(String name){
			this.name = name;
			status = ESTADO.VIVO;
		}
	}
	
	private List<Student> students;
	private String last_movement = null;
	private String last_movement_html = null;
	private Random r;
	
	public StudentWarBot(String filename){
		r = new Random();
		try {
			Scanner sc = new Scanner(new File(filename));
			students = new ArrayList<Student>();
			
			 while(sc.hasNext()){
				 students.add(new Student(sc.next()));
			 }
		} catch (FileNotFoundException e) {
			System.err.println("The file "+ filename +" doesn't exist");
			System.exit(-1);
		}
	}

	public int getActiveStudents() {
		int cont = 0;
		for(Student s: students){
			if(s.status == ESTADO.VIVO) cont++;
		}
		return cont;
	}
	
	private int selectCorrectPosition(){
		int i = r.nextInt(students.size());
		while(students.get(i).status != ESTADO.VIVO){
			i = (i+1)% students.size();
		}
		return i;
	}
	
	public void generateMovement(){
		int loser = selectCorrectPosition();
		students.get(loser).status = ESTADO.MUERTO;
		int winner = selectCorrectPosition();
		last_movement = getText(winner, loser);
		last_movement_html = getHTML(winner, loser);
	}

	private String getHTML(int winner, int loser) {
		String html = null;
		try {
			html = new String(Files.readAllBytes(Paths.get("style.css")));
			html += "<table id=\"tabla\"><tr>";
			int i = 0;
			for(Student s: students){
				if(i%5 == 0) html += "</tr><tr>";
				html +="<td id=\"";
				if(i == winner) html +="winner";
				else if(i == loser) html += "loser";
				else if(s.status == ESTADO.VIVO) html += "alive";
				else html += "dead";
				html += "\">"+s.name+"</td>";
				i++;
			}
			i = 4 - i%5;
			for(int j = 0; j <= i; j++) html += "<td></td>";
			html += "</tr></table>";
		} catch (IOException e) {
			System.err.println("The file style.css doesn't exist");
			System.exit(-1);
		}
		return html;
	}

	private String getText(int winner, int loser) {
		final String [] finales = {"WWW le hace una zancadilla a LLL",
				"WWW arranca el cable del router de LLL",
				"WWW ejecuta un sudo rm -rf en el equipo de LLL",
				"WWW gana el pulso chino a LLL",
				"WWW eligió piedra y LLL tijeras",
				"WWW eligió tijeras y LLL papel",
				"WWW eligió papel y LLL piedra",
				"WWW envía un RSET a LLL",
				"WWW envía un Connection: Close a LLL",
				"WWW envía un QUIT a LLL",
				"WWW envía un TEARDOWN a LLL",
				"WWW envía una señal de jamming a LLL",
				"WWW envía un código 401 a LLL",
				"WWW envía un código 406 a LLL",
				"LLL da un 501 y deja el paso libre a WWW"};
		int i = r.nextInt(finales.length);
		String msg = finales[i].replaceFirst("WWW", students.get(winner).name).replaceFirst("LLL", students.get(loser).name);
		return msg;
	}

	public String getMessage() {
		return last_movement;
	}

	public void generateImage(String filename) {
		HtmlImageGenerator imageGenerator = new HtmlImageGenerator(); 
		imageGenerator.loadHtml(last_movement_html); 
		imageGenerator.saveAsImage(filename);  
	}
	
	public String getWinner(){
		int i = 0; 
		while(students.get(i).status != ESTADO.VIVO) i++;
		return students.get(i).name;
	}
	
}
