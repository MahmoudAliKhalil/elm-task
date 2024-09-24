package com.github.mahmoudalikhalil.elm.mapper;

import com.github.mahmoudalikhalil.elm.model.dto.ClientUserRegistrationRequest;
import com.github.mahmoudalikhalil.elm.model.dto.SuperUserRegistrationRequest;
import com.github.mahmoudalikhalil.elm.model.entity.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.security.Principal;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    @Mapping(target = "password", source = "encodedPassword")
    @Mapping(target = "status", constant = "ACTIVE")
    @Mapping(target = "role", constant = "CLIENT")
    User mapClientRegistrationRequestToUser(ClientUserRegistrationRequest registrationRequest, String encodedPassword);

    @Mapping(target = "password", source = "encodedPassword")
    @Mapping(target = "status", constant = "ACTIVE")
    User mapSuperUserRegistrationRequestToUser(SuperUserRegistrationRequest registrationRequest, String encodedPassword);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "name")
    User mapPrinicepalToUser(Principal principal);
}
