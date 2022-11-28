package org.fastlink.mediaservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;


@Getter
@Setter
@Entity(name = "medias")
@Inheritance(strategy = InheritanceType.JOINED)

public class Media
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "MID")
    private Long mediaId;

    @Column(name = "owner_user_id")
    private Long userId;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "PID", nullable = true)
    private Post post;

    @Enumerated(EnumType.STRING)
    private MediaType mediaType;

    @Enumerated(EnumType.STRING)
    private MediaResourceType mediaResourceType;

    @Column(name = "media_code")
    private String mediaCode;

    @Column(name = "download_url")
    private String downloadUrl;

    @Column(name = "mime_type")
    private String mimeType;

    @Column(name = "file_name")
    private String fileName;

    @CreatedDate
    @Column(name = "date_created", updatable = false, nullable = false)
    private Date createdAt = new Date();

}


