package com.petweb.sponge.pet.service;

import com.petweb.sponge.exception.error.NotFoundPet;
import com.petweb.sponge.exception.error.NotFoundUser;
import com.petweb.sponge.pet.domain.Pet;
import com.petweb.sponge.pet.dto.PetCreate;
import com.petweb.sponge.pet.dto.PetUpdate;
import com.petweb.sponge.pet.repository.PetRepository;
import com.petweb.sponge.user.domain.User;
import com.petweb.sponge.user.repository.UserRepository;
import com.petweb.sponge.utils.ClockHolder;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Builder
public class PetService {
    private final PetRepository petRepository;
    private final UserRepository userRepository;
    private final ClockHolder clockHolder;

    /**
     * 펫 정보 단건 조회
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public Pet getById(Long id) {
        return petRepository.findById(id).orElseThrow(NotFoundPet::new);
    }

    /**
     * 펫 userId 해당 건 전체 조회
     *
     * @param userId
     * @return
     */
    @Transactional(readOnly = true)
    public List<Pet> getAllByUserId(Long userId) {
        return petRepository.findAllByUserId(userId);

    }

    /**
     * 펫 정보 저장
     *
     * @param loginId
     * @param petCreate
     * @return
     */
    @Transactional
    public Pet create(Long loginId, PetCreate petCreate) {
        //현재 로그인 유저 정보 가져오기
        User user = userRepository.findById(loginId).orElseThrow(
                NotFoundUser::new);
        Pet pet = Pet.from(user.getId(), petCreate, clockHolder);
        //반려견 저장
        return petRepository.save(pet);
    }

    /**
     * 펫 정보 업데이트
     *
     * @param loginId
     * @param id
     * @param petUpdate
     * @return
     */
    @Transactional
    public Pet update(Long loginId, Long id, PetUpdate petUpdate) {
        Pet pet = petRepository.findById(id).orElseThrow(NotFoundPet::new);
        pet.checkUser(loginId);
        pet = pet.update(petUpdate);
        petRepository.save(pet);
        return pet;
    }

    /**
     * 펫 정보 삭제
     *
     * @param loginId
     * @param id
     */
    @Transactional
    public void delete(Long loginId, Long id) {
        Pet pet = petRepository.findById(id).orElseThrow(NotFoundPet::new);
        pet.checkUser(loginId);
        petRepository.delete(pet);
    }

    /**
     * 펫 이미지 삭제
     *
     * @param loginId
     * @param petId
     */
    @Transactional
    public void deletePetImg(Long loginId, Long petId) {
//        PetEntity petEntity = petRepository.findById(petId).orElseThrow(NotFoundPet::new);
//        if (!petEntity.getUserEntity().getId().equals(loginId)) {
//            throw new LoginIdError();
//        }
//        petEntity.setPetImgUrl(null);
    }


}
