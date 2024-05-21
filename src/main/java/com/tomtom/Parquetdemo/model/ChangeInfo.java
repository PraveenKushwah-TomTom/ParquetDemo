package com.tomtom.Parquetdemo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Singular;
import lombok.extern.jackson.Jacksonized;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@Jacksonized
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class ChangeInfo {

    @NonNull
    @Builder.Default
    Operation op = Operation.CREATE;
    boolean geometryUpdated;

    @Builder.Default
    GeometryChange geometry = GeometryChange.builder().build();
    @NonNull
    @Singular
    Map<String, Operation> properties = new HashMap<>();
    @NonNull
    @Singular
    Map<String, Operation> links = new HashMap<>();

}
