package org.baratie.yumyum.domain.store.domain;

import jakarta.persistence.*;
import lombok.*;
import org.baratie.yumyum.domain.BaseTimeEntity;
import org.baratie.yumyum.domain.member.domain.Member;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Store extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "call", nullable = false)
    private String call;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "hours")
    private LocalDateTime hours;

    @Column(name = "is_closed")
    private boolean isClosed;

    @Column(name = "views")
    private int views;

    private BigDecimal longitude;

    private BigDecimal latitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "member")
    private Member member;

}
