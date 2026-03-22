package com.online_auction.user_service.dto;

import com.online_auction.user_service.entity.Role;
import lombok.Data;

@Data
public class RegisterRequest {

    private String email;
    private String password;
    private Role role;
}
