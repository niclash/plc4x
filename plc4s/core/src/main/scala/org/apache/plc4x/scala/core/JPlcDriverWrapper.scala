/*
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
*/
package org.apache.plc4x.scala.core

import org.apache.plc4x.java.api.authentication.PlcAuthentication
import org.apache.plc4x.java.api.{PlcDriver => JPlcDriver}
import org.apache.plc4x.scala.api.{PlcConnectionError, PlcDriver}

import scala.util.{Failure, Success, Try}

private[core] class JPlcDriverWrapper(val jPlcDriver: JPlcDriver) extends PlcDriver {

    override def protocolCode = jPlcDriver.getProtocolCode

    override def protocolName = jPlcDriver.getProtocolCode

    override def connect(url: String) = Try(jPlcDriver.connect(url)) match {
        case Success(jConnection) => Right(new JPlcConnectionWrapper(jConnection))
        case Failure(ex) => Left(PlcConnectionError(ex.getMessage))
    }

    override def connect(url: String, authentication: PlcAuthentication) = Try(jPlcDriver.connect(url, authentication)) match {
        case Success(jConnection) => Right(new JPlcConnectionWrapper(jConnection))
        case Failure(ex) => Left(PlcConnectionError(ex.getMessage))
    }
}
