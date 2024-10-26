package com.feeras.morgensonne.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;

// import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
// import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
/* ObjectMapper om = new ObjectMapper();
Root root = om.readValue(myJsonString, Root.public class); */

@Data
public class MediaResponse {
    public int id;
    public Date date;
    public Date date_gmt;
    public Guid guid;
    public Date modified;
    public Date modified_gmt;
    public String slug;
    public String status;
    public String type;
    public String link;
    public Title title;
    public int author;
    public int featured_media;
    public String comment_status;
    public String ping_status;
    public String template;
    public ArrayList<Object> meta;
    public String permalink_template;
    public String generated_slug;
    public ArrayList<String> class_list;
    public Description description;
    public Caption caption;
    public String alt_text;
    public String media_type;
    public String mime_type;
    public MediaDetails media_details;
    public Object post;
    public String source_url;
    public ArrayList<Object> missing_image_sizes;
    public Links _links;
}


