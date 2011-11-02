package org.apache.camel.component.corba;

import java.util.Map;

import org.apache.camel.spi.Registry;
import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CorbaRegistry implements Registry {

    private final NamingContext context;
    private final ORB orb;

    private Logger logger = LoggerFactory.getLogger(CorbaRegistry.class);

    public CorbaRegistry(ORB orb, NamingContext context) {
        this.orb = orb;
        this.context = context;
    }

    public CorbaRegistry(ORB orb) throws InvalidName {
        this(orb, OrbHelper.getNamingContext(orb));
    }

    @Override
    public Object lookup(String name) {

        try {
            Map<String, org.omg.CORBA.Object> objects = OrbHelper.getRegisteredObjects(orb, context);
            org.omg.CORBA.Object object = objects.get(name);

            if (object != null) {
                return object;
            }
        } catch (Exception e) {
            logger.warn("Error looking up bean {}", name, e);
        }

        return null;
    }

    @Override
    public <T> T lookup(String name, Class<T> type) {
        Map<String, org.omg.CORBA.Object> objects;
        try {
            objects = OrbHelper.getRegisteredObjects(orb, context);

            if (objects.containsKey(name)) {
                return type.cast(objects.get(name));
            }
        } catch (NotFound e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (CannotProceed e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (org.omg.CosNaming.NamingContextPackage.InvalidName e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return null;
    }

    @Override
    public <T> Map<String, T> lookupByType(Class<T> type) {
        // NOT supported
        return null;
    }

}
