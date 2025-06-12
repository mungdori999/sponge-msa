INSERT INTO trainers (
    id, email, name, gender, phone, profile_img_url, content, years, adopt_count, score, chat_count, created_at
) VALUES
      (1, 'trainer1@test.com', '김트레이너', 1, '01012345678', '', '안녕하세요. 트레이너입니다.', 5, 0, 0, 0, 1700000000),

      (2, 'trainer2@test.com', '이트레이너', 3, '01098765432', 'http://example.com/img2.jpg', '안녕하세요 훈련사입니다', 7, 0, 0, 0, 1700000001),

      (3, 'trainer3@test.com', '박트레이너', 1, '01056781234', 'http://example.com/img3.jpg', '안녕', 10, 0, 0, 0, 1700000002);



INSERT INTO history (id, title, start_dt, end_dt, description, trainer_id)
VALUES
    (1, '강아지훈련소 A', '201801', '202012', '강아지 기본 훈련 및 행동 교정', 1),
    (2, '강아지훈련소 B', '202101', '202306', '고급 훈련 및 반려동물 상담', 1),
    (3, '펫케어 서비스 C', '202007', '202205', '반려동물 심리 분석 및 훈련', 1);



INSERT INTO trainer_address (id, city, town, trainer_id)
VALUES
    (1, '서울', '강남구', 1),
    (2, '부산', '해운대구', 1),
    (3, '대구', '수성구', 1);
