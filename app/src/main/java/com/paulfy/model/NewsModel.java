package com.paulfy.model;

import java.io.Serializable;
import java.util.List;

public class NewsModel implements Serializable
{
    private static final long serialVersionUID = 854564567L;
    private int code;
    private String status;
    private String message;
    private List<Data> data;

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public List<Data> getData()
    {
        return data;
    }

    public void setData(List<Data> data)
    {
        this.data = data;
    }

    public class Data implements Serializable{
        private int id;
        private int categories_id;
        private String title;
        private String title_url;
        private String description;
        private String news_upload_time;
        private String created_at;
        private String updated_at;
        private Data news;

        public Data getNews() {
            return news;
        }

        public void setNews(Data news) {
            this.news = news;
        }

        private int likeCount;

        public int getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(int likeCount) {
            this.likeCount = likeCount;
        }

        public int getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(int commentCount) {
            this.commentCount = commentCount;
        }

        private int commentCount;


        public boolean isLike() {
            return isLike;
        }

        public void setLike(boolean like) {
            isLike = like;
        }

        private boolean isLike;
        private List<Likes> likes;
        private List<Comments> comment;

        public int getId()
        {
            return id;
        }

        public void setId(int id)
        {
            this.id = id;
        }

        public int getCategories_id()
        {
            return categories_id;
        }

        public void setCategories_id(int categories_id)
        {
            this.categories_id = categories_id;
        }

        public String getTitle()
        {
            return title;
        }

        public void setTitle(String title)
        {
            this.title = title;
        }

        public String getTitle_url()
        {
            return title_url;
        }

        public void setTitle_url(String title_url)
        {
            this.title_url = title_url;
        }

        public String getDescription()
        {
            return description;
        }

        public void setDescription(String description)
        {
            this.description = description;
        }

        public String getNews_upload_time()
        {
            return news_upload_time;
        }

        public void setNews_upload_time(String news_upload_time)
        {
            this.news_upload_time = news_upload_time;
        }

        public String getCreated_at()
        {
            return created_at;
        }

        public void setCreated_at(String created_at)
        {
            this.created_at = created_at;
        }

        public String getUpdated_at()
        {
            return updated_at;
        }

        public void setUpdated_at(String updated_at)
        {
            this.updated_at = updated_at;
        }

        public List<Likes> getLikes()
        {
            return likes;
        }

        public void setLikes(List<Likes> likes)
        {
            this.likes = likes;
        }

        public List<Comments> getComment()
        {
            return comment;
        }

        public void setComment(List<Comments> comment)
        {
            this.comment = comment;
        }

        public class Likes {
            private int id;
            private int news_id;
            private int user_id;
            private int like;
            private String created_at;
            private String updated_at;

            public int getId()
            {
                return id;
            }

            public void setId(int id)
            {
                this.id = id;
            }

            public int getNews_id()
            {
                return news_id;
            }

            public void setNews_id(int news_id)
            {
                this.news_id = news_id;
            }

            public int getUser_id()
            {
                return user_id;
            }

            public void setUser_id(int user_id)
            {
                this.user_id = user_id;
            }

            public int getLike()
            {
                return like;
            }

            public void setLike(int like)
            {
                this.like = like;
            }

            public String getCreated_at()
            {
                return created_at;
            }

            public void setCreated_at(String created_at)
            {
                this.created_at = created_at;
            }

            public String getUpdated_at()
            {
                return updated_at;
            }

            public void setUpdated_at(String updated_at)
            {
                this.updated_at = updated_at;
            }
        }

        public class Comments implements Serializable{

            private int id;
            private int news_id;
            private int user_id;
            private String  comment;
            private String  created_at;
            private String  updated_at;

            public int getId()
            {
                return id;
            }

            public void setId(int id)
            {
                this.id = id;
            }

            public int getNews_id()
            {
                return news_id;
            }

            public void setNews_id(int news_id)
            {
                this.news_id = news_id;
            }

            public int getUser_id()
            {
                return user_id;
            }

            public void setUser_id(int user_id)
            {
                this.user_id = user_id;
            }

            public String getComment()
            {
                return comment;
            }

            public void setComment(String comment)
            {
                this.comment = comment;
            }

            public String getCreated_at()
            {
                return created_at;
            }

            public void setCreated_at(String created_at)
            {
                this.created_at = created_at;
            }

            public String getUpdated_at()
            {
                return updated_at;
            }

            public void setUpdated_at(String updated_at)
            {
                this.updated_at = updated_at;
            }
        }
    }
}
