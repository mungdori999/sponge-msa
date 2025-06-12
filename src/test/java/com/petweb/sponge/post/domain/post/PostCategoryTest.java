package com.petweb.sponge.post.domain.post;

import com.petweb.sponge.post.domain.post.PostCategory;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PostCategoryTest {



    @Test
    public void POSTCATEGORY를_안전하게_생성할_수_있다() {
        // given
        Long categoryCode = 200L;

        // when
        PostCategory postCategory = PostCategory.from(categoryCode);

        // then
        assertThat(postCategory.getCategoryCode()).isEqualTo(200L);

    }
}
