/*
 * File   : $Source: /alkacon/cvs/opencms/src/com/opencms/file/Attic/CmsAccessProjectMySql.java,v $
 * Date   : $Date: 2000/02/15 17:43:59 $
 * Version: $Revision: 1.17 $
 *
 * Copyright (C) 2000  The OpenCms Group 
 * 
 * This File is part of OpenCms -
 * the Open Source Content Mananagement System
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * For further information about OpenCms, please see the
 * OpenCms Website: http://www.opencms.com
 * 
 * You should have received a copy of the GNU General Public License
 * long with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package com.opencms.file;

import java.util.*;
import java.sql.*;

import com.opencms.core.*;
import com.opencms.util.*;

/**
 * This class describes the access to projects in the Cms.<BR/>
 * 
 * This class has package-visibility for security-reasons.
 * 
 * @author Andreas Schouten
 * @version $Revision: 1.17 $ $Date: 2000/02/15 17:43:59 $
 */
class CmsAccessProjectMySql implements I_CmsAccessProject, I_CmsConstants {

    /**
     * This is the connection object to the database
     */
    private Connection m_con  = null;

	/**
	 * Column name
	 */
	private static final String C_USER_ID = "USER_ID";
	
	/**
	 * Column name
	 */
	private static final String C_PROJECT_ID = "PROJECT_ID";
	
	/**
	 * Column name
	 */
	private static final String C_GROUP_ID = "GROUP_ID";
	
	/**
	 * Column name
	 */
	private static final String C_MANAGERGROUP_ID = "MANAGERGROUP_ID";
	
	/**
	 * Column name
	 */
	private static final String C_TASK_ID = "TASK_ID";
	
	/**
	 * Column name
	 */
	private static final String C_PROJECT_NAME = "PROJECT_NAME";
	
	/**
	 * Column name
	 */
	private static final String C_PROJECT_DESCRIPTION = "PROJECT_DESCRIPTION";
	
	/**
	 * Column name
	 */
	private static final String C_PROJECT_FLAGS = "PROJECT_FLAGS";
	
	/**
	 * Column name
	 */
	private static final String C_PROJECT_PUBLISHDATE = "PROJECT_PUBLISHDATE";
	
	/**
	 * Column name
	 */
	private static final String C_PROJECT_CREATEDATE = "PROJECT_CREATEDATE";
	
	/**
     * SQL Command for creating projects.
     */    
    private static final String C_PROJECT_CREATE = "INSERT INTO " + C_DATABASE_PREFIX + "PROJECTS VALUES(null,?,?,?,?,?,?,?,?,null)";

	/**
     * SQL Command for updating projects.
     */    
    private static final String C_PROJECT_UPDATE = "UPDATE " + C_DATABASE_PREFIX + "PROJECTS set " + 
												   C_USER_ID + " = ?, " +
												   C_GROUP_ID + " = ?, " +
												   C_MANAGERGROUP_ID + " = ?, " +
												   C_TASK_ID + " = ?, " +
												   C_PROJECT_DESCRIPTION + " = ?, " +
												   C_PROJECT_FLAGS + " = ?, " +
												   C_PROJECT_CREATEDATE + " = ?, " +
												   C_PROJECT_PUBLISHDATE + " = ? " +
												   "where " + C_PROJECT_NAME + " = ?";

	/**
     * SQL Command for reading projects.
     */    
    private static final String C_PROJECT_READ = "Select * from " + C_DATABASE_PREFIX + "PROJECTS where " + C_PROJECT_NAME + " = ?";
	
	/**
     * SQL Command for reading projects.
     */    
    private static final String C_PROJECT_GET_BY_USER = "Select * from " + C_DATABASE_PREFIX + "PROJECTS where " + 
														C_USER_ID + " = ? and " + 
														C_PROJECT_FLAGS + " = " + 
														C_PROJECT_STATE_UNLOCKED;
	
	/**
     * SQL Command for reading projects.
     */    
    private static final String C_PROJECT_GET_BY_GROUP = "Select * from " + C_DATABASE_PREFIX + "PROJECTS where " + 
														 C_GROUP_ID + " = ? and " +
														 C_PROJECT_FLAGS + " = " + 
														 C_PROJECT_STATE_UNLOCKED;
	
