package com.petweb.sponge.post.repository.post;

import com.petweb.sponge.utils.CategoryCode;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.petweb.sponge.post.repository.post.QBookmarkEntity.*;
import static com.petweb.sponge.post.repository.post.QPostCategoryEntity.*;
import static com.petweb.sponge.post.repository.post.QPostEntity.*;
import static com.petweb.sponge.post.repository.post.QPostFileEntity.*;
import static com.petweb.sponge.post.repository.post.QTagEntity.*;

public class PostQueryDslRepositoryImpl implements PostQueryDslRepository {

    private final JPAQueryFactory queryFactory;
    private static final int PAGE_SIZE = 10;  // 페이지당 항목 수

    public PostQueryDslRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<PostEntity> findPostById(Long id) {
        PostEntity post = queryFactory
                .selectFrom(postEntity)
                .where(postEntity.id.eq(id))
                .fetchOne();
        List<PostFileEntity> postFileEntityList = queryFactory
                .selectFrom(postFileEntity)
                .where(postFileEntity.postEntity.id.eq(id))
                .fetch();
        List<TagEntity> tagEntityList = queryFactory
                .selectFrom(tagEntity)
                .where(tagEntity.postEntity.id.eq(id))
                .fetch();
        List<PostCategoryEntity> postCategoryEntityList = queryFactory
                .selectFrom(postCategoryEntity)
                .where(postCategoryEntity.postEntity.id.eq(id))
                .fetch();
        if (post != null) {
            post.addPostFileList(postFileEntityList);
            post.addTagList(tagEntityList);
            post.addPostCategoryList(postCategoryEntityList);
        }
        return Optional.ofNullable(post);
    }

    @Override
    public List<PostEntity> findListByUserId(Long userId, int page) {
        // 페이지 번호와 페이지 크기를 계산
        int offset = page * PAGE_SIZE;
        return queryFactory
                .select(postEntity)
                .from(postEntity)
                .leftJoin(postEntity.postCategoryEntityList, postCategoryEntity).fetchJoin()
                .where(postEntity.userId.eq(userId))
                .orderBy(postEntity.createdAt.desc()) //최신순 정렬
                .offset(offset)
                .limit(PAGE_SIZE)
                .fetch();

    }

    @Override
    public List<PostEntity> findListByKeyword(String keyword, int page) {
        // 페이지 번호와 페이지 크기를 계산
        int offset = page * PAGE_SIZE;
        List<PostEntity> postEntityList = queryFactory
                .select(postEntity)
                .from(postEntity)
                .leftJoin(postEntity.tagEntityList, tagEntity).fetchJoin()
                .where(postEntity.title.containsIgnoreCase(keyword) // 제목에서 검색
                        .or(postEntity.content.containsIgnoreCase(keyword))
                        .or(tagEntity.hashtag.containsIgnoreCase(keyword)))
                .orderBy(postEntity.createdAt.desc()) //최신순 정렬
                .offset(offset)
                .limit(PAGE_SIZE)
                .fetch();

        List<Long> postIdList = postEntityList.stream().map(PostEntity::getId).toList();

        // 포스트 카테고리 조인
        List<PostCategoryEntity> postCategoryEntityList = queryFactory
                .selectFrom(postCategoryEntity)
                .where(postCategoryEntity.postEntity.id.in(postIdList))
                .fetch();
        postEntityList.forEach(post -> post.addPostCategoryList(postCategoryEntityList.stream().filter(
                category -> Objects.equals(post.getId(), category.getPostEntity().getId())
        ).toList()));
        return postEntityList;
    }


    @Override
    public List<PostEntity> findListByCode(Long categoryCode, int page) {
        // 페이지 번호와 페이지 크기를 계산
        int offset = page * PAGE_SIZE;
        if (Objects.equals(categoryCode, CategoryCode.ALL.getCode())) {
            //전체 조회
            return queryFactory
                    .select(postEntity)
                    .from(postEntity)
                    .leftJoin(postEntity.postCategoryEntityList, postCategoryEntity).fetchJoin()
                    .orderBy(postEntity.createdAt.desc()) //최신순 정렬
                    .offset(offset)
                    .limit(PAGE_SIZE)
                    .fetch();
        } else {
            // 카테고리에 해당하는 글 ID 조회
            List<Long> postIdList = queryFactory
                    .select(postCategoryEntity.postEntity.id)
                    .from(postCategoryEntity)
                    .where(postCategoryEntity.categoryCode.eq(categoryCode))
                    .orderBy(postCategoryEntity.postEntity.createdAt.desc())  // 최신순으로 정렬
                    .offset(offset)
                    .limit(PAGE_SIZE)
                    .fetch();
            if (postIdList.isEmpty()) {
                return new ArrayList<>();  // 결과가 없으면 빈 리스트 반환
            }
            return queryFactory
                    .selectDistinct(postEntity)
                    .from(postEntity)
                    .leftJoin(postEntity.postCategoryEntityList, postCategoryEntity).fetchJoin()
                    .orderBy(postEntity.createdAt.desc()) //최신순 정렬
                    .where(postEntity.id.in(postIdList))  // IN 절 사용
                    .fetch();
        }
    }

    @Override
    public List<PostEntity> findListByIdList(List<Long> postIdList) {
        if (postIdList.isEmpty()) {
            return new ArrayList<>();  // 결과가 없으면 빈 리스트 반환
        }
        return queryFactory
                .selectDistinct(postEntity)
                .from(postEntity)
                .leftJoin(postEntity.postCategoryEntityList, postCategoryEntity).fetchJoin()
                .orderBy(postEntity.createdAt.desc()) //최신순 정렬
                .where(postEntity.id.in(postIdList))  // IN 절 사용
                .fetch();
    }

    @Override
    public void initPost(Long id) {
        queryFactory
                .delete(postCategoryEntity)
                .where(postCategoryEntity.postEntity.id.eq(id))
                .execute();
        queryFactory
                .delete(postFileEntity)
                .where(postFileEntity.postEntity.id.eq(id))
                .execute();
        queryFactory
                .delete(tagEntity)
                .where(tagEntity.postEntity.id.eq(id))
                .execute();
    }
}