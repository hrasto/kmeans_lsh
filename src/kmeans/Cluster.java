package kmeans;

import java.util.ArrayList;
import java.util.List;

import lsh.Column;
import lsh.Element;

public class Cluster {

	List<Column> points;
	
	Column centroid;
	
	public Cluster(){
		points = new ArrayList<Column>();
		centroid = new Column();
	}

	public List<Column> getPoints() {			
		return points;
	}
	
	public void setPoints(List<Column> points) {
		this.points = points;
	}
	
	public Cluster(List<Column> points){
		this.points = points;
	}
	
	public boolean addPoint(Column point){
		return points.add(point);
	}
	
	public void computeCentroid(){
		ArrayList<Element> newElements = new ArrayList<Element>();
		for(int i = 0; i < points.get(0).getSize(); ++i)
			newElements.add(new Element(0));
		
		// compute the sums
		for(Column point : points)
			for(int i = 0; i < point.getSize(); ++i)
				newElements.get(i).setValue( newElements.get(i).getValue() + point.getElement(i).getValue() );

		// divide by number of points to get the mean
		for(Column point : points)
			for(int i = 0; i < point.getSize(); ++i)
				newElements.get(i).setValue( newElements.get(i).getValue() / points.size() );
		
		centroid.setElements(newElements);
		centroid.setHashFunctions(points.get(0).getHashFunctions());
	}
	
}
