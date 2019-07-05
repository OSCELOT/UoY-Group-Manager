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
package uk.ac.york.its.b2.groupmanager.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import blackboard.base.BaseComparator;
import blackboard.base.GenericFieldComparator;
import blackboard.data.ValidationException;
import blackboard.data.course.Course;
import blackboard.data.course.CourseMembership;
import blackboard.data.course.Group;
import blackboard.data.course.GroupMembership;
import blackboard.data.user.User;
import blackboard.persist.DataType;
import blackboard.persist.Id;
import blackboard.persist.KeyNotFoundException;
import blackboard.persist.PersistenceException;
import blackboard.persist.course.CourseMembershipDbLoader;
import blackboard.persist.course.GroupDbLoader;
import blackboard.persist.course.GroupMembershipDbLoader;
import blackboard.persist.course.GroupMembershipDbPersister;
import blackboard.persist.user.UserDbLoader;

import com.Ostermiller.util.CSVParse;
import com.Ostermiller.util.ExcelCSVParser;

import uk.ac.york.its.b2.groupmanager.domain.GroupManConstants;
import uk.ac.york.its.b2.groupmanager.domain.GroupAllocation;
import uk.ac.york.its.b2.groupmanager.domain.GroupsUploadCommand;
import uk.ac.york.its.b2.groupmanager.service.GroupManagerService;

/**
 * @author Kelvin Hai {@link <a href="mailto:kelvin.hai@york.ac.uk">kelvin.hai@york.ac.uk</a>}
 * @version $Revision$ $Date$
 * 
 * Desc: 
 *       This class was developed based on Anthony's initial implementation of the UploadGroupsController class. 
 *       
 */

@Service
public class GroupManagerServiceImpl implements GroupManagerService {
	private static final Logger logger = LoggerFactory.getLogger(GroupManagerServiceImpl.class);
	private static final GenericFieldComparator<User> familyNameComparator = new GenericFieldComparator<User>(BaseComparator.ASCENDING, "getFamilyName", User.class);
	private static final GenericFieldComparator<Group> groupTitleComparator = new GenericFieldComparator<Group>(BaseComparator.ASCENDING, "getTitle", Group.class);
	
	@Autowired
	private CourseMembershipDbLoader courseMembershipDbLoader;
	
	@Autowired
	private GroupMembershipDbLoader groupMembershipDbLoader;
	
	@Autowired
	private GroupDbLoader groupDbLoader;
	
	@Autowired
	private UserDbLoader userDbLoader;
	
