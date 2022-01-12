package com.example.toyuserservice.repository;

import com.example.toyuserservice.domain.dao.UserDao;
import com.example.toyuserservice.domain.dto.UserDto;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserDao, String> {
    boolean existsByEmail(String email);

    boolean existsByNickName(String nickName);

    /** 시퀀셜 Id로 Dto 가져오기 **/
    UserDto.Common findByUserId(Long userId);

    /** 시퀀셜 Id 유무 확인 **/
    boolean existsByUserId(Long userId);
}
