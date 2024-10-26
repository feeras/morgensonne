package com.feeras.morgensonne.dto;

import lombok.Data;

@Data
public class MediaDetails {
    public int width;
    public int height;
    public String file;
    public int filesize;
    public Sizes sizes;
    public ImageMeta image_meta;
}
