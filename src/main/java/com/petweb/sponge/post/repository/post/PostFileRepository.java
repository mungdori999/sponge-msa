package com.petweb.sponge.post.repository.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostFileRepository extends JpaRepository<PostFileEntity,Long> {

    @Modifying
    @Query("DELETE FROM PostFileEntity pf WHERE pf.fileUrl IN :fileUrlList")
    void deleteByFiles(List<String> fileUrlList);
}
