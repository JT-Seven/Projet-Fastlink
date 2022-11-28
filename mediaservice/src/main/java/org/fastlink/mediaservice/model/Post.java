package org.fastlink.mediaservice.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;

@Getter
@Setter
@Entity(name = "posts")
public class Post
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PID")
    private Long id;

    @Column(name = "user_id")
    protected Long userId;

    @Column(name = "post_content")
    private String content;

    @Column(name = "like_count")
    private int nbLike = 0;

    @Column(name = "dislike_count")
    private int nbDislike = 0;

    @OneToMany(cascade = ALL)
    @JoinTable(
            name = "post_medias",
            joinColumns = @JoinColumn(
                    name = "post_id",
                    referencedColumnName = "PID"
            ), inverseJoinColumns = @JoinColumn(
                    name = "media_id",
                    referencedColumnName = "MID"
            )
    )
    private List<Media> mediaList = new ArrayList<Media>();

    @ManyToMany(cascade = ALL, fetch = EAGER )
    @JoinTable(
            name = "post_tags",
            joinColumns = @JoinColumn(
                    name = "post_id",
                    referencedColumnName = "PID"
            ), inverseJoinColumns = @JoinColumn(
                    name = "tag_id",
                    referencedColumnName = "TID"
            )
    )
    private List<Tag> tagList = new ArrayList<Tag>();


    @OneToMany(cascade = ALL)
    @JoinTable(
            name = "post_comments",
            joinColumns = @JoinColumn(
                    name = "post_id",
                    referencedColumnName = "PID"
            ), inverseJoinColumns = @JoinColumn(
                    name = "comment_id",
                    referencedColumnName = "CID"
            )
    )
    private List<Comment> commentList = new ArrayList<Comment>();

    @CreatedDate
    @Column(name = "date_created", nullable = false, updatable = false)
    private Date creationDate = new Date();
}
