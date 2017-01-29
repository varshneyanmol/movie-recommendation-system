package defaultPackage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class Mapper2 extends Mapper<Text, Text, Text, Text>{
	
	@Override
	public void map(Text key, Text value, Context context) throws IOException, InterruptedException {
		//System.out.println("in mapper 2");
		String oneReviewerMovieRatingsCombined = value.toString();
		String[] movieRatingsPairs = oneReviewerMovieRatingsCombined.split(" ");
		
		String[] movieRatingPair;
		
		
		Map<String, String> movieRatingMap = new HashMap<String, String>();
		for (int i = 0; i < movieRatingsPairs.length; i++) {
			movieRatingPair = movieRatingsPairs[i].split(",");
			movieRatingMap.put(movieRatingPair[0], movieRatingPair[1]);
		}
		
		Set<String> movie_ids = movieRatingMap.keySet();
		Text moviePair = new Text();
		Text ratingPair = new Text();
		
		for (String movie_id1 : movie_ids) {
			for (String movie_id2 : movie_ids) {
		
				if (Integer.parseInt(movie_id1) < Integer.parseInt(movie_id2)) {
				
					moviePair.set(movie_id1 + "," + movie_id2);
					ratingPair.set(movieRatingMap.get(movie_id1) + "," + movieRatingMap.get(movie_id2));
					//System.out.println(moviePair.toString() + "  " + ratingPair.toString());
					context.write(moviePair, ratingPair);
				}
			}
		}
		
	}
}
