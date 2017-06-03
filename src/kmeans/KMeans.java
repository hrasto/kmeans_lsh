package kmeans;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lsh.Dataset;

public class KMeans {
	
	private int k;

	private List<Cluster> clusters;
	
	private Dataset data;
	
	public KMeans(int k) {
		setK(k);
	}
	
	public int getK(){
		return k;
	}
	
	public void setK(int k){
		this.k = k;
	}

	public Dataset getData(){
		return data;
	}
	
	public KMeans setData(Dataset ds){
		this.data = ds;
		
		return this;
	}
	
	public List<Cluster> getClusters() {
		return clusters;
	}

	public void setClusters(List<Cluster> clusters) {
		this.clusters = clusters;
	}
	
	public void initializeCentroids() {
		
	}

	public void recomputeCentroids() {
		
	}

	public void reassignPoints() {
		
	}

	public List<Cluster> execute(){
		
		initializeCentroids();
		
		for(int i = 0; i < k; ++i){
			
			if(i > 0) recomputeCentroids();
			
			reassignPoints();
			
		}
		
		return clusters;
	}

}
