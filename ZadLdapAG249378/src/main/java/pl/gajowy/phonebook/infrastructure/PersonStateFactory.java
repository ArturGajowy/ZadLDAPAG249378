package pl.gajowy.phonebook.infrastructure;

import pl.gajowy.phonebook.domain.MimuwOrgPerson;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.spi.DirStateFactory;
import java.util.Hashtable;

import static com.google.common.base.Strings.isNullOrEmpty;

public class PersonStateFactory implements DirStateFactory {

    public Result getStateToBind(Object obj, Name name, Context nameCtx, Hashtable<?, ?> environment, Attributes inAttrs) throws NamingException {
        if (obj instanceof MimuwOrgPerson) {
            return getPersonState(inAttrs, (MimuwOrgPerson) obj);
        }
        return null;
    }

    private Result getPersonState(Attributes inAttrs, MimuwOrgPerson person) {
        Attributes stateAttributes = prepareAttributesBasedOn(inAttrs);

        putObjectClass(stateAttributes);
        stateAttributes.put("cn", person.getUsername());
        stateAttributes.put("sn", person.getLastName());
        putIfHasText(stateAttributes, "givenName", person.getFirstName());
        putIfHasText(stateAttributes, "telephoneNumber", person.getTelephoneNumber());
        putIfHasText(stateAttributes, "nameDay", person.getNameDay());

        return new Result(null, stateAttributes);
    }

    private void putObjectClass(Attributes stateAttributes) {
        if (stateAttributes.get("objectclass") == null) {
            BasicAttribute oc = new BasicAttribute("objectclass");
            oc.add("mimuwOrgPerson");
            oc.add("inetOrgPerson");
            oc.add("organizationalPerson");
            oc.add("person");
            oc.add("top");
            stateAttributes.put(oc);
        }
    }

    private void putIfHasText(Attributes stateAttributes, String attributeName, String value) {
        if (!isNullOrEmpty(value)) {
            stateAttributes.put(attributeName, value);
        }
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
