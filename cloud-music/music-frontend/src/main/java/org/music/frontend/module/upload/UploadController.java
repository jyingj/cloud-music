package org.music.frontend.module.upload;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.music.common.utils.DateUtil;
import org.music.common.utils.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;

/**
 * 文件上传
 * 
 * @author jyj
 *
 * @date 2017年11月19日
 */
@Api(description = "文件上传Controller")
@RestController
public class UploadController {

	Logger logger = LoggerFactory.getLogger(UploadController.class);
	private static final String BASEPATH = "/upload/";

	@ApiOperation(value = "文件上传", notes = "")
	@ApiResponse(response = String.class, code = 200, message = "")
	@RequestMapping(value = "/upload", method = { RequestMethod.GET, RequestMethod.POST })
	public String fileUpload(@RequestParam("file") MultipartFile file,
			@RequestParam("modelName") @ApiParam(value = "modelName(模块名称(如：product、order等)，必须)", required = true) String modelName,
			@RequestParam("detailName") @ApiParam(value = "detailName(详细名称(如：logo、accessory等)，必须)", required = true) String detailName,
			HttpServletRequest request) {
		logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> coming.");

		if (modelName == null || "".equals(modelName)) {
			System.out.println("模块名称不能为空！");
		}
		if (detailName == null || "".equals(detailName)) {
			System.out.println("详细名称不能为空！");
		}
		String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
		if (!"jpg,jpeg,png,gif".contains(suffix.toLowerCase())) {
			System.out.println("上传文件格式必须为[jpg,jpeg,png,gif]");
		}
		if (file.getSize() > 1024 * 1024 * 200) {
			System.out.println("文件大小不能超过200MB");
		}
		String dateDir = DateUtil.dateToString(new Date(), "yyyyMM");
		return FileUtil.upload2(file, BASEPATH + modelName + "/", detailName + "/" + dateDir);
	}

}
