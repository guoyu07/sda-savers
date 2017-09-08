package com.sda.savers.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Allen on 2017/8/24.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    private String guid;
    private String name;
    private String userId;
    private String idNumber;
    private String department;
    private String company;

    public UserEntity(String userId) {
        this.userId = userId;
    }
}
