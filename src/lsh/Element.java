package lsh;

public class Element {

	private Double value;
	
	private boolean infinity;
	
	public Element() {
		this.infinity = true;
	}
	
	public Element(Double value){
		setValue(value);
		setInfinity(false);
	}

	public Double getValue() {
		if(isInfinity())
			throw new NullPointerException();
		
		return value;
	}

	public void setValue(Double value) {
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

}
