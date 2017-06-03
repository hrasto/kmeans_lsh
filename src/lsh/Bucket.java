package lsh;

import java.util.ArrayList;

public class Bucket {
	
	private ArrayList<Column> cols;
	
	private Double min;

	private Double max;
	
	public Bucket(Double min, Double max){		
		this.cols = new ArrayList<Column>();
		setMin(min);
		setMax(max);
	}
	
	public void addColumn(Column col){
		this.cols.add(col);
	}
	
	public int getSize(){
		return this.cols.size();
	}
	
	public double getSpan(){
		return max - min;
	}

	public ArrayList<Column> getCols() {
		return cols;
	}

	public void setCols(ArrayList<Column> cols) {
		this.cols = cols;
	}

	public Double getMin() {
		return min;
	}

	public void setMin(double min) {
		
		try{
			if(min > max)
				throw new IllegalArgumentException("Bucket min-border must be less than max-border");
		}catch(NullPointerException e) {}
		
		this.min = min;
	}

	public Double getMax() {
		return max;
	}

	public void setMax(double max) {

		try{
			if(max < min)
				throw new IllegalArgumentException("Bucket max-border must be greater than min-border");
		}catch(NullPointerException e) {}
		
		this.max = max;
	}
	
	
}
