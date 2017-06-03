package lsh;

import java.util.ArrayList;

import util.CSVParser;

public class Column {
	
	final boolean DEBUG = true;
	
	private ArrayList<Element> elements;
	
	private ArrayList<Double> hashValues;

	private ArrayList<HashFunction> hashFunctions;
	
	public Column(){
		setElements(new ArrayList<Element>());
	}
	
	public Column(ArrayList<Element> elements) {
		setElements(elements);
	}
	
	public Column(ArrayList<Element> elements, ArrayList<HashFunction> functions) {
		setElements(elements);
		hashValues(functions);
	}
	
	public void setHashFunctions(ArrayList<HashFunction> functions){
		this.hashFunctions = functions;
	}

	public ArrayList<HashFunction> getHashFunctions(){
		return this.hashFunctions;
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
		
		boolean ignoreLastCol = true;
		ArrayList<Column> result = new ArrayList<Column>();
		if(ignoreLastCol) --width;
		
		for(ArrayList<Object> row : data){
			Column col = new Column();
			for(int i = 0; i < width; ++i){
				col.addElement(new Element(row.get(i)));
			}				
			result.add(col);
		}
			
		return result;
	}
	
	public void hashValues(ArrayList<HashFunction> functions){
		
		hashFunctions = functions;
		hashValues = new ArrayList<Double>();
		
		for(HashFunction func : functions)
			hashValues.add(func.apply(elements));
		
	}
	
	public ArrayList<Double> getHashValues(){
		return hashValues;
	}

	public double getHashValue(int index){
		return hashValues.get(index);
	}
	
	public double getHashValue(HashFunction func){
		
		int index = hashFunctions.indexOf(func);
		
		return hashValues.get(index); 
	}
}
