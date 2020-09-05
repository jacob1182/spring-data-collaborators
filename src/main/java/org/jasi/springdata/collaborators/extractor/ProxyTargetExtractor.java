package org.jasi.springdata.collaborators.extractor;

public interface ProxyTargetExtractor {
    boolean support(Object proxy);
    <T, R extends T> R extractTargetFrom(T proxy);
}
