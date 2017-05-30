package lsh;

import java.util.Random;

public class Permutation {
	
	final boolean DEBUG = true;

	private int N;
	
	private int a;
	
	private int b;
	
	private int p;
	
	public Permutation(int N, int p){
		Random rn = new Random();
		a = rn.nextInt(N);
		b = rn.nextInt(N);
		this.p = p;
		this.N = N;
		
		if(DEBUG)
			System.out.println("a = " + Integer.toString(a) + 
					"; b = " + Integer.toString(b) + 
					"; p = " + Integer.toString(p) + 
					"; N = " + Integer.toString(N));
	}
	
	public int apply(int x){
		// h(x)=((a*x+b) mod p) mod N
		int hashValue = ((a*x+b) % p) % N;
		
		if(DEBUG)
			System.out.println("Applying: h(" + x + ") = ((" + 
				a + "*" + x + "+" + b + ") % " + p + ") % " + N + " = " + hashValue);
		
		return hashValue;
	}
	
	public static int primeNumberBiggerThan(int border){
		int i = 2;
		int primeNum = 0;
		
		while(primeNum <= border){
			if(isPrime(i))
				primeNum = i;
			++i;
		}
		
		System.out.println("Prime number bigger than " + 
				Integer.toString(border) + ": " + Integer.toString(primeNum));
		return primeNum;
	}
	
	public static boolean isPrime(int n) {
	    //check if n is a multiple of 2
	    if (n%2==0) return false;
	    //if not, then just check the odds
	    for(int i=3;i*i<=n;i+=2) {
	        if(n%i==0)
	            return false;
	    }
	    return true;
	}
}
