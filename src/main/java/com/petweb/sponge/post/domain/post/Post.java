package com.petweb.sponge.post.domain.post;


import com.petweb.sponge.exception.error.LoginIdError;
import com.petweb.sponge.post.dto.post.PostCreate;
import com.petweb.sponge.post.dto.post.PostUpdate;
import com.petweb.sponge.utils.ClockHolder;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class Post {
    private Long id;
    private PostContent postContent;
    private int likeCount;
    private int answerCount;
    private Long userId;
    private Long petId;
    private List<PostFile> postFileList;
    private List<Tag> tagList;
    private List<PostCategory> postCategoryList;

    @Builder
    public Post(Long id, PostContent postContent, int likeCount, int answerCount, Long userId, Long petId, List<PostFile> postFileList, List<Tag> tagList, List<PostCategory> postCategoryList) {
        this.id = id;
        this.postContent = postContent;
        this.likeCount = likeCount;
        this.answerCount = answerCount;
        this.userId = userId;
        this.petId = petId;
        this.postFileList = postFileList;
        this.tagList = tagList;
        this.postCategoryList = postCategoryList;
    }

    public static Post from(Long userId, Long petId, PostCreate postCreate, ClockHolder clockHolder) {
        return Post.builder()
                .postContent(PostContent.builder().title(postCreate.getTitle())
                        .content(postCreate.getContent())
                        .duration(postCreate.getDuration())
                        .createdAt(clockHolder.clock())
                        .modifiedAt(0L)
                        .build())
                .userId(userId)
                .petId(petId)
                .postFileList(postCreate.getFileUrlList().stream().map(PostFile::from).collect(Collectors.toList()))
                .tagList(postCreate.getHashTagList().stream().map(Tag::from).collect(Collectors.toList()))
                .postCategoryList(postCreate.getCategoryCodeList().stream().map(PostCategory::from).collect(Collectors.toList()))
                .build();
    }

    public Post update(PostUpdate postUpdate, ClockHolder clockHolder) {
        return Post.builder()
                .id(id)
                .postContent(PostContent.builder().title(postUpdate.getTitle()).content(postUpdate.getContent())
                        .duration(postUpdate.getDuration()).createdAt(postContent.getCreatedAt()).modifiedAt(clockHolder.clock()).build())
                .userId(userId)
                .petId(petId)
                .likeCount(likeCount)
                .answerCount(answerCount)
                .postFileList(postUpdate.getFileUrlList().stream().map((fileUrl) -> PostFile.builder().fileUrl(fileUrl).build()).collect(Collectors.toList()))
                .tagList(postUpdate.getHashTagList().stream().map((hasTag) -> Tag.builder().hashtag(hasTag).build()).collect(Collectors.toList()))
                .postCategoryList(postUpdate.getCategoryCodeList().stream().map(category -> PostCategory.builder().categoryCode(category).build()).collect(Collectors.toList()))
                .build();
    }

    /**
     * 로그인한 유저와 작성한 게시글의 아이디가 동일한지
     *
     * @param loginId
     */
    public void checkUser(Long loginId) {
        if (!userId.equals(loginId)) {
            throw new LoginIdError();
        }
    }

    public void increaseLikeCount() {
        likeCount++;
    }

    public void decreaseLikeCount() {
        if (likeCount <= 0) {
            throw new IllegalStateException();
        } else {
            likeCount--;
        }
    }


    public void increaseAnswerCount() {
        answerCount++;
    }

    public void decreaseAnswerCount() {
        if (answerCount <= 0) {
            throw new IllegalStateException();
        } else {
            answerCount--;
        }
    }
}
