package jpabook.jpashop.domain.api;


import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;


    @GetMapping("/api/v2/simple-orders")
    // 직접 노출하는 것보다 좋은 방법, 그러나 지연로딩으로 인해 너무 많은 쿼리가 나감
    // 실제로 POSTMAN에 실행해보면 총 3개의 쿼리가 필요함(order,member,delivery)

    // Order 2개
    // 1 + N , 즉 1 + 회원(N) + 배송(N) = 1+2+2=5번 쿼리
    public List<SimpleOrderDto> orderV2() {
       List<Order> orders =  orderRepository.findAllByString(new OrderSearch());
       List<SimpleOrderDto> result = orders.stream().
               map(o -> new SimpleOrderDto(o)).   // 타입을 order 에서 SimpleOrderDto 변경
               collect(Collectors.toList());

       return result;
    }

    // fetch join
    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
        return result;
    }

    //JPA 에서 DTO 바로 조회
    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4() {
        return orderSimpleQueryRepository.findOrdersDtos();
    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();  // 이 시점에 LAZY초기화(member)
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); // 이 시점에 LAZY초기화(delivery)
        }
    }
}
