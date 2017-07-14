package com.jalen.thrift;

import org.apache.thrift.TException;

public class AdditionServiceHandler implements AdditionService.Iface{

    public int add(int n1, int n2) throws TException {
        // TODO Auto-generated method stub
        return n1 + n2;
    }

}
