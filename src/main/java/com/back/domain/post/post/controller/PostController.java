package com.back.domain.post.post.controller;

import com.back.domain.post.post.entity.Post;
import com.back.domain.post.post.service.PostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor

public class PostController {
    private final PostService postService;

    @ModelAttribute("siteName")
    public String siteName() {
        return "커뮤니티 사이트 A";
    }

    @AllArgsConstructor
    @Getter
    @Setter
    public static class ModifyForm {
        @NotBlank(message = "01-title-제목을 입력해주세요.")
        @Size(min = 2, max = 20, message = "02-title-제목은 2자 이상, 20자 이하로 입력가능합니다.")
        private String title;
        @NotBlank(message = "03-content-내용을 입력해주세요.")
        @Size(min = 2, max = 20, message = "04-content-내용은 2자 이상, 20자 이하로 입력가능합니다.")
        private String content;
    }

    @GetMapping("/posts/{id}/modify")
    @Transactional(readOnly = true)
    public String showModify(
            @PathVariable int id,
            @ModelAttribute("form") ModifyForm form,
            Model model
    ) {
        Post post = postService.findById(id).get();

        model.addAttribute("post", post);
        form.setTitle(post.getTitle());
        form.setContent(post.getContent());

        return "post/post/modify";
    }


    @PostMapping("/posts/{id}/modify")
    @Transactional
    public String modify(
            @PathVariable int id,
            @Valid @ModelAttribute("form") ModifyForm form,
            BindingResult bindingResult,
            Model model
    ) {
        Post post = postService.findById(id).get();
        model.addAttribute("post", post);

        if (bindingResult.hasErrors()) {
            return "post/post/modify";
        }

        postService.modify(post, form.getTitle(), form.getContent());

        return "redirect:/posts/" + post.getId();
    }

    @AllArgsConstructor
    @Getter
    public static class WriteForm {//클래스 안의 클래스는 static
        @NotBlank(message = "01-title-제목을 입력해주세요.")
        @Size(min = 2, max = 20, message = "02-title-제목은 2자 이상, 20자 이하로 입력가능합니다.")
        private String title;
        @NotBlank(message = "03-content-내용을 입력해주세요.")
        @Size(min = 2, max = 20, message = "04-content-내용은 2자 이상, 20자 이하로 입력가능합니다.")
        private String content;
    }

    @GetMapping("/posts/write")
    public String showWrite(@ModelAttribute("form") WriteForm form) {
        return "post/post/write";
    }


    @PostMapping("/posts/write")
    @Transactional
    public String write(
            @ModelAttribute("form") @Valid WriteForm form,//이 두 줄의 순서는 바뀌면 안됨 @ModelAttribute가 생략되어있다.
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) { //에러 발생시 등록을 멈추고 폼 화면을 다시 띄워줌, n개의 에러를 받아옴


            return "post/post/write";

        }

        Post post = postService.write(form.getTitle(), form.getContent());

        return "redirect:/posts/" + post.getId();
    }


    @GetMapping("/posts/{id}")
    @Transactional(readOnly = true)
    public String showDetail(
            @PathVariable int id,
            Model model
    ) {
        Post post = postService.findById(id).get();

        model.addAttribute("post", post);

        return "post/post/detail";
    }

    @GetMapping("/posts")
    @Transactional(readOnly = true)
    @ResponseBody
    public List<Post> showList() { // 데이터 불러오는 함수
        return postService.findAll();
    }

    @GetMapping("/posts/listByFetch")
    public String showListByFetch() { // 템플릿 불러오는 함수
        return "post/post/listByFetch";
    }
    /*
    //다건 조회 그룹
    @GetMapping("/posts")
    @Transactional(readOnly = true)
    public String showList(Model mod) {
        List<Post> posts = postService.findAll();

        mod.addAttribute("posts", posts);

        return "post/post/list";
    }

    @GetMapping("/posts/")
    public String redirectToList() {
        return "redirect:/posts"; //올바른 주소로 가도록 해줌
    }

     */
}
