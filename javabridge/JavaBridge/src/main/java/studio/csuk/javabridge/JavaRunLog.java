package com.wilfaskins.javabridge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaRunLog {

    private List<String> entries = new ArrayList<>();

    public JavaRunLog(){

    }

    public void add(String entry){
        this.entries.add(entry);
    }

    class Variable<T>{

        private String name;
        private int scope;
        private Map<Integer, T> values;

        public Variable(String name, int scope){
            this.name = name;
            this.scope = scope;
            values = new HashMap<>();
        }
    }

}
