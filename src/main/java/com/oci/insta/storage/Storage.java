package com.oci.insta.storage;

import com.oci.insta.exception.InstaException;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface Storage {


    String store(MultipartFile file) throws InstaException;

    Resource loadAsResource(String path);

    void delete(String path);

     String getPresignedUrl();

}
