package com.poojithairosha.webscoketdemo.ws;

import lombok.Builder;

@Builder
public record Message(String to, String message, String from) { }
