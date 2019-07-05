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
package uk.ac.york.its.b2.groupmanager.domain;

/**
 * @author Kelvin Hai {@link <a href="mailto:kelvin.hai@york.ac.uk">kelvin.hai@york.ac.uk</a>}
 * @version $Revision$ $Date$
 */

public final class GroupManConstants {
	public final static String STATUS_SUCCESS = "success";
	public final static String STATUS_FAILED = "failed";
	public final static String STATUS_ERROR = "error";
	public final static String GROUP = "group";
	public final static String STUDENTS = "students";
	public final static String TEACHING_ASSISTANTS = "teachingAssistants";
	public final static String INSTRUCTORS = "instructors";
	public final static String OTHERS = "others";
	public final static String NO_GROUPS_DEFINED = "groupman.message.no_groups_defined";
	public final static String GROUP_NOT_FOUND = "groupman.message.group_not_found";
	public final static String USER_NOT_FOUND = "groupman.message.user_not_found";
	public final static String COURSE_USER_NOT_FOUND = "groupman.message.course_user_not_found";
	public final static String GROUP_MEMBERSHIP_EXISTED = "groupman.message.group_membership_existed";
	public final static String VALIDATE_FAILURE = "groupman.message.perssist.validate_failure";
	public final static String UNKNOWN_ERROR = "groupman.message.perssist.unknown_error";
	public final static String USPECIFIED_ID_OBJECTS = "groupman.message.persist.uspecified_id_objects";
	public final static String PERSIST_SUCCESS = "groupman.message.persist.success";
	public final static String PERSIST_FAILED = "groupman.message.persist.failed";
	public final static String FILE_SIZE_EXCEEDED_EXCEPTION="groupman.message.resolve.exception.file_size_exceeded"; 
	public final static String UNEXPECTED_ERROR_EXCEPTION="groupman.message.resolve.exception.unexpected_error"; 
	
}
