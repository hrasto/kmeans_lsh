import lsh.Column;
import lsh.Dataset;

public class Main {

	public Main() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Dataset csvtest = new Dataset(Column.csv2columns("nmi_sample.csv", ","));
		csvtest.print();
		
		/*
		Dataset ds = new Dataset();
		ds.print();
		
		Dataset dsPerm = ds.permute();
		dsPerm.print();
		*/
	}

}
