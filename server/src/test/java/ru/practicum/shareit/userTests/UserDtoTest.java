package ru.practicum.shareit.userTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class UserDtoTest {
    @Autowired
    private JacksonTester<UserDto> jacksonTester;

    @Test
    void userDtoTest() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("Altair ibn La Ahad");
        userDto.setEmail("ubisoft@gmail.com");

        JsonContent<UserDto> result = jacksonTester.write(userDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Altair ibn La Ahad");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("ubisoft@gmail.com");
    }
}
