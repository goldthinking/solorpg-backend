package com.solorpgbackend;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Types;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SpringBootTest
class SolorpgBackendApplicationTests {

	@Test
	void contextLoads() {
	}

	public static void main(String[] args) {
		FastAutoGenerator.create("jdbc:mysql://124.220.94.212:3306/solorpg?serverTimezone=GMT%2B8", "Nemotte", "Jinhuahua_312")
				// 全局配置
				.globalConfig(builder -> {
					builder.author("Nemotte") // 设置作者
							.outputDir("C:\\Nemotte\\Java\\solorpg-backend\\src\\main\\java");}) // 指定输出目录
				// 包配置
				.packageConfig(builder ->
						builder.parent("com.solorpgbackend") // 设置父包名
								.moduleName("") // 设置父包模块名
								.pathInfo(Collections.singletonMap(OutputFile.xml, "C:\\Nemotte\\Java\\solorpg-backend\\src\\main\\resources\\mapper")) // 设置mapperXml生成路径
				)
				// 策略配置
				.strategyConfig((scanner, builder) -> builder.addInclude(getTables(scanner.apply("请输入表名，多个英文逗号分隔？所有输入 all")))
						.entityBuilder()
						.enableLombok()
						.addTableFills(
								new Column("create_time", FieldFill.INSERT)
						)
						.build())
				// 使用Freemarker引擎模板，默认的是Velocity引擎模板
				.templateEngine(new FreemarkerTemplateEngine())
				.execute();
	}

	// 处理 all 情况
	protected static List<String> getTables(String tables) {
		return "all".equals(tables) ? Collections.emptyList() : Arrays.asList(tables.split(","));
	}

}
