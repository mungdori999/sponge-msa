package com.petweb.sponge.pet.controller;

import com.petweb.sponge.auth.UserAuth;
import com.petweb.sponge.log.Logging;
import com.petweb.sponge.pet.controller.response.PetResponse;
import com.petweb.sponge.pet.domain.Pet;
import com.petweb.sponge.pet.dto.PetCreate;
import com.petweb.sponge.pet.dto.PetUpdate;
import com.petweb.sponge.pet.service.PetService;
import com.petweb.sponge.utils.AuthorizationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pet")
@Logging
public class PetController {

    private final PetService petService;
    private final AuthorizationUtil authorizationUtil;


    /**
     * 반려동물 정보 단건 조회
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<PetResponse> getById(@PathVariable("id") Long id) {
        Pet pet = petService.getById(id);
        return new ResponseEntity<>(PetResponse.from(pet), HttpStatus.OK);
    }


    /**
     * 반려동물 전체 조회
     *
     * @param userId
     * @return
     */
    @GetMapping()
    public ResponseEntity<List<PetResponse>> getAllByUserId(@RequestParam Long userId) {
        List<Pet> petList = petService.getAllByUserId(userId);
        return new ResponseEntity<>(petList.stream().map(PetResponse::from).collect(Collectors.toList()), HttpStatus.OK);
    }

    /**
     * 내정보로 조회
     * @return
     */
    @GetMapping("/my_info")
    @UserAuth
    public ResponseEntity<List<PetResponse>> getMyInfo() {
        List<Pet> petList = petService.getAllByUserId(authorizationUtil.getLoginId());
        return new ResponseEntity<>(petList.stream().map(PetResponse::from).collect(Collectors.toList()), HttpStatus.OK);
    }


    /**
     * 반려동물 등록
     *
     * @param petCreate
     * @return
     */
    @PostMapping
    @UserAuth
    public ResponseEntity<PetResponse> create(@RequestBody PetCreate petCreate) {
        Pet pet = petService.create(authorizationUtil.getLoginId(), petCreate);
        return new ResponseEntity<>(PetResponse.from(pet), HttpStatus.OK);
    }

    /**
     * 반려동물 수정
     *
     * @param id
     * @param petUpdate
     * @return
     */
    @PatchMapping("/{id}")
    @UserAuth
    public ResponseEntity<PetResponse> update(@PathVariable("id") Long id, @RequestBody PetUpdate petUpdate) {
        Pet pet = petService.update(authorizationUtil.getLoginId(), id, petUpdate);
        return new ResponseEntity<>(PetResponse.from(pet),HttpStatus.OK);
    }

    /**
     * 반려동물 삭제
     *
     * @param id
     */
    @DeleteMapping("/{id}")
    @UserAuth
    public void delete(@PathVariable("id") Long id) {
        petService.delete(authorizationUtil.getLoginId(), id);
    }


}
