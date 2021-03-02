package com.oci.insta.controller;

import com.oci.insta.entities.dto.ReviewDto;
import com.oci.insta.entities.models.Review;
import com.oci.insta.exception.InstaException;
import com.oci.insta.security.CurrentUser;
import com.oci.insta.security.InstaUserDetails;
import com.oci.insta.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("insta/api/v1/review")
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }


    @PostMapping("/")
    public ReviewDto postReview(@CurrentUser InstaUserDetails userDetails,
                                @RequestBody ReviewDto reviewDto) throws InstaException, IOException, InterruptedException {

        Review reviewCreated =  reviewService.postReview(userDetails.getId(), new Review(reviewDto));
        return new ReviewDto().setId(reviewCreated.getId());

    }









}
