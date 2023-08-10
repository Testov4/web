package eshop.web.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "\"order\"")
public class Order {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "address")
    private String address;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "final_price")
    private double finalPrice;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private OrderStatus orderStatus;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User buyer;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;


}
