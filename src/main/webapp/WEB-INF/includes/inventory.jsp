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
 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<table  style="width:100%" summary="Results of the Group Members"
	title="This is a table showing the group members">
	<col style="width:30%">
	<col style="width:30%">
	<col style="width:40%">
	<thead>
		<tr>
			<th scope="col" class="">User Name</th>
			<th scope="col" class="">Family Name</th>
			<th scope="col" class="">Given Name</th>
		</tr>
	</thead>
	<c:forEach items="${members}" var="user">
		<tr>
			<td>${user.userName}</td>
			<td>${user.familyName}</td>
			<td>${user.givenName}</td>
		</tr>
	</c:forEach>
</table>
