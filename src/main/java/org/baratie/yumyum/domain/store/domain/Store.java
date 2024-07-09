package org.baratie.yumyum.domain.store.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.baratie.yumyum.domain.BaseTimeEntity;
import org.baratie.yumyum.domain.category.domain.Category;
import org.baratie.yumyum.domain.favorite.domain.Favorite;
import org.baratie.yumyum.domain.hashtag.domain.Hashtag;
import org.baratie.yumyum.domain.hashtag.dto.HashtagDto;
import org.baratie.yumyum.domain.image.domain.Image;
import org.baratie.yumyum.domain.image.domain.ImageType;
import org.baratie.yumyum.domain.image.dto.ImageDto;
import org.baratie.yumyum.domain.menu.domain.Menu;
import org.baratie.yumyum.domain.menu.dto.MenuDto;
import org.baratie.yumyum.domain.review.domain.Review;
import org.baratie.yumyum.domain.store.dto.UpdateStoreDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"hashtagList", "menuList", "imageList"})
public class Store extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "calls", nullable = false)
    private String call;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "hours")
    private String hours;

    @Column(name = "is_closed")
    private boolean isClosed;

    @Column(name = "views")
    private int views;

    @Column(name = "longitude", nullable = false)
    private BigDecimal longitude;

    @Column(name = "latitude", nullable = false)
    private BigDecimal latitude;

    @JsonIgnore
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviewList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Favorite> favoriteList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Hashtag> hashtagList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Menu> menuList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Category> categoryList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> imageList = new ArrayList<>();

    public void incrementViews(){
        this.views++;
    }

    public Store updateStore(UpdateStoreDto request) {
        this.name = request.getName();
        this.call = request.getCall();
        this.address = request.getAddress();
        this.hours = request.getHours();

        updateMenuList(request.getMenuList());
        updateHashtagList(request.getHashtagList());
        updateImageList(request.getImageList());

        return this;
    }

    private void updateMenuList(List<MenuDto> menuDtos) {
        this.menuList.clear();
        for (MenuDto menuDto : menuDtos) {
            Menu menu = Menu.builder()
                    .name(menuDto.getName())
                    .price(menuDto.getPrice())
                    .store(this)
                    .build();
            this.menuList.add(menu);
        }
    }

    private void updateHashtagList(List<HashtagDto> hashtagDtos) {
        this.hashtagList.clear();
        for (HashtagDto hashtagDto : hashtagDtos) {
            Hashtag hashtag = Hashtag.builder()
                    .content(hashtagDto.getContent())
                    .store(this)
                    .build();
            this.hashtagList.add(hashtag);
        }
    }

    private void updateImageList(List<ImageDto> imageDtos) {
        this.imageList.clear();
        for (ImageDto imageDto : imageDtos) {
            Image image = Image.builder()
                    .imageUrl(imageDto.getImageUrl())
                    .imageType(ImageType.STORE)
                    .store(this)
                    .build();
            this.imageList.add(image);
        }
    }
}