	//@Autowired does not work
	private GroupMembershipDbPersister groupMembershipDbPersister = null;
	
	
	public GroupManagerServiceImpl(){
		try {
			groupMembershipDbPersister = GroupMembershipDbPersister.Default.getInstance();
		} catch (PersistenceException e) {
			logger.error(e.getMessage());
		}
	}
	
	
	public List<Map<String, Object>> loadGroupsWithMembers(String courseIdString){
		List<Map<String, Object>> groupsWithMembers = new ArrayList<Map<String, Object>>();
		if(StringUtils.isNotEmpty(courseIdString)){
			Id courseIdObject = getId(new DataType(Course.class), courseIdString);
			List<Group> groups = loadGroups(courseIdObject);
			
			for (Group group : groups) {
				Map<String, Object> map = new HashMap<String, Object>();
				List<User> instructors = new ArrayList<User>();
				List<User> teachingAssistants = new ArrayList<User>();
				List<User> students = new ArrayList<User>();
				List<User> others = new ArrayList<User>();
	
				
				for (User member : loadGroupMemebers(group.getId())) {
					CourseMembership courseMembership = loadCourseMembership(courseIdObject,
									member.getId());
					if (courseMembership.getRole().equals(
							CourseMembership.Role.STUDENT)) {
						students.add(member);
					} else if (courseMembership.getRole().equals(
							CourseMembership.Role.INSTRUCTOR)) {
						instructors.add(member);
					} else if (courseMembership.getRole().equals(
							CourseMembership.Role.TEACHING_ASSISTANT)) {
						teachingAssistants.add(member);
					} else {
						others.add(member);
					}
				}
				
				Collections.sort(instructors, familyNameComparator);
				Collections.sort(teachingAssistants, familyNameComparator);
				Collections.sort(others, familyNameComparator);
				Collections.sort(students, familyNameComparator);
	            map.put(GroupManConstants.GROUP, group);
	            map.put(GroupManConstants.STUDENTS, students);
	            map.put(GroupManConstants.TEACHING_ASSISTANTS, teachingAssistants);
	            map.put(GroupManConstants.INSTRUCTORS, instructors);
	            map.put(GroupManConstants.OTHERS, others);
				groupsWithMembers.add(map);
			}
		}
		return groupsWithMembers;
	}
	
	
	public List<GroupAllocation> persistGroup(String courseIdString, GroupsUploadCommand command){
		List<GroupAllocation> resultList = new ArrayList<GroupAllocation>();
		if(StringUtils.isNotEmpty(courseIdString)){
			Id courseIdObject = getId(new DataType(Course.class), courseIdString);
			String[][] values = getCsvValues(command);
			if(values!=null && values.length>0){
				
				List<Group> courseGroups = this.loadGroups(courseIdObject);
		        Collections.sort(courseGroups, groupTitleComparator);
				for (String[] row : values) {
					if(StringUtils.isNotEmpty(row[0]) && StringUtils.isNotEmpty(row[1])){
						String userName = row[0].trim().replaceAll("[\\r\\n]+\\s", "");
						String groupName = row[1].trim().replaceAll("[\\r\\n]+\\s", "");
						Group group = null;
						String message = null;
						User user = null;
						CourseMembership courseMembership = null;
						GroupAllocation  groupAllocation = new GroupAllocation();
						groupAllocation.setUserName(userName);
						groupAllocation.setGroupName(groupName);
						groupAllocation.setStatus(GroupManConstants.PERSIST_FAILED);
						if (courseGroups == null || courseGroups.size() < 1){
							groupAllocation.setStatusKey(GroupManConstants.NO_GROUPS_DEFINED);
						
							message = GroupManConstants.NO_GROUPS_DEFINED;
							logger.error("Method persistGroup: "+message);
						}
						else if (null == (user = this.getUser(userName))){
							groupAllocation.setStatusKey(GroupManConstants.USER_NOT_FOUND);
							message = GroupManConstants.USER_NOT_FOUND+" : "+ userName;
							logger.error("Method persistGroup: "+message);
						}
						else if (null==(courseMembership = loadCourseMembership(courseIdObject, user.getId()))){
							groupAllocation.setStatusKey(GroupManConstants.COURSE_USER_NOT_FOUND);
							message = GroupManConstants.COURSE_USER_NOT_FOUND+" : "+userName;
							logger.error("Method persistGroup: "+message);
						}
						else if (null==(group=getGroupByBinarySearch(courseGroups, groupName))){
							groupAllocation.setStatusKey(GroupManConstants.GROUP_NOT_FOUND);
							message = GroupManConstants.GROUP_NOT_FOUND+" : "+ groupName;
							logger.error("Method persistGroup: "+message);
						}else{
							groupAllocation.setUserFamilyName(user.getFamilyName());
							groupAllocation.setUserGivenName(user.getGivenName());
							String outcome = persistGroup(courseMembership.getId(), group.getId(), user.getId());
					
							if(StringUtils.isNotEmpty(outcome) && outcome==GroupManConstants.PERSIST_SUCCESS){
								groupAllocation.setStatus(GroupManConstants.PERSIST_SUCCESS);
							}
							groupAllocation.setStatusKey(outcome);
						}

						resultList.add(groupAllocation);
					}
				}
			}
		}
		
		return resultList;
	}
	
	public byte[] getCsv(List<GroupAllocation> groups){
		byte[] bytes= null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(baos);
		try {
			for (GroupAllocation group : groups) {
				out.writeUTF(group.toCsvString("|"));
			}
			bytes=baos.toByteArray();
		} catch (IOException e) {
			logger.error("Method getCsv: "+ e.getMessage());
		}finally{
			try {
				baos.close();
				out.close();
			} catch (IOException e) {
				logger.error("Method getCsv: "+ e.getMessage());
			}
		}
		return bytes;
	}
	
	private String persistGroup(Id courseMembershipId, Id groupIdObject, Id userIdObject){
		if(groupIdObject != null && courseMembershipId!=null && userIdObject != null){
			
			GroupMembership groupMembership = new GroupMembership();
			groupMembership.setCourseMembershipId(courseMembershipId);
			groupMembership.setGroupId(groupIdObject);
			
			try {
				groupMembership.validate();
			} catch (ValidationException e) {
				logger.error("Method persistGroup: ValidationException : "+ e.getMessage());
				return GroupManConstants.VALIDATE_FAILURE;
			}
			
			try {
				
				groupMembershipDbPersister.persist(groupMembership);
				return GroupManConstants.PERSIST_SUCCESS;
			} catch (ValidationException e) {
				logger.error("Method persistGroup: ValidationException : "+ e.getMessage());
				return GroupManConstants.VALIDATE_FAILURE;
			}catch (PersistenceException e) {
				e.printStackTrace();
				if(null!=loadGroupMembership(groupIdObject, userIdObject)){
					return GroupManConstants.GROUP_MEMBERSHIP_EXISTED;
				}else {
					logger.error("Method persistGroup: PersistenceException : "+ e.getMessage());
					return GroupManConstants.UNKNOWN_ERROR;
				}
			}
	     }
		 return GroupManConstants.USPECIFIED_ID_OBJECTS;
	}
	
