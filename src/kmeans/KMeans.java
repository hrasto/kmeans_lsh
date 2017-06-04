package kmeans;

import java.util.ArrayList;
import java.util.Collections;

import lsh.Bucket;
import lsh.BucketArithmetic;
import lsh.Column;
import lsh.Dataset;
import lsh.HashFunction;

public class KMeans {
	
	final static String COMBINING_STRATEGY = "OR";
	
	final static int BUCKET_SIZE = 15;

	final static int NUM_OF_CLUSTERS = 15;
	
	private ArrayList<Cluster> clusters;
	
	private Dataset data;
	
	// private ArrayList<ArrayList<Bucket>> buckets;
	
	private ArrayList<BucketArithmetic> bktResolvers;
	
	public KMeans(Dataset ds, ArrayList<HashFunction> functions) {
		clusters = new ArrayList<Cluster>();
		setData(ds);
		setUpBktResolvers(BUCKET_SIZE, functions);
		
		data.hashColumns(bktResolvers); // for the points, we only do this once; for centroids in every iteration
	}
	
	public Dataset getData(){
		return data;
	}
	
	public KMeans setData(Dataset ds){
		
		/*
		if(!ds.isAlreadyHashed())
			throw new IllegalArgumentException("the dataset must be already hashed");
			*/
		
		this.data = ds; 
		
		return this;
	}
	
	public void setUpBktResolvers(int bucketSize, ArrayList<HashFunction> functions){
		
		bktResolvers = new ArrayList<BucketArithmetic>();
		
		for(HashFunction hf : functions){
			BucketArithmetic resolver = new BucketArithmetic(data.hashedMin(hf), bucketSize, hf);
			bktResolvers.add(resolver);
		}
	}
	
	/*
	public void setUpBuckets(int bucketSize) {
		
		
		if(data == null || hashFunctions == null)
			throw new NullPointerException("Dataset and hash functions must be set before setting up buckets");
		
		buckets = new ArrayList<ArrayList<Bucket>>();
		
		for(HashFunction func : hashFunctions){
			ArrayList<Bucket> newBkts = data.groupByBuckets(bucketSize, func);	
			buckets.add(newBkts);
		}
		
	}
	*/
	
	public ArrayList<Cluster> getClusters() {
		return clusters;
	}

	public void setClusters(ArrayList<Cluster> clusters) {
		this.clusters = clusters;
	}
	
	/*
	public void setHashFunctions(ArrayList<HashFunction> functions){
		hashFunctions = functions;
	}
	
	public ArrayList<HashFunction> getHashFunctions(){
		return hashFunctions;
	}
	*/
	
	public void initializeClusters() throws Exception{
		
		System.out.println("INITIALIZING");
		
		ArrayList<Bucket> bucketsF = new ArrayList<Bucket>();
		
		try{
			bucketsF = data.groupByBuckets(bktResolvers.get(0));
			Collections.sort(bucketsF, 
                    (o1, o2) -> (new Integer(o2.getCols().size())).compareTo(o1.getCols().size()));
			
			int limit = NUM_OF_CLUSTERS <= bucketsF.size() ? NUM_OF_CLUSTERS : bucketsF.size();
			
			int count = 0;
			for(Bucket bkt : bucketsF){
				if(bkt.getCols().size() == 0) continue;
				if(count++ == limit) break;
				
				Cluster newCluster = new Cluster(bkt.getCols());
				newCluster.computeCentroid(bktResolvers);
				clusters.add(newCluster);	
			}
			
		}catch(Exception e){
			throw e;
		}
	}

	public void recomputeCentroids() {
		
		for(Cluster cluster : clusters)
			cluster.computeCentroid(bktResolvers);
		
	}

	public void reassignPoints() {
		
		System.out.println("Reassigning the points (" + data.getCols().size() + " total)");

		ArrayList<Column> processedCols = new ArrayList<Column>();
		
		// assigning columns with the help of hash buckets
		for(Cluster cluster : clusters){
			// System.out.println("Cluster HV f: " + cluster.getCentroid().getHashValue(0));
			// System.out.println("Cluster HV g: " + cluster.getCentroid().getHashValue(1));
			for(Column col : data.getCols()){	
				// System.out.println("Col HV f: " + unprCol.getHashValue(0));
				// System.out.println("Col HV g: " + unprCol.getHashValue(1));
				if(!processedCols.contains(col) && COMBINING_STRATEGY.equals("OR")){
					if(cluster.isCandidatePairOR(bktResolvers, col)){
						// process this column
						cluster.addPoint(col);
						processedCols.add(col);
					}
				}else if(!processedCols.contains(col) && COMBINING_STRATEGY.equals("AND")){
					if(cluster.isCandidatePairAND(bktResolvers, col)){
						// just add it to this cluster
						cluster.addPoint(col);
						processedCols.add(col);
					}
				}
			}			
		}
		
		System.out.println("Points assigned based on hashing: " + processedCols.size());
		System.out.println("Remaining points to assign: " + (data.getCols().size() - processedCols.size()));
		
		// assigning the remaining columns 
		for(Column col : data.getCols()){
			
			if(processedCols.contains(col)) continue;
			
			Cluster newCluster = clusters.get(0);
			int newClusterId = 0;
			double min = Column.euclidianDistance(newCluster.getCentroid(), col);
			for(int i = 1; i < clusters.size(); ++i){
				double dist = Column.euclidianDistance(clusters.get(i).getCentroid(), col);
				if(min > dist){
					min = dist;
					newCluster = clusters.get(i);
					newClusterId = i;
				}
			}
			clusters.get(newClusterId).addPoint(col);
		}
	}

	public KMeans execute(int numOfIterations) throws Exception{
		
		try{
			initializeClusters();	
		}catch(Exception e){
			throw e;
		}

		for(int i = 0; i < numOfIterations; ++i){
			
			System.out.println("ITERATION " + i);
			
			if(i > 0) recomputeCentroids();
			
			reassignPoints();
			
		}
		
		return this;
	}

}
