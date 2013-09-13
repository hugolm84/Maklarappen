/**
 * Hugo Lindström (C) 2013
 * huli1000
 */
package com.itm.projekt.JSON;

import org.json.JSONObject;

public abstract class JSONParser {

    JSONObject baseObject;

    public interface JSONParserListner {
        void onJSONParserDone(JSONObject result);
        void onJSONParserError(String error);
    }

    protected JSONParser(JSONObject obj) {
        this.baseObject = obj;
    }

    protected JSONObject getBaseObject() {
        return baseObject;
    }
    protected abstract void parse();

}
