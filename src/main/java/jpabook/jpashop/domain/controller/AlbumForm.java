package jpabook.jpashop.domain.controller;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class AlbumForm {

    private Long id;

    @NotEmpty(message = "name is mandatory")
    private String name;

    private int price;
    private int stockQuantity;
    private String artist;
    private String genre;
}
