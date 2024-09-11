package com.example.gr.controller;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Arrays;

@RestController
public class ConfigController {

    @Value("${genders}")
    private String genders;

    @Value("${animals}")
    private String animals;

    @Value("${colors}")
    private String colors;

    @Value("${vaccines}")
    private String vaccines;

    @GetMapping("/api/config")
    public ConfigResponse getConfig() {
        return new ConfigResponse(
                Arrays.asList(genders.split(",")),
                Arrays.asList(animals.split(",")),
                Arrays.asList(colors.split(",")),
                Arrays.asList(vaccines.split(","))
        );
    }

    @Data
    public static class ConfigResponse {
        private List<String> genders;
        private List<String> animals;
        private List<String> colors;
        private List<String> vaccines;

        public ConfigResponse(List<String> genders, List<String> animals, List<String> colors, List<String> vaccines) {
            this.genders = genders;
            this.animals = animals;
            this.colors = colors;
            this.vaccines = vaccines;
        }
    }
}