	/**
     * SQL Command for reading projects.
     */    
    private static final String C_PROJECT_GET_BY_MANAGERGROUP = "Select * from " + C_DATABASE_PREFIX + "PROJECTS where " + 
																C_MANAGERGROUP_ID + " = ? and " +
																C_PROJECT_FLAGS + " = " + 
																C_PROJECT_STATE_UNLOCKED;
	
	/**
     * SQL Command for reading projects.
     */    
    private static final String C_PROJECT_GET_BY_FLAG = "Select * from " + C_DATABASE_PREFIX + "PROJECTS where " + 
														 C_PROJECT_FLAGS + " = ?";
	
	/**
     * Constructor, creartes a new CmsAccessProject object and connects it to the
     * project database.
     *
     * @param driver Name of the mySQL JDBC driver.
     * @param conUrl The connection string to the database.
     * 
     * @exception CmsException Throws CmsException if connection fails.
     * 
     */
    CmsAccessProjectMySql(String driver,String conUrl)	
        throws CmsException, ClassNotFoundException {
        Class.forName(driver);
        initConnections(conUrl);
    }

	/**
	 * Reads a project from the Cms.
	 * 
	 * @param name The name of the project to read.
	 * 
	 * @exception CmsException Throws CmsException if something goes wrong.
	 */
	 public A_CmsProject readProject(String name)
		 throws CmsException {
		 try {
			 ResultSet result;
			 // create the statement
			 PreparedStatement statementReadProject = 
				m_con.prepareStatement(C_PROJECT_READ);
			 
			 statementReadProject.setString(1,name);
			 result = statementReadProject.executeQuery();			
			
			 // if resultset exists - return it
			 if(result.next()) {
				 return new CmsProject(result.getInt(C_PROJECT_ID),
									   result.getString(C_PROJECT_NAME),
									   result.getString(C_PROJECT_DESCRIPTION),
									   result.getInt(C_TASK_ID),
									   result.getInt(C_USER_ID),
									   result.getInt(C_GROUP_ID),
									   result.getInt(C_MANAGERGROUP_ID),
									   result.getInt(C_PROJECT_FLAGS),
									   SqlHelper.getTimestamp(result,C_PROJECT_CREATEDATE),
									   SqlHelper.getTimestamp(result,C_PROJECT_PUBLISHDATE));
			 } else {
				 // project not found!
				 throw new CmsException("[" + this.getClass().getName() + "] " + name, 
					 CmsException.C_NOT_FOUND);
			 }
		 } catch( Exception exc ) {
			 throw new CmsException( "[" + this.getClass().getName() + "] " + exc.getMessage(), 
				 CmsException.C_SQL_ERROR, exc);
		 }
	 }
	
	/**
	 * Creates a project.
	 * 
	 * @param name The name of the project to read.
	 * @param description The description for the new project.
	 * @param task The task.
	 * @param owner The owner of this project.
	 * @param group The group for this project.
	 * @param flags The flags for the project (e.g. archive).
	 * 
	 * @exception CmsException Throws CmsException if something goes wrong.
	 */
	 public A_CmsProject createProject(String name, String description, A_CmsTask task, 
								A_CmsUser owner, A_CmsGroup group, 
								A_CmsGroup managergroup, int flags)
		throws CmsException {
		 try {
			 
			 // create the statement
			 PreparedStatement statementCreateProject = 
				m_con.prepareStatement(C_PROJECT_CREATE);
			 
			 statementCreateProject.setInt(1,owner.getId());
			 statementCreateProject.setInt(2,group.getId());
			 statementCreateProject.setInt(3,managergroup.getId());
			 statementCreateProject.setInt(4,task.getId());
			 statementCreateProject.setString(5,name);
			 statementCreateProject.setString(6,description);
			 statementCreateProject.setInt(7,flags);
			 statementCreateProject.setTimestamp(8,new Timestamp(new java.util.Date().getTime()));
			 statementCreateProject.executeUpdate();
		 } catch( SQLException exc ) {
			 throw new CmsException("[" + this.getClass().getName() + "] " + exc.getMessage(), 
				 CmsException.C_SQL_ERROR, exc);
		 }
		 return(readProject(name));
	 }

