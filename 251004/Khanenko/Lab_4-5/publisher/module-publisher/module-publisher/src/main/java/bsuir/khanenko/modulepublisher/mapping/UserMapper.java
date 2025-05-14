package bsuir.khanenko.modulepublisher.mapping;

import bsuir.khanenko.modulepublisher.dto.user.UserRequestTo;
import bsuir.khanenko.modulepublisher.dto.user.UserResponseTo;
import bsuir.khanenko.modulepublisher.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserRequestTo userRequestTo);
    UserResponseTo toUserResponseTo(User user);
}
