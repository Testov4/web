package eshop.web.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "category")
public class Category {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    @Size(min = 2, max = 30)
    private String name;

    @Column(name = "imageurl")
    @Size(min = 2, max = 30)
    private String imageURL;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = {CascadeType.ALL, CascadeType.PERSIST})
    @JsonIgnore
    List<Category> subCategories;

    //TO DO ONE TO MANY -> product

    @OneToMany(mappedBy = "category")
    private List<Product> products;

}
