package defaultPackage;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class Reducer2 extends Reducer<Text, Text, Text, Text> {

	@Override
	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		final int MIN_COMMON_REVIEWER = 5;
		double similarityScore = 0.0;
		String[] X_Y;
		double n = 0.0; // 'n' is the number of reviewers who have rated both
						// the movies

		double sigmaX = 0.0;
		double sigmaY = 0.0;
		double sigmaXY = 0.0;
		double sigmaXsqr = 0.0;
		double sigmaYsqr = 0.0;

		double X = 0.0;
		double Y = 0.0;

		Iterator<Text> itr = values.iterator();
		while (itr.hasNext()) {
			n += 1.0;
			X_Y = itr.next().toString().split(",");

			X = Double.parseDouble(X_Y[0]);
			Y = Double.parseDouble(X_Y[1]);

			sigmaX += X;
			sigmaY += Y;
			sigmaXY += X * Y;
			sigmaXsqr += X * X;
			sigmaYsqr += Y * Y;
		}

		if (n < MIN_COMMON_REVIEWER) { // will not consider writing that pair of
										// movies which has less than
										// MAX_COMMON_REVIEWER common reviewer
										// among them.
			similarityScore = 0.0;

		} else {

			double numerator = (n * sigmaXY) - (sigmaX * sigmaY);
			double denominator = Math.sqrt((n * sigmaXsqr - (sigmaX * sigmaX)) * (n * sigmaYsqr - (sigmaY * sigmaY)));
			similarityScore = numerator / denominator;
			if (!Double.isNaN(similarityScore)) {
				// writing similariy between movie pair (a, b).
				context.write(key, new Text(String.valueOf(similarityScore)));

				// writing similarity between movie pair(b, a) which is same as
				// of between (a, b)
				String[] keySplit = key.toString().split(",");
				Text reverseKey = new Text(keySplit[1] + "," + keySplit[0]);
				context.write(reverseKey, new Text(String.valueOf(similarityScore)));
			}
		}

		// System.out.println("in reducer 2");
		// System.out.println(key.toString() + " : " + similarityScore + " n = "
		// + n);

	}
}
