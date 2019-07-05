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
<fmt:message var="pInstructions" key="groupman.upload.page.instructions"/>
<fmt:message var="tbTitle" key="groupman.show.title.action.button"/>
<fmt:message var="lFileFormat" key="groupman.data.element.label.file_format"/>
<fmt:message var="lNote" key="groupman.data.element.label.note"/>
<fmt:message var="lError" key="groupman.data.element.label.error"/>
<fmt:message var="sInstructions" key="groupman.upload.step.instructions.user_guide"/>
<fmt:message var="sTitleUserGuide" key="groupman.upload.step.title.user_guide"/>
<fmt:message var="sTitleBrowseFile" key="groupman.upload.step.title.browser_file"/>
<fmt:message var="sTitleError" key="groupman.upload.step.title.error"/>


<bbNG:learningSystemPage authentication='Y' ctxId="ctx" entitlement="${entitlment}">
	<bbNG:pageHeader instructions="${pInstructions}">
		<bbNG:pageTitleBar title="${tbTitle}" />
		
		<bbNG:breadcrumbBar environment="CTRL_PANEL">
			<bbNG:breadcrumb href="showgroups?course_id=${ctx.courseId.externalString}"><fmt:message key="groupman.title"/></bbNG:breadcrumb>
			<bbNG:breadcrumb ><fmt:message key="groupman.show.title.action.button"/></bbNG:breadcrumb>
		</bbNG:breadcrumbBar>
	</bbNG:pageHeader>
	
	<form:form commandName="command" method="post"
		enctype="multipart/form-data">
		<bbNG:dataCollection>
			<bbNG:step title="${sTitleUserGuide}">
				<bbNG:stepInstructions text="${sInstructions}"/>
				
				<bbNG:dataElement label="${lFileFormat}">
	            <ul>
	                <li>Each line in the file should define a separate allocation of a user to a group.</li>
	                <li><strong>First field</strong> in each line should contain the <strong>username</strong> of the course member.</li>
	                <li><strong>Second field</strong> in each line should contain the <strong>group name.</strong></li>
	                <li>Fields should be separated by a <strong>comma</strong>.</li>
	                <li>Where group names contain commas, the text of the group name should be surrounded by inverted commas.</li>
	                <li>Your filename should end with the letters <strong>'.csv'</strong>.</li>
	            </ul>
	            </bbNG:dataElement>
	            
	            <bbNG:dataElement label="${lNote}">
		            <ul>
		                <li>Running the tool will generate a report of which allocations succeeded and which (if any) failed with a reason.</li>
		                <li>This tool never defines new groups. Groups to which users are to be allocated must be created manually beforehand.</li>
		                <li>This tool will only ever add users to groups. Removal of users from groups must be done manually.</li>
		                <li>Users can be allocated to more than one group using the same CSV file.</li>
		            </ul>
	            </bbNG:dataElement>
			</bbNG:step>
			
			<c:if test="${not empty errorCode}">
				<bbNG:step title="${sTitleError}">
					 <bbNG:dataElement label="${lError}">
					 	<p style="color : red;"><fmt:message key="${errorCode}"/> ${errorMessage}</p>
					 </bbNG:dataElement>
				</bbNG:step>
			</c:if>
			<bbNG:step title="${sTitleBrowseFile}">
				<form:input type="file" path="file" />
			</bbNG:step>
			<bbNG:stepSubmit />
		</bbNG:dataCollection>
	</form:form>

</bbNG:learningSystemPage>
