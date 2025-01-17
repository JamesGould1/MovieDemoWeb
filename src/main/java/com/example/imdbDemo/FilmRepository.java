package com.example.imdbDemo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FilmRepository extends JpaRepository<Film, String> {
    List<Film> findByPrimaryTitleContainingIgnoreCase(String primaryTitle);

}
