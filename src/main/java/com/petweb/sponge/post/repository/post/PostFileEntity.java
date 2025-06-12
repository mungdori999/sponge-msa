package com.petweb.sponge.post.repository.post;

import com.petweb.sponge.post.domain.post.PostFile;
import com.petweb.sponge.post.repository.post.PostEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "post_file")
public class PostFileEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String fileUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private PostEntity postEntity;

    @Builder
    public PostFileEntity(Long id, String fileUrl, PostEntity postEntity) {
        this.id = id;
        this.fileUrl = fileUrl;
        this.postEntity = postEntity;
    }

    public PostFile toModel() {
        return PostFile.builder()
                .id(id)
                .fileUrl(fileUrl).build();
    }
}
