package jpabook.jpashop.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter @Setter
@DiscriminatorValue("M")
public class Movie extends Item {
    private String director;
    private String genre;

    public void createMovie(String name,int price,int stockQuantity,String director,String genre){
        this.setName(name);
        this.setPrice(price);
        this.setStockQuantity(stockQuantity);
        this.director = director;
        this.genre = genre;
    }

}
