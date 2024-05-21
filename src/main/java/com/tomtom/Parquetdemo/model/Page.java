package com.tomtom.Parquetdemo.model;

import lombok.NonNull;
import lombok.Value;

@Value
public class Page {

    @NonNull long start;
    @NonNull long end;

}
