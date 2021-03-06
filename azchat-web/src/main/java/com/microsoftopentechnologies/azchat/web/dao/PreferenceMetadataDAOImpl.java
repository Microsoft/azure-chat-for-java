/*
Copyright (c) Microsoft Open Technologies, Inc.  All rights reserved.
 
The MIT License (MIT)
 
Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
 
The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.
 
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
 */
package com.microsoftopentechnologies.azchat.web.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.microsoftopentechnologies.azchat.web.common.exceptions.AzureChatSystemException;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatSQLConstants;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatUtils;
import com.microsoftopentechnologies.azchat.web.dao.data.entities.sql.PreferenceMetadataEntity;

/**
 * Implementation class for the PreferenceMetadataDAO.This class provides the
 * operations for storing the user preference meta data.
 * 
 * @author Rupesh_Shirude
 */
@Service("preferenceMetadataDAO")
public class PreferenceMetadataDAOImpl implements PreferenceMetadataDAO {

	private static final Logger LOGGER = LogManager
			.getLogger(PreferenceMetadataDAOImpl.class);

	/**
	 * This method adds the user preference data in the azure SQL table.
	 * 
	 * @return preferenceMetadataEntity
	 * @throws SQLException
	 */
	@Override
	public PreferenceMetadataEntity addPreferenceMetadataEntity(
			PreferenceMetadataEntity preferenceMetadataEntity) throws Exception {
		LOGGER.info("[PreferenceMetadataEntity][addPreferenceMetadataEntity]         start ");
		LOGGER.debug("PreferenceMetadataEntity    "
				+ preferenceMetadataEntity.getPreferenceDesc());
		int prefMetadataId = 0;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = AzureChatUtils.getConnection(AzureChatUtils
					.buildConnectionString());
			preparedStatement = connection.prepareStatement(
					AzureChatSQLConstants.ADD_PREFERENCE_METADATA,
					Statement.RETURN_GENERATED_KEYS);
			preparedStatement = generatePreparedStatement(preparedStatement,
					preferenceMetadataEntity);
			preparedStatement.executeUpdate();
			resultSet = preparedStatement.getGeneratedKeys();
			if (resultSet.next()) {
				prefMetadataId = resultSet.getInt(1);
			}
		} catch (Exception e) {
			LOGGER.error("Exception occurred while adding preference metadata entity to the azure SQL table. Exception Message : "
					+ e.getMessage());
			throw new AzureChatSystemException(
					"Exception occurred while adding preference metadata entity to the azure SQL table. Exception Message : "
							+ e.getMessage());
		} finally {
			AzureChatUtils.closeDatabaseResources(preparedStatement, resultSet);
		}
		preferenceMetadataEntity.setPreferenceId(prefMetadataId);
		LOGGER.info("[PreferenceMetadataEntity][addPreferenceMetadataEntity]         end ");
		return preferenceMetadataEntity;
	}

	/**
	 * This method fetch the user preference from the azure SQL table for the
	 * input preference id.
	 * 
	 * @return preferenceMetadataEntity
	 */
	@Override
	public PreferenceMetadataEntity getPreferenceMetadataEntityById(
			Integer preferenceMetadataEntityId) throws Exception {
		LOGGER.info("[PreferenceMetadataEntity][getPreferenceMetadataEntityById]         start ");
		LOGGER.debug("PreferenceMetadataEntity   id "
				+ preferenceMetadataEntityId);
		PreferenceMetadataEntity preferenceMetadataEntity = null;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = AzureChatUtils.getConnection(AzureChatUtils
					.buildConnectionString());
			preparedStatement = connection
					.prepareStatement(AzureChatSQLConstants.GET_PREFERENCE_METADATA_BY_ID);
			preparedStatement.setInt(1, preferenceMetadataEntityId);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				preferenceMetadataEntity = generateUserObject(resultSet);
			}
		} catch (Exception e) {
			LOGGER.error("Exception occurred while executing get preference meta data query on azure SQL table. Exception Message : "
					+ e.getMessage());
			throw new AzureChatSystemException(
					"Exception occurred while executing get preference meta data query on azure SQL table. Exception Message : "
							+ e.getMessage());
		} finally {
			AzureChatUtils.closeDatabaseResources(preparedStatement, resultSet,
					connection);
		}
		LOGGER.info("[PreferenceMetadataEntity][getPreferenceMetadataEntityById]         end ");
		return preferenceMetadataEntity;
	}

	/**
	 * This method fetch the all user preferences from the azure SQL table.
	 * 
	 * @return preferenceMetadataEntities
	 */
	@Override
	public List<PreferenceMetadataEntity> getPreferenceMetadataEntities()
			throws Exception {
		LOGGER.info("[PreferenceMetadataEntity][getPreferenceMetadataEntityById]         start ");
		List<PreferenceMetadataEntity> preferenceMetadataEntities = new ArrayList<PreferenceMetadataEntity>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = AzureChatUtils.getConnection(AzureChatUtils
					.buildConnectionString());
			preparedStatement = connection
					.prepareStatement(AzureChatSQLConstants.GET_PREFERENCE_METADATA_ALL);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				preferenceMetadataEntities.add(generateUserObject(resultSet));
			}
		} catch (SQLException e) {
			LOGGER.error("Exception occurred while executing get preference meta data query on azure SQL table. Exception Message : "
					+ e.getMessage());
			throw new AzureChatSystemException(
					"Exception occurred while executing get preference meta data query on azure SQL table. Exception Message : "
							+ e.getMessage());
		} finally {
			AzureChatUtils.closeDatabaseResources(preparedStatement, resultSet,
					connection);
		}
		LOGGER.info("[PreferenceMetadataEntity][getPreferenceMetadataEntityById]         end ");
		return preferenceMetadataEntities;
	}

	/**
	 * This method used to generate prepare statement object from given
	 * preferenceMetadataEntity object.
	 * 
	 * @param preparedStatement
	 * @param preferenceMetadataEntity
	 * @return
	 * @throws SQLException
	 */
	public PreparedStatement generatePreparedStatement(
			PreparedStatement preparedStatement,
			PreferenceMetadataEntity preferenceMetadataEntity)
			throws SQLException {
		preparedStatement.setString(1,
				preferenceMetadataEntity.getPreferenceDesc());
		preparedStatement.setDate(2, new java.sql.Date(preferenceMetadataEntity
				.getDateCreated().getTime()));
		preparedStatement.setDate(3, new java.sql.Date(preferenceMetadataEntity
				.getDateCreated().getTime()));
		preparedStatement.setDate(4, new java.sql.Date(preferenceMetadataEntity
				.getDateCreated().getTime()));
		preparedStatement.setDate(5, new java.sql.Date(preferenceMetadataEntity
				.getDateCreated().getTime()));
		return preparedStatement;
	}

	/**
	 * This method used to set preferenceMetadataEntity object from returned
	 * result set object.
	 * 
	 * @param resutSet
	 * @return
	 * @throws SQLException
	 */
	public PreferenceMetadataEntity generateUserObject(ResultSet resutSet)
			throws SQLException {
		PreferenceMetadataEntity preferenceMetadataEntity = new PreferenceMetadataEntity();
		preferenceMetadataEntity.setPreferenceId(resutSet.getInt(1));
		preferenceMetadataEntity.setPreferenceDesc(resutSet.getString(2));
		preferenceMetadataEntity.setDateCreated(resutSet.getDate(3));
		preferenceMetadataEntity.setCreatedBy(resutSet.getDate(3));
		preferenceMetadataEntity.setDateModified(resutSet.getDate(3));
		preferenceMetadataEntity.setModifiedBy(resutSet.getDate(3));
		return preferenceMetadataEntity;

	}

	/**
	 * This method fetch the user preference id based on the input description.
	 * 
	 * @return id
	 */
	@Override
	public Integer getPreferenceMetedataIdByDescription(String desc)
			throws Exception {
		LOGGER.info("[PreferenceMetadataEntity][getPreferenceMetadataEntityById]         start ");
		Integer id = new Integer(0);
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = AzureChatUtils.getConnection(AzureChatUtils
					.buildConnectionString());
			preparedStatement = connection
					.prepareStatement(AzureChatSQLConstants.GET_PREFERENCE_METADATA_BY_DESC);
			preparedStatement.setString(1, desc);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				id = resultSet.getInt(1);
			}
		} catch (Exception e) {
			LOGGER.error("Exception occurred while retrieving the preference mata data for input description from azure SQL table. Exception Message : "
					+ e.getMessage());
			throw new AzureChatSystemException(
					"Exception occurred while retrieving the preference mata data for input description from azure SQL table. Exception Message : "
							+ e.getMessage());
		} finally {
			AzureChatUtils.closeDatabaseResources(preparedStatement, resultSet,
					connection);
		}
		LOGGER.info("[PreferenceMetadataEntity][getPreferenceMetadataEntityById]         end ");
		return id;
	}

	/**
	 * This method creates the preference meta data table.
	 */
	@Override
	public void createPreferenceMatedateTable() throws Exception {
		Connection connection = null;
		Connection connection1 = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		try {
			connection = AzureChatUtils.getConnection(AzureChatUtils
					.buildConnectionString());
			preparedStatement = connection
					.prepareStatement(AzureChatSQLConstants.CREATE_PREFERENCE_METADATA_TABLE);
			preparedStatement.execute();
			connection1 = AzureChatUtils.getConnection(AzureChatUtils
					.buildConnectionString());
			preparedStatement1 = connection1
					.prepareStatement(AzureChatSQLConstants.CREATE_PREFERENCE_METADATA_TABLE_INDEX);
			preparedStatement1.execute();
		} catch (SQLException e) {
			LOGGER.error("Exception occurred while executing create user table  query on azure SQL table. Exception Mesage : "
					+ e.getMessage());
			throw new AzureChatSystemException(
					"Exception occurred while executing createUserTable query on azure sql. Exception Mesage :  "
							+ e.getMessage());
		} finally {
			AzureChatUtils
					.closeDatabaseResources(preparedStatement, connection);
			AzureChatUtils.closeDatabaseResources(preparedStatement1,
					connection1);
		}
	}

}