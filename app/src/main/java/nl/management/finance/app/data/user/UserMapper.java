package nl.management.finance.app.data.user;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserMapper {
    @Inject
    public UserMapper() { }

    public User toEntity(UserDto dto) {
        return new User(dto.getUserId());
    }

    public UserDto toDto(String userId) {
        UserDto dto = new UserDto();
        dto.setUserId(userId);
        return dto;
    }
}
