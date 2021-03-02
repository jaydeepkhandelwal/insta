package com.oci.insta.storage.s3;

import com.oci.insta.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.nio.file.Path;
import java.util.stream.Stream;

@Component
public class S3Storage implements Storage {

    private final S3Client s3Client;

    @Autowired
    S3Storage(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public String store(MultipartFile file) {
        return null;
    }

    @Override
    public Resource loadAsResource(String path) {
        return null;
    }

    @Override
    public void delete(String path) {

    }

    @Override
    public String getPresignedUrl() {
        return null;
    }
}
