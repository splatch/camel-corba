package org.apache.camel.component.corba.idlj;

import org.omg.CORBA.StringHolder;

import Camel.Output._ComponentImplBase;

public class CorbaOutComponent extends _ComponentImplBase {

    private String value;

    @Override
    public void call(StringHolder value) {
        value.value = this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
