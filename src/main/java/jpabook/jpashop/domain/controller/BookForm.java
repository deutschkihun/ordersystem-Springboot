package jpabook.jpashop.domain.controller;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class BookForm {

    private Long id;

    @NotEmpty(message = "name is mandatory")
    private String name;

    private int price;
    private int stockQuantity;
    private String author;
    private String isbn;
}
