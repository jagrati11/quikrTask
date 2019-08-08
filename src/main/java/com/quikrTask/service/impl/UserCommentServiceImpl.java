package com.quikrTask.service.impl;


import com.quikrTask.model.ESUserCommentModel;
import com.quikrTask.repository.ESRepository;
import com.quikrTask.service.UserCommentService;
import com.quikrTask.utils.RabbitProducerConsumerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserCommentServiceImpl implements UserCommentService{

    @Autowired
    private RabbitProducerConsumerUtils rabbitProducerConsumerUtils ;

    @Autowired
    private ESRepository esRepository ;

    @Override
    public void indexUserComment(ESUserCommentModel esUserCommentModel) {
        rabbitProducerConsumerUtils.publishToUserCommentQueue(esUserCommentModel);
    }

    @Override
    public List<ESUserCommentModel> search(String matchPhrase) {
        return esRepository.search(matchPhrase) ;
    }
}
