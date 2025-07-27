package dev.gabryel.screenmatch.service.dataparser;

public interface IDataParser {
    <T> T getData (String json, Class<T> tClass);
}
