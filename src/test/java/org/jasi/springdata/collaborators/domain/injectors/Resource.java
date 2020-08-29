package org.jasi.springdata.collaborators.domain.injectors;

import java.util.List;

import static java.util.Arrays.asList;

public class Resource<T> {
    final private T data;
    final private List<String> links;

    public Resource(T data, List<String> links) {
        this.data = data;
        this.links = links;
    }

    public static <T> Resource<T> of(T data, String... links) {
        return new Resource<>(data, asList(links));
    }

    public T getData() {
        return data;
    }

    public List<String> getLinks() {
        return links;
    }
}
