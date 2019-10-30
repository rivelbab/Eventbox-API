package com.eeventbox.service.user;

import com.eeventbox.model.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class userWithCountOfInterests implements Comparable<userWithCountOfInterests> {

    private int commonInterest;
    private User user;

    public userWithCountOfInterests(User user, int commonInterest) {
        this.user = user;
        this.commonInterest = commonInterest;
    }

    @Override
    public int compareTo(userWithCountOfInterests o) {
        int difference = o.getCommonInterest() - this.commonInterest;
        if (difference > 0) {
            return 1;
        } else if (difference == 0) {
            return 0;
        }
        return -1;
    }
}
