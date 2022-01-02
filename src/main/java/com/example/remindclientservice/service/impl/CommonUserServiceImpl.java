package com.example.remindclientservice.service.impl;

import com.example.remindclientservice.constants.DatabaseConstants;
import com.example.remindclientservice.constants.MessageKeys;
import com.example.remindclientservice.constants.ModelMapperConstants;
import com.example.remindclientservice.domain.dao.UserDao;
import com.example.remindclientservice.domain.dto.UserDto;
import com.example.remindclientservice.repository.UserRepository;
import com.example.remindclientservice.repository.template.CollectionIdGenerateRepository;
import com.example.remindclientservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommonUserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CollectionIdGenerateRepository collectionIdGenerateRepository;
    private final PasswordEncoder passwordEncoder;
    private final MessageSource messageSource;

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

    @Override
    public UserDto.Common findUser(Long userId) {
        return userRepository.findByUserId(userId);
    }
}
