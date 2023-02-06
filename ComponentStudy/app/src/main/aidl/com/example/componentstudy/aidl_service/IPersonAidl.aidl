// IPersonAidl.aidl
package com.example.componentstudy.aidl_service;

// Declare any non-default types here with import statements

import com.example.componentstudy.aidl_service.Person;

interface IPersonAidl {
    void addPerson(in Person person);

    List<Person> getPersonList();
}