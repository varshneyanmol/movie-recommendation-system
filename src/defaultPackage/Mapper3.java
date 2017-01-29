package defaultPackage;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class Mapper3 extends Mapper<Text, Text, Text, Text> {
	
	@Override
	public void map(Text key, Text value, Context context) throws IOException, InterruptedException {
		
		String[] movies = key.toString().split(",");
		String similarity = value.toString();
		String movie2SimPair = movies[1] + "," + similarity;
		context.write(new Text(movies[0]), new Text(movie2SimPair));
	}

}
