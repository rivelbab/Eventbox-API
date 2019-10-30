package com.eeventbox.service.event;

import com.eeventbox.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventUserServiceImpl {

    @Autowired
    private UserService userService;
    @Autowired
    private EventService eventService;

    
}
