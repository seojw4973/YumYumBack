package org.baratie.yumyum.global.utils.file.dto;

import lombok.*;
import org.baratie.yumyum.global.utils.file.domain.Image;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ImageDto {

    private String imageUrl;

    public Image toEntity(){
        Image image = new Image().builder()
                .imageUrl(imageUrl)
                .build();

        return image;
    }

    public ImageDto fromEntity(Image image) {
        return ImageDto.builder()
                .imageUrl(image.getImageUrl())
                .build();
    }
}
