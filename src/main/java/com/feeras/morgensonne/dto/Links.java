package com.feeras.morgensonne.dto;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Links {
    public ArrayList<Self> self;
    public ArrayList<Collection> collection;
    public ArrayList<About> about;
    public ArrayList<Author> author;
    public ArrayList<Reply> replies;
    public ArrayList<WpActionUnfilteredHtml> wpActionUnfilteredHtmlList;
    public ArrayList<WpActionAssignAuthor> WpActionAssignAuthorList;
    public ArrayList<Cury> curies;
}
