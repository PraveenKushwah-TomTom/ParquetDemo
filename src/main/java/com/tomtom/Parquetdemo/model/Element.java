package com.tomtom.Parquetdemo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class Element {

    @NonNull Id id;
    @NonNull String type;
    String geometry;
    Map<String, String> properties = new HashMap<>();
    Map<String, List<Link>> links = new HashMap<>();
}
