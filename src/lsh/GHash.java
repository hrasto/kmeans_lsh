package lsh;

import java.util.ArrayList;

public class GHash implements HashFunction {
	
	private ArrayList<Double> coefficients;

	public GHash() {
		coefficients = new ArrayList<Double>();
		coefficients.add(0.25169);
		coefficients.add(0.90415);
		coefficients.add(0.62083);
		coefficients.add(0.72157);
		coefficients.add(0.18980);
		coefficients.add(0.56721);
		coefficients.add(0.48969);
		coefficients.add(0.55342);
		coefficients.add(0.37218);
		coefficients.add(0.56078);
	}

	@Override
	public double apply(ArrayList<Element> elements) {
		if(elements.size() > coefficients.size())
			throw new IllegalArgumentException();
		
		double sum = 0;
		
		for(int i = 0; i < elements.size(); ++i)
			sum += coefficients.get(i) * elements.get(i).getValue(); 
		
		return sum;
	}

}
