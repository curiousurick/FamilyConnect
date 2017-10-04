package org.georgie.web.controller.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.Validate;
import org.georgie.persistence.model.User;
import org.georgie.web.response.GenericError;
import org.georgie.web.response.UserConfirmedResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class RestUserController
{
    private final ObjectMapper objectMapper;

    public RestUserController()
    {
        objectMapper = new ObjectMapper();
    }
}
