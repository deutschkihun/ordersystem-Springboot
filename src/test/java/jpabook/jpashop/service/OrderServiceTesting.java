package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTesting {

    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @Test
    public void ProductOrder() throws Exception{
        //given
        Member member = new Member();
        member.setName("Member1");
        member.setAddress(new Address("Karlsruhe","Wahldhornstrasse","76131"));
        em.persist(member);

        Book book = new Book();
        book.setName("Justice");
        book.setPrice(50);
        book.setStockQuantity(1000);
        em.persist(book);

        int orderCount = 2;

        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);


        //then
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals("When ordering product the status should be Order",OrderStatus.ORDER,getOrder.getStatus());
        assertEquals("Ordered number of product should be correct",1,getOrder.getOrderItems().size());
        assertEquals("Ordered price is equal to count * price ",50*orderCount,getOrder.getTotalPrice());
        assertEquals("Rest stock should be reduced as like the ordered products",1000-orderCount,book.getStockQuantity());
        // Message , expected ,actual

    }


    @Test(expected = NotEnoughStockException.class)
    public void OrderedProductOverStockQuantity() throws Exception{
        //given
        Member member2 = new Member();
        member2.setName("Member2");
        member2.setAddress(new Address("Karlsruhe","Wahldhornstrasse","76131"));
        em.persist(member2);

        Book book2 = new Book();
        book2.setName("Justice2");
        book2.setPrice(50);
        book2.setStockQuantity(500);
        em.persist(book2);

        int orderCount2 = 501;

        //when
        orderService.order(member2.getId(), book2.getId(), orderCount2);

        //then
        fail("재고수량 예외가 발생해야 한다.");
        // 마지막 로직은 when 에서 예외가 발생하지 않는 경우에 then 으로 넘어와서 fail내용이 출력된다.
    }


    @Test
    public void OrderCancel() throws Exception{
        //given
        Member member3 = new Member();
        member3.setName("Member2");
        member3.setAddress(new Address("Karlsruhe","Wahldhornstrasse","76131"));
        em.persist(member3);

        Book book3 = new Book();
        book3.setName("Justice2");
        book3.setPrice(50);
        book3.setStockQuantity(500);
        em.persist(book3);

        int orderCount3 = 2;
        Long orderId2 = orderService.order(member3.getId(), book3.getId(), orderCount3);

        //when
        orderService.cancelOrder(orderId2);

        //then
        Order getOrder = orderRepository.findOne(orderId2);
        assertEquals("주문취소시 상태는 cancel",OrderStatus.CANCEL,getOrder.getStatus());
        assertEquals("주문이 취소된 상품은 그만큼 재고가 감소해야 한다.",500,book3.getStockQuantity());
    }
}