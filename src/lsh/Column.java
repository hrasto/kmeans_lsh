package lsh;

import java.util.ArrayList;

import util.CSVParser;

public class Column {
	
	final boolean DEBUG = true;
	
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
	
	public int getSize(){
		return this.elements.size();
	}

	public Column applyHashValues(ArrayList<ArrayList<Integer>> hashValues){
		Column col = new Column();
		
		if(hashValues.size() == 0)
			throw new IllegalArgumentException();
		
		// initialisation with infinity
		for(int i = 0; i < hashValues.get(0).size(); ++i)
			col.addElement(new Element());
		
		// row by row checking
		for(int rowId = 0; rowId < hashValues.size(); ++rowId){
			
			// if this row value == 1, then:
			// compute each hash-value for this row
			// and eventually assign the hash value
			// as a new row-element
			
			if(getElement(rowId).getValue() == 0) continue;
			
			int pId = 0;
			for(int hashValue : hashValues.get(rowId)){
				//int hashValue = p.apply(rowId);
				if(col.getElement(pId).isInfinity() || col.getElement(pId).getValue() > hashValue){
					col.getElement(pId).setValue(hashValue);
					if(DEBUG)
						System.out.println("Updating row " + rowId + 
							", permutation " + pId + " to the value " + hashValue);
				}
				++pId;
			}		
		}
		
		return col;
	}
	
	public Element getElement(int index){
		return elements.get(index);
	}
	
	public static ArrayList<Column> csv2columns(String path, String delimiter){
		ArrayList<ArrayList<Object>> data = CSVParser.read(path, delimiter);
		if(data.size() == 0)
			throw new IllegalArgumentException();
		
		int width = data.get(0).size();
		
		for(ArrayList<Object> row : data)
			if(row.size() != width)
				throw new IllegalArgumentException();
		
		int rowOffset = 0;
		boolean ignoreLastCol = true;
		ArrayList<Column> result = new ArrayList<Column>();
		if(ignoreLastCol)
			--width;
		for(int i = 0; i < width; ++i)
			result.add(new Column());
		for(int i = rowOffset; i < data.size(); ++i)
			for(int j = 0; j < width; ++j)
				result.get(j).addElement(new Element(data.get(i).get(j)));
			
		return result;
	}
}
