<%--
    Copyright (C) 2019 University of York, UK.

    This project was initiated through a donation of source code by the
    University of York, UK. It contains free software; you can redistribute
    it and/or modify it under the terms of the GNU General Public License as
    published by the Free Software Foundation; either version 2 of the
    License, or any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along
    with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.

    For more information please contact:

    Web Services Group
    IT Service
    University of York
    YO10 5DD
    United Kingdom

--%>

<%--
/*
 * @author Kelvin Hai {@link <a href="mailto:kelvin.hai@york.ac.uk">kelvin.hai@york.ac.uk</a>}
 * @version $Revision$ $Date$
 */
 --%>
 
<%@ include file="/WEB-INF/includes/doctype.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="/bbNG" prefix="bbNG"%>
<%@ page errorPage="/error.jsp"%>

<fmt:message var="entitlment" key="groupman.security.entitlement.course"/>

<fmt:message var="title" key="groupman.title.course_tool"/>
<fmt:message var="action_title" key="groupman.show.title.action.button"/>
<fmt:message var="step_title" key="groupman.show.step.title"/>
<fmt:message var="step_instructions" key="groupman.show.step.instructions"/>
<fmt:message var="label_techingAssistants" key="groupman.data.element.label.teaching_assistants"/>
<fmt:message var="label_instructors" key="groupman.data.element.label.instructors"/>
<fmt:message var="label_students" key="groupman.data.element.label.students"/>
<fmt:message var="label_others" key="groupman.data.element.label.others"/>
<fmt:message var="label_nogroup" key="groupman.data.element.label.no_groups_defined"/>


<bbNG:learningSystemPage authentication='Y' ctxId="ctx" entitlement="${entitlment}">
	<bbNG:pageHeader>
		<bbNG:pageTitleBar title="${title}" />
		<bbNG:breadcrumbBar environment="CTRL_PANEL">
			<bbNG:breadcrumb>${title}</bbNG:breadcrumb>
		</bbNG:breadcrumbBar>
	</bbNG:pageHeader>

	<bbNG:actionControlBar>
		<bbNG:actionButton title="${action_title}"
			url="${pageContext.request.contextPath}/uploadgroups?course_id=${ctx.courseId.externalString}"
			primary="true" />
	</bbNG:actionControlBar>

<bbNG:dataCollection>
	<bbNG:step title="${step_title}">
		<bbNG:stepInstructions text="${step_instructions}"/>
		<c:choose>
			<c:when test="${not empty groupsWithMembers}">
				<c:forEach varStatus="loop" items="${groupsWithMembers}"
					var="groupWithMembers">
					
					<h2>
						<a href="/webapps/blackboard/execute/modulepage/viewGroup?course_id=${ctx.courseId.externalString}&group_id=${groupWithMembers.group.id.externalString}">Group: ${groupWithMembers.group.title} </a>
					</h2>
					<c:if test="${not empty groupWithMembers.instructors}">
	                    <bbNG:dataElement label="${label_instructors}">
	                    	<c:set var="members" value="${groupWithMembers.instructors}"/>
		                    <%@ include file="/WEB-INF/includes/inventory.jsp" %>
						</bbNG:dataElement>
					</c:if>
					<c:if test="${not empty groupWithMembers.teachingAssistants}">
						<bbNG:dataElement label="${label_techingAssistants}">
							<c:set var="members" value="${groupWithMembers.teachingAssistants}"/>	    
							<%@ include file="/WEB-INF/includes/inventory.jsp" %>
						</bbNG:dataElement>
					</c:if>
					<c:if test="${not empty groupWithMembers.others}">
	                	<bbNG:dataElement label="${label_others}">
	                		<c:set var="members" value="${groupWithMembers.others}"/>
		                    <%@ include file="/WEB-INF/includes/inventory.jsp" %>
						</bbNG:dataElement>
					</c:if>
					<c:if test="${not empty groupWithMembers.students}">    
	                	<bbNG:dataElement label="${label_students}">
	                		<c:set var="members" value="${groupWithMembers.students}"/>
		                	<%@ include file="/WEB-INF/includes/inventory.jsp" %>
						</bbNG:dataElement>
					</c:if>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<bbNG:dataElement label="${label_nogroup}">
               		<p style="color : red;">
               			<strong><fmt:message key="groupman.message.no_groups_defined"/></strong>
               		</p>
				</bbNG:dataElement>			
			</c:otherwise>
		</c:choose>
	</bbNG:step>
	<bbNG:stepSubmit />
	</bbNG:dataCollection>
</bbNG:learningSystemPage>