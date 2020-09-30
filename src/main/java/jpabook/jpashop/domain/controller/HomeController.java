package jpabook.jpashop.domain.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j  // 기록 저장 기능 (로그데이터)
public class HomeController {


    @RequestMapping("/")
    // RequestMapping 컨트롤러로 넘어오는 다양한 URL을
    // 각기 다른 뷰로 매핑하는 방식을 설계하기 위한 어노테이션이다.
    // 작업을 처리하기 위한것이 아닌 단순히 페이지 뷰 요청만 할떄 사용한다.

    public String home(){
        log.info("home controller");
        return "home";  // home.html
    }
}
