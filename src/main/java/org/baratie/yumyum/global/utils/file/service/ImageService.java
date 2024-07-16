package org.baratie.yumyum.global.utils.file.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.domain.Member;
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

    @Value("${spring.s3.endpoint}")
    private String endpoint;

    public String profileImageUpload(MultipartFile file) throws IOException {
        System.out.println("file.toString() = " + file.toString());
        if (file == null || file.isEmpty()) {
            System.out.println("이미지가 없습니다.");
            return null;
        }

        String uuid = UUID.randomUUID().toString();
        String fileName = uuid + "_" + file.getOriginalFilename();

        String profileUrl = endpoint + "/" + bucketName + "/profile/" + fileName;
        uploadProfileImage(fileName, file);

        return profileUrl;
    }

    public void fileUploadMultiple(ImageType type,Object target, List<MultipartFile> files){
        System.out.println("file.toString() = " + files.toString());
        if (files == null || files.isEmpty()) {
            System.out.println("이미지가 없습니다.");
            return;
        }

        for(MultipartFile file : files){

            if (file.isEmpty()) {
                System.out.println("이미지가 없습니다.");
                continue;
            }

            String uuid = UUID.randomUUID().toString();
            String fileName = uuid + "_" + file.getOriginalFilename();

            Image image = Image.builder()
                    .imageType(type)
                    .imageUrl(endpoint + "/" + bucketName + "/" + type.name().toLowerCase() + "/" + fileName)
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

    private void uploadProfileImage(String fileName, MultipartFile image) throws IOException {
        String savePath = bucketName + "/profile";
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

    public List<String> findFileUrl(ImageType type, Long targetId) {
        if(type.equals(ImageType.REVIEW)){
            return imageRepository.findByReviewId(targetId);
        }else if(type.equals(ImageType.STORE)){
            return imageRepository.findByStoreId(targetId);
        }
        return null;
    }

    public void targetFilesDelete(ImageType type, Long targetId){
        List<Image> imageList = null;
        if(type.equals(ImageType.REVIEW)){
            imageList = imageRepository.findEntityByReviewId(targetId);
            imageRepository.deleteAll(imageList);
        }else if(type.equals(ImageType.STORE)){
            imageList = imageRepository.findEntityByStoreId(targetId);
            imageRepository.deleteAll(imageList);
        }

        for(Image image : imageList){
            String imageUrl = image.getImageUrl();
            String objectKey = imageUrl.substring(endpoint.length() + bucketName.length() + type.name().toLowerCase().length() + 3);

            s3.deleteObject(bucketName + "/" + type.name().toLowerCase(), objectKey);
        }
    }

    public void targetFileDelete(Member member){
        String profileUrl = member.getImageUrl();
        String objectKey = profileUrl.substring( endpoint.length() + bucketName.length() + 10);

        s3.deleteObject(bucketName + "/profile", objectKey);
    }


}
