package pl.gajowy.phonebook.application;

import pl.gajowy.phonebook.MimuwOrgPerson;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchResult;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class PersonConverter {
    public List<MimuwOrgPerson> convertToPersons(NamingEnumeration answer) {
        try {
            return tryConvertToPersons(answer);
        } catch (NamingException e) {
            throw new RuntimeException(e); //FIXME !!!!
        }
    }

    private List<MimuwOrgPerson> tryConvertToPersons(NamingEnumeration answer) throws NamingException {
        List<MimuwOrgPerson> converted = newArrayList();
        while (answer.hasMore()) {
            converted.add(convertToPerson(answer.next()));
        }
        return converted;
    }

    private MimuwOrgPerson convertToPerson(Object personSearchResultObject) throws NamingException {
        SearchResult personSearchResult = (SearchResult) personSearchResultObject;
        Attributes personAttributes = personSearchResult.getAttributes();
        return new MimuwOrgPerson(
                getString(personAttributes, "givenName"),
                getString(personAttributes, "sn"),
                getString(personAttributes, "cn"),
                getString(personAttributes, "nameDay"),
                getString(personAttributes, "telephoneNumber")
        );
    }

    private String getString(Attributes personAttributes, String atrributeName) throws NamingException {
        Attribute attribute = personAttributes.get(atrributeName);
        if (attribute == null) {
            return "";
        } else {
            return (String) attribute.get();
        }
    }
}
