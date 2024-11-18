package com.dylan.elearning.service;

import com.dylan.elearning.dto.request.admin.ChapterCreateReq;
import com.dylan.elearning.dto.request.admin.ChapterSearchReq;
import com.dylan.elearning.dto.request.admin.ChapterUpdateReq;
import com.dylan.elearning.dto.response.admin.ChapterResponseDto;
import com.dylan.elearning.dto.response.admin.ChapterSearchRes;

import java.util.List;

public interface IChapterService {
    List<ChapterResponseDto> addChapters(Long courseId, List<ChapterCreateReq> chapterCreateReqs);
    ChapterResponseDto updateChapter(Long courseId, Long chapterId, ChapterUpdateReq req);
    void softDeleteChapter(Long courseId, Long chapterId);
    ChapterSearchRes getChapters(ChapterSearchReq req);
}
