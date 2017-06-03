package lsh;

import java.util.ArrayList;

public class GHash implements HashFunction {
	
	private ArrayList<Double> coefficients;
	
	public GHash() {
		coefficients = new ArrayList<Double>();
		coefficients.add(0.41);
		coefficients.add(-0.12);
		coefficients.add(-0.22);
		coefficients.add(0.10);
		coefficients.add(-0.51);
		coefficients.add(0.28);
		coefficients.add(0.35);
		coefficients.add(0.09);
		coefficients.add(-0.37);
		coefficients.add(-0.15);
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
