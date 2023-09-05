package ru.practicum.shareit.itemTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDateDto;
import ru.practicum.shareit.item.dto.ItemDateDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemDateDtoTest {
    @Autowired
    private JacksonTester<ItemDateDto> jacksonTester;

    @Test
    void itemDateDtoTest() throws Exception {
        BookingDateDto lastBooking = new BookingDateDto();
        lastBooking.setId(1L);
        lastBooking.setBookerId(2L);

        BookingDateDto nextBooking = new BookingDateDto();
        nextBooking.setId(2L);
        nextBooking.setBookerId(4L);

        ItemDateDto itemDateDto = new ItemDateDto();
        itemDateDto.setId(1L);
        itemDateDto.setName("Eden's Apple");
        itemDateDto.setDescription("Real Apple Of Eden");
        itemDateDto.setAvailable(Boolean.TRUE);
        itemDateDto.setLastBooking(lastBooking);
        itemDateDto.setNextBooking(nextBooking);

        JsonContent<ItemDateDto> result = jacksonTester.write(itemDateDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Eden's Apple");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Real Apple Of Eden");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(Boolean.TRUE);
        assertThat(result).extractingJsonPathNumberValue("$.lastBooking.id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.nextBooking.id").isEqualTo(2);
        assertThat(result).extractingJsonPathNumberValue("$.lastBooking.bookerId").isEqualTo(2);
        assertThat(result).extractingJsonPathNumberValue("$.nextBooking.bookerId").isEqualTo(4);
    }
}
