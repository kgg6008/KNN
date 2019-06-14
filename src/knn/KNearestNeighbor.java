package knn;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class KNearestNeighbor{
	private int k = 3;
	private double[][] X_train;
	private String[] y_train;
	private int y_classifier = 0;		//범주의 개수
	private String[] y_class = null;	//범주 라벨 이름
	private boolean isFit = false;		//fit 여부
	private ArrayList<double[]> distance = new ArrayList<>();		//거리와 index를 저장할 arraylist
	public KNearestNeighbor() {
		System.out.println("Warning: k is automatically initialized to 3.");
	}
	public KNearestNeighbor(int k) {
		if(k%2 == 0) {
			System.out.println("Warning: Odd number to k is better than even number.");
			//k의 경우 보편적으로 홀수를 사용하므로 홀수를 이용하는 것이 좋다는 경고 출력.
		}		
		this.k = k;
	}
	private double d(double[]x, double y[]) {
		//거리를 계산하는 함수.
		if(x.length != y.length) {
			//길이가 다를 경우 경고문 출력 후 강제 종료.
			System.out.println("Warning:\tCan not get distance.\n\tPlease check data column's length.");
			System.exit(-1);
		}
		double sum = 0.0;
		for(int i = 0; i < x.length; i++) {
			sum += Math.pow((x[i] - y[i]), 2);
		}
		return Math.sqrt(sum);
	}
	private void getY_classifier(String []y_train) {
		//범주의 개수와 범주의 이름을 저장하는 함수.
		HashSet<String> classifier = new HashSet<String>();
		for(String str: y_train) {
			classifier.add(str);
		}
		this.y_classifier = classifier.size();//중복이 제거 된 범주의 수.
		String[] temp = new String[this.y_classifier];
		classifier.toArray(temp);
		this.y_class = temp.clone();
		Arrays.sort(this.y_class);		//이름 순으로 정렬 한 범주의 이름.
	}
	private int findIndex(String str) {
		//해당 이름을 가진 범주의 index를 돌려주는 함수.
		int index = 0;
		for(index = 0; index < this.y_classifier; index++) {
			if(str.equalsIgnoreCase(this.y_class[index]))
				break;
		}
		return index;
	}
	private void normalize(double[] prob) {
		//확률을 구해주는 함수. 각 개수를 합으로 나누어 준다.
		double sum = 0.0;
		for(int i = 0; i < prob.length; i++) 
			sum += prob[i];
		for(int i = 0; i < prob.length; i++) 
			prob[i] /= sum;
	}
	public void fit(double[][]X, int[] y) {
		//y가 int형인 경우 모델의 fitting
		String temp = Arrays.toString(y);
		String []sy = temp.substring(1, temp.length()-1).split(", ");
		fit(X, sy);
	}
	public void fit(double[][]X, double[] y) {
		//y가 double형인 경우 모델의 fitting
		String temp = Arrays.toString(y);
		String []sy = temp.substring(1, temp.length()-1).split(", ");
		fit(X, sy);
	}
	public void fit(double[][]X, String[] y) {
		//모델의 fitting(y_train을 String으로 설정)
		this.X_train = X;
		this.y_train = y;
		getY_classifier(y);
		this.isFit = true;
	}
	public double[] getProba(double[] X_test) throws ModelNotFittedException {
		//확률을 구하는 함수
		if(!isFit) {
			//모델의 fitting이 되어있지 않을 경우 Exception 발생
			System.out.println("KNN model is not fitted.\nPlease check it.");
			throw new ModelNotFittedException("KNN model is not fitted.");
		}
		double[] prob = new double[this.y_classifier];
		//모든 X_train 데이터와의 거리를 계산하여 저장한다.
		for(int index = 0; index < this.X_train.length; index++) {
			double distance = d(X_test, this.X_train[index]);
			double[] temp = {distance, index};
			if (this.distance.isEmpty()) {
				//비어있을 경우 바로 추가.
				this.distance.add(temp);
			}
			else {
				//비어있지 않을 경우 distance를 비교하여 추가.
				int tmpIndex = 0; 
				for(double[] array: this.distance) {
					if(distance < array[0])
						break;
					tmpIndex += 1;
				}
				this.distance.add(tmpIndex, temp);
			}
		}
		//가까운 k의 수만큼 가져와서 해당 index에 1씩 더해주어 개수를 센다.
		for(int i = 0; i < this.k; i++) {
			int tmp = (int)this.distance.get(i)[1];
			String tmpClass = this.y_train[tmp];
			prob[findIndex(tmpClass)] += 1;
		}
		normalize(prob);	//개수를 센 prob라는 array를 sum으로 나누어 확률로 변환.
		this.distance.clear();	//거리 data 초기화.
		return prob.clone();
	}
	public double[][] getProba(double[][] X_test) throws ModelNotFittedException{
		//여러가지 데이터의 확률을 저장하는 함수.
		double[][] prob = new double[X_test.length][this.y_classifier];
		for(int i = 0; i < X_test.length; i++) {
			prob[i] = getProba(X_test[i]);
		}
		return prob;
	}
	public String predict(double []X_test) throws ModelNotFittedException {
		//해당 data를 주어주면 예측하는 함수.
		double[] prob = getProba(X_test);
		int index = 0;
		for(int i = 0; i < prob.length; i++) {
			if(prob[i] > prob[index]) {
				index = i;
			}
		}
		return this.y_class[index];
	}
	public String[] predict(double [][]X_test) throws ModelNotFittedException{
		//여러가지 데이터를 모두 예측하는 함수.
		String[] pred = new String[X_test.length];
		for(double[] array: this.distance) {
			System.out.println(Arrays.toString(array));
		}
		
		for(int i = 0; i < X_test.length; i++) {
			pred[i] = predict(X_test[i]);
		}
		return pred;
	}
}
