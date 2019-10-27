package com.eeventbox.service.user;
/**
 * ================================================
 * Contains all useful operations for all Users
 * Created by Rivelbab on 26/10/2019 at Nanterre U.
 * ================================================
 */

import com.eeventbox.payload.response.ResponseMessage;
import com.eeventbox.payload.user.RegisterRequest;

public interface UserService {

	ResponseMessage addUser(RegisterRequest rq);

}
