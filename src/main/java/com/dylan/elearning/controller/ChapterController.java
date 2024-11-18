package com.dylan.elearning.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.dylan.elearning.constant.UrlConstant;
import com.dylan.elearning.dto.request.admin.ChapterCreateReq;
import com.dylan.elearning.dto.request.admin.ChapterSearchReq;
import com.dylan.elearning.dto.request.admin.ChapterUpdateReq;
import com.dylan.elearning.dto.response.admin.ChapterResponseDto;
import com.dylan.elearning.dto.response.admin.ChapterSearchRes;
import com.dylan.elearning.entity.Chapter;
import com.dylan.elearning.service.IChapterService;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(UrlConstant.API_V1)
@RequiredArgsConstructor
public class ChapterController {

    // use constructor injection
    private final IChapterService chapterService;

    /**
     * Thêm nhiều chapter vào một course cụ thể.
     *
     * @param courseId ID của khóa học mà các chương sẽ được thêm vào.
     * @param req      danh sách các đối tượng {@link ChapterCreateReq} chứa thông tin chapter cần tạo.
     * @return         List<ChapterResponseDto> chứa danh sách các đối tượng {@link Chapter} đã được tạo.
     * @see IChapterService#addChapters(Long, List)
     */
    @PostMapping(UrlConstant.ADD_CHAPTERS)
    public List<ChapterResponseDto> addChapters(@PathVariable("course_id") Long courseId,
                                     @Valid @RequestBody List<ChapterCreateReq> req) {
        return chapterService.addChapters(courseId, req);
    }

    /**
     * Cập nhật một chapter trong một course cụ thể.
     *
     * @param courseId  ID của course mà chapter thuộc về.
     * @param chapterId ID của chapter cần được update.
     * @param req       đối tượng {@link ChapterUpdateReq} chứa thông tin cập nhật cho chapter.
     * @return          ChapterResponseDto chứa đối tượng {@link Chapter} đã được cập nhật.
     * @see IChapterService#updateChapter(Long, Long, ChapterUpdateReq)
     */
    @PutMapping(UrlConstant.UPDATE_CHAPTERS)
    public ChapterResponseDto updateChapter(@PathVariable("course_id") Long courseId,
                                           @PathVariable("chapter_id") Long chapterId,
                                           @Valid @RequestBody ChapterUpdateReq req) {
        return chapterService.updateChapter(courseId, chapterId, req);
    }

    /**
     * soft delete chapter trong một course cụ thể.
     * <p>
     * soft delete có nghĩa là chương được đánh dấu là đã xóa nhưng không bị xóa vĩnh viễn khỏi cơ sở dữ liệu.
     * </p>
     *
     * @param courseId  ID của course mà chapter thuộc về.
     * @param chapterId ID của chapter cần được xóa mềm.
     * @return          ResponseEntity với thông báo thành công.
     * @see IChapterService#softDeleteChapter(Long, Long)
     */
    @DeleteMapping(UrlConstant.DELETE_CHAPTERS)
    public ResponseEntity<?> softDeleteChapter(@PathVariable("course_id") Long courseId,
                                               @PathVariable("chapter_id") Long chapterId) {
        chapterService.softDeleteChapter(courseId, chapterId);
        return ResponseEntity.ok("Chapter soft deleted successfully.");
    }

    /**
     * Lấy danh sách chapter theo phân trang dựa vào các field tìm kiếm.
     * <p>
     * Các field tìm kiếm bao gồm các field như tên chapter, status, ID khóa học, và khoảng thời gian.
     * </p>
     *
     * @param req         đối tượng {@link ChapterSearchReq} chứa các field tìm kiếm.
     * @return            {@link ChapterSearchRes} với kết quả tìm kiếm.
     * @see IChapterService#getChapters(ChapterSearchReq)
     */
    @GetMapping(UrlConstant.GET_CHAPTERS)
    public ChapterSearchRes getChapters(@RequestBody(required = false) ChapterSearchReq req) {
        return chapterService.getChapters(req);
    }
}

