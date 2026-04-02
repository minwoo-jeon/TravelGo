package com.travelGo.service;


import com.travelGo.domain.Member;
import com.travelGo.global.exception.BusinessException;
import com.travelGo.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;


    @Test
    @DisplayName("회원가입 성공")
    void 회원가입_성공() {
        //given
        String email = "jjw63@daum.net";
        String password = "1234";
        String name = "김철수";
        Member member = Member.createMember(email, password, name);

        //when
        Long memberId = memberService.join(member);

        //then;
        Member findMember = memberRepository.findById(memberId).get();
        Assertions.assertThat(findMember.getEmail()).isEqualTo(member.getEmail());
        //실제값 , 기대값
    }

    @Test
    @DisplayName("중복 회원 으로 회원가입 실패")
    void 중복_회원가입실패() {

        String email = "jjw63@daum.net";
        Member member1 = Member.createMember(email,"1234","김철수");
        memberService.join(member1);

        String email2 = "jjw63@daum.net";

        Member member2 = Member.createMember(email,"1234","전민우" );

        Assertions.assertThatThrownBy(() -> memberService.join(member2))
                .isInstanceOf(BusinessException.class)
                .hasMessage("이미 가입된 이메일입니다.");
    }
}