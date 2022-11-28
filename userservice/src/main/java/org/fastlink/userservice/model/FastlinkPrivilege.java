package org.fastlink.userservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "privileges")
@NoArgsConstructor
@Getter
@Setter
public class FastlinkPrivilege
{
    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "PID")
    private Long id;

    private String name;

    public FastlinkPrivilege(String name)
    {
        this.name = name;
    }
}
