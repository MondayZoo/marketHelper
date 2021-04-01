package com.month.markethelper.db.repository;

import com.month.markethelper.base.BaseApplication;
import com.month.markethelper.db.MarketDatabase;
import com.month.markethelper.db.dao.UserDAO;
import com.month.markethelper.db.entity.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * User表对应的仓库类
 */
public class UserRepository {

    private UserDAO userDAO;

    private ExecutorService mFixedThreadPool;

    public UserRepository() {
        //数据库
        MarketDatabase marketDatabase = MarketDatabase.getInstance();
        userDAO = marketDatabase.getUserDao();
        //线程池
        mFixedThreadPool = Executors.newFixedThreadPool(4);
    }

    /**
     * 数据操作
     */
    public void register(User user) {
        mFixedThreadPool.execute(new RegisterTask(userDAO, user));
    }

    public void search(String phoneNum) {
        mFixedThreadPool.execute(new SearchTask(userDAO, phoneNum));
    }

    /**
     *  Runnable
     */
    static class RegisterTask implements Runnable {

        private final UserDAO userDAO;
        private final User user;

        public RegisterTask(UserDAO userDAO, User user) {
            this.userDAO = userDAO;
            this.user = user;
        }

        @Override
        public void run() {
            userDAO.register(user);
        }
    }

    static class SearchTask implements Runnable {

        private final UserDAO userDAO;
        private final String phoneNum;

        public SearchTask(UserDAO userDAO, String phoneNum) {
            this.userDAO = userDAO;
            this.phoneNum = phoneNum;
        }

        @Override
        public void run() {
            userDAO.findUser(phoneNum);
        }
    }
}
