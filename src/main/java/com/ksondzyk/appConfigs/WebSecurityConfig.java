package com.ksondzyk.appConfigs;

import com.ksondzyk.DataBase.DB;
import com.ksondzyk.HTTP.dao.Table;
import com.ksondzyk.entities.Message;
import com.ksondzyk.entities.Packet;
import com.ksondzyk.network.TCP.TCPClientThread;
import com.ksondzyk.utilities.CipherMy;
import org.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import sun.security.krb5.internal.PAData;

import java.sql.SQLException;

@Configuration
@EnableWebSecurity

public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .permitAll();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
//        String login = "";
//        String password = "";
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("type","user");
//        jsonObject.put("login","user");
//        jsonObject.put("cType","1");
//        Packet packet = new Packet((byte) 1,new Message(1,1,jsonObject.toString(),false));
//        TCPClientThread tcpClientThread = new TCPClientThread(packet);
//        Packet answer = tcpClientThread.send();
//
//        String jsonString = CipherMy.decode(answer.getBMsq().getMessage());
//
//        JSONObject json = new JSONObject(jsonString);
//        login = "admin";
//        password = (String) json.get("password");
        UserDetails user =
                User.withDefaultPasswordEncoder()
                        .username("login")
                        .password("password")
                        .roles("USER")
                        .build();

        return new InMemoryUserDetailsManager(user);
    }
}