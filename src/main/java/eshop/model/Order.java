package eshop.model;

import eshop.util.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Builder
@Table(name = "\"order\"")
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "address")
    private String address;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "final_price")
    private Long finalPrice;

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
