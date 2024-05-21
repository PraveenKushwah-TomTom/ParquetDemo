package com.tomtom.Parquetdemo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor(force = true)
public class LinearRef {
    double start;
    double end;
    Map<String, String> properties = new HashMap<>();
    @NonNull ChangeInfo changeInfo;


}
