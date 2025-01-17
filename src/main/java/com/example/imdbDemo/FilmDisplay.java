package com.example.imdbDemo;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FilmDisplay {

    @GetMapping("/film/{filmId}")
    public String getPageOfFilmDetails() {
        return "filmDisplay";
    }

}
