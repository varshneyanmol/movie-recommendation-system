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
		job1.setJobName("movie similarity calculator Job_1");

		Path inputPath = new Path("hdfs://localhost:54310/user/hduser/MRSinput/");
		Path outputPath = new Path("hdfs://localhost:54310/user/hduser/MRSMBoutputJob1/");
		FileInputFormat.addInputPath(job1, inputPath);
		FileOutputFormat.setOutputPath(job1, outputPath);

		job1.setMapperClass(Mapper1.class);
		job1.setReducerClass(Reducer1.class);
		job1.setOutputKeyClass(IntWritable.class);
		job1.setOutputValueClass(Text.class);

		job1.waitForCompletion(true);
		

		Job job2 = Job.getInstance(conf);

		job2.setJarByClass(MRSDriver.class);
		job2.setJobName("movie similarity calculator Job_2");

		Path inputPath2 = new Path("hdfs://localhost:54310/user/hduser/MRSMBoutputJob1/");
		Path outputPath2 = new Path("hdfs://localhost:54310/user/hduser/MRSMBoutputJob2/");
		FileInputFormat.addInputPath(job2, inputPath2);
		FileOutputFormat.setOutputPath(job2, outputPath2);

		job2.setInputFormatClass(KeyValueTextInputFormat.class);

		job2.setMapperClass(Mapper2.class);
		job2.setReducerClass(Reducer2.class);

		job2.setOutputKeyClass(Text.class);
		job2.setOutputValueClass(Text.class);

		job2.waitForCompletion(true);
		
		
		Configuration conf_job3 = new Configuration();
		conf_job3.set("mapreduce.output.textoutputformat.separator", ",");
		Job job3 = Job.getInstance(conf_job3);

		job3.setJarByClass(MRSDriver.class);
		job3.setJobName("movie similarity calculator Job_3");

		Path inputPath3 = new Path("hdfs://localhost:54310/user/hduser/MRSMBoutputJob2/");
		Path outputPath3 = new Path("hdfs://localhost:54310/user/hduser/MRSMBoutputJob3/");
		FileInputFormat.addInputPath(job3, inputPath3);
		FileOutputFormat.setOutputPath(job3, outputPath3);

		job3.setInputFormatClass(KeyValueTextInputFormat.class);
		
		job3.setMapperClass(Mapper3.class);
		job3.setReducerClass(Reducer3.class);
		job3.setOutputKeyClass(Text.class);
		job3.setOutputValueClass(Text.class);

		System.exit(job3.waitForCompletion(true) ? 0 : 1);

	}
}
