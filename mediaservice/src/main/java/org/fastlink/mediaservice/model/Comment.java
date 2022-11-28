package org.fastlink.mediaservice.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity(name = "comments")

public class Comment
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CID", nullable = false)
    private Long commentId;

    @Column(name = "comment")
    private String description;

    @Column(name = "owner_user_id")
    private Long userId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "like_count", nullable = false)
    private int nbLike = 0;

    @ManyToOne
    @JoinColumn(name = "parent_id", nullable = true)
    private Comment parentComment;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "comment_medias",
            joinColumns = @JoinColumn(
                    name = "comment_id",
                    referencedColumnName = "CID"),
            inverseJoinColumns = @JoinColumn(
                    name = "media_id",
                    referencedColumnName = "MID")
    )
    private List<Media> mediaList = new ArrayList<Media>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "comment_tags",
            joinColumns = @JoinColumn(
                    name = "comment_id",
                    referencedColumnName = "CID"),
            inverseJoinColumns = @JoinColumn(
                    name = "tag_id",
                    referencedColumnName = "TID")
    )
    private List<Tag> tagList = new ArrayList<Tag>();

    @CreatedDate
    @Column(name = "date_created", nullable = false, updatable = false)
    private Date creationDate = new Date();


}
