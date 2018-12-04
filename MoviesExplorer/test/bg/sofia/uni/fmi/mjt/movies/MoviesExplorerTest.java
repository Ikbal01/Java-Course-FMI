package bg.sofia.uni.fmi.mjt.movies;

import bg.sofia.uni.fmi.mjt.movies.model.Movie;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertEquals;

public class MoviesExplorerTest {
    private static final int MOVIES_RELEASED_IN_1970 = 3;
    private static final int YEAR_1970 = 1970;
    private static final int YEAR_1967 = 1967;

    private ByteArrayInputStream input;
    private String films;
    private MoviesExplorer moviesExplorer;

    @Before
    public void setup() {
        films = "...4 ...3 ...2 ...1 ...morte (1967)/Braun, Pinkas/Dávila, Luis/Jeffries, Lang/Karlsen\n" +
                "...All the Marbles (1980)/Aames, Lang/Karlsen, Angela/Aguilar\n" +
                "*batteries not included (1980)/Aldredge, Tom/Arceri\n" +
                "$1,000,000 Duck (1970)/Andrews, Edward/Bender, Jack/Bowles\n" +
                "...And Millions Die! (1970)/Allen, Jack/Assang, George/Basehart\n" +
                "...Almost (1970)/Adele, Jan/Arquette, Rosanna/Hagan";

        input = new ByteArrayInputStream(films.getBytes());
        moviesExplorer = new MoviesExplorer(input);
    }

    @Test
    public void testCountMoviesReleasedInGivenYear() {
        int countMoviesReleasedIn = moviesExplorer.countMoviesReleasedIn(YEAR_1970);

        assertEquals("Movies released in 1970 must be 3", MOVIES_RELEASED_IN_1970, countMoviesReleasedIn);
    }

    @Test
    public void testFindFirstMovieWithTitle() {
        Movie movie = Movie.createMovie("...All the Marbles (1980)/Aames, Lang/Karlsen, Angela/Aguilar");

        String title = "Marbles";

        assertEquals(movie, moviesExplorer.findFirstMovieWithTitle(title));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindFirstMovieWithTitleException() {
        String title = "non-existent";

        moviesExplorer.findFirstMovieWithTitle(title);
    }

    @Test
    public void testGetFirstYear() {
        int earliestYear = YEAR_1967;

        assertEquals(earliestYear, moviesExplorer.getFirstYear());
    }

    @Test
    public void testFindYearWithLeastNumberOfReleasedMovies() {
        int year = YEAR_1967;

        assertEquals(year, moviesExplorer.findYearWithLeastNumberOfReleasedMovies());
    }

    @Test
    public void testFindMovieWithGreatestNumberOfActors() {
        Movie movie = Movie.createMovie("...4 ...3 ...2 ...1 ...morte" +
                " (1967)/Braun, Pinkas/Dávila, Luis/Jeffries, Lang/Karlsen");

        assertEquals(movie, moviesExplorer.findMovieWithGreatestNumberOfActors());
    }

}
