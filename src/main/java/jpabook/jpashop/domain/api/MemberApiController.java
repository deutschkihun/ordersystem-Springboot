package jpabook.jpashop.domain.api;


import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController  // REST API Controller
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    // API 에서 멤버저장
    @PostMapping("api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
       Long id =  memberService.join(member);
       return new CreateMemberResponse(id);
    }
    // api 를 만들때에는 entity(예를들어 Member,Order,Delivery.....)를 매개변수로 받지 말 것!!!
    // 이유 : 예를들어 entity properties 의 이름을 변경하게 되면 api 스펙자체가 바뀌기 때문에 다른것들과 통신 하는데 문제가 발생할수 있다.
    // tip : entity 는 최대한 순수하게 유지 해야 한다.


    @PostMapping("api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request){
        Member member = new Member();
        member.setName(request.getName());
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }


    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(@PathVariable("id") Long id, @RequestBody @Valid UpdateMemberRequest request) {

        memberService.update(id,request.getName());
        Member findMember = memberService.findOne(id);
        // 단순히 업데이트 하고 리턴하는것이 아니라 업데이트 한것은 다시 찾아서 호출해준다.
        return new UpdateMemberResponse(findMember.getId(),findMember.getName());
   }


   @GetMapping("/api/v1/members")
   public List<Member> memberV1(){
        return memberService.findMembers();
   }

   @GetMapping("api/v2/members")
   public Result memberV2(){
       List<Member> findMembers = memberService.findMembers();
       List<MemberDto> collect = findMembers.stream()
               .map(m -> new MemberDto(m.getName()))
               .collect(Collectors.toList());

       /*for (Member findMember : findMembers) {
           new MemberDto(findMember.getName());
       }*/

       return new Result(collect.size(),collect);

    }
   @Data
   @AllArgsConstructor
   static class Result<T>{
        private int count;
        // java Generic = Object
        private T data;
   }

   @Data
   @AllArgsConstructor
   static class MemberDto{
        private String name;
   }


    // DTO : Data transfer object, 별도로 만들어라, entity 쓰지 말고
    @Data
    static class CreateMemberRequest {
        private String name;
    }




    // DTO & Entity : 비추천 !!
    @Data
    static class CreateMemberResponse {
        private Long id;
        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }


    @Data
    static class UpdateMemberRequest{
        private String name;
    }

    @Data
    @AllArgsConstructor // Access level modifier setting 가능
    static class UpdateMemberResponse{
        private Long id;
        private String name;
    }

}