package knn;
import java.util.Arrays;

public class RunnableClass {
	public static void main(String[] args) {
		String path = "K:\\Program\\java\\data\\basketball_stat.csv";
		CSVtoArray ca = new CSVtoArray(path);
		String[][] data = ca.returnArray();
		double [][]X = new double[data.length][data[0].length-1];
		String []y = new String[data.length];
		for(int i = 0; i < data.length; i++) {
			y[i] = data[i][0];
			double[] temp = {Double.parseDouble(data[i][1]), Double.parseDouble(data[i][2])}; 
			X[i] = temp;
		}
		double [][]X_train = new double[(int)(data.length*0.8)][X[0].length];
		double [][]X_test = new double[(int)(data.length*0.2)][X[0].length];
		String []y_train = new String[(int)(data.length*0.8)];
		String []y_test = new String[(int)(data.length*0.2)];
		for(int i = 0; i< (int)(data.length*0.8); i++) {
			X_train[i] = X[i];
			y_train[i] = y[i];
		}
		for(int i = (int)(data.length*0.8); i< data.length; i++) {
			X_test[i - (int)(data.length*0.8)] = X[i];
			
			y_test[i - (int)(data.length*0.8)] = y[i];
		}
		KNearestNeighbor knn = new KNearestNeighbor(17);
		knn.fit(X_train, y_train);
		String[] pred = null;
		try {
			pred =knn.predict(X_test);
		} catch (ModelNotFittedException e) {
			System.out.println("done");
			e.printStackTrace();
		}
		System.out.println(Arrays.toString(pred));
		System.out.println(Arrays.toString(y_test));
		double[] y_tru = new double[y_test.length];
		double[] y_prd = new double[pred.length];
		for(int i = 0; i < y_test.length; i++) {
			if(pred[i].equalsIgnoreCase("SG")) 
				y_prd[i] = 1; 
			else 
				y_prd[i] = 0;

			if(y_test[i].equalsIgnoreCase("SG"))
				y_tru[i] = 1; 
			else 
				y_tru[i] = 0;
		}
		ResultAnalysis ra = new ResultAnalysis(y_prd, y_tru);
		System.out.println(ra.getAccuracy());
	}
}
