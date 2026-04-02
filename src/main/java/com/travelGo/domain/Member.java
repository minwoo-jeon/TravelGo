package com.travelGo.domain;


import com.travelGo.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String name;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    public static Member createMember(String email, String password, String name) {
        Member member = new Member();
        member.email = email;
        member.password = password;
        member.name = name;
        member.role = MemberRole.GUEST;
        return member;
    }
}
