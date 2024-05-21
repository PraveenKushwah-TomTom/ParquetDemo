package com.tomtom.Parquetdemo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class Change {

    @NonNull String id;
    @NonNull Element feature;
    @NonNull ChangeInfo changeInfo;
    Double confidence;
    List<LinearRef> linearRefs;
}
