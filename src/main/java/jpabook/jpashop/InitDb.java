package jpabook.jpashop;


import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

/*
    총 주문 2개
    userA : JPA1 BOOK , JPA2 BOOK
    userB : SPRING1 BOOK, SPRING2 BOOK
 */


@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init(){
        initService.dbInit1();
        initService.deInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{

        private final EntityManager em;
        public void dbInit1(){
            Member member = createMember("userA","서울","123","456");
            em.persist(member);

            Book book1 = createBook("JPA BOOK 1",10000,100,"Tim","67788");
            em.persist(book1);
            Book book2 = createBook("JPA BOOK 2",20000,200,"Tom","78654");
            em.persist(book2);

            Delivery delivery = createDelivery(member);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1,10000,1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2,20000,2);

            Order order = Order.createOrder(member,delivery,orderItem1,orderItem2);
            em.persist(order);
        }

        public void deInit2(){
            Member member = createMember("userB","동탄","789","012");
            em.persist(member);


            Book book1 = createBook("SPRING BOOK 1",30000,300,"Steven","12340");
            em.persist(book1);
            Book book2 = createBook("SPRING BOOK 2",40000,400,"Bill","567890");
            em.persist(book2);

            Delivery delivery = createDelivery(member);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1,30000,3);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2,40000,4);

            Order order = Order.createOrder(member,delivery,orderItem1,orderItem2);
            em.persist(order);
        }


        // 다른 방법 : private void createBook
        // this....

        private Book createBook(String name,int price,int stockQuantity,String author,String isbn) {
            Book book = new Book();
            book.setName(name);
            book.setPrice(price);
            book.setStockQuantity(stockQuantity);
            book.setIsbn(isbn);
            book.setAuthor(author);
            return book;
        }

        private Member createMember(String name,String city,String street,String zipcode) {
            Member member = new Member();
            member.setName(name);
            member.setAddress(new Address(city,street,zipcode));
            return member;
        }

        public Delivery createDelivery(Member member){
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            return delivery;
        }
    }
}


