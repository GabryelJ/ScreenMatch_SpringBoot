package dev.gabryel.screenmatch.service;

public interface IDataParser {
    <T> T fetchData (String json, Class<T> tClass);
}
