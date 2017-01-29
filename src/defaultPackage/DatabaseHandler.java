package defaultPackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class DatabaseHandler {
	private Connection con = null;
	private Statement st = null;
	private ResultSet rs = null;
	private PreparedStatement pst = null;

	public void connect() {
		String db_url = "jdbc:mysql://localhost:3306/mrs_db";
		String user_name = "root";
		String password = "anaconda";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			this.con = DriverManager.getConnection(db_url, user_name, password);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void close() {

		try {
			if (rs != null)
				rs.close();

			if (st != null)
				st.close();
			
			if (pst != null) {
				pst.close();
			}

			if (con != null || !con.isClosed())
				con.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public HashMap<Integer, Double> ratedMovies(int reviewer_id) {

		HashMap<Integer, Double> ratedMovies = new HashMap<Integer, Double>();
		String query = "SELECT movie_id,rating FROM user_movie_rating WHERE user_id=" + reviewer_id;

		try {
			st = con.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				ratedMovies.put(rs.getInt("movie_id"), rs.getDouble("rating"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return ratedMovies;
	}

	public int maxUserID() {
		String query = "SELECT MAX(user_id) FROM user_movie_rating";
		int max = 0;

		try {
			st = con.createStatement();
			rs = st.executeQuery(query);
			rs.next();
			max = rs.getInt(1);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return max;
	}

	public int minUserID() {
		String query = "SELECT MIN(user_id) FROM user_movie_rating";
		int min = 0;

		try {
			st = con.createStatement();
			rs = st.executeQuery(query);
			rs.next();
			min = rs.getInt(1);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return min;
	}

	public HashMap<Integer, ArrayList<String>> similarMovies(Set<Integer> ratedMovies) {

		HashMap<Integer, ArrayList<String>> _similarMovies = new HashMap<Integer, ArrayList<String>>();
		String query = "SELECT movie2, similarity FROM movie_sim where movie1 = ?";
		this.prepareStatement(query);
		ResultSet rst = null;
		int movie2;
		String value;
		ArrayList<String> ar = null;

		for (int ratedMovie : ratedMovies) {
			try {
				pst.setInt(1, ratedMovie);
				rst = pst.executeQuery();

				while (rst.next()) {
					value = Integer.toString(ratedMovie) + "," + Double.toString(rst.getDouble(2));
					movie2 = rst.getInt(1);

					if (!_similarMovies.containsKey(movie2)) {
						ar = new ArrayList<String>();
						ar.add(value);
						_similarMovies.put(movie2, ar);

					} else {
						_similarMovies.get(movie2).add(value);
					}

				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return _similarMovies;
	}

	private void prepareStatement(String query) {
		//PreparedStatement ps = null;
		try {
			pst = con.prepareStatement(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//return pst;
	}

	public String getMovieTitle(int movie_id) {

		String query = "SELECT movie_name,genre FROM movie WHERE movie_id=" + movie_id;
		String movie = null;

		try {
			st = con.createStatement();
			rs = st.executeQuery(query);
			if (rs.next()) {
				movie = rs.getString(1) + "\t" + rs.getString(2);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return movie;
	}
}
