package defaultPackage;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class Mapper1 extends Mapper<LongWritable, Text, IntWritable, Text>{
	
	@Override
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		
		String ratingRecord = value.toString();
		String ratingRecordSplit[] = ratingRecord.split(",");
		
		int reviewer_id = Integer.parseInt(ratingRecordSplit[0]);
		String movieRatingPair = ratingRecordSplit[1] + "," + ratingRecordSplit[2];
		
		context.write(new IntWritable(reviewer_id), new Text(movieRatingPair));
	}	
}
