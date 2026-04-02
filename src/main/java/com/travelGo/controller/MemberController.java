package com.travelGo.controller;

import com.travelGo.domain.Member;
import com.travelGo.dto.MemberJoinForm;
import com.travelGo.dto.MemberLoginFrom;
import com.travelGo.global.common.ErrorCode;
import com.travelGo.global.exception.BusinessException;
import com.travelGo.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/new")
    public String joinForm(Model model) {
        model.addAttribute("memberForm", new MemberJoinForm());
        return "members/createMemberForm";
    }
    @Qualifier
    @PostMapping("/new")
    public String join(@Valid @ModelAttribute("memberForm") MemberJoinForm memberForm, BindingResult result, Model model) {
        try {
            Member member = Member.createMember(memberForm.getEmail(), memberForm.getPassword(), memberForm.getName());
            memberService.join(member);
            log.info("[회원가입 성공] email={}", memberForm.getEmail());
        } catch (BusinessException e) {
            if (e.getErrorCode() == ErrorCode.DUPLICATE_EMAIL) {
                log.warn("[회원가입 실패] 중복 이메일 시도: {}", memberForm.getEmail());
                result.rejectValue("email", "duplicate", "이미 가입된 이메일입니다.");
                model.addAttribute("memberForm", memberForm);
                return "members/createMemberForm";
            }
            throw e;
        }
        model.addAttribute("signupSuccess", true);
        return "members/createMemberForm";
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        log.debug("[로그인 폼] 조회");
        model.addAttribute("loginForm", new MemberLoginFrom());
        return "members/loginForm";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("loginForm") MemberLoginFrom loginForm, BindingResult result, HttpServletRequest request) {
        log.info("[로그인 요청] email={}", loginForm.getEmail());
        try {
            Member loginMember = memberService.login(loginForm.getEmail(), loginForm.getPassword());
            HttpSession session = request.getSession();
            session.setAttribute("loginMember", loginMember);
            log.info("[로그인 성공] email={}, memberId={}", loginForm.getEmail(), loginMember.getId());
            return "redirect:/";
        } catch (BusinessException e) {
            if (e.getErrorCode() == ErrorCode.MEMBER_NOT_FOUND) {
                log.warn("[로그인 실패] 일치하는 회원 없음: email={}", loginForm.getEmail());
                result.rejectValue("password", "loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
                return "members/loginForm";
            }
            throw e;
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Object member = session.getAttribute("loginMember");
            if (member instanceof Member) {
                log.info("[로그아웃] memberId={}", ((Member) member).getId());
            }
            session.invalidate();
        }
        return "redirect:/";
    }
}
