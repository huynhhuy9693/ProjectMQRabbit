package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "category")
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "status")
    private boolean status;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "categoryId")
    private List<ProductEntity> product ;

    @PrePersist
    void onPrePersist() {
        if (status == false) {
            status=true;
        }
    }

}
