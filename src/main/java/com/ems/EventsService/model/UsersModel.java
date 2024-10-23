package com.ems.EventsService.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UsersModel
{
    private String userId;
    private String username;
    private String customName;
    private String password;
    private String email;
    private String mobile;
    private String address;
    private String account;
    private String userType;
    private String recStatus;
    private String createdBy;
    private String createdDate;
    private String updatedDate;
    private String updatedBy;
}
