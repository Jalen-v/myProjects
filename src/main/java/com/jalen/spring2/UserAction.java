package com.jalen.spring2;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class UserAction {
    public static void main(String[] args) throws Exception {
//        UserService service = new UserService();
//        service.addUser(new User("zhangsan",12,"nanjing"));
//        UserDao userDao = new UserDao();
//        userDao.insertUser(new User("zhangsan",12,"beijing"));
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:spring-config/spring.xml");
        System.out.println(ac.getBean("userService"));
        System.out.println(ac.getBean("userDao"));
        UserService userService = (UserService)ac.getBean("userService");
        userService.addUser(new User("lisi",23,"nanjing"));
    }
}
