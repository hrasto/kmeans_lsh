package lsh;

import java.util.ArrayList;
import java.util.Random;

public class Dataset {
	
	final static int RANDOM_DATASET_WIDTH = 5;
	
	final static int RANDOM_DATASET_HEIGHT = 8;
	
	final static int NUM_OF_HASH_FUNCTIONS = 5;
	
	private ArrayList<Column> cols;
	
	private boolean alreadyHashed = false;

	public Dataset(ArrayList<Column> cols) {
		setCols(cols);
	}

	public Dataset(ArrayList<Column> cols, ArrayList<HashFunction> functions) {
		setCols(cols);
		hashColumns(functions);
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
	
	public void print(boolean hashed, Integer limit){
		
		if(hashed && !alreadyHashed)
			throw new IllegalArgumentException();
		
		int outerLimit = hashed ? cols.get(0).getHashFunctions().size() : getHeight();
		int innerLimit = limit != null && limit <= getWidth() ? limit : getWidth(); 
		
		for(int i = 0; i < outerLimit; ++i){
			String line = "[ ";
			
			for(int j = 0; j < innerLimit; ++j){
				if(hashed) 
					line += cols.get(j).getHashValue(i);
				else				
					if(getElement(j,i).isInfinity()) line += "inf";
					else line += getElement(j,i).getValue();
				
				if(j < innerLimit-1) line += "\t| ";
			}
			
			line += "\t]";			
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
		
		do
			for(Column col : cols)
				newCols.add(col.applyHashValues(hashValues));			
		while(!infiniteFree(newCols));
		
		return new Dataset(newCols);
	}
	
	public static boolean infiniteFree(ArrayList<Column> cols){
		for(Column col : cols)
			for(Element el : col.getElements())
				if(el.isInfinity())
					return false;
		
		return true;
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

	public void hashColumns(ArrayList<HashFunction> functions){
		
		for(Column col : this.cols)
			col.hashValues(functions);
		
		alreadyHashed = true;
		
	}

	public ArrayList<Bucket> groupByBuckets(double bucketSize) throws Exception {
		
		if(!alreadyHashed)
			throw new Exception("Data Set must be hashed before grouping");
			
		ArrayList<Bucket> res = new ArrayList<Bucket>();
		double min = hashedMin();
		double max = hashedMax();

		// instantiate the buckets with size = 0
		double bucketBorder = min;
		while(bucketBorder < max){
			res.add(new Bucket(bucketBorder, bucketBorder+bucketSize));
			bucketBorder += bucketSize;
		}
		
		for(Column col : cols){
			int index = (int) ((col.getHashValue(0) - min) / bucketSize); // casting floors the result
			res.get(index).addColumn(col); // add column to the bucket
		}
		
		return res;
	}
	
	public double hashedMin() throws Exception {
		
		if(!alreadyHashed)
			throw new Exception("Data Set must be hashed before finding the hashed minimum");
		
		double min = cols.get(0).getHashValue(0);
		
		for(Column col : cols)
			if(min > col.getHashValue(0))
				min = col.getHashValue(0);
		
		return min;
	}

	public double hashedMax() throws Exception {
		
		if(!alreadyHashed)
			throw new Exception("Data Set must be hashed before finding the hashed minimum");
		
		double max = cols.get(0).getHashValue(0);
		
		for(Column col : cols)
			if(max < col.getHashValue(0))
				max = col.getHashValue(0);
		
		return max;
	}
}
