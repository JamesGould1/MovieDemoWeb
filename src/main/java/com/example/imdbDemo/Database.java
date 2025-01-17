package com.example.imdbDemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Component;

import java.io.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;

@Component
public class Database {
    LocalDate date;
    @Autowired
    FilmRepository repository;
    static Logger logger = LoggerFactory.getLogger(Database.class);
    List<String> lines = new ArrayList<>();


    public void processTheData() throws IOException {
        String filePath = Objects.requireNonNull(Database.class.getClassLoader().getResource("title.basics.tsv.gz")).getFile();
        String filePathOfRatings = Objects.requireNonNull(Database.class.getClassLoader().getResource("title.ratings.tsv.gz")).getFile();
        LocalTime start = LocalTime.now();
        getFilmFromFile(filePath)
                .forEach(new Consumer<Film>() {

                    private AtomicLong count = new AtomicLong(0);


                    @Override
                    public void accept(Film film) {
                        repository.saveAndFlush(film);
                        long c = count.incrementAndGet();
                        if ((c & 0xffff) == 0) logger.info("Saved " + c + " in " + Duration.between(start, LocalTime.now()));
                    }
                });
        logger.info("Done loading!");
    }

    public static Stream<Film> getFilmFromFile(String filePath) throws IOException {
        InputStream inputStream = new FileInputStream(filePath);
             GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);
             InputStreamReader inputStreamReader = new InputStreamReader(gzipInputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            return bufferedReader
                    .lines()
                    .parallel()
                    /*.filter(o -> o.contains(title))*/
                    .map(Database::formatFilm);
    }

    private static Film formatFilm(String line) {
        String[] fields = line.split("\t");
        Film film = new Film();
        if (fields.length > 1) {
            try {
                film.setId(fields[0]);
            } catch (RuntimeException e) {
                film.setId("null");
                logger.error("Error with setting film (ID) for " + film);
            }
            try {
                film.setPrimaryTitle(fields[2]);
            } catch (RuntimeException e) {
                film.setPrimaryTitle("null");
                logger.error("Error with setting film (Title) for " + film);
            }
            try {
                film.setStartYear(Integer.parseInt(fields[5]));
            } catch (NumberFormatException e) {
                film.setStartYear(0);
                logger.trace("Error with setting (Year) of release for " + film);
            }
            try {
                film.setGenres(List.of(fields[8].split(",")));
            } catch (RuntimeException e) {
                film.setGenres(List.of("Null"));
                logger.error("Error with setting (Genres) for " + film);
            }
        }
        return film;
    }
}


