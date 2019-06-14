package knn;

class ResultAnalysis {
	private double[]y_pred;
	private double[]y_true;
	private int TP;
	private int TN;
	private int FP;
	private int FN;
	public ResultAnalysis(double[] y_pred, double[]y_true) {
		this.y_pred = y_pred;
		this.y_true = y_true;
		get();
	}
	private void get() {
		for(int i = 0; i < y_pred.length; i++) {
			if(y_true[i] == 1 && y_pred[i] == 1)
				TP += 1;
			else if(y_true[i] == 1 && y_pred[i] == 0)
				FN += 1;
			else if(y_true[i] == 0 && y_pred[i] == 1)
				FP += 1;
			else
				TN += 1;
		}
	}
	public double getAccuracy() {
		return (double)(TP + TN)/(TP+TN+FP+FN);
	}
	public double getRecall() {
		return (double)TP/(TP+FN);
	}
	public double getPrecision() {
		return (double)TP/(TP + FP);
	}
	public double getSpecificity() {
		return (double)TN/(TN + FP);
	}
}
