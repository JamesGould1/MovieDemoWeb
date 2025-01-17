package com.example.imdbDemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;

@Component
public class Database {
    LocalDate date;
    @Autowired
    FilmRepository repository;

    static Logger logger = LoggerFactory.getLogger(Database.class);
    List<String> lines = new ArrayList<>();

    private record ratingInfo(
            double averageRating,
            int numVotes
    ) {
    }

    ;

    static HashMap<String, ratingInfo> filmsWithRating = new HashMap<>();

    public void processTheData() throws IOException {
        String filePath = Objects.requireNonNull(Database.class.getClassLoader().getResource("title.basics.tsv.gz")).getFile();
        String filePathOfRatings = Objects.requireNonNull(Database.class.getClassLoader().getResource("title.ratings.tsv.gz")).getFile();
        LocalTime start = LocalTime.now();
        getFilmFromRatingFile(filePathOfRatings);
        getFilmFromMainFile(filePath)
                .forEach(new Consumer<Film>() {

                    private AtomicLong count = new AtomicLong(0);


                    @Override
                    public void accept(Film film) {
                        ratingInfo hashMapMatch = filmsWithRating.get(film.getId());
                        if (hashMapMatch != null) {
                            film.setAverageRating(hashMapMatch.averageRating);
                            film.setNumVotes(hashMapMatch.numVotes);
                            repository.save(film);
                            long c = count.incrementAndGet();
                            if ((c & 0xffff) == 0)
                                logger.info("Saved " + c + " in " + Duration.between(start, LocalTime.now()));
                        }
                    }
                });
        logger.info("Done loading!");
    }

    public static void getFilmFromRatingFile(String filePath) throws IOException {
        InputStream inputStream = new FileInputStream(filePath);
        GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);
        InputStreamReader inputStreamReader = new InputStreamReader(gzipInputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        bufferedReader.readLine();
        bufferedReader
                .lines()
                .forEach(Database::doesFilmHaveRatings);

    }

    public static Stream<Film> getFilmFromMainFile(String filePath) throws IOException {
        InputStream inputStream = new FileInputStream(filePath);
        GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);
        InputStreamReader inputStreamReader = new InputStreamReader(gzipInputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        return bufferedReader
                .lines()
                .parallel()
                .map(Database::formatFilmMainData);
    }

    public static void doesFilmHaveRatings(String line) {
        String[] fields = line.split("\t");
        try {
            filmsWithRating.put(fields[0], new ratingInfo(Double.parseDouble(fields[1]), Integer.parseInt(fields[2])));
        } catch (Exception e) {
            logger.error("Error with writing Rated film to HashMap" + fields[0] + fields[1] + fields[2]);
        }
        AtomicLong count = new AtomicLong();
        long c = count.incrementAndGet();
        if (c >= 1000) {
            logger.info(c + " saved to hashmap");
            count.lazySet(0);
        }
        else;
    }

    private static Film formatFilmMainData(String line) {
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


