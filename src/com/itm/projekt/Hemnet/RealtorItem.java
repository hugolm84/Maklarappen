/**
 * Hugo Lindström (C) 2013
 * huli1000
 */
package com.itm.projekt.Hemnet;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import java.util.List;

/**
 * This class is a simple mapper class for a Realtor JSON object
 * Example usage:
 *  List<RealtorItem> items = mapper.readValue(results.toString(), new TypeReference<List<RealtorItem>>() {});
 * Items now contain a list of Realtors and declared variables
 */

@JsonIgnoreProperties(ignoreUnknown=true)
public class RealtorItem {

    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class RealtorBaseItem {

        private float price_change_down;
        private float age;
        private float price_m2;
        private float price_change_up;
        private float price;
        private float fee;
        private float size;
        private String broker;
        private String type;
        private String id;

        public float getPrice_change_down() {
            return price_change_down;
        }

        private void setPrice_change_down(float price_change_down) {
            this.price_change_down = price_change_down;
        }


        public float getFee() {
            return fee;
        }

        public void setFee(float fee){
            this.fee = fee;
        }

        public float getAge() {
            return age;
        }

        private void setAge(float age) {
            this.age = age;
        }

        public float getPrice_m2() {
            return price_m2;
        }

        private void setPrice_m2(float price_m2) {
            this.price_m2 = price_m2;
        }

        public float getPrice_change_up() {
            return price_change_up;
        }

        private void setPrice_change_up(float price_change_up) {
            this.price_change_up = price_change_up;
        }

        public float getPrice() {
            return price;
        }

        private void setPrice(float price) {
            this.price = price;
        }

        public float getSize() {
            return size;
        }

        public void setSize(float size) {
            this.size = size;
        }

        public String getBroker() {
            return broker;
        }

        public void setBroker(String broker) {
            this.broker = broker;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class RealtorListItem extends RealtorBaseItem {

        //private String broker;
        private String address;
        private String id;
        private String type;
        private int resource_image;

        public String getAddress() {
            return address;
        }

        private void setAddress(String address) {
            this.address = address;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getResource_image() {
            return resource_image;
        }

        public void setResourceImage(int resource) {
            this.resource_image = resource;
        }
    }

    private String name;
    private float percentage;
    private RealtorBaseItem average;
    private List<RealtorListItem> items;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public void setItems(List<RealtorListItem> items) {
        this.items = items;
    }

    public List<RealtorListItem> getItems() {
        return items;
    }

    public void setAverage(RealtorBaseItem average) {
        this.average = average;
    }

    public RealtorBaseItem getAverage() {
        return average;
    }
}
