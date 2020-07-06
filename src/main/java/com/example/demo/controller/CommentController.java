package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
	//@RequestBody는 파라미터로 전달받은 JSON 문자열을 객체로 변환(객체로 변환된 JSON은 CommentDTO 클래스의 객체인 params에 매핑(바인딩)된다.)
	@RequestMapping(value = {"/comments", "/comments/{idx}"}, method = {RequestMethod.POST, RequestMethod.PATCH})
	public JsonObject registerComment(@PathVariable(value = "idx", required = false) Long idx, @RequestBody final CommentDTO params){

		JsonObject jsonObj = new JsonObject();

		try {

			boolean isRegistered = commentService.registerComment(params);
			jsonObj.addProperty("result", isRegistered);

		} catch (DataAccessException e) {

			jsonObj.addProperty("message", "데이터베이스 처리 과정에 문제가 발생하였습니다.");

		} catch (Exception e) {

			jsonObj.addProperty("message", "시스템에 문제가 발생하였습니다.");
			
		}

		return jsonObj;
	}
	
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
