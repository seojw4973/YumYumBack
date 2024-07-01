package org.baratie.yumyum.domain.image.dto;

import lombok.*;
import org.baratie.yumyum.domain.image.domain.Image;
import org.baratie.yumyum.domain.image.domain.ImageType;

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
}
