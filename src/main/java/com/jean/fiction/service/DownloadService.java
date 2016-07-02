package com.jean.fiction.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jean.fiction.utils.Httpclient;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.apache.log4j.Logger;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jinshubao on 2016/6/4.
 */
public class DownloadService extends Service<Void> {
    static String contentUrl = "http://tome.zongheng.com/ajax/book.totalTomeChapter.do";
    static String chaptersUrl = "http://www.zongheng.com/ajax/book.getTomeChapterList.do";
    public StringProperty bookName = new SimpleStringProperty("");
    public StringProperty author = new SimpleStringProperty("");
    Logger logger = Logger.getLogger(this.getClass());
    private String bookId;

    public DownloadService(String bookId) {
        this.bookId = bookId;
    }

    @Override
    protected void ready() {
        super.ready();
    }

    @Override
    protected void running() {
        super.running();
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Map<String, String> params = new HashMap<>();
                params.put("bookId", bookId);
                String listString = Httpclient.get(chaptersUrl, params);
                JSONObject jsonObject = JSON.parseObject(listString);
                JSONArray chapterOrderViewList = jsonObject.getJSONArray("publicTomes");
                JSONObject publicTomes = (JSONObject) chapterOrderViewList.get(1);
                logger.debug(publicTomes.toString());

                JSONArray publicChapters = publicTomes.getJSONArray("publicChapters");

                StringBuilder content = new StringBuilder();
                for (int i = 0; i < publicChapters.size(); i++) {
                    JSONObject chapter = (JSONObject) publicChapters.get(i);
                    String chapterId = chapter.getString("chapterId");
                    String chapterName = chapter.getString("chapterName");
                    params.put("chapterId", chapterId);
                    updateMessage("【正在下载】" + chapterName);
                    String str = download(contentUrl, params);
                    content.append(chapterName).append("\r\n");
                    if (str.isEmpty()) {
                        updateMessage("【下载失败】" + chapterName);
                    } else {
                        content.append(str).append("\r\n");
                    }
                    updateProgress(i, publicChapters.size());
                }

                BufferedOutputStream bufferedOutputStream = null;
                String fileName = "E:\\Downloads\\小说\\" + bookName.get() + "-" + author.get() + ".txt";
                try {
                    File file = new File(fileName);
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                    bufferedOutputStream.write(content.toString().getBytes());
                    bufferedOutputStream.flush();
                } finally {
                    if (bufferedOutputStream != null) {
                        bufferedOutputStream.close();
                    }
                }
                updateMessage("【下载完成】");
                return null;
            }

            @Override
            protected void failed() {
                updateProgress(1, 1);
                updateMessage("【下载出错】");
                Throwable exception = getException();
                logger.error(exception);
                exception.printStackTrace();
            }


        };
    }

    private String download(String url, Map<String, String> params) {
        String jsonStr = Httpclient.get(url, params);
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        boolean success = jsonObject.getBoolean("success");
        if (success) {
            if (author.get() == null || author.get().isEmpty()) {
                author.set(jsonObject.getJSONObject("authorInfo").getString("pseudonym"));
                bookName.set(jsonObject.getJSONObject("book").getString("name"));
            }
            String content = jsonObject.getString("content");
            content = content.replaceAll("<p>", "\t");
            content = content.replaceAll("<\\/p>", "\r\n");
            content = content + "\r\n";
            return content;
        }
        return "";
    }
}
