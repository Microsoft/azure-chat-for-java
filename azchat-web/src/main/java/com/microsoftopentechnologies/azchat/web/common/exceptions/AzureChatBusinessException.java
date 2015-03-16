/*
 Copyright 2015 Microsoft Open Technologies, Inc.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package com.microsoftopentechnologies.azchat.web.common.exceptions;

import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatConstants;

/**
 * This class can be used to raise the application specific exceptions.Single
 * parameter constructor create the exception instance with customized message
 * and call the super constructor with default information for exception code
 * and exception type.This class specifically set the exception code and type as
 * a business category exception. Customized exception code and type can also be
 * provided by using parameterized constructor.
 * 
 * @author Dnyaneshwar_Pawar
 *
 */
public class AzureChatBusinessException extends AzureChatException {

	/**
	 * Generated serial version id for AzureChatBusinessException
	 */
	private static final long serialVersionUID = -8265777189718232923L;

	/**
	 * Parameterized constructor to generate custom AzChat business exceptions.
	 * 
	 * @param excpMsg
	 */
	public AzureChatBusinessException(String excpMsg) {
		super(AzureChatConstants.EXCEP_CODE_BUSSINESS_EXCEPTION,
				AzureChatConstants.EXCEP_TYPE_BUSSINESS_EXCEPTION, excpMsg);
	}

	/**
	 * Parameterized constructor to generate custom AzChat business exceptions.
	 * Exception code is the exception severity level mainly Business and System
	 * exception and exception type is exception class identifies exception
	 * area.
	 * 
	 * @param excpCode
	 * @param excpType
	 * @param excpMsg
	 */
	public AzureChatBusinessException(String excpCode, String excpType,
			String excpMsg) {
		super(excpCode, excpType, excpMsg);
	}

}