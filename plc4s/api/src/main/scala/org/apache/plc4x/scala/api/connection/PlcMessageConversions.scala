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

import org.apache.plc4x.java.api.messages.items.{ReadRequestItem => jReadRequestItem, ReadResponseItem => jReadResponseItem}
import org.apache.plc4x.java.api.messages.{PlcReadRequest => jPlcReadRequest, PlcReadResponse => jPlcReadResponse}
import org.apache.plc4x.scala.api.messages.items.{ReadRequestItem, ReadResponseItem}
import org.apache.plc4x.scala.api.messages.{PlcReadRequest, PlcReadResponse}
import scala.collection.JavaConverters._

private[connection] object PlcMessageConversions {

    implicit class requestItemOps(item: ReadRequestItem[_]){

        def toJava: jReadRequestItem[_] = new jReadRequestItem(item.datatype, item.address, item.size)
    }

    implicit class jRequestItemOps(item: jReadRequestItem[_]){

        def toScala: ReadRequestItem[_] = new ReadRequestItem(item.getDatatype, item.getAddress, item.getSize)
    }

    implicit class RequestOps(req: PlcReadRequest) {

        def toJava: jPlcReadRequest = new jPlcReadRequest(req.readRequestItems.map(_.toJava).asJava)
    }

    implicit class jRequestOps(req: jPlcReadRequest) {

        def toScala: PlcReadRequest = PlcReadRequest(req.getRequestItems.asScala.map(e => e.toScala).toList)
    }

    implicit class jResponseItemOps(item: jReadResponseItem[_]){

        def toScala: ReadResponseItem[_] = ReadResponseItem(
            item.getRequestItem.toScala.asInstanceOf[ReadRequestItem[Any]], item.getResponseCode,
            item.getValues.asScala.toList)
    }

    implicit class responseItemOps(item: ReadResponseItem[_]){

        def toJava: jReadResponseItem[_] =
           new jReadResponseItem(item.readRequestItem.toJava.asInstanceOf[jReadRequestItem[Any]],
               item.responseCode, item.values.asJava.asInstanceOf[java.util.List[Any]])
    }

    implicit class ResponseOps(resp: jPlcReadResponse) {

        def toScala: PlcReadResponse =
            PlcReadResponse(resp.getRequest.toScala,
                resp.getResponseItems.asScala.toList.map(x => x.toScala))

    }
}
