package com.tomtom.Parquetdemo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tomtom.Parquetdemo.util.Base64ConverterUtility;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class Cursor implements Serializable {

    @JsonProperty
    private int currentIndex;

    public Optional<Cursor> getNext(List<Page> pages) {
        return move(pages, 1);
    }

    public Optional<Cursor> getPrev(List<Page> pages) {
        return move(pages, -1);
    }

    private Optional<Cursor> move(List<Page> pages, int increment) {
        int index = currentIndex + increment;
        if (index < 0 || index >= pages.size()) {
            return Optional.empty();
        }
        return Optional.of(new Cursor(index));
    }

    public static Cursor getFirst() {
        return new Cursor(0);
    }



    public static String encodeCursor(final Cursor cursor) {
        return Base64ConverterUtility.toBase64(cursor);
    }

    public static List<String> toCursorList(List<Page> pages) {
        return IntStream.range(0, pages.size())
                        .mapToObj(String::valueOf)
                        .toList();
    }

}
