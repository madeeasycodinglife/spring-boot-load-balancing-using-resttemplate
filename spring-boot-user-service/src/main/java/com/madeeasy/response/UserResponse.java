package com.madeeasy.response;

import com.madeeasy.dto.UserDto;
import com.madeeasy.vo.Department;
import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserResponse {
    private List<UserDto> users;
    private List<Department> departments;
}
