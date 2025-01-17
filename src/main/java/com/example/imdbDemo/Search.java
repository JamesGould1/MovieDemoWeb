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
import java.util.Comparator;
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
        String thisFilmRating = String.valueOf(thisFilm.getAverageRating());
        String returnFilm = thisFilmTitle + " : " + thisFilmRating;

        return returnFilm;
    }


    @GetMapping("/search/byTitle")
    public String searchForMovieByTitle(
            @RequestParam(value = "title", defaultValue = "") String title,
            Model model) {
        logger.info("Searching for " + title);
        List<Film> searchedFilms = repository.findByPrimaryTitleContainingIgnoreCase(title);
        logger.info("Found " + searchedFilms.size() + " results");
        searchedFilms.sort(Comparator.comparingInt(Film::getNumVotes).reversed());

        model.addAttribute("films", searchedFilms);
        return "searchResults";
    }
}
