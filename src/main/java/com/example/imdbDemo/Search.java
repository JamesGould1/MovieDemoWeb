package com.example.imdbDemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class Search {

    private static final Logger logger = LoggerFactory.getLogger(Search.class);

    @Autowired
    FilmRepository repository;

    @ResponseBody
    @GetMapping("/search/byId")
    public String searchForMovieById(
            @RequestParam(value = "id", defaultValue = "") String id,
            Model model) {

        if (id.isEmpty()) {
            return "index";
        }
        Optional<Film> optionalFilm = repository.findById(id);
        Film thisFilm = optionalFilm.orElse(null);
        String thisFilmTitle = thisFilm.getPrimaryTitle();

        /*
        List<Film> films = repository.findByPrimaryTitle(id);
        List<String> listOfFilmTitle = new ArrayList<>();
        films.forEach(film -> listOfFilmTitle.add(film.getPrimaryTitle()));
        model.addAttribute("filmTitles", listOfFilmTitle);
        */

        return thisFilmTitle;
    }

    @ResponseBody
    @GetMapping("/search/byTitle")
    public List<String> searchForMovieByTitle(
            @RequestParam(value = "title", defaultValue = "") String title,
            Model model) {
        logger.info("Searching for " + title);
        List<Film> searchedFilms = repository.findByPrimaryTitle(title);
        logger.info("Found " + searchedFilms.size() + " results");
        List<String> searchedFilmTitles = new ArrayList<>();
        for (Film film : searchedFilms) {
            String filmTitle = film.getPrimaryTitle();
            searchedFilmTitles.add(filmTitle);
        }

        return searchedFilmTitles;
    }
}
