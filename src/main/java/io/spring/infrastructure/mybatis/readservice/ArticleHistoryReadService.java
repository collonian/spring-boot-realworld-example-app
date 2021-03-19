package io.spring.infrastructure.mybatis.readservice;

import io.spring.application.Page;
import io.spring.application.data.ArticleHistoryData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ArticleHistoryReadService {
    List<ArticleHistoryData> queryHistories(
            @Param("slug") String slug,
            @Param("page")Page page
            );
    int countHistories(@Param("slug") String slug);
}
