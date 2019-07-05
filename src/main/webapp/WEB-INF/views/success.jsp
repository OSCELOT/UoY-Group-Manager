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
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="/bbNG" prefix="bbNG"%>
<%@ page errorPage="/error.jsp"%>

<fmt:message var="entitlment" key="groupman.security.entitlement.course"/>
<fmt:message var="title" key="groupman.success.title"/>
<fmt:message var="pInstructions" key="groupman.success.page.instructions"/>
<fmt:message var="emptyMessage" key="groupman.inventory.list.empty_message"/>
<fmt:message var="nUserName" key="groupman.inventory.list.element.name.user_name"/>
<fmt:message var="lUserName" key="groupman.inventory.list.element.label.user_name"/>
<fmt:message var="nGroupName" key="groupman.inventory.list.element.name.group_name"/>
<fmt:message var="lGroupName" key="groupman.inventory.list.element.label.group_name"/>
<fmt:message var="nFamilyName" key="groupman.inventory.list.element.name.family_name"/>
<fmt:message var="lFamilyName" key="groupman.inventory.list.element.label.family_name"/>
<fmt:message var="nGivenName" key="groupman.inventory.list.element.name.given_name"/>
<fmt:message var="lGivenName" key="groupman.inventory.list.element.label.given_name"/>
<fmt:message var="nStatus" key="groupman.inventory.list.element.name.status"/>
<fmt:message var="lStatus" key="groupman.inventory.list.element.label.status"/>
<fmt:message var="nMessage" key="groupman.inventory.list.element.name.message"/>
<fmt:message var="lMessage" key="groupman.inventory.list.element.label.message"/>

<bbNG:learningSystemPage authentication='Y' ctxId="ctx" entitlement="${entitlment}">
	<bbNG:pageHeader instructions="${pInstructions}">
		<bbNG:breadcrumbBar environment="CTRL_PANEL">
			<bbNG:pageTitleBar title="${title}" />
			<bbNG:breadcrumb href="showgroups?course_id=${ctx.courseId.externalString}" ><fmt:message key="groupman.title"/></bbNG:breadcrumb>
			<bbNG:breadcrumb href="uploadgroups?course_id=${ctx.courseId.externalString}" ><fmt:message key="groupman.show.title.action.button"/></bbNG:breadcrumb>
			<bbNG:breadcrumb>${title}</bbNG:breadcrumb>
		</bbNG:breadcrumbBar>
	</bbNG:pageHeader>

	<bbNG:receipt type="SUCCESS" title="${title}" recallUrl="showgroups?course_id=${ctx.courseId.externalString}">
		
		<bbNG:inventoryList collection="${groupAllocationList}" 
							className="uk.ac.york.its.b2.groupmanager.domain.GroupAllocation" 
							objectVar="group" 
							emptyMsg="${emptyMessage}" 
							showAll="true">
			<bbNG:listElement name="${nGroupName}" label="${lGroupName}">
				${group.groupName}
			</bbNG:listElement>
			<bbNG:listElement name="${nUserName}" label="${lUserName}" isRowHeader="true">
				${group.userName}
			</bbNG:listElement>
			<bbNG:listElement name="${nFamilyName}" label="${lFamilyName}">
				${group.userFamilyName}
			</bbNG:listElement>
			<bbNG:listElement name="${nGivenName}" label="${lGivenName}">
				${group.userGivenName}
			</bbNG:listElement>
			<bbNG:listElement name="${nStatus}" label="${lStatus}">
				<c:choose>
					<c:when test="${group.status == 'groupman.message.persist.success'}">
						<p style="color : green;">
							<strong><fmt:message key="${group.status}"/></strong>
						</p>
					</c:when>
					<c:otherwise>
						<p style="color : red;">
							<strong><fmt:message key="${group.status}"/></strong>
						</p>
					</c:otherwise>
				</c:choose>
			</bbNG:listElement>
			<bbNG:listElement name="${nMessage}" label="${lMessage}">
				<c:choose>
					<c:when test="${group.status == 'groupman.message.persist.success'}">
						<p style="color : green;">
							<strong><fmt:message key="${group.statusKey}"/></strong>
						</p>
					</c:when>
					<c:otherwise>
						<p style="color : red;">
							<strong><fmt:message key="${group.statusKey}"/></strong>
						</p>
				</c:otherwise>
				</c:choose>
			</bbNG:listElement>
		</bbNG:inventoryList>	
	</bbNG:receipt>     
</bbNG:learningSystemPage>
	