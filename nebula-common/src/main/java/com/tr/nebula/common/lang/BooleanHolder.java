package com.tr.nebula.common.lang;

/**
 * Created by Mustafa Erbin on 30/01/2017.
 */
public class BooleanHolder {
    private boolean value;
    public BooleanHolder(boolean value){
        this.value = value;
    }

    public void set(boolean value) {
        this.value = value;
    }

    public boolean is() {
        return value;
    }
}
