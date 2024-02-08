package ru.skillsmart.fleet.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import javax.persistence.*;
import javax.persistence.JoinTable;
import java.util.List;
import java.util.Set;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "managers")
public class Manager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    private String username;
    private String password;

//    public Manager(String username, String password, Collection<? extends GrantedAuthority> authorities, Set<Enterprise> enterprises) {
//        super(username, password, authorities);
//        this.enterprises = enterprises;
//    }

//    protected Manager() {
//        super(null, null, null);
//    }

    @ManyToMany
    @JoinTable(
            name = "managers_enterprises",
            joinColumns = @JoinColumn(name = "manager_id"),
            inverseJoinColumns = @JoinColumn(name = "enterprise_id")
    )
    private Set<Enterprise> enterprises;
}
