# 정적 컨텐츠
- [스프링 부트 정적 컨텐츠 기능](https://docs.spring.io/spring-boot/docs/2.3.1.RELEASE/reference/html/spring-boot-features.html#boot-features-spring-mvc-static-content)

**resources/static/hello-static.html**
~~~ html
<!DOCTYPE HTML>  
<html>  
<head>  
    <title>static content</title>  
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />  
</head>  
<body>  
정적 컨텐츠 입니다.  
</body>  
</html>
~~~

#### 실행
- http://localhost:8080/hello-static.html


![Image](https://github.com/user-attachments/assets/39e2c7ed-284e-40cc-a1df-c4b4b6c5dd75)
정적인 것은 static에
동적인 것은 template에

정적 컨텐츠는 관련 컨트롤러가 없으며, 톰켓 서버에서 html 파일 그대로 클라이언트에게 전달한다.

# 동적 컨텐츠

### MVC와 템플릿 엔진
- MVC: Model, View, Controller

**Controller**
**hello_spring/controller/HelloController(Class)**
~~~ java
@Controller  
public class HelloController {  
  
    @GetMapping("hello-mvc")  
    public String helloMvc(@RequestParam("name") String name, Model model) {  
        model.addAttribute("name", name);  
        return "hello-template";  
    }
}
~~~

**View**
**resources/templates/hello-template.html**
~~~ html
<html xmlns:th="http://www.thymeleaf.org">  
<body>  
<p th:text="'hello ' + ${name}">hello! empty</p>  
</body>  
</html>
~~~

### public String helloMvc(@RequestParam("name") String name, Model model) 메서드 분석

#### RequestParam("name") String name
- 클라이언트가 요청한 값을 가져다 준다.
	- localhost:8080/hello-mvc?**name=spring**
	- name = spring ( 클라이언트가 요청한 값 )
		(url 쿼리와 관련해서 나중에 다시 배울 것이다. 필자는 배웠지만 까먹었..)

#### model.addAttribute("name", name)
- 클라이언트가 요청한 값(name)이 들어오면 model.addAttribute("name", name) 값에 들어갈 것이다.
- viewResolver에 의해 hello-template에 name값이 전달된다.

**MVC, 템플릿 엔진 이미지**
![Image](https://github.com/user-attachments/assets/c9f7da5b-6617-4237-93bf-d3bfa4ec17e3)

## API

**ResponseBody 문자 반환**
~~~ java
@Controller  
public class HelloController {  
  
	@GetMapping("hello-spring")  
	@ResponseBody  
	public String helloSpring(@RequestParam("name") String name){  
		return "hello " + name;  
}
~~~

### public String helloString(@RequestParam("name") String name) 메서드 분석

#### @ResponseBody
- @ResponseBody를 사용하면 viewResolver를 사용하지 않는다.
- 대신에 HTTP의 BODY에 문자 내용을 직접 반환한다.
- 클라이언트가 요청한 값(name)이 return에 전달하고 StringHttpMessageConverter에 의해 문자 그대로 출력하게 된다.

**실행**
**name = spring**
![Image](https://github.com/user-attachments/assets/01a2c153-3bd2-4212-8114-77c7c6773eed)
return에 있는 값을 그대로 출력한다.

**@ResponseBody 객체 반환**
~~~ java
@Controller
public class HelloController {
	@GetMapping("hello-api")  
    @ResponseBody  
    public Hello HelloApi(@RequestParam("name") String name) {  
        Hello hello = new Hello();  
        hello.setName(name);  
        return hello;  
    } 
	 
    private class Hello {  
        private String name;  
  
        public String getName() {  
            return name;  
        }  
        public void setName(String name) {  
            this.name = name;  
        }
	}
}
~~~

### public Hello helloApi(@RequestParam("name") String name)       메서드 분석

#### Hello 객체
- Hello 객체를 생성해서 클라이언트가 요청한 값을 hello의 name필드에 넣고 객체를 반환하였다.
- @ResponseBody를 사용하고 return 값에 객체를 반환하면 mappingJackson2HttpMessageConverter에 의해서 JSON 형태로 출력한다.

**실행**
**name = 김미래** 
![Image](https://github.com/user-attachments/assets/bcdc047c-fe6c-4a35-b894-0f6d3954cf53)
객체로 반환해서 JSON형태로 출력한다. (@ResponseBody를 썼을 때)
~~~ json
{"name":"김미래"}
~~~

**@ResponseBody 사용 원리**
![[Pasted image 20250101192724.png]]
- @ResponseBody를 사용
	- HTTP의 BODY에 문자 내용을 직접 반환
	- viewResolver 대신에 HttpMessageConverter가 동작
	- 기본 문자처리: StringHttpMessageConverter
	- 기본 객체처리: MappingJackson2HttpMessageConverter
	- byte 처리 등등 기타 여러 HttpMessageConverter가 기본으로 등록되어 있다.
