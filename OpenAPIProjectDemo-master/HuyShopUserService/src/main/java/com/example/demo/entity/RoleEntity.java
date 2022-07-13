package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import javax.persistence.*;
import java.util.List;


@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "role")
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id ;
    @Column(name = "name")
    private String name;
    @Column(name = "status")
    private boolean status;

    @JsonIgnore
    @OneToMany(mappedBy = "roleId" , cascade = CascadeType.ALL)
    private List<UserEntity> user;

    @PrePersist
    void onPrePersist() {
        if (status==false) {
            status=true;
        }
    }
}
