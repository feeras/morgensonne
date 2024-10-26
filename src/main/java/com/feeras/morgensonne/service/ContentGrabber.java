package com.feeras.morgensonne.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ContentGrabber {

    private final String CONSTRUCTION_PAGE = "https://now.baustellen-kamera.ch/WohnparkMorgensonne.html";

    public List<String> getImages() throws IOException {

        Document doc = Jsoup.connect(CONSTRUCTION_PAGE).get();
        Elements img = doc.getElementsByTag("img");
        List<String> images = new ArrayList<>();
        img.stream()
                .filter(e -> e.attr("src").contains("https://"))
                .filter(e -> e.attr("src").contains("/morgensonne2-"))
                .forEach(e -> images.add(e.attr("src").replaceFirst("1200.jpg", "full.jpg")));

        return images;
    }
}
