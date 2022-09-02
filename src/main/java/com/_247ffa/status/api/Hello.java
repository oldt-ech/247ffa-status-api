package com._247ffa.status.api;

import org.springframework.stereotype.Component;

import com._247ffa.status.api.model.Greeting;
import com._247ffa.status.api.model.User;

import reactor.core.publisher.Mono;

import java.util.function.Function;

@Component
public class Hello implements Function<Mono<User>, Mono<Greeting>> {

    public Mono<Greeting> apply(Mono<User> mono) {
    	// standard api to transform one bean into another
        return mono.map(user -> new Greeting("Hello, " + user.getName() + "!\n"));
    }
}