	private String[][] getCsvValues(GroupsUploadCommand command){
		String[][] values = null;
		if(command != null){
			CSVParse parser =  null;
			try {
				parser = new ExcelCSVParser(command.getFile().getInputStream());
				values = parser.getAllValues();
			} catch (IOException e) {
				logger.error(e.getMessage());
			} finally {
				try {
					parser.close();
				} catch (IOException e) {logger.error(e.getMessage());}
			}
		}
		return values;
	}
	
	private User getUser(String userName){
		if(StringUtils.isNotEmpty(userName)){
			try {
				return userDbLoader.loadByUserName(userName);
			} catch (KeyNotFoundException e) {
				logger.error("Method getUser: "+e.getMessage()+" : "+userName);
			} catch (PersistenceException e) {
				logger.error("Method getUser: "+e.getMessage()+" : "+userName);
			}
		}else{
			logger.error("Method getUser: username is missing.");
		}
		return null;
	}
	
	private CourseMembership loadCourseMembership(Id courseIdObject, Id userIdObject){
		if(courseIdObject!=null && userIdObject != null){
			CourseMembership courseMembership = null;
			try {
				courseMembership = courseMembershipDbLoader.loadByCourseAndUserId(courseIdObject, userIdObject);
				return courseMembership;
			} catch (KeyNotFoundException e) {
				logger.error("Method: loadCourseMembership: "
						+e.getMessage()
						+" : Course Id is: "+courseIdObject.getExternalString()
						+" : User Id is: "+userIdObject.getExternalString());
			} catch (PersistenceException e) {
				logger.error("Method: loadCourseMembership: "
						+e.getMessage()
						+" : Course Id is: "+courseIdObject.getExternalString()
						+" : User Id is: "+userIdObject.getExternalString());
			}
		}
		
		return null;
	}
	
	private List<Group> loadGroups(Id courseIdObject){
		if(courseIdObject != null){
			try {
				return groupDbLoader.loadByCourseId(courseIdObject);
			} catch (KeyNotFoundException e) {
				logger.error(e.getMessage());
			} catch (PersistenceException e) {
				logger.error(e.getMessage());
			}
		}
		return null;
	}
	
	private List<User> loadGroupMemebers(Id groupIdObject){
		try {
			return userDbLoader.loadByGroupId(groupIdObject);
		} catch (KeyNotFoundException e) {
			logger.error(e.getMessage());
		} catch (PersistenceException e) {
			logger.error(e.getMessage());
		}
		return null;
	}


	private Group getGroupByBinarySearch(List<Group> groups, String groupName){
		if(groups!=null && groups.size()>0 && StringUtils.isNotEmpty(groupName)){
			Group group = new Group();
			group.setTitle(groupName);
			int index = Collections.binarySearch(groups,group, groupTitleComparator);
	        if (index < 0) {
	        	return null;
	        }
	        return (Group) groups.get(index);
		}
		return null;
	}
	
	
	private Group getGroup(List<Group> groups, String groupName){
		if(groups!=null && StringUtils.isNotEmpty(groupName)){
			int groupIndex=0;
			boolean isGroupExisted = false;
			for(Group group:groups){
				if(group.getTitle().trim().equals(groupName.trim())){
					groupIndex++;
					isGroupExisted = true;
					break;
				}
			}
			
			if (isGroupExisted == true) {
				return groups.get(groupIndex);
			}
		}
		return null;
	}
	
	private GroupMembership loadGroupMembership(Id groupIdObject, Id userIdObject){
		try {
			return groupMembershipDbLoader.loadByGroupAndUserId(groupIdObject, userIdObject);
		} catch (KeyNotFoundException e) {
			logger.error("Method loadGroupMembership: KeyNotFoundException : "+ e.getMessage());
		} catch (PersistenceException e) {
			logger.error("Method loadGroupMembership: PersistenceException : "+ e.getMessage());
		}
		
		return null;
	}
	
	
	private Id getId(DataType dataType, String idString){
		try {
			return Id.generateId(dataType, idString);
		} catch (PersistenceException e) {
			logger.error("Method getId: "+e.getMessage());
		}
		return null;
	}
}
