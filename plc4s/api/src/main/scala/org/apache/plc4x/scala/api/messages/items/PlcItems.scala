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
package org.apache.plc4x.scala.api.messages.items

import org.apache.plc4x.java.api.model.Address
import org.apache.plc4x.java.api.types.ResponseCode

import scala.collection.immutable.List

trait RequestItem[D]

case class ReadRequestItem[D](datatype: Class[D],
                              address: Address,
                              size: Int) extends RequestItem[D]

object ReadRequestItem {
    def apply[D](datatype: Class[D], address: Address, size: Int) =
        new ReadRequestItem(datatype, address, size)

    def apply[D](datatype: Class[D], address: Address) =
        new ReadRequestItem(datatype, address, size = 1)
}

trait ResponseItem[REQUEST_ITEM <: RequestItem[_]]

case class ReadResponseItem[D](readRequestItem: ReadRequestItem[D],
                               responseCode: ResponseCode,
                               values: List[D]) extends ResponseItem[ReadRequestItem[D]]

object ReadResponseItem {
    def apply[D](readRequestItem: ReadRequestItem[D], responseCode: ResponseCode, values: List[D]) =
        new ReadResponseItem(readRequestItem, responseCode, values)
}
