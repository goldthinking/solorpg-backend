package com.solorpgbackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "分页返回结果")
public class PaginatedResult<T> {
    @Schema(description = "剧本详细数据")
    private List<T> data;
    @Schema(description = "总记录数")
    private long total;
}