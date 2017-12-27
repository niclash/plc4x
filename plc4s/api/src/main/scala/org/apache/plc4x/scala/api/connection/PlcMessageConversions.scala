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
package org.apache.plc4x.scala.api.connection

import scala.collection.JavaConverters._
import org.apache.plc4x.java.api.messages.{PlcReadRequest => jPlcReadRequest, PlcReadResponse => jPlcReadResponse}
import org.apache.plc4x.java.api.messages.items.{ReadRequestItem => jReadRequestItem, ReadResponseItem => jReadResponseItem}
import org.apache.plc4x.java.api.types.ResponseCode
import org.apache.plc4x.scala.api.messages.items.{ReadRequestItem, ReadResponseItem}
import org.apache.plc4x.scala.api.messages.{PlcReadRequest, PlcReadResponse}

private[connection] object PlcMessageConversions {

    implicit class requestItemOps(item: ReadRequestItem){

        def toJava: jReadRequestItem = new jReadRequestItem(item.datatype, item.address, item.size)
    }

    implicit class jRequestItemOps(item: jReadRequestItem){

        def toScala: ReadRequestItem = new ReadRequestItem(item.getDatatype(), item.getAddress(), item.getSize())
    }

    implicit class RequestOps(req: PlcReadRequest) {

        def toJava: jPlcReadRequest = new jPlcReadRequest(req.readRequestItems.map(e => e.toJava).asJava)
    }

    implicit class jRequestOps(req: jPlcReadRequest) {

        def toScala: PlcReadRequest = PlcReadRequest(req.getReadRequestItems().asScala.map(e => e.toScala).toList)
    }

    implicit class jResponseItemOps(item: jReadResponseItem){

        def toScala: ReadResponseItem =
            ReadResponseItem(item.getRequestItem().toScala, item.getResponseCode(), item.getValues().asScala.toList)
    }

    implicit class responseItemOps(item: ReadResponseItem){

        def toJava: jReadResponseItem =
           new jReadResponseItem(item.readRequestItem.toJava, item.responseCode, item.values.asJava)
    }

    implicit class ResponseOps(resp: jPlcReadResponse) {

        def toScala: PlcReadResponse =
            PlcReadResponse(resp.getRequest().toScala, resp.getResponseItems().asScala.map(e => e.toScala).toList)
    }
}
