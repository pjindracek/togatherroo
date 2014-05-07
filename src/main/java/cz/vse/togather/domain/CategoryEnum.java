package cz.vse.togather.domain;

import java.util.Arrays;


public enum CategoryEnum {

    CULTURE("Culture"),
    BUSINESS("Business"),
    SPORT("Sport"),
    SOCIAL("Social"),
    FAMILY("Family"),
    RELIGION("Religion"),
    FOOD("Food"),
    TECHNOLOGY("Technology"),
    HOBBIES("Hobbies"),
    PETS("Pets");
    
    private String name;
    
    private CategoryEnum(String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getOrder() {
        return Arrays.asList(CategoryEnum.values()).indexOf(this);
    }
    
    public static CategoryEnum getInstance(int order) {
        return CategoryEnum.values()[order];
    }
}
