package ru.practicum.shareit.bookingTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingDtoTest {
    @Autowired
    private JacksonTester<BookingDto> jacksonTester;

    @Test
    void bookingDtoTest() throws Exception {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setStart(LocalDateTime.now().plusMinutes(10));
        bookingDto.setEnd(LocalDateTime.now().plusMinutes(30));

        JsonContent<BookingDto> result = jacksonTester.write(bookingDto);

        String datePattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS";
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(datePattern);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(bookingDto.getStart().format(dateFormatter));
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(bookingDto.getEnd().format(dateFormatter));
    }
}
