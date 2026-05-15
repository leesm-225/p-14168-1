package com.back.domain.post.post.controller;

import com.back.domain.post.post.entity.Post;
import com.back.domain.post.post.service.PostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

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

        model.addAttribute("post", post);

        return "post/post/writeDone";
    }
}
