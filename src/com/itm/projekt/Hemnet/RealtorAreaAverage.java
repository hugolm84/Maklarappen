/**
 * Hugo Lindström (C) 2013
 * huli1000
 */
package com.itm.projekt.Hemnet;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown=true)
public class RealtorAreaAverage {
    public float fee;
    public float price_change_up;
    public float price;
    public float price_m2;
    public float price_change_down;
    public float rooms;
    public float age;
    public float size;

}
