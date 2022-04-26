package com.marketganada.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("CategoryLargeInsertRequest")
public class CategorySmallInsertRequest {
    @ApiModelProperty(name = "중분류 ID")
    int categoryMiddleId;

    @ApiModelProperty(name = "소분류 이름")
    String categoryName;
}
