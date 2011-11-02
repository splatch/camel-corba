package org.apache.camel.component.corba;

import java.net.URI;
import java.util.Map;
import java.util.Properties;

import org.apache.camel.Endpoint;
import org.apache.camel.component.bean.BeanComponent;

public class CorbaComponent extends BeanComponent {

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        URI url = URI.create(uri);

        Properties properties = new Properties();
        properties.put(Constants.FullORBInitialHost, url.getHost());
        properties.put(Constants.FullORBInitialPort, url.getPort());

        String beanName = url.getPath().substring(1);

        CorbaEndpoint endpoint = new CorbaEndpoint(uri, this);
        endpoint.setBeanName(beanName);
        endpoint.setServiceClass(getAndRemoveParameter(parameters, "serviceClass", Class.class));
        if (properties.containsKey("helperClass")) {
            endpoint.setHelperClass(getAndRemoveParameter(parameters, "helperClass", Class.class));
        }

        // now set additional properties on it
        setProperties(endpoint, parameters);

        return endpoint;
    }

}
