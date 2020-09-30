package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "ITEM_ID")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    public Item(){
    }

    public Item(String name,int price,int stockQuantity){
        super();
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();



    //==Business logic==//
    // 데이터를 가지고있는 쪽에 비즈니스 로직을 추가하는 것이 가장 응집력이 높은.
    // 즉 stockQuantity 를 정의한 클래스에서 비즈니스 로직을 추가했다.

    public void addStock(int quantity){
        this.stockQuantity += quantity;
        // item.stockQuanatity = stockQuanatity + quantity
    }

    public void removeStock(int quantity){
        int restStock = this.stockQuantity - quantity;
        // 남은 재고
        if(restStock<0){
            // 남은 재고 수량이 0 이하
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }

































}
