package com.mypetshop.controller;

import com.mypetshop.dto.MemberFormDto;
import com.mypetshop.entity.Member;
import com.mypetshop.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "/new")
    public String memberForm(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

    @PostMapping(value = "/new")
    public String newMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model) {
        // 검증하려는 객체 앞에 @Valid 어노테이션 선언,
        // 파라미터로 bindingResult 객체 추가.
        // 검사 후 결과는 bindingResult에 담아줌.
        if (bindingResult.hasErrors()) {
            return "member/memberForm";
        }
        // bindingResult.hasErrors()를 호출하여 에러가 있다면
        // 회원 가입 페이지로 이동

        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            // 회원가입 시 중복 회원 가입 예외가 발생하면 에러 메시지를 뷰로 전달
            return "member/memberForm";
        }
        return "redirect:/";

    }

}
