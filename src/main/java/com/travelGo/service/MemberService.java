package com.travelGo.service;

import com.travelGo.domain.Member;
import com.travelGo.global.common.ErrorCode;
import com.travelGo.global.exception.BusinessException;
import com.travelGo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long join(Member member) {
        log.info("[회원 가입 시작] Email: {}", member.getEmail());
        validationMember(member.getEmail());
        memberRepository.save(member);
        log.info("[회원 가입 성공] MemberID: {}, Email: {}", member.getId(), member.getEmail());
        return member.getId();
    }

    public Member login(String email, String password) {
        log.debug("[로그인 시도] Email: {}", email);
        return memberRepository.findByEmail(email)
                .filter(m -> m.getPassword().equals(password))
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
    }

    private void validationMember(String email) {
        log.debug("[중복 검증] Email: {}", email);
        memberRepository.findByEmail(email)
                .ifPresent(m -> {
                    log.warn("[회원 가입 거부] 이미 존재하는 이메일입니다. Email: {}", email);
                    throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
                });
    }
}
