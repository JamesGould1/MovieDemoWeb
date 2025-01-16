package com.example.imbdDemo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Search {

    private static FilmRepository repository;


    @GetMapping("/search")
    public List<Film> searchForMovie(@RequestParam(value = "title", defaultValue = "null")String title){
            return repository.findByTitle(title);
    }

}
