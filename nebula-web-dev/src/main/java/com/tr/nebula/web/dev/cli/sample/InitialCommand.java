package com.tr.nebula.web.dev.cli.sample;

import org.springframework.stereotype.Component;


@Component
public class InitialCommand {

    public void run() {
        System.out.println("=== initialize command ===");
    }
}
