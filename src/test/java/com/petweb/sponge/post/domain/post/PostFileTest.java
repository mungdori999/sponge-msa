package com.petweb.sponge.post.domain.post;

import com.petweb.sponge.post.domain.post.PostFile;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PostFileTest {


    @Test
    public void POSTFILE를_안전하게_생성할_수_있다() {
        // given
        String fileUrl = "testUrl";

        // when
        PostFile postFile = PostFile.from(fileUrl);

        // then
        assertThat(postFile.getFileUrl()).isEqualTo("testUrl");

    }
}
