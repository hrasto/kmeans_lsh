import java.util.ArrayList;

import chart.Plot;
import lsh.Bucket;
import lsh.Column;
import lsh.Dataset;
import lsh.FHash;
import lsh.HashFunction;

public class Main {
	
	final static int PRINT_COLS_LIMIT = 15;
	
	final static String CSV_PATH = "nmi.csv";
	
	final static String CSV_DELIMITER = ",";

	public static void main(String[] args) {
		
		// instantiate the hash-functions
		ArrayList<HashFunction> functions = new ArrayList<HashFunction>();
		functions.add(new FHash());
		
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
			ArrayList<Bucket> buckets = ds.groupByBuckets(12);	
			System.out.println("# of buckets: " + buckets.size());
			
			ArrayList<String> xData = new ArrayList<String>();
			ArrayList<Integer> yData = new ArrayList<Integer>();
			for(Bucket bkt : buckets){
				xData.add(bkt.getMin().toString());
				yData.add(bkt.getSize());
			}
			
			Plot.barChart(xData, yData, "hash_buckets", "Buckets", "Bucket Size", 500, 500);
			
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
