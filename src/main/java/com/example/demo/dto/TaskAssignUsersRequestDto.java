package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskAssignUsersRequestDto {

    private List<UserRef> users;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserRef {
        private UUID userId;
    }
}
