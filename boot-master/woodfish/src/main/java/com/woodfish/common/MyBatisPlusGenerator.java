package com.woodfish.common;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

public class MyBatisPlusGenerator {

    //表名
    public static final String TABLE_NAME = "platform";
    //作者
    public static final String AUTHOR = "sheng";
    //Mapper路径
    public static final String OUT_PUT_FILE = "E:/WoodFish/boot-master/woodfish/src/main/resources/mapper";
    //父级路径
    public static final String PARENT_PATH = "E:/WoodFish/boot-master/woodfish/src/main/java";
    //父级包名
    public static final String PARENT_CLASS = "com.woodfish";


    public static void main(String[] args) {
        FastAutoGenerator.create(
                        "jdbc:mysql://localhost:3306/woodfish",
                        "root",
                        "1234")
                .globalConfig(builder -> {
                    builder.author(AUTHOR) // 设置作者
                            .fileOverride() // 覆盖已生成文件
                            .enableSwagger() // 开启 swagger 模式
                            .outputDir(PARENT_PATH)
                            .disableOpenDir() //不打开目录
                            .commentDate("yyyy-MM-dd");
                })
                .packageConfig(builder -> {
                    builder.parent(PARENT_CLASS) // 设置父包名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, OUT_PUT_FILE));
                })
                .strategyConfig(builder -> {
                    builder.addInclude(TABLE_NAME)//设置表名
                            .entityBuilder()    //entity前置，才能用lombok
                            .enableLombok() //lombok注解
                            .mapperBuilder()//mapper注解
                            .enableMapperAnnotation()//使用lombok
                            .controllerBuilder() //RestController前置
                            .enableRestStyle();//使用RestController
                })
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
