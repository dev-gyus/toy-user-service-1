package com.example.toyuserservice.service.impl;

import com.example.toyuserservice.constants.DatabaseConstants;
import com.example.toyuserservice.constants.MessageKeys;
import com.example.toyuserservice.constants.ModelMapperConstants;
import com.example.toyuserservice.domain.dao.UserDao;
import com.example.toyuserservice.domain.dto.UserDto;
import com.example.toyuserservice.repository.UserRepository;
import com.example.toyuserservice.repository.impl.UserRepositoryImpl;
import com.example.toyuserservice.repository.template.CollectionIdGenerateRepository;
import com.example.toyuserservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CommonUserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CollectionIdGenerateRepository collectionIdGenerateRepository;
    private final PasswordEncoder passwordEncoder;
    private final MessageSource messageSource;
    private final UserRepositoryImpl userRepositoryImpl;

    @Override
    public UserDto.Common createUser(String email, String password, String nickName, String mobile) {
        // 이메일, 닉네임 중복체크
        emailAndNickNameDuplicationCheck(email, nickName);
        // 시퀀셜 Id 가져오기
        Long sequentialId = collectionIdGenerateRepository.generateId(DatabaseConstants.Collection.NAME_COLLECTION_ID_GENERATE);
        // 패스워드 인코딩해서 User 저장하기
        UserDao savedDao = userRepository.save(new UserDao(sequentialId, email, passwordEncoder.encode(password), mobile, nickName));
        // Dao -> Dto
        //
        return ModelMapperConstants.map(savedDao, UserDto.Common.class);
    }

    private void emailAndNickNameDuplicationCheck(String email, String nickname) {
        if(userRepository.existsByEmail(email))
            throw new RuntimeException(messageSource.getMessage(MessageKeys.PARAMS_DUPLICATE_EMAIL, null, LocaleContextHolder.getLocale()));

        if(userRepository.existsByNickName(nickname))
            throw new RuntimeException(messageSource.getMessage(MessageKeys.PARAMS_DUPLICATE_NICKNAME, null, LocaleContextHolder.getLocale()));
    }

    // 유저 정보 찾기
    @Override
    public UserDto.Common findUser(Long userId) {
        return userRepository.findByUserId(userId);
    }

    // 유저 정보 수정
    @Override
    public UserDto.Common updateUser(Long userId, String nickName, String mobile) {
        // 시퀀셜 Id 유무 확인. 없으면 예외
        if(!userRepository.existsByUserId(userId))
            throw new RuntimeException(messageSource.getMessage(MessageKeys.USER_NOT_FOUND, null, LocaleContextHolder.getLocale()));
        // user 정보 수정
        return userRepositoryImpl.updateUserDto(userId, nickName, mobile);
    }
}