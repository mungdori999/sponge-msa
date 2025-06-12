package com.petweb.sponge.jwt;

import org.springframework.data.repository.CrudRepository;

public interface RefreshRedisRepository extends CrudRepository<RefreshTokenEntity,String> {


}
