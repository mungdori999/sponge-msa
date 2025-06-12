package com.petweb.sponge.post.repository.post;

import com.petweb.sponge.post.domain.post.Tag;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "tag")
public class TagEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String hashtag;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private PostEntity postEntity;

    @Builder
    public TagEntity(Long id, String hashtag, PostEntity postEntity) {
        this.id = id;
        this.hashtag = hashtag;
        this.postEntity = postEntity;
    }

    public Tag toModel() {
        return Tag.builder()
                .id(id)
                .hashtag(hashtag).build();
    }
}
