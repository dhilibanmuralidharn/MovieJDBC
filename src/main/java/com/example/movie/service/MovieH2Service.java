package com.example.movie.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

import com.example.movie.model.Movie;
import com.example.movie.repository.MovieRepository;
import com.example.movie.model.MovieRowMapper;

@Service
public class MovieH2Service implements MovieRepository {

    @Autowired
    private JdbcTemplate db;

    @Override
    public ArrayList<Movie> getMovies() {
        List<Movie> mList = db.query("SELECT * FROM MOVIELIST ", new MovieRowMapper());
        ArrayList<Movie> moiveList = new ArrayList<>(mList);
        return moiveList;
    }

    @Override
    public Movie getMovieById(int movieId) {
        try {
            Movie movie = db.queryForObject("SELECT * FROM MOVIELIST WHERE movieId = ?", new MovieRowMapper(), movieId);
            return movie;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Movie addMovie(Movie movie) {
        db.update("INSERT INTO MOVIELIST (movieName, leadActor) values(?, ?)", movie.getMovieName(),
                movie.getLeadActor());
        Movie savedMovie = db.queryForObject("SELECT * FROM MOVIELIST WHERE movieName = ? and leadActor = ?",
                new MovieRowMapper(), movie.getMovieName(), movie.getLeadActor());
        return savedMovie;
    }

    @Override
    public Movie updateMovie(int movieId, Movie movie) {
        if (movie.getMovieName() != null) {
            db.update("Update MOVIELIST SET movieName = ? WHERE movieId = ?", movie.getMovieName(), movieId);
        }
        if (movie.getLeadActor() != null) {
            db.update("Update MOVIELIST SET leadActor = ? WHERE movieId = ?", movie.getLeadActor(), movieId);
        }
        return getMovieById(movieId);
    }

    @Override
    public void deleteMovie(int movieId) {
        db.update("DELETE * FROM MOVIELIST WHERE moiveId = ?", movieId);
        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }
}
