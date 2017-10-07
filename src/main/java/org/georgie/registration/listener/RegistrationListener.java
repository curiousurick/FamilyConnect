package org.georgie.registration.listener;

import org.georgie.persistence.model.User;
import org.georgie.registration.OnRegistrationCompleteEvent;
import org.georgie.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent>
{
    @Autowired
    public RegistrationListener(IUserService service,
                                MessageSource messageSource,
                                JavaMailSender javaMailSender,
                                Environment environment)

    {
        this.service = service;
        this.messages = messageSource;
        this.mailSender = javaMailSender;
        this.env = environment;
    }

    // API

    @Override
    public void onApplicationEvent(final OnRegistrationCompleteEvent event)
    {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(final OnRegistrationCompleteEvent event)
    {
        final User user = event.getUser();
        final String token = UUID.randomUUID().toString();
        service.createVerificationTokenForUser(user, token);

        final SimpleMailMessage email = constructEmailMessage(event, user, token);
        mailSender.send(email);
    }

    //

    private final SimpleMailMessage constructEmailMessage(final OnRegistrationCompleteEvent event, final User user, final String token)
    {
        final String recipientAddress = user.getEmail();
        final String subject = "Family Connect - Registration Confirmation";
        final String confirmationUrl = event.getAppUrl() + "/registrationConfirm.html?token=" + token;
        final String message = messages.getMessage("message.regSucc", null, event.getLocale());
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + " \r\n" + confirmationUrl);
        email.setFrom(env.getProperty("support.email"));
        return email;
    }

    private final IUserService service;
    private final MessageSource messages;
    private final JavaMailSender mailSender;
    private final Environment env;
}
