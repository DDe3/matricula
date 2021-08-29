package com.ing_software;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;

public class Informacion {

    URL url;

    public Informacion(String url) throws MalformedURLException {
        this.url = new URL("http://www.lineadecodigo.com");
    }

    public void go() {

        try {
            Desktop.getDesktop().browse(url.toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
