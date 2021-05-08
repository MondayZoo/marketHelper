package com.month.markethelper.fragment.store;

import android.util.Log;

import com.month.markethelper.db.MarketDatabase;
import com.month.markethelper.db.dao.CommentDAO;
import com.month.markethelper.db.dao.StoreDAO;
import com.month.markethelper.db.dao.UserDAO;
import com.month.markethelper.db.entity.Comment;
import com.month.markethelper.db.entity.User;
import com.month.markethelper.utils.FormatUtils;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CommentViewModel extends ViewModel {

    public static final String TAG = "CommentViewModel";

    private CommentDAO commentDAO;
    private UserDAO userDAO;
    private StoreDAO storeDAO;

    //总评相关
    private long storeId;
    private MutableLiveData<Float> finalRating;
    private MutableLiveData<String> commentCount;
    private MutableLiveData<String> favoriteRate;

    public CommentViewModel() {
        MarketDatabase marketDatabase = MarketDatabase.getInstance();
        commentDAO = marketDatabase.getCommentDao();
        userDAO = marketDatabase.getUserDao();
        storeDAO = marketDatabase.getStoreDao();
    }

    public void initLiveData() {
        Log.e("TAG", "storeId --> " + storeId);
        float ratingSum = commentDAO.getSumOfRatingByStoreId(storeId);
        int commentCount = commentDAO.getCountByStoreId(storeId);
        int favoriteCount = commentDAO.getFavoriteCountByStoreId(storeId);
        Log.e("TAG", "ratingSum --> " + ratingSum);
        Log.e("TAG", "commentCount --> " + commentCount);
        Log.e("TAG", "favoriteCount --> " + favoriteCount);

        //商家最终评分
        finalRating = new MutableLiveData<>();
        //评论数
        this.commentCount = new MutableLiveData<>();
        //好评率
        favoriteRate = new MutableLiveData<>();

        if (commentCount == 0) {
            finalRating.setValue(3.0f);
            this.commentCount.setValue("暂无");
            favoriteRate.setValue("暂无");
        } else {
            finalRating.setValue(Float.parseFloat(FormatUtils.oneDecimalFormat(ratingSum, commentCount)));
            this.commentCount.setValue(String.valueOf(commentCount));
            favoriteRate.setValue((FormatUtils.percentFormat(favoriteCount, commentCount)));
        }
    }

    //----------------------------Database Operation---------------------------------

    /**
     * 获取用户昵称
     * @param phoneNum 用户手机号
     * @return 用户昵称
     */
    public String getUserNickName(String phoneNum) {
        User user = userDAO.findUser(phoneNum);
        return user.getNickName();
    }

    /**
     * 向评论表中插入一条新记录
     * @param nickName 用户昵称
     * @param content 评论内容
     * @param rating 评分
     * @param date 评论日期
     * @param storeId 店铺id
     */
    public void addComment(String nickName, String content, float rating, String date, long storeId) {
        Comment comment = new Comment();
        comment.setNickName(nickName);
        comment.setContent(content);
        comment.setRating(rating);
        comment.setDate(date);
        comment.setStoreId(storeId);
        commentDAO.add(comment);
    }

    //----------------------------Getter Method---------------------------------

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public MutableLiveData<Float> getFinalRating() {
        return finalRating;
    }

    public MutableLiveData<String> getCommentCount() {
        return commentCount;
    }

    public MutableLiveData<String> getFavoriteRate() {
        return favoriteRate;
    }

    public LiveData<List<Comment>> getCommentList() {
        return commentDAO.findAllByStoreId(storeId);
    }
}