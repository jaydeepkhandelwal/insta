package com.oci.insta.controller;

import com.oci.insta.entities.dto.APIResponse;
import com.oci.insta.entities.dto.MediaDto;
import com.oci.insta.entities.models.Media;
import com.oci.insta.exception.InstaException;
import com.oci.insta.security.CurrentUser;
import com.oci.insta.security.InstaUserDetails;
import com.oci.insta.service.MediaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("insta/api/v1/media")
@Slf4j
public class MediaController {

    private final MediaService mediaService;

    @Autowired
    MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @PostMapping("/image/upload")
    public MediaDto uploadImage(@CurrentUser InstaUserDetails userDetails,
                                @RequestPart("file") MultipartFile file) throws InstaException {

        Media createdMedia =  mediaService.uploadImage(userDetails.getId(), file);
        return new MediaDto().setId(createdMedia.getId());
    }

    @DeleteMapping("/{mediaId}")

    public APIResponse deleteMedia(@CurrentUser InstaUserDetails userDetails,
                                   @PathVariable ("mediaId") Long mediaId) throws InstaException {
            boolean isDeleted = mediaService.deletedMedia(userDetails.getId(), mediaId);
            return new APIResponse(isDeleted);
    }






}
