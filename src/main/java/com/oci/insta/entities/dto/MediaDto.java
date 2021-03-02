package com.oci.insta.entities.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MediaDto {
    private Long id;
    private String title;
    private String description;
    private String presignedUrl;

}
