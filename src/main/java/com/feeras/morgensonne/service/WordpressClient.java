package com.feeras.morgensonne.service;

import com.afrozaar.wordpress.wpapi.v2.Wordpress;
import com.afrozaar.wordpress.wpapi.v2.config.ClientConfig;
import com.afrozaar.wordpress.wpapi.v2.config.ClientFactory;
import com.afrozaar.wordpress.wpapi.v2.exception.PostCreateException;
import com.afrozaar.wordpress.wpapi.v2.model.Post;
import com.afrozaar.wordpress.wpapi.v2.model.PostStatus;
import com.afrozaar.wordpress.wpapi.v2.model.builder.ContentBuilder;
import com.afrozaar.wordpress.wpapi.v2.model.builder.PostBuilder;
import com.afrozaar.wordpress.wpapi.v2.model.builder.TitleBuilder;
import com.feeras.morgensonne.dto.MediaResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Base64Util;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.context.WebApplicationContext;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class WordpressClient {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    private final Wordpress client;
    private final WebApplicationContext webApplicationContext;
    private final RestClient restClient;
    private byte[] storedContent;

    private final String baseUrl;
    private final String username = System.getProperty("username");
    private final String password = System.getProperty("password");

    public WordpressClient(WebApplicationContext webApplicationContext, RestClient restClient, Environment environment) {
        this.baseUrl = environment.getProperty("wordpress.baseUrl");
        boolean debug = false;

        if (!(StringUtils.hasLength(baseUrl) && StringUtils.hasLength(username) && StringUtils.hasLength(password))) {
            log.error("Either baseUrl or username or password is empty");
            System.exit(1);
        }

        client = ClientFactory.fromConfig(ClientConfig.of(baseUrl, username, password, false, debug));
        this.webApplicationContext = webApplicationContext;
        this.restClient = restClient;
    }

    public MediaResponse storeImage(String imageURL) throws Exception {
        String formattedDateTime = getFormattedDateTime();
        Resource resource = webApplicationContext.getResource("url:" + imageURL);

        if (sameContent(resource.getContentAsByteArray())) {
            log.info("Found same content. Skip creation of post");
            return null;
        }

        String fileName = getFileName(imageURL);
        fileName += "-" + formattedDateTime + ".jpg";

        String base64Credentials = Base64Util.encode(username + ":" + password);

        MediaResponse mediaResponse = restClient.post()
                .uri(baseUrl + "/wp-json/wp/v2/media")
                .header("Authorization", "Basic " + base64Credentials)
                .header("Content-Disposition", "attachment; filename=" + fileName)
                .header("Content-Type", "image/jpeg")
                .body(resource.getContentAsByteArray())
                .retrieve()
                .body(MediaResponse.class);

        storedContent = resource.getContentAsByteArray();
        return mediaResponse;
    }

    private boolean sameContent(byte[] contentAsByteArray) {
        if (storedContent == null) {
            return false;
        } else {
            return Arrays.equals(storedContent, contentAsByteArray);
        }
    }

    private String getFileName(String imageURL) {
        if (imageURL.contains("/morgensonne2-")) {
            return "morgensonne-2";
        } else if (imageURL.contains("/morgensonne1-")) {
            return "morgensonne-1";
        } else {
            return "morgensonne-0";
        }
    }

    private String getFormattedDateTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();

        return currentDateTime.format(formatter);
    }

    public void createPost(List<String> imageUrls) throws Exception {
        for (String imageUrl : imageUrls) {

            MediaResponse mediaResponse = storeImage(imageUrl);

            if (mediaResponse == null) {
                return;
            }

            String expectedTitle = getPostTitle(mediaResponse);
            String expectedContent = "<!-- wp:image {\"sizeSlug\":\"large\",\"linkDestination\":\"none\",\"align\":\"wide\"} -->\n" +
                    "<figure class=\"wp-block-image alignwide size-large\"><img src=\"" + mediaResponse.getSource_url() + "\" alt=\"\"/></figure>\n" +
                    "<!-- /wp:image -->";

            final Post post = PostBuilder.aPost()
                    .withTitle(TitleBuilder.aTitle().withRendered(expectedTitle).build())
                    .withFeaturedMedia(Long.parseLong(mediaResponse.getId() + ""))
                    .withContent(ContentBuilder.aContent().withRendered(expectedContent).build())
                    .build();

            try {
                final Post createdPost = client.createPost(post, PostStatus.publish);
                log.info("Created post: {}", createdPost.getDate());
            } catch (PostCreateException e) {
                log.error("Error creating post", e);
            }
        }
    }

    private String getPostTitle(MediaResponse mediaResponse) {
        String pattern = "dd.MM.yyyy HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(mediaResponse.getDate());
        return "Morgensonne " + date;
    }
}
