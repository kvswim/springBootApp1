package com.lessons;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.minidev.json.annotate.JsonIgnore;

@JsonIgnoreProperties(value={"took", "items"})
public class DTOTest {
//    private Object took;
//    private Object items;
    private Boolean errors;

//    public Object getTook() {
//        return took;
//    }
//
//    public void setTook(Object took) {
//        this.took = took;
//    }
//
//    public Object getItems() {
//        return items;
//    }
//
//    public void setItems(Object items) {
//        this.items = items;
//    }


    public Boolean getErrors() {
        return errors;
    }

    public void setErrors(Boolean errors) {
        this.errors = errors;
    }

}
