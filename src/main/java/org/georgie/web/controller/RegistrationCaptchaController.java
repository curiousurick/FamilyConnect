package org.georgie.web.controller;

import org.georgie.captcha.ICaptchaService;
import org.georgie.persistence.model.User;
import org.georgie.registration.OnRegistrationCompleteEvent;
import org.georgie.service.IUserService;
import org.georgie.web.dto.UserDto;
import org.georgie.web.util.GenericResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class RegistrationCaptchaController
{
    @Autowired
    public RegistrationCaptchaController(IUserService userService,
                                         ICaptchaService captchaService,
                                         ApplicationEventPublisher applicationEventPublisher)

    {
        this.userService = userService;
        this.captchaService = captchaService;
        this.eventPublisher = applicationEventPublisher;
    }

    @RequestMapping(value = "/user/registrationCaptcha", method = RequestMethod.POST)
    @ResponseBody
    public GenericResponse captchaRegisterUserAccount(@Valid final UserDto accountDto, final HttpServletRequest request)
    {

        final String response = request.getParameter("g-recaptcha-response");
        captchaService.processResponse(response);

        logger.debug("Registering user account with information: {}", accountDto);

        final User registered = userService.registerNewUserAccount(accountDto);
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), getAppUrl(request)));
        return new GenericResponse("success");
    }

    private String getAppUrl(HttpServletRequest request)
    {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final IUserService userService;
    private final ICaptchaService captchaService;
    private final ApplicationEventPublisher eventPublisher;

}
