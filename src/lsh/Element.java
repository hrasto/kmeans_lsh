package lsh;

public class Element {

	private int value;
	
	private boolean infinity;
	
	public Element() {
		this.infinity = true;
	}

	public Element(int value){
		setValue(value);
		setInfinity(false);
	}
	
	public int getValue() {
		if(isInfinity())
			throw new NullPointerException();
		
		return value;
	}

	public void setValue(int value) {
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
