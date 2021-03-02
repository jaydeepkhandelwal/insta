package com.oci.insta.storage.file;

import com.oci.insta.config.FileStorageConfig;
import com.oci.insta.exception.InstaErrorCode;
import com.oci.insta.exception.InstaException;
import com.oci.insta.storage.Storage;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class LocalFileStorage implements Storage {

    private FileStorageConfig fileStorageConfig;

    LocalFileStorage(FileStorageConfig fileStorageConfig) {
        this.fileStorageConfig = fileStorageConfig;
    }

    @Override
    public String store(MultipartFile file) throws InstaException {
        try {
            Path copyLocation = Paths
                    .get(fileStorageConfig.getDirPath() + File.separator +
                            StringUtils.cleanPath(file.getOriginalFilename()));
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
            return copyLocation.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new InstaException(InstaErrorCode.INTERNAL_SERVER_ERROR, "Could not store file " + file.getOriginalFilename()
                    + ". Please try again!");
        }

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
