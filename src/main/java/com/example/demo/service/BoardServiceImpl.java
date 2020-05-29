package com.example.demo.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.domain.BoardDTO;
import com.example.demo.mapper.BoardMapper;
import com.example.demo.paging.PaginationInfo;

@Service	//해당 클래스가 비즈니스 로직을 처리하는 서비스 클래스임을 의미
public class BoardServiceImpl implements BoardService {

	//BoardMapper 빈(Bean)을 주입
	@Autowired
	private BoardMapper boardMapper;
	
	@Override
	public boolean registerBoard(BoardDTO params) {
		
		int queryResult = 0;

		if (params.getIdx() == null) {
			queryResult = boardMapper.insertBoard(params);
		} else {
			queryResult = boardMapper.updateBoard(params);
		}

		return (queryResult == 1) ? true : false;
	}

	@Override
	public BoardDTO getBoardDetail(Long idx) {
		
		return boardMapper.selectBoardDetail(idx);
	}

	@Override
	public boolean deleteBoard(Long idx) {
		
		int queryResult = 0;

		BoardDTO board = boardMapper.selectBoardDetail(idx);

		if (board != null && "N".equals(board.getDeleteYn())) {
			queryResult = boardMapper.deleteBoard(idx);
		}

		return (queryResult == 1) ? true : false;
	}

	@Override
	public List<BoardDTO> getBoardList(BoardDTO params) {
		
		List<BoardDTO> boardList = Collections.emptyList();

		int boardTotalCount = boardMapper.selectBoardTotalCount(params);

		PaginationInfo paginationInfo = new PaginationInfo(params);
		paginationInfo.setTotalRecordCount(boardTotalCount);
		
		params.setPaginationInfo(paginationInfo);
		
		if (boardTotalCount > 0) {
			boardList = boardMapper.selectBoardList(params);
		}

		return boardList;
	}

}
