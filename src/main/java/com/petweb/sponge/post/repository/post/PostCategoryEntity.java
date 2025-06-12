package com.petweb.sponge.post.repository.post;

import com.petweb.sponge.post.domain.ProblemType;
import com.petweb.sponge.post.domain.post.PostCategory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "post_category")
public class PostCategoryEntity {

    @Id
    @GeneratedValue
    private Long id;

    private Long categoryCode;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private PostEntity postEntity;

    @Builder
    public PostCategoryEntity(Long id, Long categoryCode, PostEntity postEntity) {
        this.id = id;
        this.categoryCode = categoryCode;
        this.postEntity = postEntity;
    }

    public PostCategory toModel() {
        return PostCategory.builder()
                .id(id)
                .categoryCode(categoryCode).build();
    }

}
