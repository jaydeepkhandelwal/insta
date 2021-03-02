package com.oci.insta.entities.dto;

import com.oci.insta.entities.common.LatLong;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ReviewDto {
    private Long id;
    private String review;
    private Long mediaId;
    private Integer score;
    private LatLong location;
}
