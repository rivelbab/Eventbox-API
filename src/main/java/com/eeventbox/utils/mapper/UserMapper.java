package com.eeventbox.utils.mapper;

import com.eeventbox.model.user.User;
import com.eeventbox.payload.user.UserRequest;
import com.eeventbox.payload.user.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper
public interface UserMapper {

	User asUser(UserRequest userRequest);

	UserRequest asUserRequest(User user);

	void update(@MappingTarget User user, UserRequest userRequest);

	UserResponse asUserResponse(User user);

	List<UserResponse> asUserResponse(List<User> users);
}
