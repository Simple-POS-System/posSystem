package com.ead.deliveryservice.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "deliveryPerson")
public class DeliveryPerson {
    @Id
    private String deliveryPersonId;
    private String firstName;
    private String lastName;
    private String email;
    private String contactNumber;
    private String address;
    private List<ActiveOrder> ordersToDeliver;
    private List<String> deliveredOrders;
    private String password;
}
