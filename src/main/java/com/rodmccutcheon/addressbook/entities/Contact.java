package com.rodmccutcheon.addressbook.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyEnumerated(EnumType.STRING)
    private Map<PhoneNumberType, String> phoneNumbers = new EnumMap<>(PhoneNumberType.class);

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnoreProperties("contacts")
    private Set<AddressBook> addressBooks = new HashSet<>();

    public Contact(String name) {
        this.name = name;
    }

    public Contact(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addPhoneNumber(PhoneNumberType phoneNumberType, String phoneNumber) {
        phoneNumbers.put(phoneNumberType, phoneNumber);
    }
}
