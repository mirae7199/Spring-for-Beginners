package hello.re_hello_spirng.controller;

import hello.re_hello_spirng.domain.Member;
import hello.re_hello_spirng.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


@Slf4j
@Controller
public class MemberController {
    private final MemberService memberService;

    @Autowired // 스프링이 연관된 객체를 스프링 컨테이너에서 찾아서 넣어준다.
    public MemberController(MemberService memberService) { // new MemberService(new MemoryMemberRepository())
        this.memberService = memberService;
    }

    @GetMapping(value = "/members/new")
    public String createForm() {
        return "members/createMemberForm";
    }

    @PostMapping(value = "/members/new")
    public String create(MemberForm form) {
        Member member = new Member();
        member.setName(form.getName()); // 받아온 데이터를 회원 이름에 넣기

        memberService.join(member);
        log.info(form.getName());

        return "redirect:/";
    }

    @GetMapping(value = "/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }
}
