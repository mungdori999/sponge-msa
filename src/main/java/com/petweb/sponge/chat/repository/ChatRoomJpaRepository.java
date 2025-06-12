package com.petweb.sponge.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRoomJpaRepository extends JpaRepository<ChatRoomEntity, Long> {


    @Query(value = """
                SELECT * FROM chat_room 
                WHERE trainer_id = :trainerId 
                ORDER BY 
                    CASE 
                        WHEN modified_at IS NOT NULL AND modified_at > 0 THEN modified_at 
                        ELSE created_at 
                    END DESC
                LIMIT :limit OFFSET :offset
            """, nativeQuery = true)
    List<ChatRoomEntity> findListByTrainerId(@Param("trainerId") Long loginId, @Param("limit") int limit, @Param("offset") int offset);

    @Query(value = """
                SELECT * FROM chat_room 
                WHERE user_id = :userId 
                ORDER BY 
                    CASE 
                        WHEN modified_at IS NOT NULL AND modified_at > 0 THEN modified_at 
                        ELSE created_at 
                    END DESC
                LIMIT :limit OFFSET :offset
            """, nativeQuery = true)
    List<ChatRoomEntity> findListByUserId(@Param("userId") Long loginId, @Param("limit") int limit, @Param("offset") int offset);
}
