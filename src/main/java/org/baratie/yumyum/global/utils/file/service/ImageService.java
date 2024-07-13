package org.baratie.yumyum.global.utils.file.service;

import com.amazonaws.services.s3.AmazonS3;
import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.global.utils.file.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageService{

    private final ImageRepository imageRepository;
    private final AmazonS3 s3;

    @Value("${spring.s3.bucket}")
    private String bucketName;

}
