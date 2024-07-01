package org.baratie.yumyum.domain.store.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.baratie.yumyum.domain.BaseTimeEntity;
import org.baratie.yumyum.domain.hashtag.domain.Hashtag;
import org.baratie.yumyum.domain.hashtag.dto.HashtagDto;
import org.baratie.yumyum.domain.image.domain.Image;
import org.baratie.yumyum.domain.member.domain.Member;
import org.baratie.yumyum.domain.menu.domain.Menu;
import org.baratie.yumyum.domain.menu.dto.MenuDto;
import org.baratie.yumyum.domain.store.dto.UpdateStoreDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"hashtagList", "menuList"})
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
    @OneToMany(mappedBy = "store", cascade = CascadeType.REMOVE)
    private List<Hashtag> hashtagList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "store", cascade = CascadeType.REMOVE)
    private List<Menu> menuList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "store", cascade = CascadeType.REMOVE)
    private List<Image> imageList = new ArrayList<>();

    public Store updateStore(UpdateStoreDto request){
        this.name = request.getName();
        this.call = request.getCall();
        this.address = request.getAddress();
        this.hours = request.getHours();
        this.hashtagList = request.getHashtagList();
        this.menuList = request.getMenuList();
        this.imageList = request.getImageList();

        return this;
    }

}
