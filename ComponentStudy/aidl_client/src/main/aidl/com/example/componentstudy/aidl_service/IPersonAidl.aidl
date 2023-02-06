// IPersonAidl.aidl
package com.example.componentstudy.aidl_service;

import com.example.componentstudy.aidl_service.Person;

interface IPersonAidl {
    void addPerson(in Person person);

    List<Person> getPersonList();
}