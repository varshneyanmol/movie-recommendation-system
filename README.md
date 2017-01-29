# movie-recommendation-system-based-on-item-based-collaborative-filterting-using-mapreduce

Download the dataset from grouplens website.
I have worked upon the dataset 'ml-latest-small' which is a zip file of around 1MB.
The project should work fine with large dataset 'ml-latest' which is zip file of around 235MB.
After extracting the 'ml-latest-small.zip' you should get the following files:
links.csv
movies.csv
ratings.csv
tags.csv
readme.txt

The source code is written in java and uses JDBC to connect with database. The project uses mysql database and sqoop tool (to export hdfs data to mysql) along with mapreduce.
==============================================================================================================

Follow the below steps to run the application:

--------------------------------------------------------------------------------------------------------------
1: Copy the file ratings.csv to the HDFS directory. This file is input to the mapreduce job_1.
You can provide any directory structure but accordingly modify the 'inputPath' variable of mapreduce job_1 in MRSDriver.java file.
I have put the file under: "hdfs://localhost:54310/user/hduser/MRSinput/"

--------------------------------------------------------------------------------------------------------------
2: Run "MRSDriver.java" which in turn runs three mapreduce jobs sequentially and produces its final output in 'MRSMBoutputJob3' folder.
The output of mapreduce job_2 contains the similarity between two movies in the format:

"movie1_id,movie2_id" "similarity score"

key and value are written as Text(String) with tab(\t) as key-value seperator.

Work of mapreduce job_3 is to filer out the output of job_2 and keep at most '150'(declared under variable 'MOST_SIMILAR_MOVIES' in Reducer3.java) most-similar-movies to each movie.
It writes the output in the format:

"movie1,movie2","similarity score"

key and value are writtien as Text(String) with comma(,) as key-value seperator. This is done so that this file could be exported to mysql database through sqoop.

--------------------------------------------------------------------------------------------------------------
3: Create a database with the name 'mrs_db' in mysql and create three tables under that database:

a)	CREATE TABLE movie_sim (movie1 INT, movie2 INT, similarity DOUBLE);
b)	CREATE TABLE movie (movie_id INT, movie_name VARCHAR(200), genre(80));
c)	CREATE TABLE user_movie_rating (user_id INT, movie_id INT, rating DOUBLE);

Export the output of mapreduce job_3 in the table 'movie_sim' through sqoop command:

sqoop export --connect jdbc:mysql://localhost:3306/mrs_db \
--username give-mysql-username-here --password give-password-here \
--table movie_sim \
--export-dir /hdfs-path-here/MRSMBoutputJob3/part-r-00000 \
-m 1

Export the file ratings.csv (from the grouplens data) to the table 'user_movie_rating' through sqoop.
Export the file movies.csv (from the grouplens data) to the table 'movie' through sqoop.

--------------------------------------------------------------------------------------------------------------
4: In connect() function of DatabaseHandler.java, modify 'user_name' and 'password' varaibles accordingly to allow jdbc to connect to mysql daatbase.

--------------------------------------------------------------------------------------------------------------
5: Finally run 'RecommendMovies.java' and enter any valid user_id (which, for 'ml-latest--small' dataset, is between 1 and 671).
If everything is setup well, it should print 20 recommended movies on the IDE console.

==============================================================================================================

