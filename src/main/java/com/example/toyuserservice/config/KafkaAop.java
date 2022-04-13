package com.example.toyuserservice.config;

import com.example.toyuserservice.repository.impl.UserRepositoryImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.kafka.KafkaException;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class KafkaAop {

    private final UserRepositoryImpl userRepository;
//
    @Around("execution(* com.example.toyuserservice.service.UserService..*(..))")
    public void execute(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            log.info("kafka message send 진입. signature:{}", joinPoint.getSignature().toShortString());
            joinPoint.proceed();
            log.info("message send 성공");
        } catch (KafkaException e) {
            log.info("message send failed. trying direct message save");
            Object[] args = joinPoint.getArgs();
            if (args.length < 1) return;
            userRepository.updateUserDto((Long) args[0], args.length > 1 ? (String) args[1] : null, args.length > 2 ? (String) args[2] : null);
            log.info("direct message save completed");
        }
    }

}
