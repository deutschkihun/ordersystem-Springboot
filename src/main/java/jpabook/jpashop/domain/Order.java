package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name ="orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id @GeneratedValue
    @Column(name = "ORDER_ID")
    private Long id;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;


    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
    private List<OrderItem> orderItems =  new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "DELIVERY_ID")
    private Delivery delivery;


    //==연관관계 메소드==//

    public void setMember(Member member){
        this.member = member;
        member.getOrders().add(this);  // member 의 연관관계 로직참고!!
    }

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);  // orderItems 에 orderItem 넣음, 어디에서 : Order클래스
        orderItem.setOrder(this);  // Order 에 order를 넣음, 어디에서: orderitem 클래스
        // 양뱡향 매핑
    }

    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
        // delivery.getOrder().add(this) 오류 >>> 왜냐 자료형 타입이 아니어서
        // get하면 오류 왜냐 매개변수가 없기 때문에 (getter의 구조를 생각해보기)
    }


    //==생성 메소드==//
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){
        // ... 하면 여러개 받을수 있음
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);

        for (OrderItem orderItem : orderItems) {
            // collection type 인 orderItems 는 loop 가 돌면서 orderItem에 저장된다.
            order.addOrderItem(orderItem);
            // 그 orderItem 은 order 연관관계 메소드에 의해 주문상품으로 저장된다.
            // for each loop 참조
        }

        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //==비즈니스 로직==//
    public void cancel(){
        if (delivery.getStatus() == DeliveryStatus.COMP){
            throw new IllegalStateException("already finished delivery couldn't be canceled");
        }
        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }


    //==조회로직==//
    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice +=orderItem.getTotalPrice();
        }
        return totalPrice;
    }



}
