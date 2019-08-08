package com.quikrTask.service;

import com.quikrTask.model.ESUserCommentModel;

import java.util.List;

public interface UserCommentService {

    void indexUserComment(ESUserCommentModel esUserCommentModel) ;
    List<ESUserCommentModel> search(String matchPhrase) ;
}
