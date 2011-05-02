package pl.gajowy.phonebook;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.spi.DirStateFactory;
import java.util.Hashtable;

public class PersonStateFactory implements DirStateFactory {

    public Result getStateToBind(Object obj, Name name, Context nameCtx, Hashtable<?, ?> environment, Attributes inAttrs) throws NamingException {

        if (obj instanceof MimuwOrgPerson) {
            MimuwOrgPerson person = (MimuwOrgPerson) obj;

            Attributes stateAttributes = prepareAttributesBasedOn(inAttrs);

            if (stateAttributes.get("objectclass") == null) {
                //TODO unhardcode
                BasicAttribute oc = new BasicAttribute("objectclass");
                oc.add("mimuwOrgPerson");
                oc.add("inetOrgPerson");
                oc.add("organizationalPerson");
                oc.add("person");
                oc.add("top");
                stateAttributes.put(oc);
            }

            stateAttributes.put("cn", person.getUsername());
            stateAttributes.put("sn", person.getLastName());
            stateAttributes.put("givenName", person.getFirstName());
            stateAttributes.put("telephoneNumber", person.getTelephoneNumber());
            stateAttributes.put("nameDay", person.getNameDay());

            return new DirStateFactory.Result(null, stateAttributes);
        }
        return null;
    }

    private Attributes prepareAttributesBasedOn(Attributes inAttrs) {
        if (inAttrs == null) {
            return new BasicAttributes(true);
        } else {
            return (Attributes) inAttrs.clone();
        }
    }

    public Object getStateToBind(Object obj, Name name, Context nameCtx, Hashtable<?, ?> environment) throws NamingException {
        return null; //this state factory can't operate without attributes
    }
}
