package org.apache.camel.component.corba.idlj;

import Camel.Input._ComponentImplBase;

public class CorbaInComponent extends _ComponentImplBase {

    private String value;

    @Override
    public void call(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
