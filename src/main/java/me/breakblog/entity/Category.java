package me.breakblog.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;


@Data
@TableName(value = "category")
public class Category extends BaseEntity {

    private String name;

    @JsonIgnore
    @TableField(exist = false)
    private Integer postCount;
}
