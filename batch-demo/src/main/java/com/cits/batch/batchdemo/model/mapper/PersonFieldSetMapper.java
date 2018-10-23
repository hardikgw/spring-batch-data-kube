package com.cits.batch.batchdemo.model.mapper;

import com.cits.batch.batchdemo.model.Person;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;


public class PersonFieldSetMapper implements FieldSetMapper<Person> {
    @Override
    public Person mapFieldSet(FieldSet fieldSet) {
        String firstName = fieldSet.readString(0);
        String lastName = fieldSet.readString(1);

        return new Person(firstName, lastName);
    }
}