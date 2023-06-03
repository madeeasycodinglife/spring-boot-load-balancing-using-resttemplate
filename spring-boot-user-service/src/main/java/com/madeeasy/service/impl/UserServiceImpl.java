package com.madeeasy.service.impl;

import com.madeeasy.dto.UserDto;
import com.madeeasy.entity.User;
import com.madeeasy.repository.UserRepository;
import com.madeeasy.response.UserResponse;
import com.madeeasy.service.UserService;
import com.madeeasy.vo.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, User user) {
        user.setId(id);
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public ResponseEntity<UserResponse> getAllUsersWithDepartments() {
        List<User> userList = userRepository.findAll();
        List<Department> departmentList = restTemplate.exchange(
                "http://DEPARTMENT-SERVICE/api/departments",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Department>>() {
                }).getBody();
    /*    List<UserDto> userDtoList = new ArrayList<>();
        for (User user : userList) {
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setName(user.getName());
            userDto.setEmail(user.getEmail());
            userDtoList.add(userDto);
        }*/
        //-----------------------------OR-----------------------------
        List<UserDto> userDtoList = userList.stream()
                .map(user -> new UserDto(user.getId(), user.getName(), user.getEmail()))
                .collect(Collectors.toList());

        UserResponse userResponse = new UserResponse();
        userResponse.setUsers(userDtoList);
        userResponse.setDepartments(departmentList);

        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }
}

