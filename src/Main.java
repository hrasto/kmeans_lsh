import java.util.ArrayList;
import java.util.Collections;

import kmeans.Cluster;
import kmeans.KMeans;
import lsh.Column;
import lsh.Dataset;
import lsh.Element;
import lsh.FHash;
import lsh.GHash;
import lsh.HashFunction;
import util.CSVParser;

public class Main {
	
	final static int PRINT_COLS_LIMIT = 15;
	
	final static int NUM_OF_ITERATIONS = 10;
	
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
		Dataset ds = new Dataset(cols);
		
		/*
		// output a sample of raw data
		System.out.println("Raw:");
		ds.print(false, PRINT_COLS_LIMIT);
		
		// output a sample of hashed data
		System.out.println("Hashed:");
		ds.print(true, PRINT_COLS_LIMIT);
		*/
		
		try{
			
			KMeans km = new KMeans(ds, functions);
			km.execute(NUM_OF_ITERATIONS);
			ArrayList<Cluster> clusters = km.getClusters();
			cols = km.getData().getCols();
			
			ArrayList<Object> lastCol = CSVParser.readLastCol(CSV_PATH, CSV_DELIMITER);
			ArrayList<Integer> gt = new ArrayList<Integer>();
			
			for(Object obj : lastCol){
				Double val = new Double(obj.toString().substring(5));
				//System.out.println(val.intValue());
				gt.add((val.intValue()));
			}

			ArrayList<Integer> myResults = new ArrayList<Integer>();
			for(Column col : cols){
				myResults.add(clusters.indexOf(col.getAssignedCluster()));
			}
			
			NMI(myResults, gt);

			/*
			for(Cluster cluster : clusters){
				String line = "Cluster ";
				for(Element val : cluster.getCentroid().getElements())
					line += "| " + val.getValue() + " |";
				line += " containing " + cluster.getPoints().size() + " points";
				System.out.println(line);
			}
			*/
			
			/*
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
			*/
			
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
	
	public static double NMI(ArrayList<Integer> one, ArrayList<Integer> two){
		if(one.size()!=two.size()){
			throw new IllegalArgumentException("Sizes don't match!");
		}
		int maxone = Collections.max(one);
		int maxtwo = Collections.max(two);

		double[][] count = new double[maxone+1][maxtwo+1];
		//System.out.println(count[1][2]);
		for(int i=0;i<one.size();i++){
			count[one.get(i)][two.get(i)]++;
		}
		//i<maxone=R
		//j<maxtwo=C
		double[] bj = new double[maxtwo+1];
		double[] ai = new double[maxone+1];

		for(int m=0;m<(maxtwo+1);m++){
			for(int l=0;l<(maxone+1);l++){
				bj[m]=bj[m]+count[l][m];
			}
		}
		for(int m=0;m<(maxone+1);m++){
			for(int l=0;l<(maxtwo+1);l++){
				ai[m]=ai[m]+count[m][l];
			}
		}

		double N=0;
		for(int i=0;i<ai.length;i++){
			N=N+ai[i];
		}
		double HU = 0;
		for(int l=0;l<ai.length;l++){
			double c=0;
			c=(ai[l]/N);
			if(c>0){
				HU=HU-c*Math.log(c);
			}
		}

		double HV = 0;
		for(int l=0;l<bj.length;l++){
			double c=0;
			c=(bj[l]/N);
			if(c>0){
				HV=HV-c*Math.log(c);
			}
		}
		double HUstrichV=0;
		for(int i=0;i<(maxone+1);i++){
			for(int j=0;j<(maxtwo+1);j++){
				if(count[i][j]>0){
					HUstrichV=HUstrichV-count[i][j]/N*Math.log(((count[i][j])/(bj[j])));
				}
			}
		}
		double IUV = HU-HUstrichV;
		double reto = IUV/(Math.max(HU, HV));

		System.out.println("NMI: "+reto);
		return reto;
	}

}
