package com.internet.shop.controller;

import com.internet.shop.dto.comment.CommentRequestDto;
import com.internet.shop.dto.comment.CommentResponseDto;
import com.internet.shop.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/comments")
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/product/{productId}")
    public ResponseEntity<CommentResponseDto> create(@PathVariable("productId") Long productId,
                                                     @RequestBody @Valid CommentRequestDto commentRequestDto) {

        return ResponseEntity.ok(commentService.create(productId, commentRequestDto));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> update(@PathVariable("commentId") Long commentId,
                                                     @RequestBody @Valid CommentRequestDto commentRequestDto) {
        return ResponseEntity.ok(commentService.update(commentId, commentRequestDto));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> delete(@PathVariable("commentId") Long commentId) {
        commentService.deleteById(commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> getById(@PathVariable("commentId") Long commentId) {
        return ResponseEntity.ok(commentService.getById(commentId));
    }

}
