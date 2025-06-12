package com.petweb.sponge.post.domain.post;

import com.petweb.sponge.post.domain.post.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class TagTest {


    @Test
    public void TAG를_안전하게_생성할_수_있다() {
        // given
        String hashTag = "테스트태그";

        // when
        Tag tag = Tag.from(hashTag);

        // then
        assertThat(tag.getHashtag()).isEqualTo("테스트태그");

    }
}
