package com.example.imbdDemo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.List;

@Entity
public class Film {

        @Id
        private String id;
        private String primaryTitle;
        private int startYear;
        private Integer runtimeMinutes;
        private List<String> genres;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }


        public String getPrimaryTitle() {
            return primaryTitle;
        }

        public void setPrimaryTitle(String primaryTitle) {
            this.primaryTitle = primaryTitle;
        }

        public int getStartYear() {
            return startYear;
        }

        public void setStartYear(int startYear) {
            this.startYear = startYear;
        }

        public Integer getRuntimeMinutes() {
            return runtimeMinutes;
        }

        public void setRuntimeMinutes(Integer runtimeMinutes) {
            this.runtimeMinutes = runtimeMinutes;
        }

        public List<String> getGenres() {
            return genres;
        }

        public void setGenres(List<String> genres) {
            this.genres = genres;
        }
    }

