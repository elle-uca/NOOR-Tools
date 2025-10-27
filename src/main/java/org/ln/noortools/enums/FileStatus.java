package org.ln.noortools.enums;

public enum FileStatus {
	OK ("Ok"),  
	KO1 ("Esiste nella cartella"),
	KO ("Nome duplicato");

	private String title;
	FileStatus(String string) {
		this.title = string;
	}


	@Override
	public String toString() {
		return title;
	}

}