package com.tr.nebula.web.dev;

import com.tr.nebula.web.WebApplication;
import com.tr.nebula.web.dev.cli.sample.InitialCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.context.support.StandardServletEnvironment;

/**
 * Created by Mustafa Erbin on 03/03/2017.
 */

@ComponentScan({"com.tr.nebula.web.dev"})
public class Application extends WebApplication {

    @Autowired
    StandardServletEnvironment environment;

    @Autowired
    InitialCommand initialCommand;

    public static void main(String[] args) {
        run(Application.class, args);
    }

    @Override
    public void init(ApplicationArguments applicationArguments) {
        initialCommand.run();
    }
}
