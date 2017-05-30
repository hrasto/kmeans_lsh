package lsh;

import java.util.ArrayList;

public class Column {
	
	private ArrayList<Element> elements;
	
	public Column(){
		setElements(new ArrayList<Element>());
	}
	
	public Column(ArrayList<Element> elements) {
		setElements(elements);
	}
	
	public ArrayList<Element> getElements() {
		return elements;
	}

	public void setElements(ArrayList<Element> elements) {
		this.elements = elements;
	}
	
	public void addElement(Element el){
		this.elements.add(el);
	}
	
	public void addElements(ArrayList<Element> els){
		for (Element e : els)
			this.elements.add(e);
	}

}
