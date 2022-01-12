package com.example.toyuserservice.constants;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ModelMapperConstants {
    private static ModelMapper modelMapper;

    public ModelMapperConstants() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        ModelMapperConstants.modelMapper = modelMapper;
    }

    public static <T> T map(Object object, Class<T> tarClass) {
        Objects.requireNonNull(object);
        return modelMapper.map(object, tarClass);
    }

    public static <S, T> List<T> mapAll(List<S> object, Class<T> tarClass) {
        Objects.requireNonNull(object);
        return object.stream().map(obj -> modelMapper.map(obj, tarClass)).collect(Collectors.toList());
    }
}
