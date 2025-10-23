Task
```
A movie dataset is given with fields movie_name, genre, and trend_score, and there are two use cases:

Find the movie having the highest trend score in a given genre
 Method: getMovieWithMaxTrendScoreByGenre(string genre) : string

Method: updateMovieTrendScore(string movieName, int score): void
		Update the trend score of any movie to a new score

n: Number of movie records m: Number of times method is called.
```

Solution
```java
package MovieTrend;

import java.util.*;

public class MovieTrend {

    private static Map<String, TreeSet<Movie>> movieMapByGenre = new HashMap<>();
    private static Map<String, Movie> movieMapByName = new HashMap<>();

    static class Movie {
        String name;
        String genre;
        int trendScore;

        Movie(String name, String genre, int trendScore) {
            this.name = name;
            this.genre = genre;
            this.trendScore = trendScore;
        }

    }

    // Comparator using lambda: sort by trendScore DESC, then name ASC
    private static final Comparator<Movie> movieComparator = (a, b) -> {
        if (a.trendScore != b.trendScore)
            return Integer.compare(b.trendScore, a.trendScore); // Higher trendScore first
        return a.name.compareTo(b.name); // Lexical order as tie-breaker
    };



    private static void process(List<Movie> list) {
        for (Movie mv : list) {
            movieMapByName.put(mv.name, mv);
            movieMapByGenre.putIfAbsent(mv.genre, new TreeSet<>(movieComparator));
            movieMapByGenre.get(mv.genre).add(mv);
        }
    }

    // Update the trend score of any movie to a new score
    private static void updateMovieTrendScore(String movieName, int score) {
        if (!movieMapByName.containsKey(movieName)) {
            return;
        }

        Movie movie = movieMapByName.get(movieName);
        TreeSet<Movie> set = movieMapByGenre.get(movie.genre);
        set.remove(movie); // Remove old movie instance
        movie.trendScore = score;
        set.add(movie); // Re-insert updated movie
    }

    private static String getMovieWithMaxTrendScoreByGenre(String genre) {
        TreeSet<Movie> set = movieMapByGenre.get(genre);
        return (set == null || set.isEmpty()) ? null : set.first().name;
    }


    public static void main(String[] args) {
        List<Movie> list = Arrays.asList(
                new Movie("m1", "g1", 98),
                new Movie("m2", "g1", 99),
                new Movie("m3", "g2", 95),
                new Movie("m4", "g2", 94),
                new Movie("m5", "g3", 98),
                new Movie("m6", "g3", 98)
        );

        process(list);

        System.out.println("For genre g1: " + "Top movie is "  + getMovieWithMaxTrendScoreByGenre("g1"));
        System.out.println("For genre g2: " + "Top movie is "  + getMovieWithMaxTrendScoreByGenre("g2"));
        System.out.println("For genre g3: " + "Top movie is "  + getMovieWithMaxTrendScoreByGenre("g3"));

        System.out.println("");
        updateMovieTrendScore("m1", 99);
        updateMovieTrendScore("m4", 97);
        updateMovieTrendScore("m6", 99);



        System.out.println("For genre g1: " + "Top movie is "  + getMovieWithMaxTrendScoreByGenre("g1"));
        System.out.println("For genre g2: " + "Top movie is "  + getMovieWithMaxTrendScoreByGenre("g2"));
        System.out.println("For genre g3: " + "Top movie is "  + getMovieWithMaxTrendScoreByGenre("g3"));
    }
}

```
