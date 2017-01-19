package defaultPackage;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class Reducer2 extends Reducer<Text, Text, Text, Text>{
	
	@Override
	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		double similarityScore = 0.0;
		String[] X_Y;
		double n = 0.0;
		
		double sigmaX = 0.0;
		double sigmaY = 0.0;
		double sigmaXY = 0.0;
		double sigmaXsqr = 0.0;
		double sigmaYsqr = 0.0;
		
		double X = 0.0;
		double Y = 0.0;
		
		Iterator<Text> itr = values.iterator();
		while(itr.hasNext()) {
			n += 1.0;
			X_Y = itr.next().toString().split(",");
			
			X = Double.parseDouble(X_Y[0]);
			Y = Double.parseDouble(X_Y[1]);

			sigmaX += X;
			sigmaY += Y;
			sigmaXY += X*Y;
			sigmaXsqr += X*X;
			sigmaYsqr += Y*Y;
		}
		
		if (n == 1) {
			similarityScore = 0.0;
			
		} else {
			
			double numerator = (n * sigmaXY) - (sigmaX * sigmaY);
			double denominator = Math.sqrt((n * sigmaXsqr - (sigmaX*sigmaX)) * (n * sigmaYsqr - (sigmaY*sigmaY)));
			similarityScore = numerator / denominator;
		}
		
		System.out.println("in reducer 2");
		System.out.println(key.toString() + " : " + similarityScore + " n = " + n);
		context.write(key, new Text(String.valueOf(similarityScore)));

	}
}
