package ru.dsoccer1980.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import ru.dsoccer1980.util.config.InitProps;

import javax.persistence.*;

@Entity
@Table(name = "role", uniqueConstraints = {@UniqueConstraint(columnNames = "name", name = "role_unique_name_idx")})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role implements GrantedAuthority {

    @Id
    @SequenceGenerator(name = "global_seq", sequenceName = "global_seq", allocationSize = 1, initialValue = InitProps.START_SEQ)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_seq")
    private Long id;

    private String name;

    @Override
    public String getAuthority() {
        return "ROLE_" + getName();
    }
}
