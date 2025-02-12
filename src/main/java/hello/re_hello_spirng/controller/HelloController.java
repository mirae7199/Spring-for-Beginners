package hello.re_hello_spirng.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // controller 라는 것을 알리기 위함
public class HelloController {
    @GetMapping("hello")
    public String Hello(Model model) {
        model.addAttribute("data", "hello!!");
        return "hello"; // hello.html 이곳으로 model 통해서 값 보내기
    }

    @GetMapping("hello-mvc") // url 매핑 hello-mvc로 설정
    // @RequestParam("name") 사용자 요청 name=spring을 설정
    // localhost:8080/hello-mvc?name=spring
    // name=spring으로 설정한 값을 model에 전달, viewResolver가 html로 변환
    public String helloMvc(@RequestParam("name") String name, Model model) {
        model.addAttribute("name", name);
        return "hello-template";
    }

    @GetMapping("hello-string")
    @ResponseBody // @ResponseBody를 사용하면 viewResolver를 사용하지 않음.
    // 대신에 HTTP의 BODY에 문자 내용을 직접 반환한다.
    public String helloString(@RequestParam("name") String name) {
        return "hello " + name;
    }

    @GetMapping("hello-api")
    @ResponseBody // @ResponseBody를 사용하고, 객체를 반환하면 JSON으로 변환됨
    // viewResolver 대신에 HttpMessageConverter가 동작
    // 기본 문자처리 -> StringHttpMessateConverter
    // 기본 객체처리 -> MappingJackson2HttpMessageConveter (JSON)
    public Hello helloApi(@RequestParam("name") String name) {
        Hello hello = new Hello();
        hello.setName(name);
        return hello;
    }

    static class Hello {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}

