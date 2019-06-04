package es.uma.informatica.rysd.twitter.entidades;

public class User {
	public String name;
	public String id_str;
	public String location;
	public String description;

	@Override
	public String toString() {
		String t;
		t= name;
		if(!location.isEmpty())
			t += " (" + location + ")";
		if(!description.isEmpty())
			t += " - " + description;
		return t;
	}
}
