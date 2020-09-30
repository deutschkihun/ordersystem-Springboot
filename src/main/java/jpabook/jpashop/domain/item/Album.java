package jpabook.jpashop.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;

@Entity
@Getter @Setter
@DiscriminatorValue("A")
public class Album extends Item {

    private String artist;
    private String genre;

    public void createAlbum(String name,int price,int stockQuantity,String artist,String genre){
        this.setName(name);
        this.setPrice(price);
        this.setStockQuantity(stockQuantity);
        this.artist = artist;
        this.genre = genre;
    }
}
