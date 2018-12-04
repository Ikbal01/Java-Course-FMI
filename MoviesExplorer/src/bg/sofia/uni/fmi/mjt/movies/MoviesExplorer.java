package bg.sofia.uni.fmi.mjt.movies;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

import bg.sofia.uni.fmi.mjt.movies.model.Actor;
import bg.sofia.uni.fmi.mjt.movies.model.Movie;

public class MoviesExplorer {
    private Collection<Movie> movies = null;
    /**
     * Loads the dataset from the given {@code dataInput} stream.
     */
    public MoviesExplorer(InputStream dataInput) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(dataInput))) {
            movies = reader.lines().map(Movie::createMovie).collect(Collectors.toList());
        } catch (IOException ioExc) {
            ioExc.printStackTrace();
        }
    }

    /**
     * Returns all the movies loaded from the dataset.
     **/
    public Collection<Movie> getMovies() {
        return movies;
    }

    public int countMoviesReleasedIn(int year) {
        return (int)movies.stream()
                .filter(movie -> movie.getYear() == year)
                .count();
    }

    public Movie findFirstMovieWithTitle(String title) {
        return movies.stream()
                .filter(movie -> movie.getTitle().contains(title))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public Collection<Actor> getAllActors() {
        return null;
    }

    public int getFirstYear() {
        return movies.stream()
                .mapToInt(Movie::getYear)
                .min()
                .getAsInt();
    }

    public Collection<Movie> getAllMoviesBy(Actor actor) {
        return movies.stream()
                .filter(movie -> movie.getActors().contains(actor))
                .collect(Collectors.toList());
    }

    public Collection<Movie> getMoviesSortedByReleaseYear() {
        return movies.stream()
                .sorted(Comparator.comparing(Movie::getYear))
                .collect(Collectors.toList());
    }

    public int findYearWithLeastNumberOfReleasedMovies() {
        Map<Integer, Long> result =
                movies.stream()
                        .collect(Collectors.groupingBy(Movie::getYear, Collectors.counting()));

        return result.entrySet().stream()
                .min(Comparator.comparing(Map.Entry::getValue))
                .get().getKey();
    }

    public Movie findMovieWithGreatestNumberOfActors() {
        int maxActorsCount = movies.stream()
                .mapToInt(movie -> movie.getActors().size())
                .max()
                .orElse(-1);

        return movies.stream()
                .filter(movie -> movie.getActors().size() == maxActorsCount)
                .findAny()
                .get();
    }
}
