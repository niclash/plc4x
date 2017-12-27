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
package org.apache.plc4x.scala.api

import org.apache.plc4x.java.api.authentication.PlcAuthentication
import org.apache.plc4x.scala.api.connection.PlcConnection

/**
  * General interface defining the minimal methods required for adding a new type of driver to the PLC4J system.
  */
trait PlcDriver {

    /**
      * @return code of the implemented protocol. This is usually a lot shorter than the String returned by @see #getProtocolName().
      */
    def protocolCode: String

    /**
      * @return name of the implemented protocol.
      */
    def protocolName: String

    /**
      * Connects to a PLC using the given plc connection string.
      *
      * @param url plc connection string.
      * @return Either the established PlcConnection or an PlcConnectionError.
      */
    def connect(url: String): Either[PlcConnectionError, PlcConnection]

    /**
      * Connects to a PLC using the given plc connection string using given authentication credentials.
      *
      * @param url            plc connection string.
      * @param authentication authentication credentials.
      * @return Either the established PlcConnection or an PlcConnectionError.
      */
    def connect(url: String, authentication: PlcAuthentication): Either[PlcConnectionError, PlcConnection]

}
