package com.ead.orderserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    public String generateProductId(){
        Order lastOrder = orderRepository.findFirstByOrderByOrderIdDesc();
        if(lastOrder != null){
            int lastOrderId = Integer.parseInt(lastOrder.getOrderId().substring(1));
            int newOrderId = lastOrderId + 1;
            return "O" + newOrderId;
        }else {
            return "O1";
        }
    }

    public LocalDateTime getCurrentTime(){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return timestamp.toLocalDateTime();
    }

    public ResponseEntity<Order> saveOrder(Order order){
        try {
            if(order.getCustomerId() == null){
                return ResponseEntity.badRequest().body(null);
            }
            order.setOrderId(generateProductId());
            order.setOrderTime(getCurrentTime());
            order.setOrderStatus(OrderStatus.IN_QUEUE);
            return ResponseEntity.ok(orderRepository.save(order));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(null);
        }
    }

    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }

    public ResponseEntity<?> getOrderByOrderId(String orderId){
        return ResponseEntity.ok(orderRepository.getOrderByOrderId(orderId));
    }

    public ResponseEntity<?> setStatus(String orderId, OrderStatus orderStatus){
        Order order = orderRepository.getOrderByOrderId(orderId);
        if(order == null){
            return ResponseEntity.badRequest().body("Order not found");
        }
        order.setOrderStatus(orderStatus);
        RestTemplate restTemplate = new RestTemplate();
        String orderUrl = "http://localhost:8040/customers/setOrderStatus/" + order.getCustomerId() + "/" +orderId +"/" + orderStatus;
        restTemplate.put(orderUrl, null);
        return ResponseEntity.ok(orderRepository.save(order));
    }

    public List<Order> getOrderByOrderStatus(OrderStatus orderStatus){
        return orderRepository.getOrderByOrderStatus(orderStatus);
    }

    public ResponseEntity<?> cancelOrder(String orderId) {
        Order order = orderRepository.getOrderByOrderId(orderId);
        if(order == null){
            return ResponseEntity.badRequest().body("Order not found");
        }
        if(order.getOrderStatus() == OrderStatus.CANCELLED){
            return ResponseEntity.badRequest().body("Order already cancelled");
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        RestTemplate restTemplate = new RestTemplate();
        List<OrderItem> orderItems = order.getOrderItems();
        if(orderItems != null){
            for(OrderItem orderItem : orderItems){
                String productUrl = "http://localhost:8070/products/returnQuantity/" + orderItem.getProductId() + "/" + orderItem.getQuantity();
                restTemplate.put(productUrl, null);
            }
        }
        return ResponseEntity.ok(orderRepository.save(order));
    }

}
