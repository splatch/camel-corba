package org.apache.camel.component.corba;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.corba.idlj.CorbaInComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.spi.Registry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContext;

@RunWith(BlockJUnit4ClassRunner.class)
public class CorbaInTest extends CamelTestSupport {

    private CorbaInComponent component;

    @EndpointInject(uri = "corba:127.0.0.1:900/CorbaIn?serviceClass=CorbaIn.Component&method=call")
    private ProducerTemplate endpoint;

    private ORB orb;

    private NamingContext namingContext;

    @Override
    public void setUp() throws Exception {
        orb = OrbHelper.createOrb(new String[0], "127.0.0.1", 900);
        namingContext = OrbHelper.getNamingContext(orb);

        super.setUp();

        component = new CorbaInComponent();
        OrbHelper.register(orb, namingContext, component, "CorbaIn", "");
    }

    @Test
    public void testRegister() throws Exception {
        endpoint.sendBody("AAA");

        assertEquals("Expected AAA as value", "AAA", component.getValue());
    }

    protected CamelContext createCamelContext() throws Exception {
        CamelContext context = new DefaultCamelContext(createCorbaRegistry());
        context.setLazyLoadTypeConverters(isLazyLoadingTypeConverter());
        return context;
    }

    private Registry createCorbaRegistry() {
        return new CorbaRegistry(orb, namingContext);
    }

}
