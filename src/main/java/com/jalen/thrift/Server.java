package com.jalen.thrift;

import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.server.TThreadPoolServer;

public class Server {

    public static void startEngineServer(AdditionService.Processor<AdditionServiceHandler> processor) {
        try {
            TServerTransport serverTransport = new TServerSocket(9090);
            
            // TServer server = new TSimpleServer(new
            // Args(serverTransport).processor(processor));

            // Use this for a multithreaded server
            TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));

            System.out.println("Starting the server...");
            server.serve();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        startEngineServer(new AdditionService.Processor<AdditionServiceHandler>(new AdditionServiceHandler()));
    }

}
