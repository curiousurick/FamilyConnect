package org.georgie.web.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestUserController
{
    public RestUserController()
    {
        objectMapper = new ObjectMapper();
    }
    private final ObjectMapper objectMapper;
}
