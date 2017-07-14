package com.jalen.spring2;

public class UserService {
    private UserDao dao;
    
    public UserDao getDao() {
        return dao;
    }

    public void setDao(UserDao dao) {
        this.dao = dao;
    }

    public void addUser(User user){
        dao.insertUser(user);
    }
}
