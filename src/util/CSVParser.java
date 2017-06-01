package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class CSVParser {
	public static ArrayList<ArrayList<Object>> read(String path, String delimiter) {
		BufferedReader br = null;
		String line = "";
		try {
			br = new BufferedReader(new FileReader(path));
			// Object[][] result = new Object[lines][];
			ArrayList<ArrayList<Object>> result = new ArrayList<ArrayList<Object>>();

			while ((line = br.readLine()) != null) {
				// use comma as separator
				ArrayList<Object> values = new ArrayList<Object>();
				values.addAll(Arrays.asList(line.split(delimiter)));
				result.add(values);
			}

			return result;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return new ArrayList<ArrayList<Object>>();
	}

	public static void write(ArrayList<ArrayList<Object>> data, String path, String delimiter) {
		try {
			PrintWriter writer = new PrintWriter(path, "UTF-8");
			for (ArrayList<Object> ls : data) {
				String line = "";
				int i = 0;
				for (Object o : ls) {
					if (i > 0)
						line += ",";
					line += o.toString();
					++i;
				}
				writer.println(line);
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
