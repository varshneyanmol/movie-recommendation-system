package defaultPackage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class RecommendMovies {

	public static void main(String[] args) {

		DatabaseHandler dbHandler = new DatabaseHandler();
		dbHandler.connect();

		final int MIN_REVIEWER_ID = dbHandler.minUserID();
		final int MAX_REVIEWER_ID = dbHandler.maxUserID();
		final int MOVIES_TO_RECOMMEND = 20;
		int reviewer_id = 0;

		int ratedMovie;
		double sim = 0.0;
		double numerator = 0.0;
		double denominator = 0.0;
		double predictedRating = 0.0;

		HashMap<Integer, Double> ratedMovies = new HashMap<Integer, Double>();
		LinkedHashMap<Integer, Double> predictedRatings = new LinkedHashMap<Integer, Double>();

		HashMap<Integer, ArrayList<String>> _similarMovies = new HashMap<Integer, ArrayList<String>>();

		Scanner scanner = new Scanner(System.in);
		String movieTitle;

		do {
			System.out.println("Enter user id: ");
			reviewer_id = scanner.nextInt();
			if (reviewer_id < MIN_REVIEWER_ID || reviewer_id > MAX_REVIEWER_ID) {
				System.out.println("Enter user id between " + MIN_REVIEWER_ID + " and " + MAX_REVIEWER_ID);
			} else {
				break;
			}
		} while (true);

		ratedMovies = dbHandler.ratedMovies(reviewer_id);

		_similarMovies = dbHandler.similarMovies(ratedMovies.keySet());
		ArrayList<String> ar = null;
		String[] valueSplit;

		for (int movie2 : _similarMovies.keySet()) {
			ar = _similarMovies.get(movie2);
			for (String value : ar) {

				valueSplit = value.split(",");
				ratedMovie = Integer.parseInt(valueSplit[0]);
				sim = Double.parseDouble(valueSplit[1]);
				numerator += sim * ratedMovies.get(ratedMovie);
				denominator += Math.abs(sim);
			}

			if (numerator != 0.0 && denominator != 0.0) {
				predictedRating = numerator / denominator;
				predictedRatings.put(movie2, predictedRating);
			}
		}

		predictedRatings = sortPredictedRatingsByValue(predictedRatings);
		System.out.println(MOVIES_TO_RECOMMEND + " recommended movies for user_id " + reviewer_id + " are: ");
		System.out.println();
		int ctr = 0;
		for (Map.Entry<Integer, Double> entry : predictedRatings.entrySet()) {
			if (ctr == MOVIES_TO_RECOMMEND) {
				break;
			}
			movieTitle = dbHandler.getMovieTitle(entry.getKey());
			System.out.println(
					ctr + 1 + " id : " + entry.getKey() + " : movie: " + movieTitle + "\trating: " + entry.getValue());
			ctr++;
		}

		dbHandler.close();

	}

	private static LinkedHashMap<Integer, Double> sortPredictedRatingsByValue(
			LinkedHashMap<Integer, Double> predictedRatings) {

		List<Integer> mapKeys = new ArrayList<>(predictedRatings.keySet());
		List<Double> mapValues = new ArrayList<>(predictedRatings.values());
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
				double comp1 = predictedRatings.get(key);
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
