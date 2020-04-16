package com.passuser.net.utils;

import org.springframework.stereotype.Component;

@Component
public interface TokenGenerator {

    public String generate(String... strings);

}
