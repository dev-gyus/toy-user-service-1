package com.example.toyuserservice.service.impl;

import com.example.toyuserservice.constants.DatabaseConstants;
import com.example.toyuserservice.constants.DatabaseConstants.Field;
import com.example.toyuserservice.constants.KafkaConstants;
import com.example.toyuserservice.constants.KafkaConstants.Topic;
import com.example.toyuserservice.constants.MessageKeys;
import com.example.toyuserservice.constants.ModelMapperConstants;
import com.example.toyuserservice.domain.common.ErrorCode;
import com.example.toyuserservice.domain.dao.UserDao;
import com.example.toyuserservice.domain.dto.UserDto;
import com.example.toyuserservice.exception.CustomException;
import com.example.toyuserservice.kafka_listener.KafkaTestDto;
import com.example.toyuserservice.repository.UserRepository;
import com.example.toyuserservice.repository.impl.UserRepositoryImpl;
import com.example.toyuserservice.repository.template.CollectionIdGenerateRepository;
import com.example.toyuserservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaFailureCallback;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommonUserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CollectionIdGenerateRepository collectionIdGenerateRepository;
    private final PasswordEncoder passwordEncoder;
    private final MessageSource messageSource;
    private final UserRepositoryImpl userRepositoryImpl;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public UserDto.Common createUser(String email, String password, String nickName, String mobile) {
        // ?????????, ????????? ????????????
        emailAndNickNameDuplicationCheck(email, nickName);
        // ????????? Id ????????????
        Long sequentialId = collectionIdGenerateRepository.generateId(DatabaseConstants.Collection.NAME_COLLECTION_ID_GENERATE);
        // ???????????? ??????????????? User ????????????
        UserDao savedDao = userRepository.save(new UserDao(sequentialId, email, passwordEncoder.encode(password), mobile, nickName));
        // Dao -> Dto
        //
        return ModelMapperConstants.map(savedDao, UserDto.Common.class);
    }

    private void emailAndNickNameDuplicationCheck(String email, String nickname) {
        if(userRepository.existsByEmail(email))
            throw new CustomException(ErrorCode.DUPLICATED_EMAIL);

        if(userRepository.existsByNickName(nickname))
            throw new CustomException(ErrorCode.DUPLICATED_NICKNAME);
    }

    // ?????? ?????? ??????
    @Override
    public UserDto.Common findUser(Long userId) {
        return userRepository.findByUserId(userId);
    }
//
    // ?????? ?????? ??????
    @Retryable(value = KafkaException.class, maxAttempts = 1, backoff = @Backoff(value = 500L))
    @Override
    public void updateUser(Long userId, String nickName, String mobile) throws KafkaException {
        // ????????? Id ?????? ??????. ????????? ??????
        if(!userRepository.existsByUserId(userId))
            throw new RuntimeException(messageSource.getMessage(MessageKeys.USER_NOT_FOUND, null, LocaleContextHolder.getLocale()));
        // kafka message ??????
        Update update = new Update();
        if(nickName != null) update.set(Field.NAME_NICK_NAME, nickName);
        if(mobile != null) update.set(Field.NAME_MOBILE, mobile);
//        Map<String, Object> data = new LinkedHashMap<>();
//        data.put(KafkaConstants.Key.USER_ID, userId);
//        data.put(KafkaConstants.Key.UPDATE_OBJECT, update.getUpdateObject());
        KafkaTestDto.User dto1 = new KafkaTestDto.User(userId, update.getUpdateObject(), KafkaTestDto.User.class.getName());
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(Topic.UPDATE_USER, dto1);
        future.addCallback(result -> {
                    log.info("message send success");
                },
                (KafkaFailureCallback<String,Object>) failResult -> {
                    throw failResult;
                });

        KafkaTestDto.User2 dto2 = new KafkaTestDto.User2(userId, update.getUpdateObject(), KafkaTestDto.User2.class.getName());
        ListenableFuture<SendResult<String, Object>> future2 = kafkaTemplate.send(Topic.UPDATE_USER2, dto2);
        future2.addCallback(result -> {
                    log.info("message send success");
                },
                (KafkaFailureCallback<String,Object>) failResult -> {
                    throw failResult;
                });
        // user ?????? ??????
//        return userRepositoryImpl.updateUserDto(userId, nickName, mobile);
    }
}
