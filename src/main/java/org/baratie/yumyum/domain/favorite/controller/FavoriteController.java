package org.baratie.yumyum.domain.favorite.controller;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.favorite.dto.FavoriteDto;
import org.baratie.yumyum.domain.favorite.service.FavoriteService;
import org.baratie.yumyum.domain.member.domain.Member;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/star")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping
    public ResponseEntity<Void> addFavorite(@AuthenticationPrincipal Member member, FavoriteDto request) {
        favoriteService.addFavorite(member, request);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}

