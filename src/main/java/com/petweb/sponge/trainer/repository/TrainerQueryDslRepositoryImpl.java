package com.petweb.sponge.trainer.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import static com.petweb.sponge.trainer.repository.QHistoryEntity.*;
import static com.petweb.sponge.trainer.repository.QTrainerAddressEntity.*;
import static com.petweb.sponge.trainer.repository.QTrainerEntity.*;

public class TrainerQueryDslRepositoryImpl implements TrainerQueryDslRepository{
    private final JPAQueryFactory queryFactory;


    public TrainerQueryDslRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<TrainerEntity> findTrainerById(Long id) {
        TrainerEntity trainer = queryFactory
                .selectFrom(trainerEntity)
                .where(trainerEntity.id.eq(id))
                .fetchOne();
        List<TrainerAddressEntity> trainerAddressEntityList = queryFactory.selectFrom(trainerAddressEntity)
                .where(trainerAddressEntity.trainerEntity.id.eq(id))
                .fetch();
        List<HistoryEntity> historyEntityList = queryFactory.selectFrom(historyEntity)
                .where(historyEntity.trainerEntity.id.eq(id))
                .fetch();
        if (trainer != null) {
            trainer.addTrainerAddressList(trainerAddressEntityList);
            trainer.addHistoryList(historyEntityList);
        }
        return Optional.ofNullable(trainer);
    }

    @Override
    public void initTrainer(Long id) {
            queryFactory
                    .delete(historyEntity)
                    .where(historyEntity.trainerEntity.id.eq(id))
                    .execute();
            queryFactory
                    .delete(trainerAddressEntity)
                    .where(trainerAddressEntity.trainerEntity.id.eq(id))
                    .execute();
    }
}
