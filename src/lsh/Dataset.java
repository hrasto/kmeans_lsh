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

	/*
	public Dataset(ArrayList<Column> cols, ArrayList<HashFunction> functions) {
		setCols(cols);
		hashColumns(functions);
	}
	*/
	
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
	
	public boolean isAlreadyHashed(){
		return alreadyHashed;
	}
	
	public void print(boolean hashed, Integer limit){
		
		if(hashed && !alreadyHashed)
			throw new IllegalArgumentException();
		
		int outerLimit = hashed ? cols.get(0).getHashBuckets().size() : getHeight();
		int innerLimit = limit != null && limit <= getWidth() ? limit : getWidth(); 
		
		for(int i = 0; i < outerLimit; ++i){
			String line = "[ ";
			
			for(int j = 0; j < innerLimit; ++j){
				if(hashed) 
					line += cols.get(j).getHashBucket(i);
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

	public void hashColumns(ArrayList<BucketArithmetic> resolvers){
		
		for(Column col : this.cols)
			col.hashValues(resolvers);
		
		alreadyHashed = true;
		
	}

	public ArrayList<Bucket> groupByBuckets(BucketArithmetic resolver) {
		
		if(!alreadyHashed)
			return null;
			
		ArrayList<Bucket> res = new ArrayList<Bucket>();
		
		// instantiate the buckets with size = 0
		double bucketBorder = resolver.getMin();
		
		while(bucketBorder < hashedMax(resolver.getFunc())){
			res.add(new Bucket(bucketBorder, bucketBorder+resolver.getBucketSize()));
			bucketBorder += resolver.getBucketSize();
		}
		
		// add columns to the corresponding hash-buckets
		for(Column col : cols){
			int index = resolver.getBucketIndex(col);			
			res.get(index).addColumn(col);
		}
		
		return res;
	}
	
	public double hashedMin(HashFunction func) {
		/*
		if(!alreadyHashed)
			throw new Exception("Data Set must be hashed before finding the hashed minimum");
			*/
		
		double min = func.apply(cols.get(0).getElements());
		
		for(Column col : cols){
			double newMin = func.apply(col.getElements());
			if(min > newMin)
				min = newMin;
		}
		
		return min;
	}

	public double hashedMax(HashFunction func) {
		
		/*
		if(!alreadyHashed)
			throw new Exception("Data Set must be hashed before finding the hashed minimum");
			*/
		
		double max = func.apply(cols.get(0).getElements());
		
		for(Column col : cols){
			double newMax = func.apply(col.getElements());
			if(max < newMax)
				max = newMax;
		}
		
		return max;
	}

}
