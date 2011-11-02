package org.apache.camel.component.corba;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.Binding;
import org.omg.CosNaming.BindingIteratorHolder;
import org.omg.CosNaming.BindingListHolder;
import org.omg.CosNaming.BindingType;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;

public class OrbHelper {

	public static ORB createOrb(String[] args, String host, int port) {
		Properties props = new Properties();
		props.put(Constants.FullORBInitialHost, host);
		props.put(Constants.FullORBInitialPort, port);
		return ORB.init(args, props);
	}

	public static void register(ORB orb, NamingContext context, Object object, String name, String kind) throws NotFound, CannotProceed, org.omg.CosNaming.NamingContextPackage.InvalidName {
		orb.connect(object);

		NameComponent[] names = new NameComponent[] {new NameComponent(name, kind)};
		context.rebind(names, object);
	}

	public static <T> T lookup(ORB obr, String name, Class<T> type) {
		return null;
	}

	public static NamingContext getNamingContext(ORB orb) throws InvalidName {
		org.omg.CORBA.Object object = orb.resolve_initial_references(Constants.NameService);

		return NamingContextHelper.narrow(object);
	}

	public static Map<String, Object> getRegisteredObjects(ORB orb, NamingContext namingService) throws NotFound, CannotProceed, org.omg.CosNaming.NamingContextPackage.InvalidName {
		Map<String, Object> objects = new HashMap<String, Object>();

		BindingListHolder holder = new BindingListHolder();
		BindingIteratorHolder iterator = new BindingIteratorHolder();
		// TODO we list only 1000 names, it is enough?
		namingService.list(1000, holder, iterator);

		Binding bindings[] = holder.value;

		if (bindings.length == 0) {
			return objects;
		}

		for (Binding binding : bindings) {
			// get the object reference for each binding
			org.omg.CORBA.Object obj = namingService.resolve(binding.binding_name);

			// check to see if this is a naming context
			if (binding.binding_type == BindingType.nobject) {
				NameComponent name = binding.binding_name[binding.binding_name.length - 1];
				objects.put(name.id, obj);
			}
		}

		return objects;
	}

	public static void main(String[] args) throws Exception {
		ORB orb = OrbHelper.createOrb(args, "", 950);
		Map<String, Object> registeredObjects = getRegisteredObjects(orb, getNamingContext(orb));
		System.out.println(registeredObjects);

		Object object = registeredObjects.get("CustomerService");
		System.out.println(object._get_interface_def());
	}
}
