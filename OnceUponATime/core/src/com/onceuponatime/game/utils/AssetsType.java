package com.onceuponatime.game.utils;

public enum AssetsType {
    PNG(".png");

    private final String format;

    private AssetsType(String format){
        this.format = format;
    }

    public String getFormat(){
        return format;
    }
}
