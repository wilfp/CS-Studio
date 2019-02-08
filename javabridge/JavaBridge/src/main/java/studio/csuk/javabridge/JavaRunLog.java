package com.wilfaskins.javabridge;

import java.util.ArrayList;
import java.util.List;

public class JavaRunLog {

    private List<String> entries = new ArrayList<>();

    public JavaRunLog(){

    }

    public void add(String entry){
        this.entries.add(entry);
    }
}
