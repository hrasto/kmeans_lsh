package lsh;

public class Permutator {
	
	private int N;
	
	private int p;
	
	public Permutator(int N){
		setN(N);
		
		// straight finding the prime number, so that
		// it does not have to be found in every iteration
		setP(Permutation.primeNumberBiggerThan(N));
	}

	public int getN() {
		return N;
	}

	public void setN(int n) {
		N = n;
	}

	public void setP(int p){
		this.p = p;
	}
	
	public Permutation next(){
		return new Permutation(N, p);
	}
}
