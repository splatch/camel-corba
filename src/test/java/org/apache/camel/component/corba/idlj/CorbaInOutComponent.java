package org.apache.camel.component.corba.idlj;

import org.omg.CORBA.StringHolder;

import Camel.InputOutput._ComponentImplBase;

public class CorbaInOutComponent extends _ComponentImplBase {

    @Override
    public void call(StringHolder value) {
        value.value = value.value + " " + value.value;
    }

}
