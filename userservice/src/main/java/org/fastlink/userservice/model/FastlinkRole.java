package org.fastlink.userservice.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.FetchType.EAGER;

@Entity
@Table(name = "roles")
@Getter
@Setter
public class FastlinkRole
{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "RID", nullable = false)
    private Long id;

    private String name;

    @ManyToMany(fetch = EAGER)
    @JoinTable(
            name = "roles_privileges",
            joinColumns = @JoinColumn(
                    name = "role_id",
                    referencedColumnName = "RID"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "privilege_id",
                    referencedColumnName = "PID"
            )
    )
    private List<FastlinkPrivilege> privileges;

    // Constructors
    public FastlinkRole(String name)
    {
        this.name = name;
    }

    public FastlinkRole()
    {
    }

    // Methods

}