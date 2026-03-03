package school.sptech.fluxybackend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "Users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data

public class User {
    @Id
    @GeneratedValue(strategy  = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false, unique = true)
    private String cnpj;

    private String address;

    private String city;

    private String state;

    @Column(nullable = false, updatable = false)
    private Long createdAt = System.currentTimeMillis();

}
