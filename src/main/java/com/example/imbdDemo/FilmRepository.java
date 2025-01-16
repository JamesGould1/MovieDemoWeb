package com.example.imbdDemo;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FilmRepository extends CrudRepository<Film, Long> {
        Film findById(long id);
    List<Film> findByTitle(String title);

}
