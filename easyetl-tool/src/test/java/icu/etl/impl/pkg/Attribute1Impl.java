package icu.etl.impl.pkg;

import icu.etl.util.Attribute;

public class Attribute1Impl implements Attribute<String> {
   
    public boolean contains(String key) {
        return false;
    }

    public void setAttribute(String key, String value) {

    }

    public String getAttribute(String key) {
        return null;
    }
}
