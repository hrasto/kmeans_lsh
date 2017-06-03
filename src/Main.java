import java.util.ArrayList;

import lsh.Column;
import lsh.Dataset;
import lsh.FHash;
import lsh.HashFunction;

public class Main {
	
	final static int PRINT_COLS_LIMIT = 5;
	
	final static String CSV_PATH = "nmi_sample.csv";
	
	final static String CSV_DELIMITER = ",";

	public Main() {
		
	}

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
		
		/*
		Dataset ds = new Dataset();
		ds.print();
		
		Dataset dsPerm = ds.permute();
		dsPerm.print();
		*/
	}

}
