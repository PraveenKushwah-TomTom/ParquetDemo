package com.tomtom.Parquetdemo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class Link {

    @NonNull Id refId;
    Map<String, String> properties = new HashMap<>();

}
