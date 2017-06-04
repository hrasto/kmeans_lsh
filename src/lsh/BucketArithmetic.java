package lsh;

public class BucketArithmetic {
	
	private double min;
	
	private int bucketSize;
	
	private HashFunction func;
	
	public BucketArithmetic(double min, int bucketSize, HashFunction func){
		this.min = min;
		this.bucketSize = bucketSize;
		this.func = func;
	}
	
	public int getBucketIndex(double value){
		return (int) ((value - min) / bucketSize);
	}

	public int getBucketIndex(Column point){
		return (int) ((func.apply(point.getElements()) - min) / bucketSize);
	}
	
	public double getMin(){
		return min;
	}
	
	public int getBucketSize(){
		return bucketSize;
	}
	
	public HashFunction getFunc(){
		return func;
	}

}
