package com.oci.insta.service;

import com.oci.insta.entities.dto.ReviewDto;
import com.oci.insta.entities.event.ReviewEvent;
import com.oci.insta.entities.models.Review;
import com.oci.insta.entities.models.User;
import com.oci.insta.event.ReviewEventProducer;
import com.oci.insta.exception.InstaErrorCode;
import com.oci.insta.exception.InstaException;
import com.oci.insta.repository.ReviewRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private MediaService mediaService;
    private ReviewEventProducer reviewEventProducer;

    @Autowired
    ReviewService(ReviewRepository reviewRepository, MediaService mediaService,
                  ReviewEventProducer reviewEventProducer) {
       this.reviewRepository = reviewRepository;
       this.mediaService = mediaService;
       this.reviewEventProducer = reviewEventProducer;
    }

    public Review postReview(Long userId, Review review) throws InstaException, IOException, InterruptedException {
        log.info("Inside postReview");
        Long mediaUserId = mediaService.getUserByMediaId(review.getMedia().getId());
        if(mediaUserId.equals(userId)) {
            throw new InstaException(InstaErrorCode.BAD_REQUEST, "user is trying to review his own photo");
        }
        review.setBy(new User(userId));
        Review reviewCreated = reviewRepository.save(review);

        reviewEventProducer.sendEvent(new ReviewEvent(review));
        return reviewCreated;

    }
}
