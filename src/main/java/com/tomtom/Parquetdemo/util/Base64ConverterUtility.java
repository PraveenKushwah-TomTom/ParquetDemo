package com.tomtom.Parquetdemo.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import java.util.Base64;

@UtilityClass
@Slf4j
public class Base64ConverterUtility {


    private static final ObjectMapper OM =
        new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    @SneakyThrows
    public <T> String toBase64(T c) {
        return Base64.getUrlEncoder().encodeToString(OM.writeValueAsString(c).getBytes());
    }

    @SneakyThrows
    public <T> T fromBase64(final String base64, Class<T> tClass) {
        return OM.readValue(Base64.getUrlDecoder().decode(base64), tClass);
    }
}
