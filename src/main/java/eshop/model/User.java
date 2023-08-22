package eshop.model;

import eshop.util.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@ToString
@Builder
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "\"user\"")
public class User {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

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

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserInformation userInformation;
}
