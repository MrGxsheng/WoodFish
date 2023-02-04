package com.woodfish.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author sheng
 * @since 2023-01-14
 */
@Getter
@Setter
@ApiModel(value = "Cost对象", description = "消费记录")
public class Cost implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("外键，用户id")
    private Integer userid;

    @ApiModelProperty("花费数目")
    private Integer cost;

    @ApiModelProperty("消费项目")
    private Integer itemid;

    @ApiModelProperty("消费平台")
    private Integer platformid;

    @ApiModelProperty("创建日期")
    private LocalDateTime createtime;


}
