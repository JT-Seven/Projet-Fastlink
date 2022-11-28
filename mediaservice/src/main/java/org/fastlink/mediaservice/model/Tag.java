package org.fastlink.mediaservice.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "tags")
@Getter
@Setter
public class Tag
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "TID", nullable = false)
    private Long id;

    @Column(name = "tag", unique = true)
    private String hashtag;

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Tag)) return false;
        Tag that = (Tag) o;
        return this.hashtag.equals(that.hashtag);
    }

}
