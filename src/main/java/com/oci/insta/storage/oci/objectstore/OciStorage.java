package com.oci.insta.storage.oci.objectstore;

import com.oci.insta.storage.ImageStorage;
import com.oci.insta.storage.Storage;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

public class OciStorage implements Storage {

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
