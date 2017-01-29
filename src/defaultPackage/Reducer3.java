package defaultPackage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class Reducer3 extends Reducer<Text, Text, Text, Text> {

	@Override
	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

		final int MOST_SIMILAR_MOVIES = 150;
		int movie;
		double sim;

		LinkedHashMap<Integer, Double> movieSimMap = new LinkedHashMap<Integer, Double>();
		Iterator<Text> itr = values.iterator();
		while (itr.hasNext()) {
			String[] valueSplit = itr.next().toString().split(",");
			movie = Integer.parseInt(valueSplit[0]);
			sim = Double.parseDouble(valueSplit[1]);
			movieSimMap.put(movie, sim);
		}

		movieSimMap = sortMoviesOnSimilarityBasis(movieSimMap);
		int ctr = 0;
		for (Map.Entry<Integer, Double> entry : movieSimMap.entrySet()) {

			if (ctr < MOST_SIMILAR_MOVIES) {
				String movieString = entry.getKey().toString();
				String simString = entry.getValue().toString();
				Text moviePair = new Text(key.toString() + "," + movieString);

				context.write(moviePair, new Text(simString));

			} else {
				break;
			}

			ctr++;
		}

	}

	private LinkedHashMap<Integer, Double> sortMoviesOnSimilarityBasis(LinkedHashMap<Integer, Double> movieSimMap) {

		List<Integer> mapKeys = new ArrayList<>(movieSimMap.keySet());
		List<Double> mapValues = new ArrayList<>(movieSimMap.values());
		Comparator<Integer> cmpInteger = (Comparator) Collections.reverseOrder();
		Comparator<Double> cmpDouble = (Comparator) Collections.reverseOrder();
		Collections.sort(mapValues, cmpDouble);
		Collections.sort(mapKeys, cmpInteger);

		LinkedHashMap<Integer, Double> sortedMap = new LinkedHashMap<Integer, Double>();

		Iterator<Double> valueIt = mapValues.iterator();
		while (valueIt.hasNext()) {
			double val = valueIt.next();
			Iterator<Integer> keyIt = mapKeys.iterator();

			while (keyIt.hasNext()) {
				int key = keyIt.next();
				double comp1 = movieSimMap.get(key);
				double comp2 = val;

				if (comp1 == comp2) {
					keyIt.remove();
					sortedMap.put(key, val);
					break;
				}
			}
		}
		return sortedMap;

	}

}
