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

import org.apache.commons.lang3.StringUtils;

/**
 * @author Kelvin Hai {@link <a href="mailto:kelvin.hai@york.ac.uk">kelvin.hai@york.ac.uk</a>}
 * @version $Revision$ $Date$
 */

public class GroupAllocation {
	private String userName;
	private String groupName;
	private String userFamilyName;
	private String userGivenName;
	private String status=GroupManConstants.STATUS_FAILED;
	private String statusKey=GroupManConstants.PERSIST_FAILED;
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getGroupName() {
		return groupName;
	}
	
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	public String getUserFamilyName() {
		return userFamilyName;
	}
	
	public void setUserFamilyName(String userFamilyName) {
		this.userFamilyName = userFamilyName;
	}
	
	public String getUserGivenName() {
		return userGivenName;
	}
	
	public void setUserGivenName(String userGivenName) {
		this.userGivenName = userGivenName;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getStatusKey() {
		return statusKey;
	}
	
	public void setStatusKey(String statusKey) {
		this.statusKey = statusKey;
	}
	
	public String toCsvString(String delimiter){
		if(StringUtils.isEmpty(delimiter)){
			delimiter=",";
		}
		StringBuffer sb = new StringBuffer();
		sb.append(this.userName).append(delimiter);
		sb.append(this.groupName).append(delimiter);
		sb.append(this.userFamilyName).append(delimiter);
		sb.append(this.userGivenName).append(delimiter);
		sb.append(this.status).append(delimiter);
		sb.append(this.statusKey);
		return sb.toString();
	}
}
