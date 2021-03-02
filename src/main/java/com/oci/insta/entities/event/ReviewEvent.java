package com.oci.insta.entities.event;


import com.oci.insta.entities.models.Review;

import java.io.Serializable;

public class ReviewEvent  extends BaseEvent implements Serializable {
    private Long mediaId;
    private Integer score;
    private Long by;
    private static final long serialVersionUID = -1L;
    public ReviewEvent(Review review) {
        if(null == review) {
            return;
        }
        this.mediaId = review.getMedia().getId();
        this.score = review.getScore();
        this.by = review.getBy().getId();
    }
}
