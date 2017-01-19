package defaultPackage;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class Reducer1 extends Reducer<IntWritable, Text, IntWritable, Text>{
	@Override
	public void reduce(IntWritable key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		StringBuilder oneReviewerMovieRatingsCombined = new StringBuilder();
		
		Iterator<Text> itr = values.iterator();
		while (itr.hasNext()) {
			oneReviewerMovieRatingsCombined.append(itr.next().toString() + " ");
		}
		
		context.write(key, new Text(oneReviewerMovieRatingsCombined.toString().trim()));
	}
}
