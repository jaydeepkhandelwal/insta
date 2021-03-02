package com.oci.insta.service;

/*This service takes care of upload Media to Object/File Storage and persisting this information to Database
  This service also takes care of compression/encoding of images/videos.
 */

import com.oci.insta.entities.models.Media;
import com.oci.insta.entities.models.User;
import com.oci.insta.exception.InstaErrorCode;
import com.oci.insta.exception.InstaException;
import com.oci.insta.repository.MediaRepository;
import com.oci.insta.storage.Storage;
import com.oci.insta.storage.file.LocalFileStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class MediaService {

    /* Currently, I am using localFileStorage which can be replaced with any
     Object Storage service like S3 or OCI object storage.

     */
    private Storage storage;
    public MediaRepository mediaRepository;


    @Autowired
    MediaService(LocalFileStorage storage, MediaRepository mediaRepository) {
        this.storage = storage;
        this.mediaRepository = mediaRepository;
    }
    public Media uploadImage(Long userId, MultipartFile multipartFile) throws InstaException {
        log.info("Entering uploadImage");
        String path = storage.store(multipartFile);
        Media media = new Media();
        media.setPath(path);
        media.setUser(new User(userId));
        return mediaRepository.save(media);
    }

    public boolean deletedMedia(Long userId,
                                Long mediaId) throws InstaException {
        if(mediaId == null) {
            throw new InstaException(InstaErrorCode.BAD_REQUEST, "mediaId is null");
        }
        log.info("Entering deletedMedia");

        Media media = mediaRepository.findById(mediaId).orElse(null);
        if(media != null) {
            if(!media.getUser().getId().equals(userId)) {
                throw new InstaException(InstaErrorCode.BAD_REQUEST, "media belongs to some other user");
            }
            /* first delete from storage. In case of S3, we can send that media to Glacier so that we can
            recover the image later if we want*/
            storage.delete(media.getPath());
            media.setDeleted(true);
            mediaRepository.save(media);
        } else {
            throw new InstaException(InstaErrorCode.BAD_REQUEST, "no media for the given mediaID");

        }
        return true;


    }

    public Long getUserByMediaId(Long mediaId) throws InstaException {
        log.info("inside getUserByMediaId");
        Media media = mediaRepository.findById(mediaId).orElse(null);
        if(null == media) {
            throw new InstaException(InstaErrorCode.BAD_REQUEST, "no media for the given mediaID");
        }
        return media.getUser().getId();
    }


}
