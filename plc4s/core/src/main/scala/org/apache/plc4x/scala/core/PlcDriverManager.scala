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

import org.apache.plc4x.java.{PlcDriverManager => JPlcDriverManager}
import org.apache.plc4x.scala.api.PlcConnectionError

import scala.util.{Failure, Success, Try}

class PlcDriverManager private(val jPlcDriverManager: JPlcDriverManager){

    def this(classLoader: ClassLoader) = this(new JPlcDriverManager(classLoader))

    def this() = this(Thread.currentThread.getContextClassLoader)

    def getConnection(url: String): Either[PlcConnectionError, JPlcConnectionWrapper] = {
        Try(jPlcDriverManager.getConnection(url)) match {
            case Success(jConnection) => Right(new JPlcConnectionWrapper(jConnection))
            case Failure(ex) => Left(PlcConnectionError(ex.getMessage))
        }
    }
}