	/**
	 * Updates a project.
	 * 
	 * @param project The project that will be written.
	 * 
	 * @exception CmsException Throws CmsException if something goes wrong.
	 */
	 public A_CmsProject writeProject(A_CmsProject project)
		 throws CmsException {
		 try {
			 // create the statement
			 PreparedStatement statementUpdateProject = 
				m_con.prepareStatement(C_PROJECT_UPDATE);
			 
			 statementUpdateProject.setInt(1,project.getOwnerId());
			 statementUpdateProject.setInt(2,project.getGroupId());
			 statementUpdateProject.setInt(3,project.getManagerGroupId());
			 statementUpdateProject.setInt(4,project.getTaskId());
			 statementUpdateProject.setString(5,project.getDescription());
			 statementUpdateProject.setInt(6,project.getFlags());
			statementUpdateProject.setTimestamp(7,new Timestamp(project.getCreateDate()));
			 if(project.getPublishingDate() == C_UNKNOWN_LONG) {
				statementUpdateProject.setNull(8,java.sql.Types.TIMESTAMP);
			 } else {
				statementUpdateProject.setTimestamp(8,new Timestamp(project.getPublishingDate()));
			 }
			 statementUpdateProject.setString(9,project.getName());
			 
			 statementUpdateProject.executeUpdate();
		 } catch( SQLException exc ) {
			 throw new CmsException("[" + this.getClass().getName() + "] " + exc.getMessage(), 
				 CmsException.C_SQL_ERROR, exc);
		 }
		 return(readProject(project.getName()));
	 }
	 
	/**
	 * Returns all projects, which are owned by a user.
	 * 
	 * @param user The requesting user.
	 * 
	 * @return a Vector of projects.
	 */
	 public Vector getAllAccessibleProjectsByUser(A_CmsUser user)
		 throws CmsException {
 		 Vector projects = new Vector();

		 try {
			 ResultSet result;
			 
			 // create the statement
			 PreparedStatement statementGetProjectsByUser = 
				m_con.prepareStatement(C_PROJECT_GET_BY_USER);

			 statementGetProjectsByUser.setInt(1,user.getId());
			 result = statementGetProjectsByUser.executeQuery();
			 
			 while(result.next()) {
				 projects.addElement( new CmsProject(result.getInt(C_PROJECT_ID),
													 result.getString(C_PROJECT_NAME),
													 result.getString(C_PROJECT_DESCRIPTION),
													 result.getInt(C_TASK_ID),
													 result.getInt(C_USER_ID),
													 result.getInt(C_GROUP_ID),
													 result.getInt(C_MANAGERGROUP_ID),
													 result.getInt(C_PROJECT_FLAGS),
													 SqlHelper.getTimestamp(result,C_PROJECT_CREATEDATE),
													 SqlHelper.getTimestamp(result,C_PROJECT_PUBLISHDATE)));
			 }
			 return(projects);
		 } catch( Exception exc ) {
			 throw new CmsException("[" + this.getClass().getName() + "] " + exc.getMessage(), 
				 CmsException.C_SQL_ERROR, exc);
		 }
	 }

	/**
	 * Returns all projects, which the group may access.
	 * 
	 * @param group The group to test.
	 * 
	 * @return a Vector of projects.
	 */
	 public Vector getAllAccessibleProjectsByGroup(A_CmsGroup group)
		 throws CmsException {		 
 		 Vector projects = new Vector();

		 try {
			 ResultSet result;
			 
			 // create the statement
			PreparedStatement statementGetProjectsByGroup = 
				m_con.prepareStatement(C_PROJECT_GET_BY_GROUP);
			
			statementGetProjectsByGroup.setInt(1,group.getId());
			result = statementGetProjectsByGroup.executeQuery();
			 
			 while(result.next()) {
				 projects.addElement( new CmsProject(result.getInt(C_PROJECT_ID),
													 result.getString(C_PROJECT_NAME),
													 result.getString(C_PROJECT_DESCRIPTION),
													 result.getInt(C_TASK_ID),
													 result.getInt(C_USER_ID),
													 result.getInt(C_GROUP_ID),
													 result.getInt(C_MANAGERGROUP_ID),
													 result.getInt(C_PROJECT_FLAGS),
													 SqlHelper.getTimestamp(result,C_PROJECT_CREATEDATE),
													 SqlHelper.getTimestamp(result,C_PROJECT_PUBLISHDATE)));
			 }
			 return(projects);
		 } catch( Exception exc ) {
			 throw new CmsException("[" + this.getClass().getName() + "] " + exc.getMessage(), 
				 CmsException.C_SQL_ERROR, exc);
		 }
	 }

