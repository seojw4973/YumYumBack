package org.baratie.yumyum.domain.favorite.controller;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.favorite.dto.FavoriteDto;
import org.baratie.yumyum.domain.favorite.service.FavoriteService;
import org.baratie.yumyum.domain.member.domain.CustomUserDetails;
import org.baratie.yumyum.domain.member.domain.Member;
import org.baratie.yumyum.domain.member.service.MemberService;
import org.baratie.yumyum.domain.store.domain.Store;
import org.baratie.yumyum.domain.store.service.StoreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/star")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final MemberService memberService;
    private final StoreService storeService;

    @PostMapping
    public ResponseEntity<Void> addFavorite(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody FavoriteDto request) {

        Member member = memberService.validationMemberId(customUserDetails.getId());
        Store store = storeService.validationStoreId(request.getStoreId());

        favoriteService.addFavorite(member, store, request);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}

