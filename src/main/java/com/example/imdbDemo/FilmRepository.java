package com.example.imdbDemo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface FilmRepository extends JpaRepository<Film, String> {
    List<Film> findByPrimaryTitle(String primaryTitle);

}
