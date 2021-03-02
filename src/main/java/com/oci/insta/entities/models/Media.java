package com.oci.insta.entities.models;

import com.oci.insta.entities.dto.MediaDto;
import com.oci.insta.entities.models.base.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Table(name = "media")
@Data
@Entity
@Accessors(chain = true)
@NoArgsConstructor
@Where(clause = "deleted!=true")
public class Media extends BaseEntity {

    @Column(name = "title")
    private String title;

    @Column(name = "path")
    private String path;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Media(MediaDto mediaDto) {
        this.title = mediaDto.getTitle();
        this.description = mediaDto.getDescription();
        this.title = mediaDto.getTitle();
    }


}
