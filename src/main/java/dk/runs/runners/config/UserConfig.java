package dk.runs.runners.config;

import dk.runs.runners.repositories.mysqlImpl.UserRepositoryImpl;
import dk.runs.runners.services.interfaceRepositories.UserRepository;
import dk.runs.runners.services.interfaceServices.UserService;
import dk.runs.runners.services.serviceImpl.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class UserConfig {

    @Bean
    @Scope("prototype")
    public UserRepository getUserRepository(){
        return new UserRepositoryImpl();
    }

    @Bean
    @Scope("prototype")
    public UserService getUserService(){
        return new UserServiceImpl(getUserRepository());
    }


}
