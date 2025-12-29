package com.hogwart.hogwarts_artifacts_online.hogwartsuser.converter;

import com.hogwart.hogwarts_artifacts_online.hogwartsuser.HogwartsUser;
import com.hogwart.hogwarts_artifacts_online.hogwartsuser.dto.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToUserDtoConverter implements Converter<HogwartsUser, UserDto> {

    @Override
    public UserDto convert(HogwartsUser source) {
        // We are not setting password in DTO
        final UserDto userDto = new UserDto(source.getId(),
                source.getUsername(),
                source.isEnabled(),
                source.getRoles()
                )   ;
        return userDto;
    }
}
