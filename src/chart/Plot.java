package chart;

import java.io.File;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class Plot {
		
	public static void barChart(ArrayList<String> xData, ArrayList<Integer> yData, String title, String xLabel, String yLabel, int width, int height){
		
		if(xData.size() != yData.size())
			throw new IllegalArgumentException("Non-conformant arrays inserted");
		
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
		 
		for(int i = 0; i < yData.size(); ++i)
			dataset.addValue(yData.get(i), "Hashed value", xData.get(i));
		
		JFreeChart barChart = ChartFactory.createBarChart(
		   title, 
		   xLabel, 
		   yLabel, 
		   dataset,PlotOrientation.VERTICAL, 
		   true, true, false);
		
		saveChartAs(barChart, title, width, height);
	}
	
	public static void saveChartAs(JFreeChart chart, String filename, int width, int height){
		File BarChart = new File(filename + ".jpeg");
		
		try{
		    ChartUtilities.saveChartAsJPEG( BarChart , chart , width , height );  
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
