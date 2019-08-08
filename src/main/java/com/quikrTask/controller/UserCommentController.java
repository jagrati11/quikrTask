package com.quikrTask.controller;

import com.quikrTask.model.ESUserCommentModel;
import com.quikrTask.service.UserCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/quikrTask/userComment")
public class UserCommentController {

    @Autowired
    private UserCommentService userCommentService ;

    @RequestMapping(method = RequestMethod.POST, path = "/add")
    public ResponseEntity<Object> addUserComment(@RequestBody ESUserCommentModel esUserCommentModel){
        userCommentService.indexUserComment(esUserCommentModel);
        return new ResponseEntity<>(HttpStatus.OK) ;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/search")
    public ResponseEntity<Object> search(@RequestParam(value = "matchPhrase") String matchPhrase){
        return new ResponseEntity<>(userCommentService.search(matchPhrase), HttpStatus.OK) ;
    }





}
