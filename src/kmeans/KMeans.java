package kmeans;

import java.util.ArrayList;
import java.util.Collections;

import lsh.Bucket;
import lsh.BucketArithmetic;
import lsh.Column;
import lsh.Dataset;
import lsh.HashFunction;

public class KMeans {
	
	final static String COMBINING_STRATEGY = "AND";
	
	final static int BUCKET_SIZE = 10;

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
		
	public ArrayList<Cluster> getClusters() {
		return clusters;
	}

	public void setClusters(ArrayList<Cluster> clusters) {
		this.clusters = clusters;
	}
	
	public void initializeClusters() throws Exception{
		
		System.out.println("------------");
		System.out.println("INITIALIZING");
		System.out.println("------------");
		
		ArrayList<Bucket> bucketsF = new ArrayList<Bucket>();
		
		try{
			bucketsF = data.groupByBuckets(bktResolvers.get(1));
			System.out.println("# of buckets: " + bucketsF.size());
			Collections.sort(bucketsF, 
                    (o1, o2) -> (new Integer(o2.getCols().size())).compareTo(o1.getCols().size()));
			
			int limit = NUM_OF_CLUSTERS <= bucketsF.size() ? NUM_OF_CLUSTERS : bucketsF.size();
			
			int count = 0;
			for(Bucket bkt : bucketsF){
				if(bkt.getCols().size() == 0) continue;
				if(count++ == limit) break;
				
				Cluster newCluster = new Cluster(bkt.getCols());
				newCluster.computeCentroid(bktResolvers);
				//newCluster.resetPoints();
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

		ArrayList<Column> unprocessedCols = new ArrayList<Column>();
		double begin,end;

		for(Cluster cluster : clusters){
			cluster.resetPoints(); // centroid stays
		}

		boolean assigned;
		double timeSums = 0;
		int count = 0;
		
		for(Column col : data.getCols()){

			begin = System.nanoTime();
			
			assigned = false;
			for(Cluster cluster : clusters){
				if(COMBINING_STRATEGY.equals("OR")){
					if(cluster.isCandidatePairOR(bktResolvers, col)){
						cluster.addPoint(col);
						assigned = true;
						break;
					}
				}else if(COMBINING_STRATEGY.equals("AND")){
					if(cluster.isCandidatePairAND(bktResolvers, col)){
						cluster.addPoint(col);
						assigned = true;
						break;
					}
				}
			}
			if(assigned == false)
				unprocessedCols.add(col);

			end = System.nanoTime();
			timeSums += end - begin;
			++count;
		}
		
		//end = System.nanoTime();
		System.out.println("Mean-Iteration-Time, 1st loop: " + (timeSums/count));
		System.out.println("Points assigned based on hashing: " + (data.getCols().size() - unprocessedCols.size()));
		System.out.println("Remaining points to assign: " + (unprocessedCols.size()));
		
		// assigning the remaining columns 
		// begin = System.nanoTime();
		count = 0;
		timeSums = 0;
		for(Column col : unprocessedCols){
			
			begin = System.nanoTime();
			
			Cluster newCluster = clusters.get(0);
			//int newClusterId = 0;
			double min = Column.euclidianDistance(newCluster.getCentroid(), col);
			for(Cluster cluster : clusters){
				double dist = Column.euclidianDistance(cluster.getCentroid(), col);
				if(min > dist){
					min = dist;
					newCluster = cluster;
					//newClusterId = i;
				}
			}
			newCluster.addPoint(col);

			end = System.nanoTime();
			timeSums += end-begin;
			++count;
		}
		//end = System.nanoTime();
		System.out.println("Mean-Iteration-Time, 2nd loop: " + ((timeSums)/count));
		
		System.out.println("Assigned points: " + count);		
	}

	public KMeans execute(int numOfIterations) throws Exception{
		
		try{
			initializeClusters();	
		}catch(Exception e){
			throw e;
		}
		
		double before = System.nanoTime();
		
		for(int i = 0; i < numOfIterations; ++i){

			System.out.println("------------");
			System.out.println("ITERATION " + i);
			System.out.println("------------");
			
			if(i > 0) recomputeCentroids();
			
			reassignPoints();
			
		}
		
		double after = System.nanoTime();

		System.out.println("------------");
		double elapsedMillis = (after - before) / 1000000;
		System.out.println("FINISHED; Elapsed milliseconds: " + elapsedMillis);
		System.out.println("------------");
		
		return this;
	}

}
