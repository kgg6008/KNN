package knn;

public class ModelNotFittedException extends Exception{
	public ModelNotFittedException() {
		super();
	}
	public ModelNotFittedException(String msg) {
		super(msg);
	}
}
