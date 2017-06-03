import java.util.ArrayList;

import chart.Plot;
import lsh.Bucket;
import lsh.Column;
import lsh.Dataset;
import lsh.FHash;
import lsh.GHash;
import lsh.HashFunction;

public class Main {
	
	final static int PRINT_COLS_LIMIT = 15;
	
	final static String CSV_PATH = "nmi.csv";
	
	final static String CSV_DELIMITER = ",";

	public static void main(String[] args) {
		
		// instantiate the hash-functions
		ArrayList<HashFunction> functions = new ArrayList<HashFunction>();
		HashFunction fh = new FHash();
		HashFunction gh = new GHash();
		functions.add(fh);
		functions.add(gh);
		
		// read the raw data from CSV, create data set
		ArrayList<Column> cols = Column.csv2columns(CSV_PATH, CSV_DELIMITER);
		Dataset ds = new Dataset(cols, functions);
		
		// output a sample of raw data
		System.out.println("Raw:");
		ds.print(false, PRINT_COLS_LIMIT);
		
		// output a sample of hashed data
		System.out.println("Hashed:");
		ds.print(true, PRINT_COLS_LIMIT);
		
		try{
			int bucketSize = 25; // such value, that we get ~15 buckets (clusters)
			
			ArrayList<Bucket> buckets = ds.groupByBuckets(bucketSize, gh);	
			System.out.println("# of buckets: " + buckets.size());
			
			ArrayList<String> xData = new ArrayList<String>();
			ArrayList<Integer> yData = new ArrayList<Integer>();
			for(Bucket bkt : buckets){
				xData.add(Double.toString(Math.floor(bkt.getMin())));
				yData.add(bkt.getSize());
			}
			
			Plot.barChart(xData, yData, "hash_buckets", "Buckets", "Bucket Size", 1600, 500);
			
			// output buckets
			for(int i = 0; i < buckets.size(); ++i){
				System.out.println("Bucket "+i+": "+buckets.get(i).getSize()+" columns (points)");
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		/*
		Dataset ds = new Dataset();
		ds.print();
		
		Dataset dsPerm = ds.permute();
		dsPerm.print();
		*/
	}

}
