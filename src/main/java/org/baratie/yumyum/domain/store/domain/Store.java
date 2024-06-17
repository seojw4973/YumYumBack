package org.baratie.yumyum.domain.store.domain;

import jakarta.persistence.*;
import lombok.*;
import org.baratie.yumyum.domain.member.domain.Member;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Store {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "call", nullable = false)
    private String call;

    @Column(name = "address", nullable = false)
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "member")
    private Member member;
}
