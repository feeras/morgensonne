package com.feeras.morgensonne.dto;

import lombok.Data;

import java.util.ArrayList;

@Data
public class ImageMeta {
    public String aperture;
    public String credit;
    public String camera;
    public String caption;
    public String created_timestamp;
    public String copyright;
    public String focal_length;
    public String iso;
    public String shutter_speed;
    public String title;
    public String orientation;
    public ArrayList<Object> keywords;
}
