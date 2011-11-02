package org.apache.camel.component.corba;

import org.apache.camel.Processor;
import org.apache.camel.component.bean.BeanEndpoint;
import org.apache.camel.component.bean.BeanHolder;
import org.apache.camel.component.bean.BeanProcessor;
import org.apache.camel.component.bean.RegistryBean;

public class CorbaEndpoint extends BeanEndpoint {

    Class serviceClass;
    Class helperClass;

    public CorbaEndpoint(String uri, CorbaComponent component) {
        super(uri, component);
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setServiceClass(Class serviceClass) {
        this.serviceClass = serviceClass;
    }

    public void setHelperClass(Class helperClass) {
        this.helperClass = helperClass;
    }

    @Override
    protected Processor createProcessor() throws Exception {
        BeanHolder holder = getBeanHolder();
        if (holder == null) {
            RegistryBean registryBean = new RegistryBean(getCamelContext(), getBeanName());
            if (isCache()) {
                holder = registryBean.createCacheHolder();
            } else {
                holder = registryBean;
            }
        }
        BeanProcessor processor = new BeanProcessor(holder);
        if (getMethod() != null) {
            processor.setMethod(getMethod());
        }
        processor.setMultiParameterArray(isMultiParameterArray());

        return processor;
    }
}
