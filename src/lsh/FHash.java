package lsh;

import java.util.ArrayList;

public class FHash implements HashFunction{
	
	private ArrayList<Double> coefficients;
	
	public FHash() {
		coefficients = new ArrayList<Double>();
		coefficients.add(0.39);
		coefficients.add(0.21);
		coefficients.add(-0.42);
		coefficients.add(0.09);
		coefficients.add(-0.31);
		coefficients.add(0.05);
		coefficients.add(0.13);
		coefficients.add(-0.25);
		coefficients.add(0.67);
		coefficients.add(-0.46);
	}
	
	public double apply(ArrayList<Element> elements){
		if(elements.size() > coefficients.size())
			throw new IllegalArgumentException();
		
		double sum = 0;
		
		for(int i = 0; i < elements.size(); ++i)
			sum += coefficients.get(i) * elements.get(i).getValue(); 
		
		return sum;
	}
	
}
