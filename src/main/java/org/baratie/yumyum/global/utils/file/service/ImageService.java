package org.baratie.yumyum.global.utils.file.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.global.exception.ErrorCode;
import org.baratie.yumyum.global.utils.file.domain.Image;
import org.baratie.yumyum.global.utils.file.domain.ImageType;
import org.baratie.yumyum.global.utils.file.exception.FileUploadException;
import org.baratie.yumyum.global.utils.file.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageService{

    private final ImageRepository imageRepository;
    private final AmazonS3 s3;

    @Value("${spring.s3.bucket}")
    private String bucketName;

    public void fileUploadMultiple(ImageType type,Object target, List<MultipartFile> files){
        for(MultipartFile file : files){
            String uuid = UUID.randomUUID().toString();
            String fileName = uuid + "_" + file.getOriginalFilename();

            Image image = Image.builder()
                    .imageType(type)
                    .imageUrl(bucketName + "/" + type.name().toLowerCase() + "/" + fileName)
                    .build();

            image.addEntity(target);

            imageRepository.save(image);
            try{
                uploadMutipartFile(fileName, file, type);
            }catch(IOException e){
                throw new FileUploadException(ErrorCode.FILE_UPLOAD_FAILED);
            }
        }
    }

    private void uploadMutipartFile(String fileName, MultipartFile image, ImageType type) throws IOException {
        String savePath = bucketName + "/" + type.toString().toLowerCase();
        try{
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(image.getSize());
            metadata.setContentType(image.getContentType());

            s3.putObject(savePath, fileName, image.getInputStream(), metadata);
            setFileAccessPublic(fileName, savePath);
        }catch(AmazonS3Exception e){
            e.printStackTrace();
        }catch(SdkClientException e){
            e.printStackTrace();
        }
    }

    private void setFileAccessPublic( String objectName, String savePath) {
        try {
            s3.setObjectAcl(savePath, objectName, CannedAccessControlList.PublicRead);
        } catch (AmazonS3Exception e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        }
    }

}
