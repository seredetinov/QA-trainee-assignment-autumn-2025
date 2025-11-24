package resources.dto;

import resources.utils.ActualType;

public class Item {
    public @ActualType(Integer.class) Object sellerID;
    public @ActualType(String.class) Object name;
    public @ActualType(Integer.class) Object price;
    public @ActualType(Statistics.class) Object statistics;

    //Конструктор для базового объекта с валидными значениями
    public Item(){
        sellerID = 123;
        name = "testItem";
        price = 9900;
        statistics = new Statistics();
    }

    public Item(Object sellerID, Object name, Object price, Object statistics){
        this.sellerID = sellerID;
        this.name = name;
        this.price = price;
        this.statistics = statistics;
    }

    public static class ItemReversed {
        public @ActualType(Statistics.class) Object statistics;
        public @ActualType(Integer.class) Object price;
        public @ActualType(String.class) Object name;
        public @ActualType(Integer.class) Object sellerID;

        public ItemReversed(){
            sellerID = 123;
            name = "testItem";
            price = 9900;
            statistics = new Statistics();
        }
    }
}
