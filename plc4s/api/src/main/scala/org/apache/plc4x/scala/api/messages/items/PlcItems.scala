package org.apache.plc4x.scala.api.messages.items

import org.apache.plc4x.java.api.model.Address
import org.apache.plc4x.java.api.types.ResponseCode
import scala.collection.immutable.List

case class ReadRequestItem(datatype: Class[_], address: Address, size: Int)
object ReadRequestItem{
    def apply(datatype: Class[_], address: Address, size: Int): ReadRequestItem =
        new ReadRequestItem(datatype, address, size)
    def apply(datatype: Class[_], address: Address): ReadRequestItem =
        new ReadRequestItem(datatype, address, size = 1)
}

case class ReadResponseItem(readRequestItem: ReadRequestItem, responseCode: ResponseCode, values: List[AnyRef])
object ReadResponseItem{
    def apply(readRequestItem: ReadRequestItem, responseCode: ResponseCode, values: List[AnyRef]): ReadResponseItem =
        new ReadResponseItem(readRequestItem, responseCode, values)
}