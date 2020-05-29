package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.adapter.GsonLocalDateTimeAdapter;
import com.example.demo.domain.CommentDTO;
import com.example.demo.service.CommentService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@RestController
public class CommentController {

	@Autowired
	public CommentService commentService;
	
	//@PathVariable은 URI에 파라미터로 전달받을 변수를 지정
	@GetMapping(value = "/comments/{boardIdx}")
	public JsonObject getCommentList(@PathVariable("boardIdx") Long boardIdx, @ModelAttribute("params") CommentDTO params) {
		
		JsonObject jsonObj = new JsonObject();
		
		List<CommentDTO> commentList = commentService.getCommentList(params);
		
		if(CollectionUtils.isEmpty(commentList) == false) {
			
			Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter()).create();
			JsonArray jsonArr = gson.toJsonTree(commentList).getAsJsonArray();
			jsonObj.add("commentList", jsonArr);
		}
		
		return jsonObj;
	}
}
