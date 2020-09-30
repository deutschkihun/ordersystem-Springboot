package jpabook.jpashop.domain.api;


import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;

    @GetMapping("api/v2/orders")   // Entity -> Dto
    public List<OrderDto> orderV2(){
        List<Order> orders= orderRepository.findAllByString(new OrderSearch());
        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());
        return result;
        // 쿼리 갯수 : (order, 식별자 있어서 한번만 출력됨) + (member 1) + (delivery 1) + (order item 1) + (item1.1) + (item1.2)
        //                                           (member 2) + (delivery 2) + (order item 2) + (item2.1) + (item2.2) = 11개
    }



    @GetMapping("api/v3/orders")  // Entity -> Dto(fetch join)
    public List<OrderDto> orderV3(){
        List<Order> orders= orderRepository.findAllWithItem();

        for (Order order : orders) {
            System.out.println("order ref=" + order + "id = " +order.getId());
        }
        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());

        return result;
        // 쿼래 갯수 = (order member delivery ,fetch join(XtoOne, no collection) + (order item 1) + (item1.1) + (item1.2)
        //                                                                  (order item 2) + (item2.1) + (item2.2) = 7개
    }

    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_page(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "100") int limit) {
        List<Order> orders = orderRepository.findAllWithMemberDeliveryPage(offset, limit);
        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());
        return result;
        // 쿼리 갯수 총 3개 : (order & delivery & member fetch join(XtoOne, no collection) + (order item(id 2개 한번에)) + (item(id 4개 한번에)) = 3쿼리
        // 뒤에 한번에 처리한 order item 하고 item 은 페이징 설정 및 batchsize 설정을 변경해주어서 최적화 시킬수 있음
        // default_batch_fetch_size: 100
    }




    @Data
    static class OrderDto{
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address; // 값타입은 노출해도 됨
        private List<OrderItemDto> orderItems;  // 많이 하는 실수 : List<OrderItem> 이라고 하면 안됨. Dto 변환 해주어야함.


        public OrderDto(Order order){
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
            orderItems = order.getOrderItems().stream()
                    .map(orderItem -> new OrderItemDto(orderItem))
                    .collect(Collectors.toList());
        }
    }



    @Data
    static class OrderItemDto{
        private String itemName;
        private int orderPrice;
        private int count;

        // 생성자로 OrderItem 넘긴다.
        public OrderItemDto(OrderItem orderItem){
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }
}
