package org.amb.main;


import org.amb.cli.Commands;
import org.apache.commons.cli.ParseException;

import java.io.IOException;

public class App {

    public static void main(String[] args) throws ParseException, IOException {
        Commands c = new Commands();
        c.initCLI(args);
    }
}
