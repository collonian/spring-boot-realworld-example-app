package io.spring.infrastructure.mybatis.mapper;

import io.spring.core.article.ArticleHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ArticleHistoryMapper {
    void insert(@Param("history") ArticleHistory articleHistory);
}
