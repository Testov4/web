package eshop.web.models;

import eshop.web.util.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.aspectj.weaver.ast.Or;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;

@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Table(name = "\"user\"")
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username")
    @Size(min = 2, max = 12, message = "username should be between 2 and 12 characters")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    @Email
    private String email;

    @ToString.Exclude
    @OneToMany(mappedBy = "buyer")
    private List<Order> orders;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;


    @OneToOne(mappedBy = "user")
    private UserInformation userInformation;
}
