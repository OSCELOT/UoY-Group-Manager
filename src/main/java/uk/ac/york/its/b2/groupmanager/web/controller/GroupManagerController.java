/**
 *  Copyright (C) 2019 University of York, UK.
 *
 *  This project was initiated through a donation of source code by the
 *  University of York, UK. It contains free software; you can redistribute
 *  it and/or modify it under the terms of the GNU General Public License as
 *  published by the Free Software Foundation; either version 2 of the
 *  License, or any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 *  For more information please contact:
 *
 *  Web Services Group
 *  IT Service
 *  University of York
 *  YO10 5DD
 *  United Kingdom
 */
package uk.ac.york.its.b2.groupmanager.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import uk.ac.york.its.b2.groupmanager.domain.GroupManConstants;
import uk.ac.york.its.b2.groupmanager.domain.GroupsUploadCommand;
import uk.ac.york.its.b2.groupmanager.service.GroupManagerService;

/**
 * @author Kelvin Hai {@link <a href="mailto:kelvin.hai@york.ac.uk">kelvin.hai@york.ac.uk</a>}
 * @version $Revision$ $Date$
 */

@Controller
public class GroupManagerController  implements HandlerExceptionResolver{

	private static final Logger logger = LoggerFactory.getLogger(GroupManagerController.class);

	@Autowired
	private GroupManagerService groupManagerService;

	@RequestMapping(value = "/showgroups", method = RequestMethod.GET)
	public String show(
			@RequestParam(value = "course_id", required = true) String courseId, Model model) {
		logger.info("Welcome GroupManagerController.show!");
		if (StringUtils.isNotEmpty(courseId)) {
			model.addAttribute("groupsWithMembers", groupManagerService.loadGroupsWithMembers(courseId));
			return "show";
		} else {
			logger.error("Method Show: Parameter course_id is missing.");
			return "error";
		}
	}

	
	@RequestMapping(value="/uploadgroups", method=RequestMethod.GET)
	public String showUpload(@RequestParam(value = "course_id", required = true) String courseId, Model model) {	
		GroupsUploadCommand command = new GroupsUploadCommand();
		model.addAttribute("command", command);
		return "upload";
	}
	
	@RequestMapping(value="/uploadgroups", method=RequestMethod.POST)
	public String processUpload(
			@ModelAttribute(value="command") GroupsUploadCommand command,
			@RequestParam(value = "course_id", required = true) String courseId,
			BindingResult result,
			ModelMap model){
		if (!result.hasErrors()) {
			model.addAttribute("groupAllocationList", groupManagerService.persistGroup(courseId, command));
			return "success";
		}else{ return "upload"; }
	}
	
	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object object, Exception exception) {
		logger.info("resolveException: " + exception.getMessage());
		Map<String, Object> model = new HashMap<String, Object>();
		if (exception instanceof MaxUploadSizeExceededException) {
			MaxUploadSizeExceededException mException = (MaxUploadSizeExceededException) exception;
			model.put("errorCode", GroupManConstants.FILE_SIZE_EXCEEDED_EXCEPTION);
			model.put("errorMessage", mException.getMaxUploadSize());
			model.put("command",  new GroupsUploadCommand());
			return new ModelAndView("upload", (Map<String, Object>)model);
		} else {
			model.put("errorCode", GroupManConstants.UNEXPECTED_ERROR_EXCEPTION);
			model.put("errorMessage", exception.getMessage());
			return new ModelAndView("error", (Map<String, Object>)model);
		}
		
	}
}
