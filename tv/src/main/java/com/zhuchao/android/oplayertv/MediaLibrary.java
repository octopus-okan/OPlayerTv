/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.zhuchao.android.oplayertv;

import com.zhuchao.android.playsession.OPlayerSession;
import com.zhuchao.android.playsession.OPlayerSessionManager;
import com.zhuchao.android.video.Video;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class MediaLibrary {
    private static long count = 0;
    private static String MobileBlockName = null;
    private static String MobileBlockPath = null;
    private static OPlayerSessionManager mSessionManager = OplayerApplication.getOpsM();
    private static Map<Integer, OPlayerSession> mSessions = null;// new HashMap<Integer, OPlayerSession>();
    public static Map<Integer, String> mVideoCategory = null;//new HashMap<Integer, String>();
    public static Map<Integer, String> mVideoTypes = null;//new HashMap<Integer, String>();
    public static ArrayList MOVIE_CATEGORY = new ArrayList();
    public static ArrayList MOVIE_Types = new ArrayList();

    public static void setupCategoryList() {

        //LocalSession = mSessionManager.initLocalSessionContent(OplayerApplication.getAppContext());
        if (mVideoCategory == null)
            mVideoCategory = mSessionManager.getmTopSession().getmVideoCategoryNameList();//获取大的分类列表
        if (mVideoTypes == null)
            mVideoTypes = mSessionManager.getmTopSession().getmVideoTypeNameList();//获取视频类型信息
        setupVideoCategory();
    }

    public static void setupVideoCategory() {
        MOVIE_CATEGORY.clear();
        mSessions = mSessionManager.getmSessions();
        for (Map.Entry<Integer, OPlayerSession> entry : mSessions.entrySet()) {
            String name = entry.getValue().getmVideoCategoryNameList().get(entry.getKey());
            MOVIE_CATEGORY.add(name);
        }
        MOVIE_Types.clear();

        for (Map.Entry<Integer, String> entry : mVideoTypes.entrySet()) {
            MOVIE_Types.add(entry.getValue());
        }
    }

    public static void updateMobileMedia(String DeviceName, String DevicePath) {
       mSessionManager.initSessionFromMobileDisc();
    }

    public static List<Video> getMediaList(int categoryIndex) {
        List<Video> videos = null;
        String categoryName = MOVIE_CATEGORY.get(categoryIndex).toString();
        int categoryId = getCategoryIdByValue(categoryName);

        if (mSessionManager.isInitComplete() && (categoryId > 0)) {
            mSessions = mSessionManager.getmSessions();//获取板块分类集合
            videos = mSessions.get(categoryId).getVideos();//从集合中得到直播视频列表
        }
        return videos;
    }


    public static int getCategoryIdByValue(String value) {
        for (Map.Entry entry : mVideoCategory.entrySet()) {
            if (value.equals(entry.getValue()))
                return (int) entry.getKey();
        }
        return -10;
    }

    public static boolean isInitComplete() {
        return mSessionManager.isInitComplete();
    }


    /*
     public static  String MOVIE_CATEGORY[] = {
            "Category Zero",
            "Category One",
            "Category Two",
            "Category Three",
            "Category Four",
            "Category Five",
    };*/
   /*
    public static List<Movie> setupMovies() {
        list = new ArrayList<>();
        String title[] = {
                "Zeitgeist 2010_ Year in Review",
                "Google Demo Slam_ 20ft Search",
                "Introducing Gmail Blue",
                "Introducing Google Fiber to the Pole",
                "Introducing Google Nose"
        };

        String description = "Fusce id nisi turpis. Praesent viverra bibendum semper. "
                + "Donec tristique, orci sed semper lacinia, quam erat rhoncus massa, non congue tellus est "
                + "quis tellus. Sed mollis orci venenatis quam scelerisque accumsan. Curabitur a massa sit "
                + "amet mi accumsan mollis sed et magna. Vivamus sed aliquam risus. Nulla eget dolor in elit "
                + "facilisis mattis. Ut aliquet luctus lacus. Phasellus nec commodo erat. Praesent tempus id "
                + "lectus ac scelerisque. Maecenas pretium cursus lectus id volutpat.";
        String studio[] = {
                "Studio Zero", "Studio One", "Studio Two", "Studio Three", "Studio Four"
        };
        String videoUrl[] = {
                "http://commondatastorage.googleapis.com/android-tv/Sample%20videos/Zeitgeist/Zeitgeist%202010_%20Year%20in%20Review.mp4",
                "http://commondatastorage.googleapis.com/android-tv/Sample%20videos/Demo%20Slam/Google%20Demo%20Slam_%2020ft%20Search.mp4",
                "http://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Gmail%20Blue.mp4",
                "http://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Google%20Fiber%20to%20the%20Pole.mp4",
                "http://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Google%20Nose.mp4"
        };
        String bgImageUrl[] = {
                "http://commondatastorage.googleapis.com/android-tv/Sample%20videos/Zeitgeist/Zeitgeist%202010_%20Year%20in%20Review/bg.jpg",
                "http://commondatastorage.googleapis.com/android-tv/Sample%20videos/Demo%20Slam/Google%20Demo%20Slam_%2020ft%20Search/bg.jpg",
                "http://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Gmail%20Blue/bg.jpg",
                "http://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Google%20Fiber%20to%20the%20Pole/bg.jpg",
                "http://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Google%20Nose/bg.jpg",
        };
        String cardImageUrl[] = {
                "http://commondatastorage.googleapis.com/android-tv/Sample%20videos/Zeitgeist/Zeitgeist%202010_%20Year%20in%20Review/card.jpg",
                "http://commondatastorage.googleapis.com/android-tv/Sample%20videos/Demo%20Slam/Google%20Demo%20Slam_%2020ft%20Search/card.jpg",
                "http://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Gmail%20Blue/card.jpg",
                "http://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Google%20Fiber%20to%20the%20Pole/card.jpg",
                "http://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Google%20Nose/card.jpg"
        };

        for (int index = 0; index < title.length; ++index) {
            list.add(
                    buildMovieInfo(
                            title[index],
                            description,
                            studio[index],
                            videoUrl[index],
                            cardImageUrl[index],
                            bgImageUrl[index]));
        }

        return list;
    }

    private static Movie buildMovieInfo(
            String title,
            String description,
            String studio,
            String videoUrl,
            String cardImageUrl,
            String backgroundImageUrl) {
        Movie movie = new Movie();
        movie.setId(count++);
        movie.setTitle(title);
        movie.setDescription(description);
        movie.setStudio(studio);
        movie.setCardImageUrl(cardImageUrl);
        movie.setBackgroundImageUrl(backgroundImageUrl);
        movie.setVideoUrl(videoUrl);
        return movie;
    }*/
}