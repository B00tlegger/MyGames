package com.onceuponatime.game.model.characters;

public enum TypeOfRace {
    HUMAN("human"),
    SKELETON("skeleton");
    private final String value;

    private TypeOfRace(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
