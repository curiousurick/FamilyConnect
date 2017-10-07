package org.georgie.spring;

import org.georgie.security.facebook.FacebookConnectionSignup;
import org.georgie.security.facebook.FacebookSignInAdapter;
import org.georgie.security.google2fa.CustomAuthenticationProvider;
import org.georgie.security.google2fa.CustomWebAuthenticationDetailsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.mem.InMemoryUsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInController;

@Configuration
@ComponentScan(basePackages = {"org.georgie.security"})
// @ImportResource({ "classpath:webSecurityConfig.xml" })
@EnableWebSecurity
public class SecSecurityConfig extends WebSecurityConfigurerAdapter
{

    @Autowired
    public SecSecurityConfig(UserDetailsService userDetailsService,
                             AuthenticationSuccessHandler myAuthenticationSuccessHandler,
                             LogoutSuccessHandler myLogoutSuccessHandler,
                             @Qualifier(value = "authenticationFailureHandler") AuthenticationFailureHandler authenticationFailureHandler,
                             CustomWebAuthenticationDetailsSource authenticationDetailsSource,
                             ConnectionFactoryLocator connectionFactoryLocator,
                             UsersConnectionRepository usersConnectionRepository,
                             FacebookConnectionSignup facebookConnectionSignup,
                             FacebookSignInAdapter facebookSignInAdapter)
    {
        this.userDetailsService = userDetailsService;
        this.myAuthenticationSuccessHandler = myAuthenticationSuccessHandler;
        this.myLogoutSuccessHandler = myLogoutSuccessHandler;
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.authenticationDetailsSource = authenticationDetailsSource;
        this.connectionFactoryLocator = connectionFactoryLocator;
        this.usersConnectionRepository = usersConnectionRepository;
        this.facebookConnectionSignup = facebookConnectionSignup;
        this.facebookSignInAdapter = facebookSignInAdapter;
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception
    {
        auth.authenticationProvider(authProvider());
    }

    @Override
    public void configure(final WebSecurity web) throws Exception
    {
        web.ignoring()
            .antMatchers("/resources/**");
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception
    {
        // @formatter:off
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/user/loginFailure", "/login*", "/logout*",
                        "/user/registration*", "/registrationConfirm*", "/expiredAccount*", "/registration*",
                        "/badUser*", "/user/resendRegistrationToken*" ,"/forgetPassword*", "/user/resetPassword*",
                        "/user/changePassword*", "/emailError*", "/resources/**","/old/user/registration*","/successRegister*","/qrcode*").permitAll()
                .antMatchers("/user/loginSuccess").authenticated()
                .antMatchers("/user/updatePassword*", "/user/savePassword*", "/updatePassword*").hasAuthority("CHANGE_PASSWORD_PRIVILEGE")
                .anyRequest().hasAuthority("READ_PRIVILEGE")
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/homepage.html")
                .failureUrl("/login?error=true")
                .successHandler(myAuthenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                .authenticationDetailsSource(authenticationDetailsSource)
                .permitAll()
                .and()
                .sessionManagement()
                .maximumSessions(1)
                .sessionRegistry(sessionRegistry())
                .and()
                .sessionFixation().migrateSession()
                .and()
                .logout()
                .logoutSuccessHandler(myLogoutSuccessHandler)
                .invalidateHttpSession(false)
                .logoutSuccessUrl("/user/logout.html?logSucc=true")
                .deleteCookies("JSESSIONID")
                .permitAll()
                .and()
                .httpBasic();
        // @formatter:on
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public SessionRegistry sessionRegistry()
    {
        return new SessionRegistryImpl();
    }

    @Bean
    public DaoAuthenticationProvider authProvider()
    {
        final CustomAuthenticationProvider authProvider = new CustomAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder());
        return authProvider;
    }

    @Bean
    // @Primary
    public ProviderSignInController providerSignInController() {
        ((InMemoryUsersConnectionRepository) usersConnectionRepository).setConnectionSignUp(facebookConnectionSignup);
        return new ProviderSignInController(connectionFactoryLocator, usersConnectionRepository, facebookSignInAdapter);
    }

    @Bean
    public PasswordEncoder encoder()
    {
        return new BCryptPasswordEncoder(11);
    }

    private final UserDetailsService userDetailsService;
    private final AuthenticationSuccessHandler myAuthenticationSuccessHandler;
    private final LogoutSuccessHandler myLogoutSuccessHandler;
    private final AuthenticationFailureHandler authenticationFailureHandler;
    private final CustomWebAuthenticationDetailsSource authenticationDetailsSource;

    private final ConnectionFactoryLocator connectionFactoryLocator;
    private final UsersConnectionRepository usersConnectionRepository;
    private final FacebookConnectionSignup facebookConnectionSignup;
    private final FacebookSignInAdapter facebookSignInAdapter;
}