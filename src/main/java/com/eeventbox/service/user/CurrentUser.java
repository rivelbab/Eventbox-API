package com.eeventbox.service.user;
/**
 * ==================================================
 * Spring security annotation to access the currently
 * authenticated user in the controllers.
 * Created by Rivelbab on 27/10/2019 at Nanterre U.
 * ==================================================
 */
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal
public @interface CurrentUser {

}
