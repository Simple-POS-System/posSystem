package com.ead.customerservice;

import com.ead.customerservice.Exceptions.UserNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @PostMapping("/add")
    public ResponseEntity<String> addCustomer(@RequestBody Customer customer){
        return customerService.saveCustomer(customer);
    }

    @GetMapping("/getAll")
    public List<Customer> getAllCustomers(){
        return customerService.getAllCustomers();
    }

    @GetMapping("/getById/{userId}")
    public ResponseEntity<?> getCustomerById(@PathVariable String userId) {
        try {
            Customer customer = customerService.getCustomerById(userId);
            return ResponseEntity.ok(customer);
        } catch (UserNotFoundException e) {
            return null;
        }
    }

    @PutMapping("/updateDetails/{userId}")
    public ResponseEntity<String> updateCustomer(@RequestBody UpdateCustomer customer,@PathVariable String userId){
        return customerService.updateCustomer(customer,userId);
    }

    @DeleteMapping("/deleteById/{userId}")
    public ResponseEntity<String> deleteCustomerById(@PathVariable String userId) {
        return customerService.deleteCustomerById(userId);
    }

    @GetMapping("/getCart/{userId}")
    public ResponseEntity<?> getCart(@PathVariable String userId){
        return customerService.getCartItems(userId);
    }

    @PostMapping("/addToCart/{userId}")
    public ResponseEntity<String> addToCart(@RequestBody CartItem cartItem, @PathVariable String userId) {
        return customerService.userCartAdd(cartItem, userId);
    }

    @PutMapping("/updateCart/{userId}")
    public ResponseEntity<?> updateCart(@RequestBody CartItem cartItem,@PathVariable String userId){
        try {
            return customerService.userCartUpdate(cartItem,userId);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/deleteCartItem/{userId}/{productId}")
    public ResponseEntity<?> deleteCartItem(@PathVariable String userId,@PathVariable String productId){
        return customerService.removeCartItem(userId,productId);
    }


    @GetMapping("/getOrderStatus/{userId}/{orderId}")
    public ResponseEntity<?> getOrderStatus(@PathVariable String userId,@PathVariable String orderId){
        return customerService.getOrderStatus(userId,orderId);
    }

    @PutMapping("/setOrderStatus/{userId}/{orderId}/{orderStatus}")
    public ResponseEntity<?> setOrderStatus(@PathVariable String userId,@PathVariable String orderId,@PathVariable String orderStatus){
        return customerService.setOrderStatus(userId,orderId,orderStatus);
    }

    @PostMapping("/placeOrder/{userId}")
    public ResponseEntity<?> placeOrder(@PathVariable String userId){
        return customerService.placeOrder(userId);
    }


    @DeleteMapping("/deleteCart/{userId}")
    public ResponseEntity<?> deleteCart(@PathVariable String userId){
        return customerService.clearCart(userId);
    }

    @PutMapping("/cancelOrder/{customerId}/{orderId}")
    public ResponseEntity<?> cancelOrder(@PathVariable String customerId,@PathVariable String orderId) throws JsonProcessingException {
        return customerService.cancelOrder(customerId,orderId);
    }
}
