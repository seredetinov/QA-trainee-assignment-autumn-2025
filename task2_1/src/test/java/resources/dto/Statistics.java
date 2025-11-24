package resources.dto;

import resources.utils.ActualType;

public class Statistics {
    public @ActualType(Integer.class) Object likes;
    public @ActualType(Integer.class) Object viewCount;
    public @ActualType(Integer.class) Object contacts;

    //Конструктор для базового объекта с валидными значениями
    public Statistics(){
        likes = 21;
        viewCount = 11;
        contacts = 43;
    }

    public Statistics(Object likes, Object viewCount, Object contacts){
        this.likes = likes;
        this.viewCount = viewCount;
        this.contacts = contacts;
    }

    @Override
    public String toString(){
        return "{contacts=" + this.contacts + ", likes=" + this.likes + ", viewCount=" + this.viewCount + "}";
    }
}
