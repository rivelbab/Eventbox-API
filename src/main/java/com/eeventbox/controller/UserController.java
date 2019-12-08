package com.eeventbox.controller;
/**
 * ================================================
 * Contains all endpoints for the user resources
 * Created by Rivelbab on 26/10/2019 at Nanterre U.
 * ================================================
 */
import com.eeventbox.exception.AppException;
import com.eeventbox.model.user.User;
import com.eeventbox.payload.user.UserRequest;
import com.eeventbox.payload.user.UserResponse;
import com.eeventbox.service.user.UserService;
import com.eeventbox.utils.helper.PatchHelper;
import com.eeventbox.utils.mapper.UserMapper;
import com.eeventbox.utils.web.PatchMediaType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.json.JsonMergePatch;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

	@Autowired
	private UserService userService;

	private final UserMapper mapper;

	private final PatchHelper patchHelper;


	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<UserResponse>> findUsers() {

		List<User> users = userService.findUsers();
		List<UserResponse> userResponses = mapper.asUserResponse(users);

		return ResponseEntity.ok(userResponses);
	}

	@GetMapping(path = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserResponse> findUserByUsername(@PathVariable String username) {

		User user = userService.findUserByUsername(username).orElseThrow(() -> new AppException("User not exist."));
		UserResponse userResponse = mapper.asUserResponse(user);

		return ResponseEntity.ok(userResponse);
	}

	@PutMapping(path = "/{username}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateUser(@PathVariable String username, @RequestBody @Valid UserRequest userRequest) {

		Boolean isUpdateble = userService.updateUser(username, userRequest);

		if(isUpdateble == false) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.noContent().build();
	}

	@PatchMapping(path = "/{username}", consumes = PatchMediaType.APPLICATION_MERGE_PATCH_VALUE)
	public ResponseEntity<?> updateUser(@PathVariable String username, @RequestBody JsonMergePatch mergePatchDocument) {

		User user = userService.findUserByUsername(username).orElseThrow(() -> new AppException("User not exist."));
		UserRequest userRequest = mapper.asUserRequest(user);
		UserRequest userRequestPatched = patchHelper.mergePatch(mergePatchDocument, userRequest, UserRequest.class);

		mapper.update(user, userRequestPatched);
		userService.updateUser(user);

		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{email}")
	public ResponseEntity<Void> deleteUser(@PathVariable String email) {

		Boolean isDeleteble = userService.deleteUser(email);
		if(isDeleteble == false) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.noContent().build();
	}
}
