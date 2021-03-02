package com.oci.insta.entities.models;

import com.oci.insta.entities.dto.ReviewDto;
import com.oci.insta.entities.models.base.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Data
@Table(name = "reviews")
@Entity
@Where(clause = "deleted!=true")
public class Review extends BaseEntity {

    @Column(name = "review")
    private String review;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User by;


    /* Here alternatively, we can use just media_id */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "media_id")
    private Media media;

    @Column(name = "score")
    private Integer score;

    public Review(ReviewDto reviewDto) {
        if(reviewDto == null) {
            return;
        }
        this.review = reviewDto.getReview();
        Media media = new Media();
        media.setId(reviewDto.getMediaId());
        this.media = media;
        this.score = reviewDto.getScore();


    }
}

