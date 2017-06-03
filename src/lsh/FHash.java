package lsh;

import java.util.ArrayList;

public class FHash implements HashFunction{
	
	private ArrayList<Double> coefficients;
	
	public FHash() {
		coefficients = new ArrayList<Double>();
		/*
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
		*/
		coefficients.add(0.107048);
		coefficients.add(0.730205);
		coefficients.add(0.592126);
		coefficients.add(0.047635);
		coefficients.add(0.078536);
		coefficients.add(0.524925);
		coefficients.add(0.968359);
		coefficients.add(0.887374);
		coefficients.add(0.123474);
		coefficients.add(0.848133);
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
