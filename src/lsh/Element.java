package lsh;

public class Element {

	private double value;
	
	private boolean infinity;
	
	public Element() {
		this.infinity = true;
	}

	public Element(int value){
		setValue(value);
		setInfinity(false);
	}

	public Element(Object value){
		setValue(new Double(value.toString()));
		setInfinity(false);
	}

	public Element(double value){
		setValue(value);
		setInfinity(false);
	}
	
	public double getValue() {
		if(isInfinity())
			throw new NullPointerException();
		
		return value;
	}

	public void setValue(double value) {
		if(isInfinity())
			setInfinity(false);
		
		this.value = value;
	}

	public boolean isInfinity() {
		return infinity;
	}

	public void setInfinity(boolean infinity) {
		this.infinity = infinity;
	}
	
	public double hash(){
		double res = value;
		
		return res;
	}

}
