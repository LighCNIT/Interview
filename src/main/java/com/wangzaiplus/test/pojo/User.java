package com.wangzaiplus.test.pojo;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class User {

    private Integer id;
    private String username;
    private String password;
    private String passwordSalt;
    private String ip;
    private String mobile;
    private String mail;
    private Integer type;
    private Integer status;
    private Integer isDeleted;
    private Date createdTime;
    private Date updatedTime;

}
