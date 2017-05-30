package lsh;

import java.util.ArrayList;

public class Dataset {
	
	private ArrayList<Column> docs;

	public Dataset() {
		// TODO Auto-generated constructor stub
	}
	
	public void setDocs(ArrayList<Column> docs){
		this.docs = docs;
	}
	
	public ArrayList<Column> getDocs(){
		return this.docs;
	}

}
