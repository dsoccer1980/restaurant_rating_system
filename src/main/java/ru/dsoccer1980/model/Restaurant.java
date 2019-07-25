package ru.dsoccer1980.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.dsoccer1980.util.config.InitProps;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Entity
@Table(name = "restaurant")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {

    @Id
    @SequenceGenerator(name = "global_seq", sequenceName = "global_seq", allocationSize = 1, initialValue = InitProps.START_SEQ)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_seq")
    private Long id;

    @NotBlank
    @Size(min = 2, max = 100)
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address", nullable = false)
    @NotBlank
    @Size(max = 100)
    private String address;

    public Restaurant(@NotBlank @Size(min = 2, max = 100) String name, @NotBlank @Size(max = 100) String address) {
        this.name = name;
        this.address = address;
    }
}