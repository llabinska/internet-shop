package com.internet.shop.service;

import com.internet.shop.dmo.Comment;
import com.internet.shop.dmo.Product;
import com.internet.shop.dmo.User;
import com.internet.shop.dto.comment.CommentRequestDto;
import com.internet.shop.dto.comment.CommentResponseDto;
import com.internet.shop.exception.CommentNotFoundException;
import com.internet.shop.mapper.CommentConverter;
import com.internet.shop.repository.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    public static final String COMMENT_NOT_FOUND_MSG = "Comment with id=%s not found";
    private final ProductService productService;
    private final UserService userService;
    private final CommentRepository commentRepository;
    private final CommentConverter commentConverter;

    @Transactional
    public CommentResponseDto create(Long productId, CommentRequestDto commentRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(authentication.getName());

        Product product = productService.getProductById(productId);

        Comment comment = new Comment();
        comment.setMessage(commentRequestDto.getMessage());
        comment.setProduct(product);
        comment.setUser(user);

        Comment savedComment = commentRepository.save(comment);
        return commentConverter.toCommentResponseDto(savedComment);
    }

    @Transactional
    public CommentResponseDto update(Long commentId, CommentRequestDto commentRequestDto) {
        Comment comment = getCommentById(commentId);
        comment.setMessage(commentRequestDto.getMessage());
        Comment savedComment = commentRepository.save(comment);
        return commentConverter.toCommentResponseDto(savedComment);
    }

    public CommentResponseDto getById(Long commentId) {
        Comment comment = getCommentById(commentId);
        return commentConverter.toCommentResponseDto(comment);
    }

    @Transactional
    public void deleteById(Long commentId) {
        getCommentById(commentId);
        commentRepository.deleteById(commentId);
    }

    private Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new CommentNotFoundException(String.format(COMMENT_NOT_FOUND_MSG, commentId)));
    }

}
