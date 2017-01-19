package defaultPackage;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
//import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class MRSDriver {
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
		Job job1 = Job.getInstance(conf);

		job1.setJarByClass(MRSDriver.class);
		job1.setJobName("movie similarity calculator Job1");

		Path inputPath = new Path("hdfs://localhost:54310/user/hduser/MRSinput/");
		Path outputPath = new Path("hdfs://localhost:54310/user/hduser/MRSoutputJob1/");
		FileInputFormat.addInputPath(job1, inputPath);
		FileOutputFormat.setOutputPath(job1, outputPath);

		job1.setMapperClass(Mapper1.class);
		job1.setReducerClass(Reducer1.class);
		job1.setOutputKeyClass(IntWritable.class);
		job1.setOutputValueClass(Text.class);

		job1.waitForCompletion(true);
		

		Job job2 = Job.getInstance(conf);

		job2.setJarByClass(MRSDriver.class);
		job2.setJobName("movie similarity calculator Job2");

		Path inputPath2 = new Path("hdfs://localhost:54310/user/hduser/MRSoutputJob1/");
		Path outputPath2 = new Path("hdfs://localhost:54310/user/hduser/MRSoutputFinal/");
		FileInputFormat.addInputPath(job2, inputPath2);
		FileOutputFormat.setOutputPath(job2, outputPath2);

		job2.setInputFormatClass(KeyValueTextInputFormat.class);

		job2.setMapperClass(Mapper2.class);
		job2.setReducerClass(Reducer2.class);

		job2.setOutputKeyClass(Text.class);
		job2.setOutputValueClass(Text.class);

		System.exit(job2.waitForCompletion(true) ? 0 : 1);

	}
}