	/**
	 * Returns all projects, which the managergroup may manage.
	 * 
	 * @param managergroup The group to test.
	 * 
	 * @return a Vector of projects.
	 */
	 public Vector getAllAccessibleProjectsByManagerGroup(A_CmsGroup managergroup)
		 throws CmsException {		 
 		 Vector projects = new Vector();

		 try {
			 ResultSet result;
			 
			 // create the statement
			PreparedStatement statementGetProjectsByGroup = 
				m_con.prepareStatement(C_PROJECT_GET_BY_MANAGERGROUP);
			
			statementGetProjectsByGroup.setInt(1,managergroup.getId());
			result = statementGetProjectsByGroup.executeQuery();
			 
			 while(result.next()) {
				 projects.addElement( new CmsProject(result.getInt(C_PROJECT_ID),
													 result.getString(C_PROJECT_NAME),
													 result.getString(C_PROJECT_DESCRIPTION),
													 result.getInt(C_TASK_ID),
													 result.getInt(C_USER_ID),
													 result.getInt(C_GROUP_ID),
													 result.getInt(C_MANAGERGROUP_ID),
													 result.getInt(C_PROJECT_FLAGS),
													 SqlHelper.getTimestamp(result,C_PROJECT_CREATEDATE),
													 SqlHelper.getTimestamp(result,C_PROJECT_PUBLISHDATE)));
			 }
			 return(projects);
		 } catch( Exception exc ) {
			 throw new CmsException("[" + this.getClass().getName() + "] " + exc.getMessage(), 
				 CmsException.C_SQL_ERROR, exc);
		 }
	 }
	 
	/**
	 * Returns all projects with the overgiven flag.
	 * 
	 * @param flag The flag for the project.
	 * 
	 * @return a Vector of projects.
	 */
	 public Vector getAllProjects(int flag)
		 throws CmsException {
 		 Vector projects = new Vector();

		 try {
			 ResultSet result;
			 
			 // create the statement
			PreparedStatement statementGetProjectsByFlag = 
				m_con.prepareStatement(C_PROJECT_GET_BY_FLAG);
			
			statementGetProjectsByFlag.setInt(1,flag);
			result = statementGetProjectsByFlag.executeQuery();
			 
			 while(result.next()) {
				 projects.addElement( new CmsProject(result.getInt(C_PROJECT_ID),
													 result.getString(C_PROJECT_NAME),
													 result.getString(C_PROJECT_DESCRIPTION),
													 result.getInt(C_TASK_ID),
													 result.getInt(C_USER_ID),
													 result.getInt(C_GROUP_ID),
													 result.getInt(C_MANAGERGROUP_ID),
													 result.getInt(C_PROJECT_FLAGS),
													 SqlHelper.getTimestamp(result,C_PROJECT_CREATEDATE),
													 SqlHelper.getTimestamp(result,C_PROJECT_PUBLISHDATE)));
			 }
			 return(projects);
		 } catch( Exception exc ) {
			 throw new CmsException("[" + this.getClass().getName() + "] " + exc.getMessage(), 
				 CmsException.C_SQL_ERROR, exc);
		 }
	 }
	 
	 /**
     * Connects to the project database.
     * 
     * @param conUrl The connection string to the database.
     * 
     * @exception CmsException Throws CmsException if connection fails.
     */
    private void initConnections(String conUrl)	
      throws CmsException {
      
        try {
        	m_con = DriverManager.getConnection(conUrl);
       	} catch (SQLException e)	{
         	throw new CmsException("[" + this.getClass().getName() + "] " + e.getMessage(), 
				CmsException.C_SQL_ERROR, e);
		}
    }
}
