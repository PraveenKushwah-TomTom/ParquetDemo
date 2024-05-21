package com.tomtom.Parquetdemo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Id {

    long most;
    long least;

    public UUID asUUID() {
        return new UUID(most, least);
    }

    public static Id fromUUID(UUID id) {
        return new Id(id.getMostSignificantBits(), id.getLeastSignificantBits());
    }
}
