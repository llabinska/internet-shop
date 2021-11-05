package com.internet.shop.dmo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Transient
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    public Role(RoleName name) {
        this.name = name.name();
    }

    public void setName(RoleName name) {
        this.name = name.name();
    }

    @Override
    public String getAuthority() {
        return name;
    }

    public enum RoleName {
        ROLE_USER, ROLE_ADMIN
    }

}
