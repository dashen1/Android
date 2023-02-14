package com.example.hilt.base.interfacedi;

import android.util.Log;

import javax.inject.Inject;

public class TestClass implements TestInterface{

    @Inject
    TestClass(){}

    @Override
    public void method() {
        Log.d("TestClass", "TestClass method() was called!");
    }
}
