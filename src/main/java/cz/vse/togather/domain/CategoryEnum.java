package cz.vse.togather.domain;

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
}
