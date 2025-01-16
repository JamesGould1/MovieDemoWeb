package com.example.imbdDemo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

import static java.util.stream.Collectors.toList;

public class Database {
    static String filePath = Objects.requireNonNull(Database.class.getClassLoader().getResource("title.basics.tsv.gz")).getFile();

    List<String> lines = new ArrayList<>();

    public static void main(String[] args) throws FileNotFoundException {
        List<Film> films = findFilm("Inception");
        for (int i = 0; i < 10; i++) {
            System.out.println("Title: " + films.get(i).getPrimaryTitle());
            System.out.println("Year Of Release: " + films.get(i).getStartYear());
            System.out.println("Genres: " + films.get(i).getGenres());
            System.out.println(" ");
        }
    }

    public static List<Film> findFilm(String title) throws FileNotFoundException {
        try (InputStream inputStream = new FileInputStream(filePath);
             GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);
             InputStreamReader inputStreamReader = new InputStreamReader(gzipInputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

            return bufferedReader.lines().filter(o -> o.contains(title)).map(Database::formatFilm).collect(Collectors.toList());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static Film formatFilm(String line) {
        String[] fields = line.split("\t");
        Film film = new Film();
        if (fields.length > 1) {
            if (!(fields[0] == null)){film.setId(fields[0]);}
            if (!(fields[2] == null)){film.setPrimaryTitle(fields[2]);}
            if (!(fields[5] == null) && !(fields[5]).contains("\\N")){film.setStartYear(Integer.parseInt(fields[5]));}
            if (!(fields[8] == null)){film.setGenres(List.of(fields[8].split(",")));}
        }
        return film;
    }
}


