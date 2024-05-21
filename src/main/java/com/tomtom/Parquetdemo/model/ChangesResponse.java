package com.tomtom.Parquetdemo.model;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import java.net.URI;
import java.util.Collection;

@Value
@Builder
@Jacksonized
public class ChangesResponse {

    URI self;
    URI prev;
    URI next;
    @Singular
    Collection<String> cursors;

    @Singular
    Collection<Change> changes;
}
