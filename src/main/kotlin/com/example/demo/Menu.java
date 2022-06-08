package com.example.demo;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;

@Getter
@Setter
public class Menu {
    private String href;
    private String text;
    private LinkedHashSet<Menu> menu;

    public Menu() {

    }

    public Menu(String href, String text) {
        this.href = href;
        this.text = text;
    }

    public Menu(String href, String text, LinkedHashSet<Menu> menu) {
        this.href = href;
        this.text = text;
        this.menu = menu;
    }
}
