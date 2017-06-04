package kmeans;

import java.util.ArrayList;
import java.util.List;

import lsh.BucketArithmetic;
import lsh.Column;
import lsh.Element;

public class Cluster {

	List<Column> points;
	
	private Column centroid;
	
	final static boolean DEBUG = false;
	
	public Cluster(){
		points = new ArrayList<Column>();
		centroid = new Column();
	}
	
	public Cluster(List<Column> points){
		setPoints(points);
	}

	public List<Column> getPoints() {			
		return points;
	}
	
	public void setPoints(List<Column> points) {
		this.points = points;
		assignToThisCluster(points);
	}
	
	public void setCentroid(Column col){
		this.centroid = col;
	}
	
	public Column getCentroid(){
		return this.centroid;
	}
	
	public void assignToThisCluster(List<Column> points){
		for(Column col : points)
			col.setAssignedCluster(this);
	}
	
	public void addPoint(Column point){
		// remove from the former cluster
		//if(point.getAssignedCluster() != null && point.getAssignedCluster().getPoints().contains(point))
			//point.getAssignedCluster().removePoint(point);
		
		// assign to this cluster
		point.setAssignedCluster(this);
		
		// finally add to this cluster
		//System.out.println("adding");
		points.add(point);
	}
	
	public boolean removePoint(Column point){
		//System.out.println("removing point");
		return points.remove(point);
	}
	
	public void computeCentroid(ArrayList<BucketArithmetic> resolvers){
		
		if(DEBUG) System.out.println("Computing the centroid (" + points.size() + " points)");
		
		if(points.size() == 0){
			// System.out.println("Returning, no points");
			return;
		}
		
		ArrayList<Element> newElements = new ArrayList<Element>();
		for(int i = 0; i < points.get(0).getSize(); ++i)
			newElements.add(new Element(0));
		
		// compute the sums
		for(int i = 0; i < points.get(0).getSize(); ++i)
			for(int j = 0; j < points.size(); ++j)
				newElements.get(i).setValue( newElements.get(i).getValue() + points.get(j).getElement(i).getValue() );

		// divide by number of points to get the mean
		for(int i = 0; i < points.get(0).getSize(); ++i)
			newElements.get(i).setValue( newElements.get(i).getValue() / points.size() );
		
		if(centroid != null){
			for(int i = 0; i < newElements.size(); ++i)
				centroid.getElement(i).setValue(newElements.get(i).getValue());
		}else{
			Column newCentroid = new Column(newElements);
			setCentroid(newCentroid);	
		}
		
		centroid.hashValues(resolvers);
	}

	public boolean isCandidatePairOR(ArrayList<BucketArithmetic> resolvers, Column col){
			
		for(int i = 0; i < resolvers.size(); ++i)
			if(col.getHashBucket(i) == centroid.getHashBucket(i))
				return true;
		
		return false;		
	}

	public boolean isCandidatePairAND(ArrayList<BucketArithmetic> resolvers, Column col){
		
		for(int i = 0; i < resolvers.size(); ++i)
			if(col.getHashBucket(i) != centroid.getHashBucket(i))
				return false;
		
		return true;		
	}
	
	public void resetPoints(){
		points = new ArrayList<Column>();
	}
}
