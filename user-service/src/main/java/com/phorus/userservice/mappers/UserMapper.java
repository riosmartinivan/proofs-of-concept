package com.phorus.userservice.mappers;

import com.phorus.userservice.model.dbentities.User;
import com.phorus.userservice.model.dtos.UserDTO;
import com.phorus.userservice.model.dtos.responses.UserResponse;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse userToUserResponse(User user);

    User userDtoToUser(UserDTO userDTO);
}
