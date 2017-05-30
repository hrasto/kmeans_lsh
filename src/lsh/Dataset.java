package lsh;

import java.util.ArrayList;
import java.util.Random;

public class Dataset {
	
	final int RANDOM_DATASET_WIDTH = 1;
	
	final int RANDOM_DATASET_HEIGHT = 5;
	
	final int NUM_OF_HASH_FUNCTIONS = 2;
	
	private ArrayList<Column> cols;

	public Dataset(ArrayList<Column> cols) {
		setCols(cols);
	}
	
	public Dataset(){
		ArrayList<Column> testCols = generateRandomCols(RANDOM_DATASET_WIDTH, RANDOM_DATASET_HEIGHT);
		setCols(testCols);
	}
	
	public ArrayList<Column> generateRandomCols(int width, int height){
		ArrayList<Column> testCols = new ArrayList<Column>();
		
		for(int i = 0; i < width; ++i){
			ArrayList<Element> testEls = new ArrayList<Element>();
			for(int j = 0; j < height; ++j){
				Random rn = new Random();
				int val = rn.nextInt(2);
				testEls.add(new Element(val));
			}
			testCols.add(new Column(testEls));
		}
		
		return testCols;
	}
	
	public void setCols(ArrayList<Column> cols){
		
		if(cols.size() == 0)
			throw new IllegalArgumentException();
		
		int colsize = getHeight(cols);
		
		for (Column c : cols)
			if(c.getSize() != colsize)
				throw new IllegalArgumentException();
		
		this.cols = cols;			
	}
	
	public ArrayList<Column> getCols(){
		return this.cols;
	}
	
	public void print(){
		for(int i = 0; i < getHeight(); ++i){
			String line = "";
			line += "[ ";
			for(int j = 0; j < getWidth(); ++j){
				Element el = getElement(j,i);
				
				if(el.isInfinity())
					line += "inf";
				else
					line += el.getValue();
				
				if(j < getWidth()-1) line += "\t| ";
			}
			line += " ]";
			
			System.out.println(line);
		}
	}
	
	public int getHeight(){
		return getHeight(cols);
	}

	public int getHeight(ArrayList<Column> cols){
		return cols.get(0).getSize();
	}
	
	public int getWidth(){
		return this.cols.size();
	}
	
	public Element getElement(int x, int y){
		return getCols().get(x).getElements().get(y);
	}

	public Dataset permute(){
		ArrayList<Column> newCols = new ArrayList<Column>();
		ArrayList<Permutation> pms = createPermutations(NUM_OF_HASH_FUNCTIONS);
		ArrayList<ArrayList<Integer>> hashValues = getHashValues(pms);
		
		for(Column col : cols)
			newCols.add(col.applyHashValues(hashValues));
		
		return new Dataset(newCols);
	}
	
	public ArrayList<ArrayList<Integer>> getHashValues(ArrayList<Permutation> pms){
		ArrayList<ArrayList<Integer>> hashValues = new ArrayList<ArrayList<Integer>>();
		for(int i = 0; i < getHeight(); ++i){
			ArrayList<Integer> vals = new ArrayList<Integer>();
			for(Permutation p : pms)
				vals.add(p.apply(i));
			hashValues.add(vals);
		}
		return hashValues;
	}
	
	public ArrayList<Permutation> createPermutations(int howMany){
		ArrayList<Permutation> pms = new ArrayList<Permutation>();
		Permutator p = new Permutator(getHeight());
		for(int i = 0; i < howMany; ++i)
			pms.add(p.next());
		return pms;
	}
}
